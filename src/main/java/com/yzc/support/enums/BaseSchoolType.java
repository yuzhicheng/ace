package com.yzc.support.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 基地校类别：1-区县基地校，2-地市基地校
 * @author tangcc
 * @date 2017年5月17日 下午5:13:05
 *
 */
public enum BaseSchoolType {

    /** 1-区县基地校 **/
    BASE_SCHOOL_AREA(1, "区县基地校"),

    /** 2-地市基地校 **/
    BASE_SCHOOL_CITY(2, "地市基地校");

    private Integer value;

    private String desc;

    private BaseSchoolType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static Map<Integer, String> getMap() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (BaseSchoolType v : BaseSchoolType.values()) {
            result.put(v.getValue(), v.getDesc());
        }
        return result;
    }

    public static String getDesc(Integer value) {
        for (BaseSchoolType v : BaseSchoolType.values()) {
            if (v.getValue().equals(value)) {
                return v.getDesc();
            }
        }
        return "";
    }
}
