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
import com.quang.app.JavaWeb_cdquang.entity.Item;
import com.quang.app.JavaWeb_cdquang.mappers.ItemMapper;
import com.quang.app.JavaWeb_cdquang.utils.ConvertHelper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

@Service
public class ElasticItemService {
	@Autowired
	private ItemMapper itemMapper;

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
					.document(itemDoc));
		} catch (ElasticsearchException e) {
			e.printStackTrace();
			throw new ElasticsearchException("Error save item to Elasticsearch with id: " + item.getId(), null);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Can not connect with Elasticsearch: " + e.getMessage(), e);
		}
	}
	
	public void filterItemInElasticsearch(FilterItemsRequest filterRequest) {
		
	}
}
