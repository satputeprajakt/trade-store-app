package com.db.tradeStore.exception;

public class APIException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private Exception exception;
	
	public APIException(String message) {
		super(message);
	}
	
	public APIException(String message, Exception e) {
		super(message);
		this.exception = e;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public void setException(Exception exception) {
		this.exception = exception;
	}
}