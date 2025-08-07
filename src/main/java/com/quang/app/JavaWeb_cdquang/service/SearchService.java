package com.quang.app.JavaWeb_cdquang.service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quang.app.JavaWeb_cdquang.document.OrderItemDocument;
import com.quang.app.JavaWeb_cdquang.document.SearchLogDocument;
import com.quang.app.JavaWeb_cdquang.dto.TopItemSoldResponse;
import com.quang.app.JavaWeb_cdquang.utils.SearchHelper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;

@Service
public class SearchService {
	@Autowired
	private ElasticsearchClient elasticsearchClient;
	
	// Save log search to elastic
	public void saveSearchLog(String keyword) {
		// Get keyword to save
		String keyWordToSave = SearchHelper.findClosestKeyword(keyword);
		
		if(keyWordToSave == null) {
			return;
		}
		
		ZonedDateTime vietnamTime = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		Instant utcTime = vietnamTime.toInstant();
		SearchLogDocument searchLog = new SearchLogDocument(keyWordToSave, utcTime.toString());
		
		try {
			elasticsearchClient.index(index -> index
					.index("search_logs")
					.document(searchLog));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Can not save search log");
		}
	}
	
	// Find key word is the top in this week
	public List<String> getTopSearchItemThisWeek() {
		List<String> topSearch = new ArrayList<>();
		
		// Get monday and now time
		ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
		LocalDateTime now = LocalDateTime.now(vietnamZone);
		LocalDate today = LocalDate.now(vietnamZone);
		LocalDate monday = today.with(DayOfWeek.MONDAY);
		LocalDateTime start = LocalDateTime.of(monday, LocalTime.MIN);
		
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		
		try {
			SearchResponse<Void> response = elasticsearchClient.search(search -> search
					.index("search_logs")
					.size(0)
					.query(q -> q
							.range(range -> range
									.date(d -> d
											.field("searchedAt")
											.gte(formatter.format(start))
								            .lte(formatter.format(now))
									)
							)
					)
					.aggregations("popular_keywords", a -> a
							.terms(t -> t
									.field("keyword.keyword")
									.size(3)
							)
					)
					, Void.class);
			
			Aggregate agg = response.aggregations().get("popular_keywords");
			List<StringTermsBucket> listBucket = agg.sterms().buckets().array();
			
			for(StringTermsBucket bucket : listBucket) {
				topSearch.add(bucket.key().stringValue());
			}

		} catch (Exception e) {
			throw new RuntimeException("Can not search");
		}
		
		return topSearch;
	}
	
	// Get top 5 item sold
	public List<TopItemSoldResponse> getTopItemSell() {
		List<TopItemSoldResponse> topItem = new ArrayList<>();
		
		try {
			// Query
			SearchResponse<Void> response = elasticsearchClient.search(search -> search
					.index("order_items")
					.size(0)
					.aggregations("top_products", agg -> agg
							.terms(t -> t
									.field("name.keyword")
									.size(5)
									.order(List.of(NamedValue.of("total_sold", SortOrder.Desc)))
							)
							.aggregations("total_sold", a -> a
									.sum(sum -> sum.field("quantity")))
							.aggregations("top_hits", a -> a
									.topHits(th -> th
											.size(1)
											.source(s -> s
													.filter(f -> f.includes("name", "image", "price")))))
					)
					, Void.class);
			
			Aggregate agg = response.aggregations().get("top_products");
			List<StringTermsBucket> listBucket = agg.sterms().buckets().array();
			
			for(StringTermsBucket bucket : listBucket) {
				Integer totalSold = (int) bucket.aggregations().get("total_sold").sum().value();
				
				Hit<JsonData> topHit = bucket.aggregations().get("top_hits").topHits().hits().hits().get(0);
				
				OrderItemDocument doc = topHit.source().to(OrderItemDocument.class);
				topItem.add(new TopItemSoldResponse(doc.getName(), doc.getImage(), doc.getPrice(), totalSold));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Can not search");
		}
		
		return topItem;
	}
}
