package com.nexe.securityapp.dto;

public class ApiResponse {
	
	private boolean sucess;
	private String massage;
	
	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponse(boolean sucess, String massage) {
		super();
		this.sucess = sucess;
		this.massage = massage;
	}

	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}

	public String getMassage() {
		return massage;
	}

	public void setMassage(String massage) {
		this.massage = massage;
	}
	
	

}
