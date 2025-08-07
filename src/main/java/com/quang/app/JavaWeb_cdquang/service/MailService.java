package com.quang.app.JavaWeb_cdquang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Async // Send asynchronously
	public void sendOrderConfirmation(String toEmail, String subject, String content) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			
			helper.setFrom("chungducquang2@gmail.com");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(content, true);
			
			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Error when send email: " + e.getMessage(), e);
		}
	}
	
}
