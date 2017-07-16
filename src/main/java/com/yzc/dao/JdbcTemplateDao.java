package com.yzc.dao;

import java.util.List;

import com.yzc.entity.Student;

public interface JdbcTemplateDao {
	
	/**
	 * 创建学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param student
	 * @return 
	 */
	public boolean createStudent(Student student);
	
	/**
	 * 修改学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param student
	 * @return 
	 */
	public boolean updateStudent(Student student);
	
	/**
	 * 删除学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param student
	 * @return 
	 */
	public boolean deleteStudent(Student student);
	
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
	public Student queryStudentById(String id);
	
	/**
	 * 根据username查询学生
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param username
	 * @return
	 */
	public Student queryStudentByUsername(String username);

	/**
	 * 查询学生列表
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return
	 */
	public List<Student> queryStudentItems(String words, String limit);

	/**
	 * 查询学生总数
	 * 
	 * @author yzc
	 * @date 2016年9月1日
	 * @param words
	 * @param limit
	 * @return
	 */
	public Long queryStudentTotal(String words, String limit);

	

}
