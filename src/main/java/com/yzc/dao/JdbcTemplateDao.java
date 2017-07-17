package com.yzc.dao;

import java.util.List;

import com.yzc.entity.Student;

public interface JdbcTemplateDao {

    /**
     * 创建学生
     *
     * @param student 学生对象
     * @return boolean
     */
    boolean createStudent(Student student);

    /**
     * 修改学生
     *
     * @param student 学生对象
     * @return boolean
     */
    boolean updateStudent(Student student);

    /**
     * 删除学生
     *
     * @param student 学生对象
     * @return boolean
     */
    boolean deleteStudent(Student student);

    /**
     * 删除学生
     *
     * @param id 学生id
     * @return boolean
     */
    boolean deleteStudent(String id);

    /**
     * 根据id查询学生
     *
     * @param id 学生id
     * @return 学生对象
     */
    Student queryStudentById(String id);

    /**
     * 根据username查询学生
     *
     * @param username 用户名
     * @return 学生对象
     */
    Student queryStudentByUsername(String username);

    /**
     * 查询学生列表
     *
     * @param words 关键字
     * @param limit 分页参数
     * @return 学生列表
     */
    List<Student> queryStudentItems(String words, String limit);


    /**
     * 查询学生总数
     *
     * @param words 关键字
     * @param limit 分页参数
     * @return 学生总数
     */
    Long queryStudentTotal(String words, String limit);


}
