package com.yzc.support.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别
 * @author tangcc
 * @date 2017年5月17日 上午10:44:07
 *
 */
public enum Gender {

    /** 1-男 **/
    MALE(1, "男"),

    /** 2-女 **/
    FEMALE(2, "女");

    private Integer value;

    private String desc;

    private Gender(Integer value, String desc) {
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
        Map<Integer, String> result = new HashMap<>();
        for (Gender v : Gender.values()) {
            result.put(v.getValue(), v.getDesc());
        }
        return result;
    }

    public static String getDesc(Integer value) {
        if (value == null) {
            return "";
        }
        for (Gender v : Gender.values()) {
            if (v.getValue().equals(value)) {
                return v.getDesc();
            }
        }
        return "";
    }

}
