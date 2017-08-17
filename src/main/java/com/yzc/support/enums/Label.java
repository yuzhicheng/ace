package com.yzc.support.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 标签 0表示基地校, 1表示项目校
 * @author Administrator
 *
 */
public enum Label {

    /** 0-基地校 **/
	BASE_SCHOOL(0, "基地校"),

    /** 1-项目校 **/
	PROJECT_SCHOOL(1, "项目校");
	
	private Integer value;
	private String desc;
	
	private Label(Integer value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	public static Map<Integer, String> getMap(){
		Map<Integer, String> result = new HashMap<Integer, String>();
		for(Label v : Label.values()){
			result.put(v.getValue(), v.getDesc());
		}
		return result;
	}
	
	public static String getDesc(Integer value){
		for(Label v : Label.values()){
			if(v.getValue() == value){
				return v.getDesc();
			}
		}
		return "";
	}
	
	
}
