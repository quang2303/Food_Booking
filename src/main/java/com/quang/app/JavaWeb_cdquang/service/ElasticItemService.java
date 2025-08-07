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
import com.quang.app.JavaWeb_cdquang.dto.FilterItemsRequest;
import com.quang.app.JavaWeb_cdquang.dto.ItemResponse;
import com.quang.app.JavaWeb_cdquang.dto.PageResponse;
import com.quang.app.JavaWeb_cdquang.entity.Item;
import com.quang.app.JavaWeb_cdquang.mappers.ItemMapper;
import com.quang.app.JavaWeb_cdquang.utils.ConvertHelper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Service
public class ElasticItemService {
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired 
	SearchService searchService;

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	// Sync data items to elastic search
	public void syncDataToElasticsearch() throws IOException {

		// Check index items is exist ?
		boolean indexExist = elasticsearchClient.indices().exists(e -> e.index("items")).value();
		
		// Return if exist
		if (indexExist) {
	        return;
	    }

		//Create and mapping for index items
		String mappingJson = new String(
				Files.readAllBytes(Paths.get("src/main/resources/elasticsearch/items-mapping.json")));
		elasticsearchClient.indices().create(c -> c
				.index("items")
				.withJson(new StringReader(mappingJson)));
		
		//Save list item from database to elastic search
		List<Item> items = itemMapper.getAllItem();
		for (Item item : items) {
			ItemDocument itemDoc = ConvertHelper.itemToDoc(item);

			elasticsearchClient.index(i -> i
					.index("items")
					.id(itemDoc.getId().toString())
					.document(itemDoc));
		}
	}

	// Return list of name item suggest base on keyword
	public List<String> suggest(String keyword) throws ElasticsearchException, IOException {
		SearchResponse<ItemDocument> response = elasticsearchClient.search(
				s -> s.index("items")
				.query(q -> q
						.bool(b -> b
								.should(sh1 -> sh1
										.prefix(prefix -> prefix
												.field("name")
												.value(keyword)
												.boost(2.0f)
										)
								)
								.should(sh2 -> sh2
										.match(match -> match
												.field("name")
												.query(keyword)
												.fuzziness("AUTO")
												.boost(1.0f)
										)
								)
								.filter(filter -> filter // Filter item does not delete
										.term(term -> term
												.field("status")
												.value(true)
										)
								)
								.minimumShouldMatch("1")
						)
				)
				.size(5),
				ItemDocument.class);

		List<String> listHitName = new ArrayList<>();

		List<Hit<ItemDocument>> listHit = response.hits().hits();

		for (Hit<ItemDocument> hit : listHit) {
			listHitName.add(hit.source().getName());
		}

		return listHitName;
	}
	
	// Save new item, update to elastic
	public void saveItemToElasticsearch(Item item) {
		ItemDocument itemDoc = ConvertHelper.itemToDoc(item);
		try {
			elasticsearchClient.index(i -> i
					.index("items")
					.id(itemDoc.getId().toString())
					.document(itemDoc)
					.refresh(Refresh.True));
		} catch (ElasticsearchException e) {
			e.printStackTrace();
			throw new ElasticsearchException("Error save item to Elasticsearch with id: " + item.getId(), null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Can not connect with Elasticsearch: " + e.getMessage(), e);
		}
	}
	
	public PageResponse<ItemResponse> filterItemInElasticsearchUser(FilterItemsRequest filterRequest) {
		PageResponse<ItemResponse> pageResponse = new PageResponse<>();
		
		int page = filterRequest.getPage() - 1;
		
		int size = filterRequest.getSize();
		
		String sortField = "price";
		
		boolean isAsc = "ASC".equalsIgnoreCase(filterRequest.getSortByPrice());
		try {
			SearchResponse<ItemDocument> response = elasticsearchClient.search(
					s -> s.index("items")
					.from(page*size)
					.size(size)
					.sort(sort -> sort
							.field(f -> f
									.field(sortField)
									.order(isAsc ? SortOrder.Asc : SortOrder.Desc)))
					.query(q -> q
							.bool(b -> {
								if(filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
									b.should(sh1 -> sh1
											.prefix(prefix -> prefix
													.field("name")
													.value(filterRequest.getName())
													.boost(3.0f)));
									
									b.should(sh2 -> sh2
											.match(match -> match
													.field("name")
													.query(filterRequest.getName())
													.fuzziness("AUTO")
													.boost(1.0f)));
									b.minimumShouldMatch("1");
								}
									
								b.filter(filter -> filter // Filter item does not delete
										.term(term -> term
												.field("status")
												.value(true)
											)
								);
								b.filter(filter2 -> filter2
										.term(t -> t
												.field("type.keyword")
												.value(filterRequest.getType())));
								
								return b;
							})
					),
					ItemDocument.class);
			
			List<Hit<ItemDocument>> listHit = response.hits().hits();
			
			List<ItemResponse> itemResponse = new ArrayList<>();
			
			for(Hit<ItemDocument> hit : listHit) {
				ItemDocument doc = hit.source();
				if(doc != null) {
					itemResponse.add(ConvertHelper.itemDocToResponse(doc));
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
		
		searchService.saveSearchLog(filterRequest.getName());

		return pageResponse;
	}
	
	public PageResponse<ItemDocument> filterItemInElasticsearchAdmin(FilterItemsRequest filterRequest) {
		PageResponse<ItemDocument> pageResponse = new PageResponse<>();
		
		int page = filterRequest.getPage() - 1;
		
		int size = filterRequest.getSize();
		
		try {
			
			SearchResponse<ItemDocument> response = elasticsearchClient.search(
					s -> s.index("items")
					.from(page*size)
					.size(size)
					.sort(sort -> sort
							.field(f -> f
									.field("updateAt")
									.order(SortOrder.Desc)))
					.query(q -> q
							.bool(b -> {
								if(filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
									b.should(sh -> sh
											.matchPhrasePrefix(prefix -> prefix
													.field("name")
													.query(filterRequest.getName())
													.boost(5.0f)));
									
									b.should(sh1 -> sh1
											.matchPhrase(prefix -> prefix
													.field("name")
													.query(filterRequest.getName())
													.boost(3.0f)));
									
									b.minimumShouldMatch("1");
								}

								b.filter(filter2 -> filter2
										.term(t -> t
												.field("type.keyword")
												.value(filterRequest.getType())));
								
								return b;
							})
					),
					ItemDocument.class);
			
			List<Hit<ItemDocument>> listHit = response.hits().hits();
			
			List<ItemDocument> itemResponse = new ArrayList<>();
			
			for(Hit<ItemDocument> hit : listHit) {
				ItemDocument doc = hit.source();
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
		
		searchService.saveSearchLog(filterRequest.getName());

		return pageResponse;
	}
}
