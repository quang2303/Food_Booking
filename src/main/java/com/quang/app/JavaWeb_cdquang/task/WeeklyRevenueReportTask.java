package com.quang.app.JavaWeb_cdquang.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.quang.app.JavaWeb_cdquang.entity.Order;
import com.quang.app.JavaWeb_cdquang.mappers.OrderMapper;
import com.quang.app.JavaWeb_cdquang.utils.FileHelper;

@Component
public class WeeklyRevenueReportTask {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Scheduled(cron = "0 0 17 * * SUN")
	public void generateReport() {
		ZoneId vnZone = ZoneId.of("\"Asia/Ho_Chi_Minh\"");
		
		LocalDateTime now = LocalDateTime.now(vnZone);
		LocalDate monday = now.toLocalDate().with(DayOfWeek.MONDAY);
		LocalDateTime from = LocalDateTime.of(monday, LocalTime.MIN);
		LocalDateTime to = now;
		
		List<Order> orders = orderMapper.getRevenueOrder(from, to);
		
		FileHelper.exportRevenueReport(orders, from.toLocalDate(), to.toLocalDate());
	}
}
