package com.yzc.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yzc.models.ResourceModel;

public interface ResourceDao {

    /**
     * 查询总数
     *
     * @param resType         资源类型
     * @param resCodes        资源编码
     * @param categories      资源分类维度
     * @param categoryExclude 排除的分类维度
     * @param relations       资源关系
     * @param coverages       资源覆盖范围
     * @param propsMap        属性值map
     * @param words           关键字
     * @param limit           分页参数
     * @param reverse         关系是否反转
     * @param useIn           否使用in查询
     * @param tags            标签
     * @return 资源数量
     */
    Long commomQueryCount(String resType, String resCodes, Set<String> categories, Set<String> categoryExclude,
                          List<Map<String, String>> relations, List<String> coverages, Map<String, Set<String>> propsMap,
                          String words, String limit, boolean reverse, boolean useIn, List<String> tags);


    /**
     * 通用查询
     *
     * @param resType         资源类型
     * @param resCodes        资源编码
     * @param includes        包括的属性信息
     * @param categories      资源分类维度
     * @param categoryExclude 排除的分类维度
     * @param relations       资源关系
     * @param coverages       资源覆盖范围
     * @param propsMap        属性值map
     * @param orderMap        排序map
     * @param words           关键字
     * @param limit           分页参数
     * @param reverse         关系是否反转
     * @param useIn           是否使用in查询
     * @param tags            标签
     * @return 资源列表
     */
    List<ResourceModel> commomQueryByDB(String resType, String resCodes, List<String> includes, Set<String> categories,
                                        Set<String> categoryExclude, List<Map<String, String>> relations, List<String> coverages,
                                        Map<String, Set<String>> propsMap, Map<String, String> orderMap, String words, String limit,
                                        boolean reverse, boolean useIn, List<String> tags);


    /**
     * 判断使用IN 还是 EXISTS，与业务关联比较大,存在不确定性
     *
     * @param resType         资源类型
     * @param resCodes        资源编码
     * @param categories      资源分类维度
     * @param categoryExclude 排除的分类维度
     * @param relations       资源关系
     * @param coverages       资源覆盖范围
     * @param propsMap        属性值map
     * @param words           关键字
     * @param reverse         关系是否反转
     * @param tags            标签
     * @return boolean
     */
    boolean judgeUseInOrExists(String resType, String resCodes, Set<String> categories, Set<String> categoryExclude,
                               List<Map<String, String>> relations, List<String> coverages, Map<String, Set<String>> propsMap,
                               String words, boolean reverse, List<String> tags);

    /**
     * 根据资源类型与id查找数目
     *
     * @param resType    资源类型
     * @param identifier 资源id
     * @return 资源数量
     */
    int queryResCountByResId(String resType, String identifier);

    /**
     * 判断资源编码是否重复
     *
     * @param resType    资源类型
     * @param identifier 资源id
     * @param code       资源编码
     * @return 资源编码数量
     */
    int queryCodeCountByResId(String resType, String identifier, String code);

}
