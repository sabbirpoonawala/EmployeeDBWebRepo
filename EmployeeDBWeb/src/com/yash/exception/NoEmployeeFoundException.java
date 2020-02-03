package com.yash.exception;

public class NoEmployeeFoundException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;
	public NoEmployeeFoundException(String message) {
		this.message=message;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
