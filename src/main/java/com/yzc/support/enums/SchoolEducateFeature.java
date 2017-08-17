package com.yzc.support.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 育人特色
 * @author tangcc
 * @date 2017年5月17日 上午10:22:13
 *
 */
public enum SchoolEducateFeature {

    /** 1:师生关系 **/
    TEACHER_STUDENT_RELATION(1, "师生关系"),

    /** 2:教书育人 **/
    TEACHING_AND_EDUCATE(2, "教书育人"),

    /** 4:班级建设 **/
    CLASS_BUILDING(4, "班级建设"),

    /** 8:典型学生问题 **/
    STUDENT_PROBLEM(8, "典型学生问题"),

    /** 16:教师自我发展 **/
    TEACHER_DEVELOPMENT(16, "教师自我发展"),

    /** 32: 其他 **/
    other(32, "其他");

    private Integer value;

    private String desc;

    private SchoolEducateFeature(Integer value, String desc) {
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
        for (SchoolEducateFeature v : SchoolEducateFeature.values()) {
            result.put(v.getValue(), v.getDesc());
        }
        return result;
    }

    public static String getDesc(Integer value) {
        if (value == null) {
            return "";
        }
        String desc = "";
        for (SchoolEducateFeature v : SchoolEducateFeature.values()) {
            if (v.equals(SchoolEducateFeature.other) && value == (value | v.getValue())) {
                desc += v.getDesc() + ":{desc},";
                continue;
            }
            if (value.equals((value | v.getValue()))) {
                desc += v.getDesc() + ",";
            }
        }

        return desc;
    }
}
