package com.quang.app.JavaWeb_cdquang.entity;

public enum OrderStatus {
	NEW("New"),COMPLETED("Completed"),CANCELED("Canceled"),SHIPPING("Shipping");
	
	private final String status;

	OrderStatus(String status) {
		this.status = status;
	}
	
	public boolean canTransitionTo(OrderStatus next) {
		switch (this) {
		case NEW: 
			return next == SHIPPING || next == CANCELED;
		case SHIPPING:
			return next == COMPLETED || next == CANCELED;
		default:
			return false;
		}
	}
	
	public String getStatusString() {
		return this.status;
	}
}
