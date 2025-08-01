package com.quang.app.JavaWeb_cdquang.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quang.app.JavaWeb_cdquang.dto.ApiResponse;
import com.quang.app.JavaWeb_cdquang.dto.TopItemSoldResponse;
import com.quang.app.JavaWeb_cdquang.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {
	
	@Autowired
	SearchService searchService;

	@GetMapping("/top_search")
	public ResponseEntity<ApiResponse<List<String>>> getTopSearch() {
		List<String> listTop = searchService.getTopSearchItemThisWeek();
		ApiResponse<List<String>> response = new ApiResponse<>("success","Successfully", listTop);
		
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("/top_sold")
	public ResponseEntity<ApiResponse<List<TopItemSoldResponse>>> getTopSold() {
		List<TopItemSoldResponse> listTop = searchService.getTopItemSell();
		ApiResponse<List<TopItemSoldResponse>> response = new ApiResponse<>("success","Successfully", listTop);
		
		return ResponseEntity.status(200).body(response);
	}
}
