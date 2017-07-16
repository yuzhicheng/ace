package com.yzc.service;

import java.util.List;

import com.yzc.models.paints.AuthorModel;
import com.yzc.models.paints.PaintModel;
import com.yzc.vos.ListViewModel;
public interface PaintService {
	/**
	 * 保存名画
	 * @param paintModel
	 * @return
	 */
	public PaintModel savePaintModel(PaintModel paintModel);
	
	/**
	 * 修改名画
	 * @param paintModel
	 * @return
	 */
	public PaintModel updatePaintModel(PaintModel paintModel);
	
	/**
	 * 删除名画
	 * @param id
	 */
	public void deletePaintModel(String id);
	
	/**
	 * 根据id获取名画详情
	 * @param id
	 * @return
	 */
	public PaintModel getPaintModel(String id);
	
	/**
	 * 保存名画作者信息
	 * @param authorModel
	 * @return
	 */
	public AuthorModel saveAuthorModel(AuthorModel authorModel);
	
	/**
	 * 修改名画作者信息
	 * @param authorModel
	 * @return
	 */
	public AuthorModel updateAuthorModel(AuthorModel authorModel);
	
	/**
	 * 删除名画作者信息
	 * @param id
	 */
	public void deleteAuthorModel(String id);
	
	/**
	 * 获取名画作者信息
	 * @param id
	 * @return
	 */
	public AuthorModel getAuthorModel(String id);
	
	/**
	 * 根据作者姓名获取名画作者列表
	 * @param authorName
	 * @param limit
	 * @return
	 */
	public ListViewModel<AuthorModel> queryAuthorList(String authorName,String limit);
	
	/**
	 * 根据标题模糊查找名画
	 * @param title
	 * @param titleCn
	 * @param limit
	 * @return
	 */
	public ListViewModel<PaintModel> queryListByCond(
			String title, List<String> tags, String authorName, String nationality, String creator,String limit);

	/*
     * 根据姓名和国籍查找作者列表
	 * 
	 * @param authorName
	 * @nationality           
	 * @param limit        
	 * @return
	 */
	public ListViewModel<AuthorModel> queryAuthorByNameAndNationality(String authorName, String nationality,
			String limit);

}

