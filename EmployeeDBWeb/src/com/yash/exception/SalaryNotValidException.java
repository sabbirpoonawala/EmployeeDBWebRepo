package com.yash.exception;

public class SalaryNotValidException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String message;
	public SalaryNotValidException(String message) {
		super();
		this.message = message;
	}
    @Override
	public String getMessage() {
		return message;
	}
}
