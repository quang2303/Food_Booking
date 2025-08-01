package com.quang.app.JavaWeb_cdquang.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.quang.app.JavaWeb_cdquang.entity.Item;

@Mapper
public interface ItemMapper {
	public boolean findItemByName(@Param("name") String name);
	
	public boolean checkNameItemExisted(@Param("name") String name, @Param("id") int id);

	public Item getItemById(@Param("id") int id);

	public void createItem(Item item);

	public List<Item> filterItems(@Param("name") String name, @Param("type") String type, @Param("limit") int limit,
			@Param("offset") int offset);
	
	public List<Item> filterItemsUser(@Param("name") String name, @Param("type") String type, @Param("limit") int limit,
			@Param("offset") int offset, @Param("sortByPrice") String sortType);

	public int countFilteredItems(@Param("name") String name, @Param("type") String type);
	public int countFilteredItemsUser(@Param("name") String name, @Param("type") String type);
	
	public void deleteItem(@Param("id") int id);
	
	public void updateItem(Item item);
	
	public List<Item> getAllItem();
}
