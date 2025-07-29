package com.quang.app.JavaWeb_cdquang.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

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
}
