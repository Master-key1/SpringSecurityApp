package com.nexe.exception;

public class UserAlreadyExitsException  extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

	public UserAlreadyExitsException(String message) {
		super(message);
	}

    

}
