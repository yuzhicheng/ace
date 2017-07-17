package com.yzc.dao;

import java.util.List;

import com.yzc.entity.PaintAuthor;
import com.yzc.vos.ListViewModel;

public interface PaintAuthorDao {
    /**
     * 创建名画作者
     *
     * @param paintAuthor 名画作者数据
     * @return 名画作者信息
     */
    PaintAuthor savePaintAuthor(PaintAuthor paintAuthor);

    /**
     * 修改名画作者
     *
     * @param paintAuthor 名画作者数据
     * @return 名画作者信息
     */
    PaintAuthor updatePaintAuthor(PaintAuthor paintAuthor);

    /**
     * 删除名画作者
     *
     * @param id 名画作者id
     */
    void deletePaintAuthor(String id);

    /**
     * 获取名画作者详情
     *
     * @param id 名画作者id
     * @return 名画作者信息
     */
    PaintAuthor getPaintAuthor(String id);

    /**
     * 根据作者名查找名画作者列表
     *
     * @param authorName 作者名称
     * @param limit      分页参数
     * @return 名画作者列表
     */
    ListViewModel<PaintAuthor> queryPaintAuthorList(String authorName, String limit);

    /**
     * 批量获取作者信息
     *
     * @param ids 作者id列表
     * @return 名画作者列表
     */
    List<PaintAuthor> batchGetPaintAuthor(List<String> ids);

    /**
     * 根据authorName和nationality查询相关的authorId
     *
     * @param authorName  作者名称
     * @param nationality 作者国籍
     * @return 作者id列表
     */
    List<String> getAuthorIdByNameAndNationality(String authorName, String nationality);

    /**
     * @param authorName  作者名称
     * @param nationality 作者国籍
     * @param limit       分页参数
     * @return 作者列表
     */
    List<PaintAuthor> queryAuthorByNameAndNationality(String authorName, String nationality, String limit);

    /**
     * 根据姓名和国籍查找作者数量
     *
     * @param authorName  作者名称
     * @param nationality 作者国籍
     * @param limit       分页参数
     * @return 作者数量
     */
    Long queryCountByNameAndNationality(String authorName, String nationality, String limit);
}
