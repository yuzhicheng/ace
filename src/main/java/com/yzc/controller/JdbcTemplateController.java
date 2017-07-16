package com.yzc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.models.StudentModel;
import com.yzc.service.JdbcTemplateService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.ValidResultHelper;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.student.StudentViewModel;

/**
 * 学生管理
 * 
 * @author yzc
 * @date 2016年8月30
 * @title 学生管理
 */
@RestController // 默认为所有方法使用消息转换
@RequestMapping("/jdbcTemplate")
public class JdbcTemplateController {

	private static final Logger LOG = LoggerFactory.getLogger(JdbcTemplateController.class);
	@Autowired
	private JdbcTemplateService jdbcTemplateService;

	// UUID格式,这个用于验证uuid的正则表达式存在一定的问题（不够严格）
	private final static String uuidReg = "[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";

	/**
	 * 创建学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param svm
	 * @param validResult
	 * @return StudentViewModel
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public StudentViewModel createStudent(@Valid @RequestBody StudentViewModel svm, BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "CREATE_STUDENT_PARAM_VALID_FAIL", "StudentController", "createStudent");
		StudentModel sm = jdbcTemplateService.createStudent(BeanMapperUtils.beanMapper(svm, StudentModel.class));
		return changeToViewModel(sm);
	}

	/**
	 * 修改学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @param studentViewModel
	 * @param validResult
	 * @return StudentViewModel
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public StudentViewModel updateStudent(@PathVariable String id,
			@Valid @RequestBody StudentViewModel studentViewModel, BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "LC/UPDATE_STUDENT_PARAM_VALID_FAIL", "StudentController",
				"updateStudent");

		studentViewModel.setIdentifier(id);
		StudentModel com = jdbcTemplateService
				.updateStudent(BeanMapperUtils.beanMapper(studentViewModel, StudentModel.class));

		return changeToViewModel(com);
	}

	/**
	 * 删除学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @return Map<String,String>
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Map<String, String> deleteStudent(@PathVariable String id) {

		jdbcTemplateService.deleteStudent(id);

		LOG.info("删除学生成功");
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteStudentSuccess);
	}

	/**
	 * 查看学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param content
	 *            可能是id和 username
	 * @return
	 */
	@RequestMapping(value = "/{content}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public StudentViewModel getStudent(@PathVariable("content") String content) {
		StudentModel resultModel = null;
		// 两个接口，共用一个函数，需要判断是否为id
		if (checkReg(content, uuidReg)) {

			resultModel = jdbcTemplateService.queryStudentById(content);
		} else {

			resultModel = jdbcTemplateService.queryStudentByUsername(content);
		}

		if (null != resultModel) {
			return changeToViewModel(resultModel);
		}
		return null;
	}

	/**
	 * 条件查询学生: 根据words关键字精确查询
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return ListViewModel<StudentViewModel>
	 */
	@RequestMapping(value = "/student/list", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ListViewModel<StudentViewModel> queryStudentList(
			@RequestParam(value = "words", required = true) String words,
			@RequestParam(value = "limit", required = true) String limit) {

		ListViewModel<StudentModel> comList = jdbcTemplateService.queryStudentList(words, limit);

		ListViewModel<StudentViewModel> viewListResult = new ListViewModel<StudentViewModel>();
		viewListResult.setLimit(comList.getLimit());
		viewListResult.setTotal(comList.getTotal());
		List<StudentModel> modelItems = comList.getItems();
		List<StudentViewModel> viewItems = new ArrayList<StudentViewModel>();
		if (CollectionUtils.isNotEmpty(modelItems)) {
			for (StudentModel model : modelItems) {
				StudentViewModel viewModel = changeToViewModel(model);
				viewItems.add(viewModel);
			}
		}
		viewListResult.setItems(viewItems);
		return viewListResult;
	}

	/**
	 * Model转成viewModel
	 * 
	 * @author yzc
	 * @date 2016年10月9日
	 * @param um
	 * @return
	 */
	private StudentViewModel changeToViewModel(StudentModel um) {

		return BeanMapperUtils.beanMapper(um, StudentViewModel.class);
	}

	/**
	 * 正则校验
	 * 
	 * @author yzc
	 * @param value
	 *            值
	 * @param pattern
	 *            正则表达式
	 * @return boolean
	 */
	private boolean checkReg(String value, String pattern) {
		return Pattern.matches(pattern, value);
	}

}
