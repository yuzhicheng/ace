package com.yzc.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.yzc.models.StudentModel;
import com.yzc.service.ExcelService;
import com.yzc.service.StudentService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.vos.ListViewModel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@RestController
@RequestMapping("/excel")
public class ExcelController<T> {

	@Autowired
	private StudentService studentService;

	@Autowired
	private ExcelService excelService;

	/**
	 * 导出学生信息excel文件
	 * 
	 * @param fileName
	 * @param words
	 * @param limit
	 * @param username
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/export/student", method = RequestMethod.GET)
	public Map<String, String> exportExcel(@RequestParam String fileName,
			@RequestParam(value = "words", required = true) String words,
			@RequestParam(value = "limit", required = true) String limit,
			@RequestParam(value = "username", required = false) String username, HttpServletResponse response) {

		String[] title = { "id", "用户名", "密码", "姓名", "性别", "学校", "年级", "班级", "主题", "描述" };
		ListViewModel<StudentModel> list = studentService.queryStudentList(words, limit, username);
		List content = list.getItems();
		excelService.exportExcel(fileName, title, content, response);

		return MessageConvertUtil.getMessageString(ErrorMessageMapper.ExportExcelSuccess);
	}

	/**
	 * 导入学生信息Excel文件
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/import/student")
	public Map<String, String> importExcel(HttpServletRequest request) {
		CommonsMultipartResolver resolve = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (resolve.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multiRequest.getFile("excel");

			try {
				BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
				WorkbookSettings setting=new WorkbookSettings();
				setting.setEncoding("ISO-8859-1");
				Workbook workbook = Workbook.getWorkbook(bis,setting);
				Sheet sheet = workbook.getSheet(0);
				for (int i = 1; i < sheet.getRows(); i++) {

					for (int j = 0; j < sheet.getColumns(); j++) {

						String id = sheet.getCell(j++, i).getContents();
						String username = sheet.getCell(j++, i).getContents();
						String password = sheet.getCell(j++, i).getContents();
						String name = sheet.getCell(j++, i).getContents();
						String sex = sheet.getCell(j++, i).getContents();
						String school = sheet.getCell(j++, i).getContents();
						String grade = sheet.getCell(j++, i).getContents();
						String classes = sheet.getCell(j++, i).getContents();
						String title = sheet.getCell(j++, i).getContents();
						String description = sheet.getCell(j++, i).getContents();

						System.out.println("id:" + id + "  username:" + username + "  password:" + password + "  sex:"
								+ sex + "  name:" + name + "  school:" + school + "  grade:" + grade + "  classes:"
								+ classes + "  title:" + title + "  description:" + description);
					}
				}
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.ImportExcelSuccess);
	}

	/**
	 * 从指定的路径读取Excel文件
	 * 
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/import/path", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> importExcelToPath(@RequestParam String fileName) {

		String filePath = "D:\\Downloads\\yzc.xls";
		excelService.importExcel(filePath);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.ImportExcelSuccess);
	}

	/**
	 * 导出Excel到指定的路径
	 * 
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/export/path", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> exportExcelToPath(@RequestParam String filePath,
			@RequestParam(value = "words", required = true) String words,
			@RequestParam(value = "limit", required = true) String limit) {

		if (com.yzc.utils.StringUtils.isEmpty(filePath)) {
			filePath = "D:\\Downloads\\ace.xls";
		}
		ListViewModel<StudentModel> list = studentService.getStudentList(words, limit);
		List content = list.getItems();
		excelService.exportExcelToPath(filePath,content);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.ExportExcelSuccess);
	}

}
