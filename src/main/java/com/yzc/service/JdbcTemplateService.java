package com.yzc.service;

import com.yzc.models.StudentModel;
import com.yzc.vos.ListViewModel;

public interface JdbcTemplateService {

	/**
	 * 创建学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param studentModel
	 * @return
	 */
	public StudentModel createStudent(StudentModel studentModel);

	/**
	 * 修改学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @param studentModel
	 * @return
	 */
	public StudentModel updateStudent(StudentModel studentModel);

	/**
	 * 删除学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @return
	 */
	public boolean deleteStudent(String id);

	/**
	 * 根据id查询学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param id
	 * @return
	 */
	public StudentModel queryStudentById(String id);

	/**
	 * 根据username查询学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param username
	 * @return
	 */
	public StudentModel queryStudentByUsername(String username);

	/**
	 * 条件查询学生: 根据words关键字精确查询
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return
	 */
	public ListViewModel<StudentModel> queryStudentList(String words, String limit);

}
