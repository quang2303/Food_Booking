package com.quang.app.JavaWeb_cdquang.utils;

import java.util.ArrayList;
import java.util.List;

import com.quang.app.JavaWeb_cdquang.dto.*;
import com.quang.app.JavaWeb_cdquang.entity.*;

public class ConvertHelper {
	public static Item dtoToItem(ItemRequest itemRequest, String image) {
		Item item = new Item();
		item.setDescription(itemRequest.getDescription());
		item.setImage(image);
		item.setName(itemRequest.getName());
		item.setPrice(itemRequest.getPrice());
		item.setType(itemRequest.getType());

		return item;
	}

	public static Item dtoToItem(int id, UpdateItemRequest itemRequest, String image) {
		Item item = new Item();
		item.setId(id);
		item.setDescription(itemRequest.getDescription());
		item.setImage(image);
		item.setName(itemRequest.getName());
		item.setPrice(itemRequest.getPrice());
		item.setType(itemRequest.getType());

		return item;
	}

	public static Order dtoToOrder(OrderRequest orderInfo) {
		Order order = new Order();
		order.setAddress(orderInfo.getAddress());
		order.setName(orderInfo.getName());
		order.setPhone(orderInfo.getPhone());
		order.setMessage(orderInfo.getMessage());
		order.setTotalPrice(orderInfo.getTotalPrice());
		order.setFeeShip(orderInfo.getFeeShip());

		return order;
	}

	public static OrderDetailResponse orderToDto(Order order) {
		OrderDetailResponse orderResponse = new OrderDetailResponse();
		orderResponse.setId(order.getId());
		orderResponse.setName(order.getName());
		orderResponse.setMessage(order.getMessage());
		orderResponse.setPhone(order.getPhone());
		orderResponse.setAddress(order.getAddress());
		orderResponse.setTotalPrice(order.getTotalPrice());
		orderResponse.setFeeShip(order.getFeeShip());
		orderResponse.setStatus(order.getStatus().getStatusString());
		orderResponse.setCreateAt(FormatHelper.formatDateOrder(order.getCreateAt()));

		List<ItemOrderResponse> listItemOrder = new ArrayList<>();
		List<Item> items = order.getItems();

		for (Item item : items) {
			OrderItems orderItems = item.getOrderItem();
			listItemOrder.add(new ItemOrderResponse(item.getName(), item.getDescription(), item.getImage(),
					item.getPrice(), orderItems.getQuantity()));
		}
		
		orderResponse.setItems(listItemOrder);
		
		return orderResponse;
	}
	
	public static List<OrderResponse> orderToDto(List<Order> listOrder) {
		List<OrderResponse> listOrderResponse = new ArrayList<>();
		
		for(Order order: listOrder) {
			OrderResponse orderResponse = new OrderResponse();
			orderResponse.setId(order.getId());
			orderResponse.setStatus(order.getStatus().getStatusString());
			orderResponse.setCreateAt(FormatHelper.formatDateOrder(order.getCreateAt()));
			orderResponse.setTotalPrice(order.getTotalPrice());
			orderResponse.setItemCount(order.getItemCount());
			
			Item item = order.getItem();
			ItemOrderResponse itemOrderResponse = new ItemOrderResponse();
			itemOrderResponse.setName(item.getName());
			itemOrderResponse.setDescription(item.getDescription());
			itemOrderResponse.setImage(item.getImage());
			itemOrderResponse.setPrice(item.getPrice());
			itemOrderResponse.setQuantity(item.getOrderItem().getQuantity());
			
			orderResponse.setItem(itemOrderResponse);
			
			listOrderResponse.add(orderResponse);
		}
		
		return listOrderResponse;
	}
}
