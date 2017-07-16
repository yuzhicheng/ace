package com.yzc.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yzc.models.StudentModel;
import com.yzc.vos.ListViewModel;

public interface StudentService {

	/**
	 * 删除学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @return boolean
	 */
	public boolean deleteUser(String id);

	/**
	 * 创建学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param StudentModel
	 * @return StudentModel
	 */
	public StudentModel createStudent(StudentModel studentModel);

	/**
	 * 修改学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param StudentModel
	 * @return StudentModel
	 */
	public StudentModel updateStudent(StudentModel studentModel);

	/**
	 * 默认查询学生，根据title或者description进行查询。
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return ListViewModel<StudentModel>
	 */
	public ListViewModel<StudentModel> getStudentList(String words, String limit);

	/**
	 * 查询学生:根据username字段模糊查询
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return ListViewModel<StudentModel>
	 */
	public ListViewModel<StudentModel> readStudentList(String words,
			String limit);

	/**
	 * 查询学生: 根据words关键字精确查询，同时可以用可选参数username查询
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return ListViewModel<StudentModel>
	 */
	public ListViewModel<StudentModel> queryStudentList(String words,
			String limit, String username);

	/**
	 * 根据id查询学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param content
	 * @return StudentModel
	 */
	public StudentModel queryStudentById(String content);

	/**
	 * 根据username查询学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param content
	 * @return StudentModel
	 */
	public StudentModel queryStudentByUsername(String content);

	/**
	 * 批量删除学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param idSet
	 * @return boolean
	 */
	public boolean batchDeteleStudent(LinkedHashSet<String> idSet);

	/**
	 * 批量创建学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param studentModelList
	 * @return List
	 */
	public List<StudentModel> batchCreateStudent(
			List<StudentModel> studentModelList);

	/**
	 * 批量加载学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param studentNameSet
	 * @return Map
	 */
	public Map<String, StudentModel> batchGetDetailStudent(
			Set<String> studentNameSet);

	/**
	 * 根据username批量加载学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param usernameSet
	 * @return Map
	 */
	public Map<String, StudentModel> batchGetDetailStudentByUsername(
			Set<String> usernameSet);

	public StudentModel patchStudent(StudentModel beanMapper);

}
