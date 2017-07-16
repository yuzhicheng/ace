package com.yzc.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {

	/**
	 * @title 数据导出为Excel
	 * @author yzc
	 * @date 2016年11月1日
	 * @param fileName
	 * @param listcontent
	 * @param response
	 * @return boolean
	 */
	public boolean exportExcel(String fileName, String[] title, List<Object> listcontent, HttpServletResponse response);

	/**
	 * @title 从Excel中导入数据
	 * @author yzc
	 * @date 2016年11月1日
	 * @param fileName
	 * @param listcontent
	 * @param response
	 * @return boolean
	 */
	public boolean importExcel(String filePath);

	/**
	 * 导出Excel到指定的路径
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean exportExcelToPath(String filePath,List<Object> listcontent);

}
