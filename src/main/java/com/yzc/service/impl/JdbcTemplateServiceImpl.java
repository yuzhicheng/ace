package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzc.dao.JdbcTemplateDao;
import com.yzc.entity.Student;
import com.yzc.models.StudentModel;
import com.yzc.service.JdbcTemplateService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.RecordStatus;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.vos.ListViewModel;

/**
 * 用户管理 Service层
 * 
 * @author yzc
 * @date 2016年9月30日
 */
@Service("JdbcTemplateService")
@Transactional
public class JdbcTemplateServiceImpl implements JdbcTemplateService {

	private static final Logger LOG = LoggerFactory.getLogger(JdbcTemplateServiceImpl.class);

	@Autowired
	private JdbcTemplateDao jdbcTemplateDao;

	@Override
	public StudentModel createStudent(StudentModel sm) {

		// 校验username是否重复
		Student student4Username = jdbcTemplateDao.queryStudentByUsername(sm.getUsername());
		if (student4Username != null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckStudentExist);
		}
		LOG.info("创建学生");
		Student st = BeanMapperUtils.beanMapper(sm, Student.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		st.setIdentifier(UUID.randomUUID().toString());
		st.setCreateTime(ts);
		st.setUpdateTime(ts);
		st.setState(RecordStatus.NORMAL);
		jdbcTemplateDao.createStudent(st);
		return BeanMapperUtils.beanMapper(st, StudentModel.class);
	}

	@Override
	public StudentModel updateStudent(StudentModel sm) {

		// 校验id是否存在
		Student student4Id = jdbcTemplateDao.queryStudentById(sm.getIdentifier());
		if (student4Id == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StudentNotFound);
		}
		// 判断username是否重复,如果是修改成原本的username是允许的
		Student student4Username = jdbcTemplateDao.queryStudentByUsername(sm.getUsername());
		if (student4Username != null && !student4Username.getIdentifier().equals(student4Id.getIdentifier())) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckStudentExist);
		}
		LOG.info("修改学生");
		Student s = BeanMapperUtils.beanMapper(sm, Student.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		s.setUpdateTime(ts);
		jdbcTemplateDao.updateStudent(s);
		return BeanMapperUtils.beanMapper(s, StudentModel.class);
	}

	@Override
	public boolean deleteStudent(String id) {

		Student student4Id = jdbcTemplateDao.queryStudentById(id);
		if (student4Id == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StudentNotFound);
		}
		jdbcTemplateDao.deleteStudent(id);
		return true;
	}

	@Override
	public StudentModel queryStudentById(String id) {
		Student student = jdbcTemplateDao.queryStudentById(id);
		if (student == null) {
			return null;
		}
		return BeanMapperUtils.beanMapper(student, StudentModel.class);
	}

	@Override
	public StudentModel queryStudentByUsername(String username) {

		Student student = jdbcTemplateDao.queryStudentByUsername(username);
		if (student == null) {
			return null;
		}
		return BeanMapperUtils.beanMapper(student, StudentModel.class);
	}

	@Override
	public ListViewModel<StudentModel> queryStudentList(String words, String limit) {

		List<Student> studentList = jdbcTemplateDao.queryStudentItems(words, limit);
		Long total = jdbcTemplateDao.queryStudentTotal(words, limit);

		List<StudentModel> studentModelList = new ArrayList<StudentModel>();
		if (CollectionUtils.isNotEmpty(studentList)) {
			for (Student student : studentList) {
				StudentModel Model = BeanMapperUtils.beanMapper(student, StudentModel.class);
				studentModelList.add(Model);
			}
		}
		ListViewModel<StudentModel> result = new ListViewModel<StudentModel>();
		result.setTotal(total);
		result.setLimit(limit);
		result.setItems(studentModelList);
		return result;
	}

}
