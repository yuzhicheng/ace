package com.yzc.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yzc.service.FileService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;

import sun.misc.BASE64Encoder;

/**
 * @title 文件管理 Service层
 * @author yzc
 * @version 0.1
 * @date 2016年11月1日
 */
@SuppressWarnings("restriction")
@Service("FileService")
public class FileServiceImpl implements FileService {

	private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
	
	public static final String IMAGE_PATH = "image/";

	@Override
	public String uploadFile(String fileName, String path, MultipartFile file) {

		File tempFile = null;
		if (!file.isEmpty()) {
			if (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("png")
					|| fileName.toLowerCase().endsWith("jpeg")) {
				tempFile = new File(path + IMAGE_PATH, new Date().getTime() + String.valueOf(fileName));
			} else {
				tempFile = new File(path, new Date().getTime() + String.valueOf(fileName));
			}
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdir();
			}
			if (!tempFile.exists()) {
				try {
					tempFile.createNewFile();
					file.transferTo(tempFile);
				} catch (IOException e) {
					LOG.error("IO异常：" + e);
					throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
							ErrorMessageMapper.ExportExcelFail.getCode(), e.getMessage());
				}
			}

		}

		return tempFile.getName();
	}

	@Override
	public String downloadFile(String fileName, String path,HttpServletResponse response) {
		
		try {
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition",
					"attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
			if (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("png")
					|| fileName.toLowerCase().endsWith("jpeg")) {
				path=path+IMAGE_PATH;
			}
			InputStream inputStream = new FileInputStream(new File(path + File.separator + fileName));
			OutputStream os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
			os.close();
			inputStream.close();
		} catch (FileNotFoundException e) {
			LOG.error("文件未找到：" + e);
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error("IO异常：" + e);
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("resource")
	@Override
	public String loadPicture(String fileName, String filePath){
		BASE64Encoder encoder = new sun.misc.BASE64Encoder(); 
		File file = new File(filePath + File.separator + fileName);  
		FileInputStream fips;
		ByteArrayOutputStream bops = new ByteArrayOutputStream();  
		try {
			fips = new FileInputStream(file);
		
		int data = -1;  
		 while((data = fips.read()) != -1){  
		        bops.write(data);  
		    }   
		} catch(Exception e){  
		    return null;  
		}  
		byte[] btImg = bops.toByteArray();
		return encoder.encodeBuffer(btImg).trim();
	}

}
