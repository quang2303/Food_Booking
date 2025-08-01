package com.quang.app.JavaWeb_cdquang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.quang.app.JavaWeb_cdquang.dto.FilterItemsRequest;
import com.quang.app.JavaWeb_cdquang.dto.ItemRequest;
import com.quang.app.JavaWeb_cdquang.dto.PageResponse;
import com.quang.app.JavaWeb_cdquang.dto.UpdateItemRequest;
import com.quang.app.JavaWeb_cdquang.entity.Item;
import com.quang.app.JavaWeb_cdquang.exception.NotFoundException;
import com.quang.app.JavaWeb_cdquang.exception.OperationFailedException;
import com.quang.app.JavaWeb_cdquang.mappers.ItemMapper;
import com.quang.app.JavaWeb_cdquang.utils.ConvertHelper;
import com.quang.app.JavaWeb_cdquang.utils.FileHelper;

@Service
public class ItemService {
	private final String TYPE_FOOD = "FOOD";
	private final String TYPE_DRINK = "DRINK";
	
	@Autowired
	ItemMapper itemMapper;
	
	@Autowired
	SearchService searchService;
	
	@Autowired
	ElasticItemService elasticItemService;

	@Transactional
	public void createItem(ItemRequest item) {
		MultipartFile image = item.getImage();

		String imageName = item.getName().replace(" ", "-") + "." + image.getContentType().replace("image/", "");
		
		if(!TYPE_FOOD.equals(item.getType()) && !TYPE_DRINK.equals(item.getType())) {
			throw new IllegalArgumentException("Invalid type item: " + item.getType());
		}

		if (itemMapper.findItemByName(item.getName())) {
			throw new OperationFailedException("Item name is exist");
		}

		FileHelper.saveImage(image, imageName);

		Item itemCreate = ConvertHelper.dtoToItem(item, imageName);

		try {
			itemMapper.createItem(itemCreate);
		} catch (Exception e) {
			throw new OperationFailedException("Create is failed");
		}
		
		Item itemAdded = itemMapper.getItemById(itemCreate.getId());
		elasticItemService.saveItemToElasticsearch(itemAdded);
	}

	public PageResponse<Item> filterItems(FilterItemsRequest filterItem) {
		int limit = filterItem.getSize();
		int currentPage = filterItem.getPage();
		int offset = (currentPage - 1) * limit;

		List<Item> items = itemMapper.filterItems(filterItem.getName(), filterItem.getType(), limit, offset);
		Integer totalItems = itemMapper.countFilteredItems(filterItem.getName(), filterItem.getType());
		Integer totalPages = (int) Math.ceil((double) totalItems / limit);

		return new PageResponse<>(items, totalItems, totalPages);
	}
	
	public PageResponse<Item> filterItemsUser(FilterItemsRequest filterItem) {
		int limit = filterItem.getSize();
		int currentPage = filterItem.getPage();
		int offset = (currentPage - 1) * limit;

		List<Item> items = itemMapper.filterItemsUser(filterItem.getName(), filterItem.getType(), limit, offset,
				filterItem.getSortByPrice());
		Integer totalItems = itemMapper.countFilteredItemsUser(filterItem.getName(), filterItem.getType());
		Integer totalPages = (int) Math.ceil((double) totalItems / limit);
		
		searchService.saveSearchLog(filterItem.getName());

		return new PageResponse<>(items, totalItems, totalPages);
	}

	@Transactional
	public void deleteItem(Integer id) {
		Item item = itemMapper.getItemById(id);
		if(item == null) {
			throw new NotFoundException("Item not found: " + id);
		}
		if(!item.getStatus()) {
			throw new OperationFailedException("This item is deleted, can not delete again");
		}
		try {
			itemMapper.deleteItem(id);
			Item itemUpdated = itemMapper.getItemById(id);
			elasticItemService.saveItemToElasticsearch(itemUpdated);
		} catch (Exception e) {
			throw new OperationFailedException("Delete is failed");
		}
	}

	@Transactional
	public void updateItem(Integer id, UpdateItemRequest item) {
		MultipartFile image = item.getImage();
		
		if(!TYPE_FOOD.equals(item.getType()) && !TYPE_DRINK.equals(item.getType())) {
			throw new IllegalArgumentException("Invalid type item: " + item.getType());
		}
		
		Item oldItem = itemMapper.getItemById(id);
		if(oldItem == null) {
			throw new NotFoundException("Item not found: " + id);
		}
		
		if(!oldItem.getStatus()) {
			throw new OperationFailedException("Item is deleted, can not update");
		}
		
		String imageName;

		if (itemMapper.checkNameItemExisted(item.getName(), id)) {
			throw new OperationFailedException("Item name is existed");
		}
		
		String oldNameImage = oldItem.getImage();

		// Check image
		if ( image == null) {
			//No image -> rename name
			imageName = item.getName().replace(" ", "-") + oldNameImage.substring(oldNameImage.indexOf("."), oldNameImage.length());
			FileHelper.renameImage(oldNameImage, imageName);
			
		} else {
			// Has image -> change file 
			imageName = item.getName().replace(" ", "-") + "." + image.getContentType().replace("image/", "");
			FileHelper.deleteImage(oldItem.getImage());
			FileHelper.saveImage(image, imageName);
		}

		Item itemCreate = ConvertHelper.dtoToItem(id, item, imageName);

		try {
			itemMapper.updateItem(itemCreate);
		} catch (Exception e) {
			throw new OperationFailedException("Update is failed");
		}
		
		Item itemUpdated = itemMapper.getItemById(id);
		elasticItemService.saveItemToElasticsearch(itemUpdated);
	}
}
