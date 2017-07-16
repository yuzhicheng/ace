package com.yzc.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.StudentModel;
import com.yzc.service.StudentService;
import com.yzc.support.MessageException;
import com.yzc.support.ValidResultHelper;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.vos.student.StudentViewModel;
import com.yzc.vos.user.UserViewModel;

@Controller
@RequestMapping(value = "/home")
public class HomeController {
	
	@Autowired
	private StudentService studentService;
	
	@RequestMapping(value = "/homepage")
	public String home() {

		return "home";
	}
	
	//获取@SpringExceptionHandler配置的属性
	@RequestMapping(value = "/msg")
	public String getMessage(@ModelAttribute("msg") String msg,Model model) {

		model.addAttribute("msg", "额外信息");
		System.out.println(msg);
		return "home";
	}
	
	// ResponseEntity包含@ResponseBody的语义，如果返回的类型为ResponseEntity<Object>,就没有必要在方法前面使用@ResponseBody注解
	// 在响应中设置头部信息
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<StudentViewModel> createStudent(@Valid @RequestBody StudentViewModel svm,BindingResult validResult, UriComponentsBuilder ucb) {

		// 校验
		ValidResultHelper.valid(validResult, "CREATE_STUDENT_PARAM_VALID_FAIL", "StudentController", "createStudent");
		StudentModel sm = studentService.createStudent(BeanMapperUtils.beanMapper(svm, StudentModel.class));
		HttpHeaders headers = new HttpHeaders();
		URI locationUri = ucb.path("/home/create").build().toUri();headers.setLocation(locationUri);
		ResponseEntity<StudentViewModel> responseEntity = new ResponseEntity<StudentViewModel>(changeToViewModel(sm),headers, HttpStatus.CREATED);
		return responseEntity;
		}

		// 在控制器类上使用@RestController注解，就可以移除方法前面的@ResponseBody注解
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudentViewModel updateStudent(@PathVariable String id,@Valid @RequestBody StudentViewModel studentViewModel, BindingResult validResult) {

		// 校验
		ValidResultHelper.valid(validResult, "LC/UPDATE_STUDENT_PARAM_VALID_FAIL", "StudentController","updateStudent");

		studentViewModel.setIdentifier(id);
		StudentModel com = studentService.updateStudent(BeanMapperUtils.beanMapper(studentViewModel, StudentModel.class));

		return changeToViewModel(com);
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
	
	@RequestMapping(value = "/test1")
	public String test() {

		int id=1;
		if (id==1) {
			throw new MessageException("DatabaseException", "databaseException");
		}
		return "sockclient";
	}
	
	@RequestMapping(value = "/test")
	public String test2(@RequestBody UserViewModel uvm) throws EspStoreException {

		int id=1;
		if (id==1) {
			throw new EspStoreException("EspStoreException", "espStoreException");
		}
		return "sockclient";
	}

}
