package com.quang.app.JavaWeb_cdquang.controller;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quang.app.JavaWeb_cdquang.dto.*;
import com.quang.app.JavaWeb_cdquang.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	OrderService orderService;
	
	@PostMapping("/orders")
	public ResponseEntity<ApiResponse<Integer>> createOrder(@RequestBody @Valid OrderRequest orderRequest) {
		Integer orderId = orderService.createOrder(orderRequest);
		ApiResponse<Integer> response = new ApiResponse<>("success","Successfully", orderId);
		
		return ResponseEntity.status(201).body(response);
	}
	
	@GetMapping("/orders/{id}")
	public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrder(@PathVariable Integer id) {
		OrderDetailResponse order = orderService.getOrder(id);
		ApiResponse<OrderDetailResponse> response = new ApiResponse<>("success","Successfully", order);
		
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("/orders/info")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<InfoDasboardResponse>> getInfoDashboard() {
		InfoDasboardResponse order = orderService.getInfoDashboard();
		ApiResponse<InfoDasboardResponse> response = new ApiResponse<>("success","Successfully", order);
		
		return ResponseEntity.status(200).body(response);
	}
	
	@GetMapping("/orders")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getAllOrder(@ModelAttribute @Valid FilterOrderRequest orderRequest) {
		PageResponse<OrderResponse> listOrder = orderService.filterOrders(orderRequest);
		ApiResponse<PageResponse<OrderResponse>> response = new ApiResponse<>("success","Successfully", listOrder);
		
		return ResponseEntity.status(200).body(response);
		
	}
	
	@PutMapping("/orders")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<String>> updateStatusOrder(@ModelAttribute @Valid UpdateOrderRequest orderRequest) throws NotFoundException {
		orderService.updateStatusOrder(orderRequest);
		ApiResponse<String> response = new ApiResponse<>("success","Successfully", null);
		
		return ResponseEntity.status(200).body(response);
		
	}
	
	@GetMapping("/orders/stats")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOrderStats(@RequestParam String time) {
		List<Map<String, Object>> listOrder = orderService.getOrderStats(time);
		ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>("success","Successfully", listOrder);
		
		return ResponseEntity.status(200).body(response);
		
	}
}
