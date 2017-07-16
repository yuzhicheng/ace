package com.yzc.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.entity.Student;
import com.yzc.support.ValidResultHelper;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.RedisTemplateUtils;
import com.yzc.vos.student.StudentViewModel;

@RestController//默认为所有方法使用消息转换
@RequestMapping("/redis")
public class RedisTemplateOperation {
	
	@Autowired
	private RedisTemplateUtils<Student> rtu;

	/**
	 * 创建用户
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param svm
	 * @param validResult
	 * @return StudentViewModel
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public StudentViewModel createStudent(
			@Valid @RequestBody StudentViewModel svm, BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "CREATE_STUDENT_PARAM_VALID_FAIL",
				"StudentController", "createStudent");
		Student st=BeanMapperUtils.beanMapper(svm, Student.class);
		rtu.set("student", st);
		Student student=rtu.get("student", Student.class);
		return BeanMapperUtils.beanMapper(student, StudentViewModel.class);
	}
}
