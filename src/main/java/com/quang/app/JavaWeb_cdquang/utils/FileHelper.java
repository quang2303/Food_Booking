package com.quang.app.JavaWeb_cdquang.utils;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.quang.app.JavaWeb_cdquang.entity.Order;

public class FileHelper {
	final static int MAX_IMAGE_SIZE = 5000000;
	
	public static boolean checkImageType(String contentType) {
		return contentType.equals("image/png") || contentType.equals("image/jpg") || contentType.equals("image/jpeg");
	}

	public static boolean checkImageSize(long sizeImage) {
		return sizeImage > MAX_IMAGE_SIZE;
	}

	public static void deleteImage(String fileName) {
		try {
			Path path = Paths.get("uploads", fileName).toAbsolutePath();
			File file = path.toFile();
			if (file.exists()) {
				boolean deleted = file.delete();
				if(!deleted) {
					throw new RuntimeException("Failed to delete file");
				}
			} else {
				throw new RuntimeException("File not found");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in delete file: " + fileName, e);
		}
	}
	
	public static void renameImage(String oldFileName, String newFileName) {
		try {
			Path oldPath = Paths.get("uploads", oldFileName).toAbsolutePath();
			File oldFile = oldPath.toFile();
			
			Path newPath = Paths.get("uploads", newFileName).toAbsolutePath();
			File newFile = newPath.toFile();

			boolean renamed = oldFile.renameTo(newFile);
			
			if(!renamed) {
				throw new RuntimeException("Failed to rename file");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in rename file: " + oldFileName, e);
		}
	}

	public static void saveImage(MultipartFile image, String imageName) {
		Path path = Paths.get("uploads", imageName);

		try {
			Files.write(path, image.getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Error in save file: " + imageName, e);
		}
	}
	
	public static void exportRevenueReport(List<Order> orders, LocalDate from, LocalDate to) {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Revenue Report");
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Order Id");
		header.createCell(1).setCellValue("Total Amount");
		header.createCell(2).setCellValue("Status");
		header.createCell(3).setCellValue("Created At");
		
		int rowNum = 1;
		
		for(Order order : orders) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(order.getId());
			row.createCell(1).setCellValue(order.getTotalPrice());
			row.createCell(2).setCellValue(order.getStatus().toString());
			row.createCell(3).setCellValue(order.getCreateAt());
		}
		
		String fileName = String.format("weekly_revenue_%s_to_%s.xlsx", from, to);
		
		File reportDir = new File("reports");
		
		if(!reportDir.exists()) {
			reportDir.mkdirs();
		}
		
		try (FileOutputStream fileOut = new FileOutputStream("reports/" + fileName)) {
			workbook.write(fileOut);
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
