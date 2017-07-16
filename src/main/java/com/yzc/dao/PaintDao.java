package com.yzc.dao;

import java.util.List;

import com.yzc.entity.Paint;

public interface PaintDao {
	/**
	 * 新增名画
	 * @param paint
	 * @return
	 */
	public Paint savePaint(Paint paint);
	
	/**
	 * 修改名画
	 * @param paint
	 * @return
	 */
	public Paint updatePaint(Paint paint);
	
	/**
	 * 删除名画
	 * @param id
	 * @return
	 */
	public void deletePaint(String id);
	
	/**
	 * 获取名画详情
	 * @param id
	 * @return
	 */
	public Paint getPaint(String id);
	
	/**
	 * 根据条件获取名画的总数
	 * @param title
	 * @param titleCn
	 * @return
	 */
	public long getCountByCond(String title, List<String> tags, String creator, List<String> userIds);
	
	/**
	 * 根据条件查询名画列表
	 * @param title
	 * @param titleCn
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Paint> queryListByCond(String title, List<String> tags, String creator, 
			List<String> userIds, int offset,int pageSize);
	
	/**
	 * 判断id是否重复
	 * @author xiezy
	 * @date 2016年11月8日
	 * @param identifier
	 * @return
	 */
	public int queryCountByResId(String identifier); 
}

