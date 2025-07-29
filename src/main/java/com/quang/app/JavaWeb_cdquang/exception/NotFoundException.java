package com.quang.app.JavaWeb_cdquang.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6753741187201164263L;

	public NotFoundException(String message) {
		super(message);
	}
}
