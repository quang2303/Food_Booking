package com.quang.app.JavaWeb_cdquang.utils;

import java.util.ArrayList;
import java.util.List;

import com.quang.app.JavaWeb_cdquang.document.ItemDocument;
import com.quang.app.JavaWeb_cdquang.document.OrderDocument;
import com.quang.app.JavaWeb_cdquang.document.OrderItemDocument;
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
	
	public static ItemDocument itemToDoc(Item item) {
		ItemDocument itemDoc = new ItemDocument();
		itemDoc.setId(item.getId());
		itemDoc.setName(item.getName());
		itemDoc.setDescription(item.getDescription());
		itemDoc.setImage(item.getImage());
		itemDoc.setPrice(item.getPrice());
		itemDoc.setStatus(item.getStatus());
		itemDoc.setType(item.getType());
		itemDoc.setCreateAt(item.getCreateAt().toString());
		itemDoc.setUpdateAt(item.getUpdateAt().toString());
		
		return itemDoc;
	}
	
	public static OrderDocument orderToDoc(Order order) {
		OrderDocument orderDoc = new OrderDocument();
		orderDoc.setId(order.getId());
		orderDoc.setName(order.getName());
		orderDoc.setMessage(order.getMessage());
		orderDoc.setPhone(order.getPhone());
		orderDoc.setAddress(order.getAddress());
		orderDoc.setTotalPrice(order.getTotalPrice());
		orderDoc.setFeeShip(order.getFeeShip());
		orderDoc.setStatus(order.getStatus().getStatusString());
		orderDoc.setCreateAt(order.getCreateAt().toString());
		
		List<OrderItemDocument> listItemOrder = new ArrayList<>();
		List<Item> items = order.getItems();
		
		for (Item item : items) {
			OrderItems orderItems = item.getOrderItem();
			String orderItemId = order.getId() + "_" + item.getId();
			listItemOrder.add(new OrderItemDocument(orderItemId, item.getName(), item.getDescription(), item.getImage(),
					item.getPrice(), orderItems.getQuantity()));
		}
		
		orderDoc.setItems(listItemOrder);
		
		return orderDoc;
	}
}
