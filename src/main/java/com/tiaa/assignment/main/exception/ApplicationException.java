package com.tiaa.assignment.main.exception;

/**
 * Exception class for invalid input data provided
 * 
 * @author Anks
 *
 */
public final class ApplicationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}
}