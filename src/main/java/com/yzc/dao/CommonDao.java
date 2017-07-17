package com.yzc.dao;

import com.yzc.entity.ResCategory;
import com.yzc.entity.TechInfo;

import java.util.List;
import java.util.Set;


public interface CommonDao {

    /**
     * 删除tech_info
     *
     * @param resType    资源类型
     * @param resourceId 资源id
     * @return 删除的记录条数
     */
    int deleteTechInfoByResource(String resType, String resourceId);

    /**
     * 删除resource_categories
     *
     * @param resType    资源类型
     * @param resourceId 资源id
     * @return 删除的记录条数
     */
    int deleteResourceCategoryByResource(String resType, String resourceId);

    /**
     * 查询技术属性（批量resource id）
     *
     * @param resTypes 资源类型列表
     * @param keySet   资源id集合
     * @return 技术属性列表
     */
    List<TechInfo> queryTechInfosByResourceSet(List<String> resTypes, Set<String> keySet);

    /**
     * 查询维度数据（批量resource id）
     *
     * @param resTypes 资源类型列表
     * @param keySet   资源id集合
     * @return 资源分类维度列表
     */
    List<ResCategory> queryCategoriesUseHql(List<String> resTypes, Set<String> keySet);
}

