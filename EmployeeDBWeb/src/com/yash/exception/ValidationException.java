package com.yash.exception;
public class ValidationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;
	public ValidationException(String message) {
		super();
		this.message = message;
	}
	@Override
	public String getMessage() {
		return message;
	}	
}
