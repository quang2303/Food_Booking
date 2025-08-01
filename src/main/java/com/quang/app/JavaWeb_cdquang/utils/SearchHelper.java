package com.quang.app.JavaWeb_cdquang.utils;

import java.text.Normalizer;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class SearchHelper {
	private final static List<String> VALID_KEYWORDS = List.of(
			"Hủ tiếu", "Cơm chiên", "Cơm trứng", "Phở bò", "Gà rán", "Trà sữa", "Trà đào", "Trà lài", "Bánh canh", "Thịt kho"
			);
	
	private static final int MAX_DISTANCE = 3;
	
	public static String findClosestKeyword(String input) {
		LevenshteinDistance distance = new LevenshteinDistance();
		String closest = null;
		int minDistance = Integer.MAX_VALUE;
		
		String normalizedInput = normalize(input.toLowerCase());
		
		for(String keyword : VALID_KEYWORDS) {
			String normalizedKeyword = normalize(keyword);
			int distanceInput = distance.apply(normalizedInput, normalizedKeyword);
			if(distanceInput < minDistance && distanceInput <= MAX_DISTANCE) {
				minDistance = distanceInput;
				closest = keyword;
			}
		}
		return closest;
	}
	
	// Remove accents in the string input
	private static String normalize(String input) {
		String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
		return normalized.replaceAll("\\p{M}", "").replaceAll("đ", "d").replaceAll("Đ", "D");
	}
}
