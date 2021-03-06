package com.yzc.utils.collection;
/**
 * 集合的选择器
 *
 * @author bifeng.liu
 */
public interface CollectionSelector {
    /**
     * 对数据对象进行选择
     *
     * @param data
     * @return
     */
    Object select(Object data);
}
