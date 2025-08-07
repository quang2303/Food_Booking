package com.quang.app.JavaWeb_cdquang.mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.quang.app.JavaWeb_cdquang.dto.InfoOrderEmail;
import com.quang.app.JavaWeb_cdquang.entity.Order;
import com.quang.app.JavaWeb_cdquang.entity.OrderStatus;

@Mapper
public interface OrderMapper {
	public void createOrder(Order order);

	public Order getOrderById(@Param("orderId") Integer id);

	public List<Order> findAllOrder(@Param("nameSearch") String nameSearch,
			@Param("listStatus") List<OrderStatus> listStatus, @Param("time") String time, @Param("limit") Integer limit,
			@Param("offset") Integer offset);
	
	public int countFilteredOrders(@Param("nameSearch") String nameSearch,
			@Param("listStatus") List<OrderStatus> listStatus, @Param("time") String time);

	public int countOrderByStatus(String status);

	public long sumTodaySale();
	
	public void updateStatus(@Param("id") Integer orderId, @Param("status") OrderStatus status);
	
	public List<Map<String, Object>> getOrderStats(String time);
	
	public List<Order> getAllOrder();
	
	public InfoOrderEmail getInfoOrderEmail(Integer id);
	
	public List<Order> getRevenueOrder(@Param("start") LocalDateTime from, @Param("end") LocalDateTime to);
}
