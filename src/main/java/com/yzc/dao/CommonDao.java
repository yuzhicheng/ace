package com.yzc.dao;

import java.util.List;
import java.util.Set;

import com.yzc.entity.ResCategory;
import com.yzc.entity.TechInfo;


public interface CommonDao {
	
	/**
	 * 根据资源id删除tech_infos表数据
	 * 
	 * @author:xuzy
	 * @date:2015年11月4日
	 * @param resourceId
	 * @return 删除的记录条数
	 */
	public int deleteTechInfoByResource(String resType,String resourceId);
	
	/**
	 * 根据资源id删除resource_categories表数据
	 * 
	 * @author:xuzy
	 * @date:2015年11月4日
	 * @param resourceId
	 * @return 删除的记录条数
	 */
	public int deleteResourceCategoryByResource(String resType,String resourceId);
	
    /**
     * 查询技术属性（批量resource id）
     * @author yzc
     * @param resTypes
     * @param keySet
     * @return
     * @since
     */
    public List<TechInfo> queryTechInfosByResourceSet(List<String> resTypes, Set<String> keySet);
    
    /**
     * 查询维度数据（批量resource id）
     * @author linsm
     * @param resTypes
     * @param keySet
     * @return
     * @since
     */
    public List<ResCategory> queryCategoriesUseHql(List<String> resTypes, Set<String> keySet);
}

