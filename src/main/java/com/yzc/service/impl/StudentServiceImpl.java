package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.yzc.entity.Student;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.StudentModel;
import com.yzc.repository.StudentRepository;
import com.yzc.repository.index.AdaptQueryRequest;
import com.yzc.repository.index.QueryRequest;
import com.yzc.repository.index.QueryResponse;
import com.yzc.service.StudentService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.RecordStatus;
import com.yzc.support.log.DBLogUtil;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ListViewModel;

/**
 * 用户管理 Service层
 * 
 * @author yzc
 * @date 2016年9月30日
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	private static final Logger LOG = LoggerFactory
			.getLogger(StudentServiceImpl.class);
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public StudentModel createStudent(StudentModel sm) {
		
		// 判断主键id是否已经存在
		boolean flag = isDuplicateId(sm.getIdentifier());
		if (flag) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckDuplicateIdFail);
		}
		// 逻辑校验,username不允许重复
		// Student s4username = null;
		Student s4username = new Student();
		s4username.setUsername(sm.getUsername());
		try {
			// s4username = studentRepository.findByUsername(sm.getUsername());
			s4username = studentRepository.getByExample(s4username);
		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s4username != null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.CheckStudentExist);
		}

		Student st = BeanMapperUtils.beanMapper(sm, Student.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		st.setCreateTime(ts);
		st.setUpdateTime(ts);
		st.setState(RecordStatus.NORMAL);
		try {
			st = studentRepository.add(st);
		} catch (EspStoreException e) {
			LOG.error("创建学生出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (st == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail);
		}
		MDC.put("identifier", UUID.randomUUID().toString());
        MDC.put("user_id", "940605");
        MDC.put("method", "POST");
        MDC.put("user_name", "余志诚");
        MDC.put("action", "/student/create");
        DBLogUtil.getDBlog().info("创建学生成功");
        MDC.clear();
		return BeanMapperUtils.beanMapper(st, StudentModel.class);
	}

	@Override
	public StudentModel updateStudent(StudentModel sm) {

		Student s4Detail = null;
		// 校验id
		try {
			s4Detail = studentRepository.get(sm.getIdentifier());
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s4Detail == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StudentNotFound);
		}

		// 判断username是否重复,如果是修改成原本的username是允许的
		Student s4Username = new Student();
		s4Username.setUsername(sm.getUsername());
		try {
			s4Username = studentRepository.getByExample(s4Username);
		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		if (s4Username != null && !s4Username.getIdentifier().equals(s4Detail.getIdentifier())) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.CheckStudentExist);
		}

		Student s = BeanMapperUtils.beanMapper(sm, Student.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		s.setUpdateTime(ts);
		s.setCreateTime(s4Detail.getCreateTime());
		s.setState(s4Detail.getState());
		try {
			s = studentRepository.update(s);

		} catch (EspStoreException e) {
			LOG.error("修改学生出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail);
		}
		return BeanMapperUtils.beanMapper(s, StudentModel.class);
	}
	
	@Override
	public boolean deleteUser(String id) {

		Student u4Detail = null;
		// 校验id
		try {
			u4Detail = studentRepository.get(id);
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (u4Detail == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StudentNotFound);
		}
		try {
			studentRepository.del(id);
		} catch (EspStoreException e) {
			LOG.error("删除学生出错");
		}
		return true;

	}

	@Override
	public ListViewModel<StudentModel> readStudentList(String words,
			String limit) {
		ListViewModel<StudentModel> result = new ListViewModel<StudentModel>();

		AdaptQueryRequest<Student> adaptQueryRequest = new AdaptQueryRequest<Student>();
		Integer limitResult[] = ParamCheckUtil.checkLimit(limit);
		adaptQueryRequest.setLimit(limitResult[1]);
		adaptQueryRequest.setOffset(limitResult[0]);
		if (StringUtils.hasText(words)) {
			adaptQueryRequest.and("username", words);
		}
		try {
			QueryResponse<Student> queryResponse = studentRepository
					.searchByExampleSupportLike(adaptQueryRequest);
			long total = 0L;
			List<StudentModel> items = new ArrayList<StudentModel>();
			if (queryResponse != null && queryResponse.getHits() != null) {

				items = ObjectUtils.fromJson(
						ObjectUtils.toJson(queryResponse.getHits().getDocs()),
						new TypeToken<List<StudentModel>>() {
						});
				total = queryResponse.getHits().getTotal();
			}
			result.setTotal(total);
			result.setItems(items);
			result.setLimit(limit);

		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		return result;
	}

	@Override
	public ListViewModel<StudentModel> queryStudentList(String words,
			String limit, String username) {
		ListViewModel<StudentModel> result = new ListViewModel<StudentModel>();

		// requestParam
		AdaptQueryRequest<Student> adaptQueryRequest = new AdaptQueryRequest<Student>();
		adaptQueryRequest.and("username", username);
		Integer limitResult[] = ParamCheckUtil.checkLimit(limit);
		adaptQueryRequest.setKeyword(words);
		adaptQueryRequest.setLimit(limitResult[1]);
		adaptQueryRequest.setOffset(limitResult[0]);

		// 调用sdk

		LOG.debug("调用sdk方法:searchByExample");

		try {
			QueryResponse<Student> response = studentRepository
					.searchByExample(adaptQueryRequest);

			// 处理返回数据
			long total = 0L;
			List<StudentModel> items = new ArrayList<StudentModel>();
			if (response != null && response.getHits() != null) {
				List<Student> student = response.getHits().getDocs();
				if (student != null) {
					for (Student st : student) {
						StudentModel studentModel = BeanMapperUtils.beanMapper(
								st, StudentModel.class);
						items.add(studentModel);
					}
				}
				total = response.getHits().getTotal();
			}
			result.setItems(items);
			result.setTotal(total);
			result.setLimit(limit);

		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		return result;
	}

	@Override
	public ListViewModel<StudentModel> getStudentList(String words, String limit) {
		ListViewModel<StudentModel> result = new ListViewModel<StudentModel>();

		// requestParam
		QueryRequest queryRequest = new QueryRequest();
		Integer limitResult[] = ParamCheckUtil.checkLimit(limit);// 这里其实只需要分解数据
		queryRequest.setKeyword(words);
		queryRequest.setLimit(limitResult[1]);
		queryRequest.setOffset(limitResult[0]);

		// 调用sdk
		try {
			LOG.debug("调用sdk方法:search");
			QueryResponse<Student> response = studentRepository
					.search(queryRequest);

			// 处理返回数据
			long total = 0L;
			List<StudentModel> items = new ArrayList<StudentModel>();
			if (response != null && response.getHits() != null) {
				items = ObjectUtils.fromJson(
						ObjectUtils.toJson(response.getHits().getDocs()),
						new TypeToken<List<StudentModel>>() {
						});
				total = response.getHits().getTotal();
			}
			result.setTotal(total);
			result.setItems(items);
			result.setLimit(limit);

		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		return result;
	}

	@Override
	public StudentModel queryStudentById(String id) {
		StudentModel result = null;
		try {
			LOG.debug("调用sdk方法:get");
			Student beanResult = studentRepository.get(id);
			if (beanResult != null) {
				// 成功返回了数据
				result = BeanMapperUtils.beanMapper(beanResult,
						StudentModel.class);
			}

		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		return result;
	}

	@Override
	public StudentModel queryStudentByUsername(String username) {

		Student s4username = new Student();
		s4username.setUsername(username);
		StudentModel result = null;
		try {
			LOG.debug("调用sdk方法:get");
			Student beanResult = studentRepository.getByExample(s4username);
			if (beanResult != null) {
				// 成功返回了数据
				result = BeanMapperUtils.beanMapper(beanResult,
						StudentModel.class);
			}

		} catch (Exception e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		return result;
	}

	@Override
	public boolean batchDeteleStudent(LinkedHashSet<String> idSet) {
		// 先断定是否存在：
		for (String id : idSet) {
			assertStudentById(id);
		}
		try {
			LOG.debug("调用sdk方法:batchDel");
			studentRepository.batchDel(new ArrayList<String>(idSet));
		} catch (EspStoreException e) {
			LOG.error("批量删除学生出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		return true;
	}

	@Override
	public Map<String, StudentModel> batchGetDetailStudentByUsername(
			Set<String> usernameSet) {
		Map<String, StudentModel> modelMap = new HashMap<String, StudentModel>();
		try {
			LOG.debug("调用sdk方法:getListWhereInCondition");

			List<Student> beanListResult = studentRepository
					.getListWhereInCondition("username", new ArrayList<String>(
							usernameSet));
			if (beanListResult != null && !beanListResult.isEmpty()) {
				for (Student beanResult : beanListResult) {
					if (beanResult != null) {
						modelMap.put(beanResult.getUsername(), BeanMapperUtils
								.beanMapper(beanResult, StudentModel.class));
					}
				}
			}

		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		return modelMap;
	}

	@Override
	public List<StudentModel> batchCreateStudent(
			List<StudentModel> studentModelList) {

		for (StudentModel studentModel : studentModelList) {
			if (studentModel != null) {
				// 判断学生是否已存在：通过校验username
				assertStudentByUsername(studentModel.getUsername());
			}

		}

		// 入参转换model->Bean
		List<Student> beanList = new ArrayList<Student>();
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		for (StudentModel studentModel : studentModelList) {
			if (studentModel != null) {
				Student st = BeanMapperUtils.beanMapper(studentModel,Student.class);
				st.setIdentifier(UUID.randomUUID().toString());
				st.setCreateTime(ts);
				st.setUpdateTime(ts);
				st.setState(RecordStatus.NORMAL);
				beanList.add(st);
			}
		}

		List<StudentModel> resultList = new ArrayList<StudentModel>();

		List<Student> beanListResult;
		try {

			LOG.debug("调用sdk方法:batchAdd");
			beanListResult = studentRepository.batchAdd(beanList);
		} catch (EspStoreException e) {

			LOG.error(ErrorMessageMapper.StoreSdkFail.getMessage(), e);

			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		// 结果处理
		if (beanListResult != null && !beanListResult.isEmpty()) {
			for (Student bean : beanListResult) {

				LOG.debug("创建学生:{}", bean.getIdentifier());

				resultList.add(BeanMapperUtils.beanMapper(bean,
						StudentModel.class));
			}
		}
		return resultList;
	}

	@Override
	public Map<String, StudentModel> batchGetDetailStudent(
			Set<String> studentNameSet) {

		Map<String, StudentModel> modelMap = new HashMap<String, StudentModel>();
		try {
			LOG.debug("调用sdk方法:getListWhereInCondition");

			List<Student> beanListResult = studentRepository
					.getListWhereInCondition("name", new ArrayList<String>(
							studentNameSet));
			if (beanListResult != null && !beanListResult.isEmpty()) {
				for (Student beanResult : beanListResult) {
					if (beanResult != null) {
						modelMap.put(beanResult.getName(), BeanMapperUtils
								.beanMapper(beanResult, StudentModel.class));
					}
				}
			}

		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		return modelMap;
	}

	private void assertStudentByUsername(String username) {

		Student condition = new Student();
		condition.setUsername(username);
		List<Student> relations = null;
		try {
			LOG.debug("调用sdk方法:getAllByExample");
			relations = studentRepository.getAllByExample(condition);
		} catch (EspStoreException e) {

			LOG.error(ErrorMessageMapper.StoreSdkFail.getMessage());

			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (CollectionUtils.isEmpty(relations)) {

		} else {

			if (relations.get(0) != null) {
				LOG.error(ErrorMessageMapper.CheckStudentExist.getMessage());
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
						ErrorMessageMapper.CheckStudentExist);
			}
		}

	}

	private void assertStudentById(String id) {

		Student testStudent = null;
		try {
			testStudent = studentRepository.get(id);
		} catch (EspStoreException e) {
			LOG.error("检测学生不存在");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (testStudent == null) {
			// 抛出不存在该数据的异常;
			LOG.error(ErrorMessageMapper.StudentNotFound.getMessage());
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StudentNotFound);
		}

	}

	@Override
	public StudentModel patchStudent(StudentModel sm) {
		
		Student s4Detail = null;
		// 校验id
		try {
			s4Detail = studentRepository.get(sm.getIdentifier());
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s4Detail == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StudentNotFound);
		}

		// 判断username是否重复,如果是修改成原本的username是允许的
		Student s4Username = new Student();
		s4Username.setUsername(sm.getUsername());
		try {
			s4Username = studentRepository.getByExample(s4Username);
		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}

		if (s4Username != null && !s4Username.getIdentifier().equals(s4Detail.getIdentifier())) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.CheckStudentExist);
		}

		Student s = BeanMapperUtils.beanMapper(sm, Student.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		s.setUpdateTime(ts);
		s.setCreateTime(s4Detail.getCreateTime());
		s.setState(s4Detail.getState());
		try {
			s = studentRepository.update(s);

		} catch (EspStoreException e) {
			LOG.error("修改学生出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail);
		}
		return BeanMapperUtils.beanMapper(s, StudentModel.class);
	}
	
	private boolean isDuplicateId(String identifier) {
		Student s4Detail = null;
		// 校验id
		try {
			s4Detail = studentRepository.get(identifier);
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (s4Detail != null) {
			return true;
		}
		
		return false;
	}

}
