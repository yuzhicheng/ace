package com.yzc.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yzc.config.BaseControllerConfig;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.utils.MockUtil;
import com.yzc.utils.ObjectUtils;
import com.yzc.vos.ListViewModel;
import com.yzc.vos.student.StudentViewModel;

public class TestStudentController extends BaseControllerConfig {

	// 几个变量使用的默认值，方便清理数据
	public final static String DERAULT_TITLE = "ace-test-student-title";
	public final static String DERAULT_USERNAME = "ace-test-student-username";
	public final static String DERAULT_DESCRIPTION = "ace-test-student-description";
	Logger logger = Logger.getLogger(this.getClass().getName());

	@SuppressWarnings("unchecked")
	@Test
	public void testAll() {

		StudentViewModel svm = getDefaultStudentViewModel();
		svm.setDescription(DERAULT_DESCRIPTION);
		svm.setTitle(DERAULT_TITLE);
		svm.setUsername(DERAULT_USERNAME);

		// 创建学生接口
		StudentViewModel createViewModel = testCreate(svm,UUID.randomUUID().toString());
		Assert.assertNotNull(createViewModel);
		Assert.assertEquals("测试创建学生不通过", DERAULT_USERNAME, createViewModel.getUsername());

		// 测试创建学生时username是否重复
		String returnStr = postCreate(createViewModel,UUID.randomUUID().toString());
		Map<String, Object> createMap = ObjectUtils.fromJson(returnStr, Map.class);
		Assert.assertEquals("测试username是否重复不通过", ErrorMessageMapper.CheckStudentExist.getCode(),createMap.get("code"));

		// 修改学生接口
		createViewModel.setTitle(DERAULT_TITLE + "-update");
		StudentViewModel updateViewModel = testUpdate(createViewModel);
		Assert.assertNotNull(updateViewModel);
		Assert.assertEquals("测试修改学生接口不通过", DERAULT_TITLE + "-update", updateViewModel.getTitle());

		// 测试修改时学生是否存在
		updateViewModel.setIdentifier(UUID.randomUUID().toString());
		String updateStr = putUpdate(updateViewModel);
		Map<String, Object> updateMap = ObjectUtils.fromJson(updateStr, Map.class);
		Assert.assertEquals("测试修改时学生是否存在不通过", ErrorMessageMapper.StudentNotFound.getCode(), updateMap.get("code"));

		// 测试修改时username是否重复
		StudentViewModel rpm = getDefaultStudentViewModel();
		rpm.setUsername(DERAULT_USERNAME + "again");
		StudentViewModel rpmResult = testCreate(rpm,UUID.randomUUID().toString());// 先创建，后修改，然后测试
		rpmResult.setUsername(DERAULT_USERNAME);
		String updateUsername = putUpdate(rpmResult);
		Map<String, Object> updateResult = ObjectUtils.fromJson(updateUsername, Map.class);
		Assert.assertEquals("测试修改时username是否重复不通过", ErrorMessageMapper.CheckStudentExist.getCode(),
				updateResult.get("code"));
		// 删除创建的记录
		testDelete(rpmResult.getIdentifier());

		// 测试根据内容(id)获取学生详细
		StudentViewModel object = testGetDetail(UUID.randomUUID().toString());
		Assert.assertNull(object);

		// 测试根据内容(username)获取学生详细
		StudentViewModel objectTwo = testGetDetail(createViewModel.getUsername());
		Assert.assertEquals("测试根据内容获取学生详细不通过", createViewModel.getUsername(), objectTwo.getUsername());

		// 测试默认查询学生
		String limit = "(0,20)";
		ListViewModel<StudentViewModel> getM = testGetStudentList(DERAULT_TITLE + "-update", limit);
		Assert.assertNotNull(getM);
		Assert.assertEquals("测试默认查询学生不通过", limit, getM.getLimit());

		// 测试根据words关键字(title,description)精确查询,同时可以用可选参数username查询，两者之间的关系为and
		ListViewModel<StudentViewModel> queryM = testQueryStudentList(DERAULT_TITLE + "-update", limit);
		Assert.assertNotNull(getM);
		Assert.assertEquals("测试根据words精确查询,可选参数username查询学生不通过", limit, queryM.getLimit());

		// 测试根据username模糊查询学生
		ListViewModel<StudentViewModel> readM = testReadStudentList(DERAULT_USERNAME, limit);
		Assert.assertNotNull(readM);
		Assert.assertEquals("测试根据username模糊查询学生不通过", limit, readM.getLimit());

		// 测试删除时学生是否存在
		String str = testDelete(UUID.randomUUID().toString());
		Map<String, Object> Str = ObjectUtils.fromJson(str, Map.class);
		Assert.assertNotNull(Str);
		Assert.assertEquals("测试删除时学生不存在不通过", ErrorMessageMapper.StudentNotFound.getCode(), Str.get("code"));

		// 删除学生接口
		String s = testDelete(createViewModel.getIdentifier());
		Map<String, Object> returnMap = ObjectUtils.fromJson(s, Map.class);
		Assert.assertNotNull(returnMap);
		Assert.assertEquals("测试删除学生接口不通过", ErrorMessageMapper.DeleteStudentSuccess.getCode(),
				returnMap.get("process_code").toString());

		// 测试批量创建学生接口,学生username重复
		List<StudentViewModel> studentList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			StudentViewModel model = getDefaultStudentViewModel();
			model.setUsername(DERAULT_USERNAME);
			studentList.add(model);
		}
		String batchAddStr = postBatchAdd(studentList);
		Map<String, Object> batchAddMap = ObjectUtils.fromJson(batchAddStr, Map.class);
		Assert.assertNotNull(Str);
		Assert.assertEquals("测试批量创建学生username重复不通过", ErrorMessageMapper.BatchAddStudentDuplicate.getCode(),
				batchAddMap.get("code"));

