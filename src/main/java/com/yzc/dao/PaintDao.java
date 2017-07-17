package com.yzc.dao;

import com.yzc.entity.Paint;

import java.util.List;

public interface PaintDao {
    /**
     * 新增名画
     *
     * @param paint 名画数据
     * @return 名画
     */
    Paint savePaint(Paint paint);

    /**
     * 修改名画
     *
     * @param paint 名画数据
     * @return 名画
     */
    Paint updatePaint(Paint paint);

    /**
     * 删除名画
     *
     * @param id 名画id
     */
    void deletePaint(String id);

    /**
     * 获取名画详情
     *
     * @param id 名画id
     * @return 名画
     */
    Paint getPaint(String id);

    /**
     * 根据条件获取名画的总数
     *
     * @param title   标题
     * @param tags    标签
     * @param creator 创建者
     * @param userIds 作者id列表
     * @return 名画总数
     */
    long getCountByCond(String title, List<String> tags, String creator, List<String> userIds);

    /**
     * 根据条件查询名画列表
     *
     * @param title    标题
     * @param tags     标签
     * @param creator  创建者
     * @param userIds  作者id列表
     * @param offset   偏移量
     * @param pageSize 页数
     * @return 名画列表
     */
    List<Paint> queryListByCond(String title, List<String> tags, String creator,
                                List<String> userIds, int offset, int pageSize);

    /**
     * 判断id是否重复
     *
     * @param identifier 名画id
     * @return 名画记录数量
     */
    int queryCountByResId(String identifier);
}

