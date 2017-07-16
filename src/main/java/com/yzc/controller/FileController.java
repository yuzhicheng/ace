package com.yzc.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.yzc.service.FileService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.utils.MessageConvertUtil;

@RestController
@RequestMapping("/file")
public class FileController {

	private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
	@Autowired
	private FileService fileService;

	public static final String FILE_PATH = "F:\\workspace\\IdeaProjects\\acefile\\upload\\";
	
	public static final String IMAGE_PATH = "image\\";

	@RequestMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, method = RequestMethod.POST)
	public Map<String, String> fileUpload(@RequestParam("file") MultipartFile file) {

		if (file.isEmpty()) {
			LOG.error("上传文件时，文件为空");
			throw new MessageException(ErrorMessageMapper.FileIsEmpty.getMessage());
		} else {
			fileService.uploadFile(file.getOriginalFilename(), FILE_PATH, file);
		}
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.UploadFileSuccess);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET, produces = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String download(String fileName, HttpServletRequest request, HttpServletResponse response){
		fileService.downloadFile(fileName, FILE_PATH,response);
		return null;
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String load(String fileName){

		return fileService.loadPicture(fileName, FILE_PATH+IMAGE_PATH);

	}
	
	@RequestMapping("/yzc/upload")
	public Map<String, String> clientUpload(@RequestParam(value = "path") String path,
			@RequestParam(value = "name") String name, HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multiRequest.getFileNames();
		while (iter.hasNext()) {
			MultipartFile file = multiRequest.getFile(iter.next());
			if (file != null) {
				if (file.isEmpty()) {
					LOG.error("上传文件时，文件为空");
					throw new MessageException(ErrorMessageMapper.FileIsEmpty.getMessage());
				} else {
					System.out.println("file:"+file.getOriginalFilename());
					fileService.uploadFile(file.getOriginalFilename(), FILE_PATH, file);
				}
			}

		}
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.UploadFileSuccess);
	}

}
