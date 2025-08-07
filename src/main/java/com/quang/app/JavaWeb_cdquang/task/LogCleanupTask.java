package com.quang.app.JavaWeb_cdquang.task;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteByQueryRequest;
import co.elastic.clients.elasticsearch.core.DeleteByQueryResponse;

@Component
public class LogCleanupTask {

	@Autowired
	private ElasticsearchClient elasticsearchClient;


	// Run  23:59
	@Scheduled(cron = "0 59 23 * * SUN", zone = "Asia/Ho_Chi_Minh")
	@Async
	public void cleanupLogs() {
		try {
			// Calculation 0h monday viet nam
			ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
			LocalDate startOfWeek = LocalDate.now(vietnamZone).with(DayOfWeek.MONDAY);
			Instant weekStartInstant = startOfWeek.atStartOfDay(vietnamZone).toInstant();
			long timestamp = weekStartInstant.toEpochMilli();


			// Query delete
			DeleteByQueryRequest request = new DeleteByQueryRequest.Builder().index("search_logs")
					.query(q -> q
							.range(range -> range
									.date(d -> d
											.field("searchedAt")
											.lt(String.valueOf(timestamp))))).build();

			DeleteByQueryResponse response = elasticsearchClient.deleteByQuery(request);
			System.out.println("Deleted logs before " + startOfWeek + ". Deleted count: " + response.deleted());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
