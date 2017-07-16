package com.yzc.exception.repositoryException;

public class EspStoreException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;

	public EspStoreException() {
		super();
	}

	public EspStoreException(String msg) {
		super(msg);
	}

	public EspStoreException(Exception e) {
		super(e.getMessage());
	}

	public EspStoreException(String code, String message) {
	
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
