package com.yzc.dao;

import java.util.List;

import com.yzc.entity.PaintAuthor;
import com.yzc.vos.ListViewModel;

public interface PaintAuthorDao {
	/**
	 * 创建名画作者
	 * 
	 * @param paintAuthor
	 * @return
	 */
	public PaintAuthor savePaintAuthor(PaintAuthor paintAuthor);

	/**
	 * 修改名画作者
	 * 
	 * @param paintAuthor
	 * @return
	 */
	public PaintAuthor updatePaintAuthor(PaintAuthor paintAuthor);

	/**
	 * 删除名画作者
	 * 
	 * @param id
	 * @return
	 */
	public void deletePaintAuthor(String id);

	/**
	 * 获取名画作者详情
	 * 
	 * @param id
	 * @return
	 */
	public PaintAuthor getPaintAuthor(String id);

	/**
	 * 根据作者名查找名画作者列表
	 * 
	 * @param authorName
	 * @param limit
	 * @return
	 */
	public ListViewModel<PaintAuthor> queryPaintAuthorList(String authorName, String limit);

	/**
	 * 批量获取作者信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<PaintAuthor> batchGetPaintAuthor(List<String> ids);

	/**
	 * 根据authorName和nationality查询相关的authorId
	 * 
	 * @author yzc
	 * @date 2016年11月7日
	 * @param authorName
	 * @param nationality
	 * @return
	 */
	public List<String> getAuthorIdByNameAndNationality(String authorName, String nationality);

	/*
     * 根据姓名和国籍查找作者列表
	 * 
	 * @param authorName
	 * @nationality           
	 * @param limit        
	 * @return
	 */
	public List<PaintAuthor> queryAuthorByNameAndNationality(String authorName, String nationality, String limit);

	/*
     * 根据姓名和国籍查找作者数量
	 * 
	 * @param authorName
	 * @nationality           
	 * @param limit        
	 * @return
	 */
	public Long queryCountByNameAndNationality(String authorName, String nationality, String limit);
}
