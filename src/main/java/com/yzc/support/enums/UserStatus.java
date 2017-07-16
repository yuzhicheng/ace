package com.yzc.support.enums;

public enum UserStatus {
	
	ONLINE(0,"online","在线"),
	BUSY(1,"busy","忙碌"),
	LEAVE(2,"leave","离开"),
	OFFLINE(3,"offline","下线"),
	INVISIBLE(4,"invisible","隐身"),
	;
	
	private UserStatus(int state,String code,String description){
		
		this.state=state;
		this.code=code;
		this.description=description;
		
	}
	
	private int state;
	
	private String code;
	
	private String description;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
