package com.yzc.controller;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.yzc.support.ErrorMessageMapper;
import com.yzc.utils.MessageConvertUtil;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.paints.AuthorViewModel;
import com.yzc.vos.student.StudentViewModel;

@Controller
@RequestMapping(value = "/rest")
public class RestClientController {

	// Rest客户端调用创建学生接口
	@RequestMapping(value = "/client/create/student", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudentViewModel addStudent(@RequestBody StudentViewModel svm) {
		RestTemplate rest = new RestTemplate();
		return rest.postForObject("http://localhost:8080/ace/student/"+UUID.randomUUID().toString(), svm, StudentViewModel.class);
	}

	// Rest客户端调用创建学生接口获取资源位置
	@RequestMapping(value = "/client/student/location", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String addStudentForLocation(@RequestBody StudentViewModel svm) {
		RestTemplate rest = new RestTemplate();
		URI uri = URI.create("http://localhost:8080/ace/student/"+UUID.randomUUID().toString());
		System.out.println(rest.postForLocation(uri, svm));
		// return rest.postForLocation(uri, svm).toString();
		return "ssss";
	}

	// Rest客户端调用修改学生接口
	@RequestMapping(value = "/client/update/student", method = RequestMethod.PUT, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<String, String> modifyStudent(@RequestBody StudentViewModel svm) {
		RestTemplate rest = new RestTemplate();
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("id", svm.getIdentifier());
		rest.put("http://localhost:8080/ace/student/{id}", svm, urlVariables);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.UpdateStudentSuccess);
	}

	// Rest客户端调用删除学生接口
	@RequestMapping(value = "/client/delete/student/{id}", method = RequestMethod.DELETE, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<String, String> deleteStudent(@PathVariable String id) {
		RestTemplate rest = new RestTemplate();
		rest.delete(URI.create("http://localhost:8080/ace/student/" + id));
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteStudentSuccess);
	}

	// Rest客户端调用获取学生接口
	@RequestMapping(value = "/client/profile/student/{content}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudentViewModel StudentProfile(@PathVariable("content") String content) {
		RestTemplate rest = new RestTemplate();

		return rest.getForObject("http://localhost:8080/ace/student/{content}", StudentViewModel.class, content);
	}

	// Rest客户端调用获取学生接口(exchange允许在请求头中设置头信息)
	@RequestMapping(value = "/client/detail/student/{content}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody StudentViewModel StudentDetail(@PathVariable("content") String content) {
		RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Accept", "application/json");
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
		ResponseEntity<StudentViewModel> response = rest.exchange("http://localhost:8080/ace/student/{content}",
				HttpMethod.GET, requestEntity, StudentViewModel.class, content);
		return response.getBody();
	}

	// Rest客户端调用默认查询学生接口
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/client/list/student", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ListViewModel<StudentViewModel> StudentList(
			@RequestParam(value = "words", required = true) String keywords,
			@RequestParam(value = "limit", required = true) String limit) {
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("words", keywords);
		urlVariables.put("limit", limit);
		RestTemplate rest = new RestTemplate();

		String uri = "http://localhost:8080/ace/student?words=" + keywords + "&limit=" + limit;
		ResponseEntity<ListViewModel> response = rest.getForEntity(uri, ListViewModel.class, urlVariables);
		return response.getBody();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/client/paints/author", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ListViewModel<AuthorViewModel> AuthorList(
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "nationality", required = true) String nationality,
			@RequestParam(value = "limit", required = true) String limit) {
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("author_name", name);
		urlVariables.put("nationality", nationality);
		urlVariables.put("limit", limit);
		RestTemplate rest = new RestTemplate();

		String uri = "http://localhost:8080/ace/paints/author/query?author_name=" + name + "&nationality=" + nationality
				+ "&limit=" + limit;
		ResponseEntity<ListViewModel> response = rest.getForEntity(uri, ListViewModel.class, urlVariables);
		return response.getBody();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/client/file/upload", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<String, String> clientFileUpload() {
		String filePath = "D:\\backup\\shop.sql";
		RestTemplate rest = new RestTemplate();
		FileSystemResource resource = new FileSystemResource(new File(filePath));
		System.out.println(resource);
		String url = "http://127.0.0.1:8080/file/upload";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("file", resource);
		Map<String, String> map = rest.postForObject(url, param, Map.class);
		return map;
	}

}
