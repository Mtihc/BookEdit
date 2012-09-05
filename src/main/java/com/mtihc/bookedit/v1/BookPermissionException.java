package com.mtihc.bookedit.v1;

public class BookPermissionException extends BookException {

	private static final long serialVersionUID = -2381484387971591550L;

	public BookPermissionException() {
		
	}

	public BookPermissionException(String msg) {
		super(msg);
	}

	public BookPermissionException(Throwable cause) {
		super(cause);
	}

	public BookPermissionException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
