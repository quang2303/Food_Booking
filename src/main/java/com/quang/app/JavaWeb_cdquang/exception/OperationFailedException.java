package com.quang.app.JavaWeb_cdquang.exception;

public class OperationFailedException extends RuntimeException {

	private static final long serialVersionUID = 1044414364594366458L;

	public OperationFailedException(String message) {
		super(message);
	}

}
