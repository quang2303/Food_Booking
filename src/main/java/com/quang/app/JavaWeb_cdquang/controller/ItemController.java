package com.quang.app.JavaWeb_cdquang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quang.app.JavaWeb_cdquang.dto.ApiResponse;
import com.quang.app.JavaWeb_cdquang.dto.FilterItemsRequest;
import com.quang.app.JavaWeb_cdquang.dto.ItemRequest;
import com.quang.app.JavaWeb_cdquang.dto.PageResponse;
import com.quang.app.JavaWeb_cdquang.dto.UpdateItemRequest;
import com.quang.app.JavaWeb_cdquang.entity.Item;
import com.quang.app.JavaWeb_cdquang.service.ItemService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@PostMapping("/items")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<String>> createItem(@Valid @ModelAttribute ItemRequest dataItem) {
		itemService.createItem(dataItem);
		ApiResponse<String> response = new ApiResponse<>("success","Successfully", null);
		
		return ResponseEntity.status(201).body(response);
	}
	
	@GetMapping("/items")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<PageResponse<Item>>> filterItems(@ModelAttribute @Valid FilterItemsRequest filterItem) {
		PageResponse<Item> pageResponse = itemService.filterItems(filterItem);
		ApiResponse<PageResponse<Item>> response = new ApiResponse<>("success","Successfully", pageResponse);
		
		return ResponseEntity.status(200).body(response);
	}
	
	@DeleteMapping("/items")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<String>> deleteItem(@RequestParam @NotNull(message = "Id item must be not null") Integer id) {
		itemService.deleteItem(id);
		ApiResponse<String> response = new ApiResponse<>("success","Delete Successfully", null);
		
		return ResponseEntity.status(200).body(response);
	}
	
	@PutMapping("/items")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<String>> updateItem(@RequestParam @NotNull(message = "Id item must be not null") Integer id, @Valid @ModelAttribute UpdateItemRequest dataItem) {
		itemService.updateItem(id, dataItem);
		ApiResponse<String> response = new ApiResponse<>("success","Update Successfully", null);
		
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("/user/items")
	public ResponseEntity<ApiResponse<PageResponse<Item>>> filterItemsUser(@ModelAttribute @Valid FilterItemsRequest filterItem) {
		PageResponse<Item> pageResponse = itemService.filterItemsUser(filterItem);
		ApiResponse<PageResponse<Item>> response = new ApiResponse<>("success","Successfully", pageResponse);
		
		return ResponseEntity.status(200).body(response);
	}
}
