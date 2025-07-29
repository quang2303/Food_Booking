package com.quang.app.JavaWeb_cdquang.service;

import java.util.List;
import java.util.Map;

import com.quang.app.JavaWeb_cdquang.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quang.app.JavaWeb_cdquang.dto.OrderRequest;
import com.quang.app.JavaWeb_cdquang.dto.OrderResponse;
import com.quang.app.JavaWeb_cdquang.dto.PageResponse;
import com.quang.app.JavaWeb_cdquang.dto.UpdateOrderRequest;
import com.quang.app.JavaWeb_cdquang.dto.FilterOrderRequest;
import com.quang.app.JavaWeb_cdquang.dto.InfoDasboardResponse;
import com.quang.app.JavaWeb_cdquang.dto.OrderDetailResponse;
import com.quang.app.JavaWeb_cdquang.entity.Order;
import com.quang.app.JavaWeb_cdquang.entity.OrderStatus;
import com.quang.app.JavaWeb_cdquang.exception.OperationFailedException;
import com.quang.app.JavaWeb_cdquang.mappers.OrderItemsMapper;
import com.quang.app.JavaWeb_cdquang.mappers.OrderMapper;
import com.quang.app.JavaWeb_cdquang.utils.ConvertHelper;
import com.quang.app.JavaWeb_cdquang.utils.FormatHelper;

@Service
public class OrderService {
	private final String DAY = "day";
	private final String WEEK = "week";
	private final String MONTH = "month";

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	OrderItemsMapper orderItemsMapper;

	@Transactional
	public Integer createOrder(OrderRequest orderInfo) {
		System.out.println(orderInfo);
		Order order = ConvertHelper.dtoToOrder(orderInfo);

		try {
			orderMapper.createOrder(order);
		} catch (Exception e) {
			throw new OperationFailedException("Create order is failed");
		}

		try {
			orderItemsMapper.createOrderItems(orderInfo.getItems(), order.getId());
		} catch (Exception e) {
			throw new OperationFailedException("Create items order is failed");
		}

		return order.getId();
	}

	public OrderDetailResponse getOrder(Integer id) {
		Order order = orderMapper.getOrderById(id);
		if (order == null) {
			throw new NotFoundException("Not found order id: " + id);
		}
		OrderDetailResponse orderResponse = ConvertHelper.orderToDto(order);
		return orderResponse;
	}

	public InfoDasboardResponse getInfoDashboard() {
		long totalSale = orderMapper.sumTodaySale();
		int totalShipping = orderMapper.countOrderByStatus("Shipping");
		int totalCompleted = orderMapper.countOrderByStatus("Completed");
		int totalNew = orderMapper.countOrderByStatus("New");
		int totalCanceled = orderMapper.countOrderByStatus("Canceled");

		int totalOrder = totalShipping + totalCanceled + totalCompleted + totalNew;

		return new InfoDasboardResponse(totalSale, totalOrder, totalShipping, totalCompleted, totalCanceled);
	}

	public PageResponse<OrderResponse> filterOrders(FilterOrderRequest orderFilter) {
		int limit = orderFilter.getSize();
		int currentPage = orderFilter.getPage();
		int offset = (currentPage - 1) * limit;

		List<Order> listOrder = orderMapper.findAllOrder(orderFilter.getNameSearch(), orderFilter.getListStatus(),
				orderFilter.getTime(), limit, offset);

		List<OrderResponse> listOrderResponse = ConvertHelper.orderToDto(listOrder);
		Integer totalOrders = orderMapper.countFilteredOrders(orderFilter.getNameSearch(), orderFilter.getListStatus(),
				orderFilter.getTime());
		Integer totalPages = (int) Math.ceil((double) totalOrders / limit);

		return new PageResponse<>(listOrderResponse, totalOrders, totalPages);
	}

	@Transactional
	public void updateStatusOrder(UpdateOrderRequest orderRequest) {
		Order order = orderMapper.getOrderById(orderRequest.getOrderId());
		if (order == null) {
			throw new NotFoundException("Order not found");
		}

		OrderStatus currentStatus = order.getStatus();
		OrderStatus newStatus = orderRequest.getStatus();
		if (!currentStatus.canTransitionTo(newStatus)) {
			throw new IllegalArgumentException("Invalid status transition: " + currentStatus + " -> " + newStatus);
		}

		try {
			orderMapper.updateStatus(orderRequest.getOrderId(), newStatus);
		} catch (Exception e) {
			throw new OperationFailedException("Update status order is failed");
		}
	}

	public List<Map<String, Object>> getOrderStats(String time) {
		if (DAY.equals(time)) {
			List<Map<String, Object>> stats = orderMapper.getOrderStats(time);
			return FormatHelper.formatDay(stats);
		} else if (WEEK.equals(time)) {
			List<Map<String, Object>> stats = orderMapper.getOrderStats(time);
			return FormatHelper.formatWeek(stats);
		} else if (MONTH.equals(time)) {
			List<Map<String, Object>> stats = orderMapper.getOrderStats(time);
			return FormatHelper.formatMonth(stats);
		} else {
			throw new IllegalArgumentException("Invalid time to get stats: " + time);
		}
	}
}
