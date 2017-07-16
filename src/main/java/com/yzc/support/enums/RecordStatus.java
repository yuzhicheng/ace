package com.yzc.support.enums;

import com.yzc.utils.StringUtils;

public enum RecordStatus {
	
	REMOVED(0,"REMOVED"),
	NORMAL(1,"NORMAL"),
	;
	
	private RecordStatus(int state,String code){
		
		this.state=state;
		this.code=code;
		
	}
	
	private int state;
	
	private String code;
	
	
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
	
	public  int getValue(){
		if (StringUtils.hasText(code)) {
			for (RecordStatus type : RecordStatus.values()) {
				if (type.getCode().equals(code)) {
					return type.getState();
				}
			}
		}
		return 1;
	}
	
}
