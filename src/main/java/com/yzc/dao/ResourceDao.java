package com.yzc.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yzc.models.ResourceModel;

public interface ResourceDao {

    /**
     * 查询总数
     * 
     * @param resType
     * @param resCodes          支持多种资源查询,resType=eduresource时生效
     * @param categories
     * @param relations
     * @param coverages
     * @param propsMap
     * @param words
     * @param limit
     * @param isNotManagement 判断是否需要对ND库下的资源只允许查出ONLINE的限制
     * @param reverse 判断关系查询是否反转
     * @return
     */
	public Long commomQueryCount(String resType, String resCodes, Set<String> categories, Set<String> categoryExclude,
			List<Map<String, String>> relations, List<String> coverages, Map<String, Set<String>> propsMap,
			String words, String limit, boolean reverse, boolean useIn, List<String> tags);

    /**
     * 通用查询	
     * 
     * @param resType
     * @param resCodes          支持多种资源查询,resType=eduresource时生效
     * @param includes
     * @param categories
     * @param relations
     * @param coverages
     * @param propsMap
     * @param words
     * @param limit
     * @param isNotManagement 判断是否需要对ND库下的资源只允许查出ONLINE的限制
     * @param reverse 判断关系查询是否反转
     * @return
     */
	List<ResourceModel> commomQueryByDB(String resType, String resCodes, List<String> includes, Set<String> categories,
			Set<String> categoryExclude, List<Map<String, String>> relations, List<String> coverages,
			Map<String, Set<String>> propsMap, Map<String, String> orderMap, String words, String limit,
			boolean reverse, boolean useIn, List<String> tags);

    /**
     * 判断使用IN 还是 EXISTS 
     * TODO 该判断与业务关联比较大,存在不确定性
     * <p>Create Time: 2015年12月2日   </p>
     * <p>Create author: xiezy   </p>
     * @param relations
     * @param coverages
     * @return  true == IN  false == EXISTS
     */
	boolean judgeUseInOrExists(String resType, String resCodes, Set<String> categories, Set<String> categoryExclude,
			List<Map<String, String>> relations, List<String> coverages, Map<String, Set<String>> propsMap,
			String words, boolean reverse, List<String> tags);

    /**
     * 根据资源类型与id查找数目
     * (资源库中根据类型进行分区，加上类型查询速度更快)
     * @param resType
     * @param identifier
     * @return
     */
	public int queryResCountByResId(String resourceType, String identifier);

    /**
     * 判断资源编码是否重复
     * @param resType
     * @param identifier
     * @param code
     * @return
     */
	public int queryCodeCountByResId(String resourceType, String identifier, String code);

}
