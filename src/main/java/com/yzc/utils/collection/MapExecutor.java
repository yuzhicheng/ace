package com.yzc.utils.collection;

/**
 * Map执行器，用于在For循环时的执行器
 *
 * @author yzc
 */
public interface MapExecutor {
    /**
     * 执行
     *
     * @param key
     * @param value
     */
    void execute(Object key, Object value);
}
