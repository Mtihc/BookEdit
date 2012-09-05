package com.mtihc.bookedit.v1;

public class BookException extends Exception {

	private static final long serialVersionUID = -2381484387971591560L;

	public BookException() {
		
	}

	public BookException(String msg) {
		super(msg);
	}

	public BookException(Throwable cause) {
		super(cause);
	}

	public BookException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
