package com.yzc.support.statics;

import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * @title 系统中使用到的常量
 * @author yzc
 * @version 1.0
 * @date 2016年9月30日
 */
public class Constant {
    /**
     * 私有化构造函数，不允许实例化该类
     */
    private Constant() {
    }
    
    
    /**
     * 默认的时区，中国、东八区
     */
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+8");

    /**
     * 默认的字符集名称
     */
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    /**
     * 默认的字符集
     */
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    
    /**
     * 混排resType标识
     */
    public static String RESTYPE_EDURESOURCE = "eduresource";
    
    /**
     * limit允许的最大查询数量
     */
    public static final int MAX_LIMIT = 500;

    /**
     * 预览图key前置
     */
    public static String RESOURCE_PREIVEW_PREFIX = "Slide";
        
}

