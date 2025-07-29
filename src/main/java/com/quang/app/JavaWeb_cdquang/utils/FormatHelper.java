package com.quang.app.JavaWeb_cdquang.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FormatHelper {
	public static String formatDateOrder(LocalDateTime dateTime) {
		StringBuilder result = new StringBuilder();
		
		Integer date = dateTime.getDayOfMonth();
		
		Integer year = dateTime.getYear();
		
		Month month = dateTime.getMonth();
		
		result.append(date.toString() + " ");
		
		result.append(capitalizeWords(month.toString()) + " ");
		
		result.append(year.toString());
		
		return result.toString();
	}
	
	public static String capitalizeWords(String str) {
		String[] words = str.split("\\s+");
		StringBuilder capitalized = new StringBuilder();

		for (String word : words) {
			if (word.length() > 0) {
				capitalized.append(Character.toUpperCase(word.charAt(0)))
							.append(word.substring(1).toLowerCase())
							.append(" ");
			}
		}
		return capitalized.toString().trim();
	}
	
	public static List<Map<String, Object>> formatDay(List<Map<String, Object>> rawStats) {
		Map<String, BigDecimal> statMap = new HashMap<>();
		for (Map<String, Object> row : rawStats) {
			LocalDate date = ((Date) row.get("label")).toLocalDate();
			String label = String.valueOf(date.getDayOfMonth()) + "/" + capitalizeWords(String.valueOf(date.getMonth()));
			BigDecimal total = (BigDecimal) row.get("total");
			statMap.put(label, total);
		}

		List<Map<String, Object>> finalStats = new ArrayList<>();
		LocalDate today = LocalDate.now();
		for (int i = 6; i >= 0; i--) {
			LocalDate d = today.minusDays(i);
			String label = String.valueOf(d.getDayOfMonth()) + "/" + capitalizeWords(String.valueOf(d.getMonth()));
			BigDecimal total = statMap.getOrDefault(label, BigDecimal.ZERO);
			BigDecimal totalInMilion = total.divide(BigDecimal.valueOf(1_000_000), 3, RoundingMode.HALF_UP);

			finalStats.add(Map.of("label", label, "total", totalInMilion));
		}
		return finalStats;
	}
	
	public static List<Map<String, Object>> formatWeek(List<Map<String, Object>> rawStats) {
		Map<Integer, BigDecimal> statMap = new HashMap<>();
		for (Map<String, Object> row : rawStats) {
		    int yearWeek = (int) row.get("label");
		    BigDecimal total = (BigDecimal) row.get("total");
		    statMap.put(yearWeek, total);
		}

		List<Map<String, Object>> finalStats = new ArrayList<>();
		LocalDate today = LocalDate.now();
		WeekFields wf = WeekFields.ISO;

		for (int i = 11; i >= 0; i--) {
		    LocalDate weekStart = today.minusWeeks(i).with(wf.dayOfWeek(), 1); // Monday
		    int yearWeek = Integer.parseInt(weekStart.format(DateTimeFormatter.ofPattern("YYYYww")));
		    int weekNum = yearWeek % 100;
		    String label = "W" + weekNum;

		    BigDecimal total = statMap.getOrDefault(yearWeek, BigDecimal.ZERO);
		    BigDecimal totalInMilion = total.divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP);
		    finalStats.add(Map.of("label", label, "total", totalInMilion));
		}
		
		return finalStats;
	}
	
	public static List<Map<String, Object>> formatMonth(List<Map<String, Object>> rawStats) {
		Map<String, BigDecimal> statMap = new HashMap<>();
		for (Map<String, Object> row : rawStats) {
		    Long y = (Long) row.get("y");
		    int m = (int) row.get("m");
		    String key = y + "-" + m; // key to compare
		    BigDecimal total = (BigDecimal) row.get("total");
		    statMap.put(key, total);
		}

		List<Map<String, Object>> finalStats = new ArrayList<>();
		YearMonth current = YearMonth.now();
		for (int i = 11; i >= 0; i--) {
		    YearMonth ym = current.minusMonths(i);
		    String key = ym.getYear() + "-" + ym.getMonthValue();
		    String label = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear(); //Format month name
		    BigDecimal total = statMap.getOrDefault(key, BigDecimal.ZERO);
		    BigDecimal totalInMilion = total.divide(BigDecimal.valueOf(1_000_000), 2, RoundingMode.HALF_UP);

		    finalStats.add(Map.of("label", label, "total", totalInMilion));
		}
		return finalStats;
	}
}
