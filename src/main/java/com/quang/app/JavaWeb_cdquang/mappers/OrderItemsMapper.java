package com.quang.app.JavaWeb_cdquang.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.quang.app.JavaWeb_cdquang.dto.ItemsOrderRequest;

@Mapper
public interface OrderItemsMapper {
	public void createOrderItems(@Param("items") List<ItemsOrderRequest> items, @Param("orderId") int orderId);
}
