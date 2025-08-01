package com.quang.app.JavaWeb_cdquang.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.quang.app.JavaWeb_cdquang.service.ElasticItemService;
import com.quang.app.JavaWeb_cdquang.service.ElasticOrderService;

@Component
public class DataInitializer {

	@Autowired
	ElasticItemService elasticItemService;
	
	@Autowired
	ElasticOrderService elasticOrderService;

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws IOException {
		try {
			elasticItemService.syncDataToElasticsearch();
			elasticOrderService.syncDataToElasticsearch();
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Sync data error", e);
		}
	}
}
