package com.nexe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nexe.securityapp.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserAlreadyExitsException.class)
	public ResponseEntity<ApiResponse> handleUserAlreadyExistsException(UserAlreadyExitsException ex){
		
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setSucess(false);
		apiResponse.setMassage(ex.getMessage());
        return  ResponseEntity
        		.status( HttpStatus.CONFLICT)
        		.body(apiResponse);

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex){
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setSucess(false);
		apiResponse.setMassage("Internal Server Error......!");
		
		return  ResponseEntity
        		.status( HttpStatus.INTERNAL_SERVER_ERROR)
        		.body(apiResponse);
	}
}
