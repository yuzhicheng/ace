package com.yzc.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	/**
	 * @title 上传文件
	 * @author yzc
	 * @date 2016年11月1日
	 * @param fileName
	 * @param listcontent
	 * @param response
	 * @return boolean
	 */
	public String uploadFile(String fileName, String path,MultipartFile file);
	
	/**
	 * @title  下载文件
	 * @author yzc
	 * @date 2016年11月1日
	 * @param fileName
	 * @param listcontent
	 * @param response
	 * @return boolean
	 */
	public String downloadFile(String fileName,String path,HttpServletResponse response);
	
	/**
	 * @title  下载文件
	 * @author yzc
	 * @date 2016年11月1日
	 * @param fileName
	 * @param listcontent
	 * @param response
	 * @return boolean
	 */
	public String loadPicture(String fileName,String filePath);

}
