/* =============================================================
 * Created: [2015/8/3 15:07] by wuzj(971643)
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */
package com.yzc.mongo.search;

/**
 *
 * Created by yzc on 2017/7/15.
 */
public enum Operator {
    eq("is"),
    like("regex"),
    gt("gt"),
    ge("gte"),
    lt("lt"),
    le("lte"),
    ne("ne"),
    in("in"),;
    private String value;

    Operator(String value) {
        this.value = value;
    }

    public static String getValue(String key) {
        Operator operator = Operator.valueOf(key);
        return operator.value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
