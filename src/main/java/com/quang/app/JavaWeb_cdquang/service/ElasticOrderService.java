package com.quang.app.JavaWeb_cdquang.service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quang.app.JavaWeb_cdquang.document.OrderDocument;
import com.quang.app.JavaWeb_cdquang.document.OrderItemDocument;
import com.quang.app.JavaWeb_cdquang.entity.Order;
import com.quang.app.JavaWeb_cdquang.mappers.OrderMapper;
import com.quang.app.JavaWeb_cdquang.utils.ConvertHelper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;

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
}
