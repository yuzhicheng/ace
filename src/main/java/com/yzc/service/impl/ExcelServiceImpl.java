package com.yzc.service.impl;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzc.service.ExcelService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 数据管理 Service层
 * 
 * @author yzc
 * @version 0.1
 * @date 2016年11月1日
 */
@Service("ExcelService")
@Transactional
public class ExcelServiceImpl implements ExcelService {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelServiceImpl.class);

	@Override
	public boolean exportExcel(String fileName, String[] title, List<Object> listcontent,
			HttpServletResponse response) {

		OutputStream os = null;
		try {

			os = response.getOutputStream();
			// 清空输出流
			response.reset();
			response.setCharacterEncoding("UTF-8");
			fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("UTF-8"), "GBK") + ".xls");
			// 定义输出类型
			response.setContentType("application/msexcel");
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			// 设置单元格字体
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			// 以下设置三种单元格样式，灵活备用
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			// EXCEL标题
			for (int i = 0; i < title.length; i++) {
				sheet.addCell(new Label(i, 0, title[i], wcf_center));
			}

			// EXCEL正文数据
			Field[] fields = null;
			int i = 1;
			for (Object obj : listcontent) {
				fields = obj.getClass().getDeclaredFields();// 没有包括父类属性
				int j = 0;
				for (Field v : fields) {
					// 设置该属性是否可以访问
					v.setAccessible(true);
					Object va = v.get(obj);
					if (va == null) {
						va = "";
					}
					// 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
					sheet.addCell(new Label(j, i, va.toString(), wcf_left));
					j++;
				}
				i++;
			}
			workbook.write();
			workbook.close();
			os.close();
		} catch (Exception e) {
			LOG.error("导出Excel异常：" + e);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.ExportExcelFail.getCode(),
					e.getMessage());
		}
		return true;
	}

	@Override
	public boolean importExcel(String filePath) {
		try {
			// 以后根据业务需求，从指定的路径中读取数据，将取到的值可以录入到对应的数据表中
			Workbook workbook = Workbook.getWorkbook(new File(filePath));
			Sheet sheet = workbook.getSheet(0);
			// 得到所有的列和行
			int clos = sheet.getColumns();
			int rows = sheet.getRows();
			for (int i = 1; i < rows; i++) {
				for (int j = 0; j < clos; j++) {
					// 第一个是列数，第二个是行数
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

					System.out.println("id:" + id + "  username:" + username + "  password:" + password + "  sex:" + sex
							+ "  name:" + name + "  school:" + school + "  grade:" + grade + "  classes:" + classes
							+ "  title:" + title + "  description:" + description);
				}
			}
		} catch (Exception e) {
			LOG.error("导出Excel异常：" + e);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.ImportExcelFail.getCode(),
					e.getMessage());
		}
		return false;
	}

	@Override
	public boolean exportExcelToPath(String filePath, List<Object> list) {

		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath));
			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
			String[] title = { "id", "用户名", "密码", "姓名", "性别", "学校", "年级", "班级", "主题", "描述" };
			// 标题
			for (int i = 0; i < title.length; i++) {
				sheet.addCell(new Label(i, 0, title[i]));
			}

			int i = 1;
			Field[] fields = null;
			for (Object obj : list) {
				fields = obj.getClass().getDeclaredFields();// 没有包括父类属性
				int j = 0;
				for (Field v : fields) {
					// 设置该属性是否可以访问
					v.setAccessible(true);
					Object va = v.get(obj);
					if (va == null) {
						va = "";
					}
					// 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
					sheet.addCell(new Label(j, i, va.toString()));
					j++;
				}
				i++;
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			LOG.error("导出Excel异常：" + e);
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.ExportExcelFail.getCode(),
					e.getMessage());
		}
		return false;

	}

}
