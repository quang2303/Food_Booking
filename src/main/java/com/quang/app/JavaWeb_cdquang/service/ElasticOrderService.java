package com.quang.app.JavaWeb_cdquang.service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quang.app.JavaWeb_cdquang.document.ItemDocument;
import com.quang.app.JavaWeb_cdquang.document.OrderDocument;
import com.quang.app.JavaWeb_cdquang.document.OrderItemDocument;
import com.quang.app.JavaWeb_cdquang.dto.FilterItemsRequest;
import com.quang.app.JavaWeb_cdquang.dto.PageResponse;
import com.quang.app.JavaWeb_cdquang.entity.Order;
import com.quang.app.JavaWeb_cdquang.mappers.OrderMapper;
import com.quang.app.JavaWeb_cdquang.utils.ConvertHelper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Service
public class ElasticOrderService {
	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	private ElasticsearchClient elasticsearchClient;
	
	public void syncDataToElasticsearch() throws IOException {
		// Check index items is exist ?
		boolean indexExist = elasticsearchClient.indices().exists(e -> e.index("orders")).value();
				
		// Return if exist
		if (indexExist) {
			return;
		}

		//Create and mapping for index items
		String mappingJson = new String(
			Files.readAllBytes(Paths.get("src/main/resources/elasticsearch/order-mapping.json")));
		elasticsearchClient.indices().create(c -> c
					.index("orders")
					.withJson(new StringReader(mappingJson)));
				
		//Save list item from database to elastic search
		List<Order> orders = orderMapper.getAllOrder();
		
		for(Order order : orders) {
			OrderDocument orderDoc = ConvertHelper.orderToDoc(order);
			
			elasticsearchClient.index(i -> i
					.index("orders")
					.id(orderDoc.getId().toString())
					.document(orderDoc));
			
			for(OrderItemDocument orderItem : orderDoc.getItems()) {
				elasticsearchClient.index(i -> i
						.index("order_items")
						.id(orderItem.getId())
						.document(orderItem));
			}
		}
	}
	
	public void saveOrderToElastic(Integer id) {
		Order order = orderMapper.getOrderById(id);
		
		try {
			OrderDocument orderDoc = ConvertHelper.orderToDoc(order);
			elasticsearchClient.index(i -> i
					.index("orders")
					.id(orderDoc.getId().toString())
					.document(orderDoc));
			
			for(OrderItemDocument orderItem : orderDoc.getItems()) {
				elasticsearchClient.index(i -> i
						.index("order_items")
						.id(orderItem.getId())
						.document(orderItem));
			}
		} catch (ElasticsearchException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error to save orders and order items in elastic");
		}
	}
	
	public PageResponse<OrderDocument> filterOrderInElasticsearch(FilterItemsRequest filterRequest) {
		PageResponse<OrderDocument> pageResponse = new PageResponse<>();
		
		int page = filterRequest.getPage() - 1;
		
		int size = filterRequest.getSize();
		
		try {
			
			SearchResponse<OrderDocument> response = elasticsearchClient.search(
					s -> s.index("orders")
					.from(page*size)
					.size(size)
					.sort(sort -> sort
							.field(f -> f
									.field("updateAt")
									.order(SortOrder.Desc)))
					.query(q -> q
							.bool(b -> {
								if(filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
									b.must(sh1 -> sh1
											.prefix(prefix -> prefix
													.field("name")
													.value(filterRequest.getName())
													));

								}

								b.filter(filter2 -> filter2
										.term(t -> t
												.field("type.keyword")
												.value(filterRequest.getType())));
								
								return b;
							})
					),
					OrderDocument.class);
			
			List<Hit<OrderDocument>> listHit = response.hits().hits();
			
			List<OrderDocument> itemResponse = new ArrayList<>();
			
			for(Hit<OrderDocument> hit : listHit) {
				OrderDocument doc = hit.source();
				if(doc != null) {
					itemResponse.add(doc);
				}
			}
			
			int totalElements = (int) response.hits().total().value();
			
			int totalPages = (int) Math.ceil((double) totalElements / size);
			
			pageResponse.setData(itemResponse);
			pageResponse.setTotalElements(totalElements);
			pageResponse.setTotalPages(totalPages);
		} catch (ElasticsearchException | IOException e) {
			e.printStackTrace();
		}

		return pageResponse;
	}
}