		// 测试批量创建学生接口
		List<StudentViewModel> paramList = new ArrayList<>();
		Set<String> usernameSet = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			StudentViewModel model = getDefaultStudentViewModel();
			model.setUsername(DERAULT_USERNAME + i);
			usernameSet.add(DERAULT_USERNAME + i);
			paramList.add(model);
		}
		List<StudentViewModel> studentViewModelList = testBatchAdd(paramList);
		Assert.assertNotNull(studentViewModelList);
		Assert.assertEquals("测试批量创建学生不通过", 3, studentViewModelList.size());

		// 测试批量获取学生接口
		Map<String, StudentViewModel> studentMap = testBatchGetDetail(usernameSet);
		Assert.assertEquals("测试批量获取学生不通过", 3, studentMap.size());

		// 测试批量删除学生接口
		Set<String> idSet = new HashSet<>();
		for (int i = 0; i < studentViewModelList.size(); i++) {
			studentViewModelList.get(i);
			String id = studentViewModelList.get(i).getIdentifier();

			idSet.add(id);

		}
		String batchDeleteStr = testBatchDelete(idSet);
		Map<String, Object> batchDeleteMap = ObjectUtils.fromJson(batchDeleteStr, Map.class);
		Assert.assertNotNull(batchDeleteMap);
		Assert.assertEquals("测试删除学生接口不通过", ErrorMessageMapper.BatchDeleteStudentSuccess.getCode(),
				batchDeleteMap.get("process_code").toString());

	}

	public List<StudentViewModel> testBatchAdd(List<StudentViewModel> paramList) {
		String resStr = postBatchAdd(paramList);
		List<StudentViewModel> m = new Gson().fromJson(resStr, new TypeToken<List<StudentViewModel>>() {
		}.getType());
		return m;
	}

	@SuppressWarnings("unchecked")
	public Map<String, StudentViewModel> testBatchGetDetail(Set<String> usernameSet) {
		String resStr = getBatchGetDetail(usernameSet);
		Map<String, StudentViewModel> m = ObjectUtils.fromJson(resStr, Map.class);
		return m;
	}

	public String testBatchDelete(Set<String> idSet) {
		String str = batchDelete(idSet);
		return str;
	}

	public StudentViewModel testCreate(StudentViewModel rpvm,String identifier) {
		String resStr = postCreate(rpvm,identifier);
		StudentViewModel m = ObjectUtils.fromJson(resStr, StudentViewModel.class);
		return m;
	}

	public StudentViewModel testUpdate(StudentViewModel rpvm) {
		String resStr = putUpdate(rpvm);
		StudentViewModel m = ObjectUtils.fromJson(resStr, StudentViewModel.class);
		return m;
	}

	public String testDelete(String uuid) {
		String str = del(uuid);
		return str;
	}

	public StudentViewModel testGetDetail(String content) {
		String resStr = getObject(content);
		StudentViewModel m = ObjectUtils.fromJson(resStr, StudentViewModel.class);
		return m;
	}

	@SuppressWarnings("unchecked")
	public ListViewModel<StudentViewModel> testGetStudentList(String words, String limit) {
		String resStr = getDetail(words, limit);

		ListViewModel<StudentViewModel> m = ObjectUtils.fromJson(resStr, ListViewModel.class);
		return m;
	}

	@SuppressWarnings("unchecked")
	public ListViewModel<StudentViewModel> testQueryStudentList(String words, String limit) {
		String resStr = queryDetail(words, limit);
		ListViewModel<StudentViewModel> m = ObjectUtils.fromJson(resStr, ListViewModel.class);
		return m;
	}

	@SuppressWarnings("unchecked")
	public ListViewModel<StudentViewModel> testReadStudentList(String words, String limit) {
		String resStr = readDetail(words, limit);
		ListViewModel<StudentViewModel> m = ObjectUtils.fromJson(resStr, ListViewModel.class);
		return m;
	}

	protected String postBatchAdd(List<StudentViewModel> paramList) {
		StringBuffer uri = new StringBuffer("/student/batch/create");
		String resStr = null;
		String param = ObjectUtils.toJson(paramList);
		try {
			resStr = MockUtil.mockPost(mockMvc, uri.toString(), param);
		} catch (Exception e) {
			logger.error("postBatchAdd error", e);
		}
		return resStr;
	}

	protected String getBatchGetDetail(Set<String> usernameSet) {

		String resStr = null;
		StringBuffer uri = new StringBuffer("/student/username/list?username=" + usernameSet.toArray()[0] + ","
				+ usernameSet.toArray()[1] + "," + usernameSet.toArray()[2]);
		try {
			resStr = MockUtil.mockGet(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("getDetail error", e);
		}
		return resStr;
	}

	protected String batchDelete(Set<String> idSet) {
		String resStr = null;
		String uri = "/student/batch/delete?" + "student_id=" + idSet.toArray()[0] + "," + idSet.toArray()[1] + ","
				+ idSet.toArray()[2];
		try {
			resStr = MockUtil.mockDelete(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("del error", e);
		}
		return resStr;
	}
	protected String postCreate(StudentViewModel rpvm,String identifier) {

		StringBuffer uri = new StringBuffer("/student/"+identifier);
		String resStr = null;
		String param = ObjectUtils.toJson(rpvm);
		try {
			resStr = MockUtil.mockPost(mockMvc, uri.toString(), param);
		} catch (Exception e) {
			logger.error("postCreate error", e);
		}
		return resStr;
	}

	protected String putUpdate(StudentViewModel rpvm) {

		String param = ObjectUtils.toJson(rpvm);

		String uri = "/student/" + rpvm.getIdentifier();
		String resStr = null;
		try {
			resStr = MockUtil.mockPut(mockMvc, uri.toString(), param);
		} catch (Exception e) {
			logger.error("putUpdate error", e);
		}
		return resStr;
	}

	protected String del(String uuid) {
		String resStr = null;
		String uri = "/student" + "/" + uuid;
		try {
			resStr = MockUtil.mockDelete(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("del error", e);
		}
		return resStr;
	}

	protected String getObject(String content) {
		String resStr = null;
		StringBuffer uri = new StringBuffer("/student/" + content);
		try {
			resStr = MockUtil.mockGet(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("getObject error", e);
		}
		return resStr;
	}

	protected String getDetail(String words, String limit) {
		String resStr = null;
		StringBuffer uri = new StringBuffer("/student?words=" + words + "&limit=" + limit);
		try {
			resStr = MockUtil.mockGet(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("getDetail error", e);
		}
		return resStr;
	}

	protected String queryDetail(String words, String limit) {
		String resStr = null;
		StringBuffer uri = new StringBuffer("/student/condition?words=" + words + "&limit=" + limit);
		try {
			resStr = MockUtil.mockGet(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("queryDetail error", e);
		}
		return resStr;
	}

	protected String readDetail(String words, String limit) {
		String resStr = null;
		StringBuffer uri = new StringBuffer("/student/username?words=" + words + "&limit=" + limit);
		try {
			resStr = MockUtil.mockGet(mockMvc, uri.toString(), null);
		} catch (Exception e) {
			logger.error("readDetail error", e);
		}
		return resStr;
	}

	private StudentViewModel getDefaultStudentViewModel() {
		StudentViewModel svm = new StudentViewModel();
		svm.setClasses("一班");
		svm.setGrade("一年级");
		svm.setName("yzc");
		svm.setPassword("123456");
		svm.setSchool("万福小学");
		svm.setSex(0);
		svm.setTitle(DERAULT_TITLE);
		svm.setDescription(DERAULT_DESCRIPTION);
		return svm;
	}

}
