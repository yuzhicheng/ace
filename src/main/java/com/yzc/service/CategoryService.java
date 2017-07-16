package com.yzc.service;

import com.yzc.models.CategoryDataModel;
import com.yzc.models.CategoryModel;
import com.yzc.vos.ListViewModel;

public interface CategoryService {

	/**
	 * 创建维度分类
	 * 
	 * @param categoryModel
	 */
	public CategoryModel creatCategory(CategoryModel categoryModel);

	/**
	 * 修改分类维度的信息
	 * 
	 * @param categoryModel
	 */
	public CategoryModel updateCategory(CategoryModel categoryModel);

	/**
	 * 根据名字进行匹配分类维度信息
	 * 
	 * @param words
	 * @param limit
	 */
	public ListViewModel<CategoryModel> readCategory(String words, String limit);

	/**
	 * 通过id删除
	 * 
	 * 删除分类的时候，需要确定与其有关的维度数据需要删除，同时模式数据中与此相关的数据也要进行更新
	 * 
	 * @param cid
	 */
	public void deleteCategory(String cid);

	/**
	 * 创建维度数据
	 * 
	 * @param categoryDataModel
	 */
	public CategoryDataModel createCategoryData(CategoryDataModel categoryDataModel);
	
	public CategoryDataModel updateCategoryData(CategoryDataModel modifyModel);

	/**
	 * 通过id删除 删除数据的时候，需要根据实际数据关系，进行关系模式以及资源定位相关数据的更新
	 * 
	 * @param id
	 */
	public void deleteCategoryData(String did);

	/**
	 * 根据上级节点类型和名称模糊匹配下级节点内容
	 * 
	 * @param all
	 * @param parent
	 * @param words
	 * @param limit
	 */
	public ListViewModel<CategoryDataModel> readCategoryData(String ndCode, boolean all, String parent, String words,String limit, Integer isDefault);

}
