package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.yzc.dao.UserDao;
import com.yzc.entity.User;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.user.UserModel;
import com.yzc.repository.UserRepository;
import com.yzc.repository.index.QueryRequest;
import com.yzc.repository.index.QueryResponse;
import com.yzc.service.UserService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.RecordStatus;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.vos.ListViewModel;

/**
 * 用户管理 Service层
 * 
 * @author yzc
 * @date 2016年9月30日
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDao userDao;
	
	private final static ExecutorService executorService = CommonHelper.getPrimaryExecutorService();

	@Override
	public UserModel createUser(UserModel um) {

		// 判断主键id是否已经存在
		boolean flag = isDuplicateId(um.getIdentifier());
		if (flag) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckDuplicateIdFail);
		}
		// 逻辑校验,username不允许重复
		User u4username = new User();
		u4username.setUsername(um.getUsername());
		try {
			u4username = userRepository.getByExample(u4username);
		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}
		if (u4username != null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckUserExist);
		}

		User u = BeanMapperUtils.beanMapper(um, User.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		u.setCreateTime(ts);
		u.setUpdateTime(ts);
		u.setState(RecordStatus.NORMAL);

		try {
			u = userRepository.add(u);
		} catch (EspStoreException e) {
			LOG.error("创建用户出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}
		if (u == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CreateUserFail);
		}
		return BeanMapperUtils.beanMapper(u, UserModel.class);
	}

	@Override
	public List<User> getUserListPage(String words, String limit) {

		Integer result[] = ParamCheckUtil.checkLimit(limit);
		// Pageable pageable=new PageRequest(result[0], result[1],
		// Sort.Direction.ASC);
		Pageable pageable = new PageRequest(result[0], result[1], Direction.ASC, "cardId");
		Page<User> page = userRepository.findByName(words, pageable);
		return page.getContent();
	}

	@Override
	public List<UserModel> queryUser(String id, String include) {

		return userDao.queryUserById(id, include);
	}

	@Override
	public boolean deleteUser(String id) {

		User u4Detail = null;
		// 校验id
		try {
			u4Detail = userRepository.get(id);
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}
		if (u4Detail == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.UserNotFound);
		}
		try {
			userRepository.del(id);
		} catch (EspStoreException e) {
			LOG.error("删除用户出错");
		}
		return true;
	}

	@Override
	public UserModel updateUser(UserModel um) {

		User u4Detail = null;
		// 校验id
		try {
			u4Detail = userRepository.get(um.getIdentifier());
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}
		if (u4Detail == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.UserNotFound);
		}

		// 判断username是否重复,如果是修改成原本的username是允许的
		User u4Username = new User();
		u4Username.setUsername(um.getUsername());
		try {
			u4Username = userRepository.getByExample(u4Username);
		} catch (EspStoreException e) {
			LOG.error("校验username是否重复时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}

		if (u4Username != null && !u4Username.getIdentifier().equals(u4Detail.getIdentifier())) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckUserExist);
		}

		User u = BeanMapperUtils.beanMapper(um, User.class);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		u.setCreateTime(u4Detail.getCreateTime());
		u.setUpdateTime(ts);
		u.setState(u4Detail.getState());
		try {
			u = userRepository.update(u);
		} catch (EspStoreException e) {
			LOG.error("修改用户出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}
		if (u == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail);
		}
		return BeanMapperUtils.beanMapper(u, UserModel.class);
	}
	
	private boolean isDuplicateId(String identifier) {
		User u4Detail = null;
		// 校验id
		try {
			u4Detail = userRepository.get(identifier);
		} catch (EspStoreException e) {
			LOG.error("校验id是否存在时查询出错");
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorMessageMapper.StoreSdkFail.getCode(), e.getMessage());
		}
		if (u4Detail != null) {
			return true;
		}
		
		return false;
	}

	@Override
	public ListViewModel<UserModel> getUserList(String words, String limit) {

		ListViewModel<UserModel> resultList = new ListViewModel<UserModel>();
		List<Callable<QueryThread>> threads=new ArrayList<Callable<QueryThread>>();
		QueryThread countThread=new QueryThread(true,words,limit);
		QueryThread queryThread=new QueryThread(false,words,limit);
		threads.add(countThread);
        threads.add(queryThread);
        List<UserModel> items = new ArrayList<UserModel>();
        List<User> userList = new ArrayList<User>();
        try {
            List<Future<QueryThread>> results = executorService.invokeAll(threads, 10*60, TimeUnit.SECONDS);
            for(Future<QueryThread> result:results){
                try {
                    if(result.get().isCount()){
                    	resultList.setTotal(result.get().getTotal());
                    }else{
                    	userList = result.get().getItems();
                    }
                } catch (ExecutionException e) {
                    throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "ACE/USER_QUERY_GET_FAIL",
                            e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "ACE/USER_QUERY_GET_FAIL",
                    e.getMessage());
        }
      //根据username倒序
        if(CollectionUtils.isNotEmpty(userList)){
        	Collections.sort(userList, new Comparator<User>(){
				@Override
				public int compare(User o1, User o2) {
					return o2.getUsername().compareTo(o1.getUsername());
				}
        	});
        }
        //User to UserModel
    	if(CollectionUtils.isNotEmpty(userList)){
    		for (User user : userList) {
                if (user != null) {
                	items.add(BeanMapperUtils.beanMapper(user, UserModel.class));
                }
            }
    	}
        resultList.setItems(items);
		resultList.setLimit(limit);
		return resultList;
	}

	@Override
	public ListViewModel<UserModel> fuzzyGetUserList(String words, String limit) {

		List<User> userList = userDao.fuzzyQueryUserItems(words, limit);

		Long total = userDao.fuzzyQueryUserTotal(words, limit);

		List<UserModel> userModelList = new ArrayList<UserModel>();
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User student : userList) {
				UserModel Model = BeanMapperUtils.beanMapper(student, UserModel.class);
				userModelList.add(Model);
			}
		}

		ListViewModel<UserModel> result = new ListViewModel<UserModel>();
		result.setTotal(total);
		result.setLimit(limit);
		result.setItems(userModelList);
		return result;
	}

	@Override
	public ListViewModel<UserModel> queryUserList(String words, String limit) {
		ListViewModel<UserModel> result = new ListViewModel<UserModel>();

		// requestParam
		QueryRequest queryRequest = new QueryRequest();
		Integer limitResult[] = ParamCheckUtil.checkLimit(limit);// 这里其实只需要分解数据
		queryRequest.setKeyword(words);
		queryRequest.setLimit(limitResult[1]);
		queryRequest.setOffset(limitResult[0]);

		// 调用sdk
		try {
			LOG.debug("调用sdk方法:search");
			QueryResponse<User> response = userRepository.search(queryRequest);

			// 处理返回数据
			long total = 0L;
			List<UserModel> items = new ArrayList<UserModel>();
			if (response != null && response.getHits() != null) {

				items = ObjectUtils.fromJson(ObjectUtils.toJson(response.getHits().getDocs()),
						new TypeToken<List<UserModel>>() {
						});
				total = response.getHits().getTotal();
			}
			result.setTotal(total);
			result.setItems(items);
			result.setLimit(limit);

		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getMessage());
		}

		return result;
	}
	
	class QueryThread implements Callable<QueryThread> {
		
		private boolean isCount;
		private String words;
		private String limit;
		
		private Long total;
		private List<User> items;
		
		public Long getTotal(){
			return total;
		}
		
		public List<User> getItems(){
			return items;
		}
		
		public boolean isCount(){
			return isCount;
		}
		public QueryThread(boolean isCount,String words,String limit) {
			
			this.isCount=isCount;
			this.words=words;
			this.limit=limit;
		}

		@Override
		public QueryThread call() throws Exception {
			if(isCount){
				this.total=userDao.exactQueryUserTotal(words, limit);
			}
			else{
				this.items=userDao.exactQueryUserItems(words, limit);
			}
			return this;
		}
	}

	// /**
	// * 获取用户信息
	// * @author yzc
	// * @date 2016年9月2日
	// * @param ids
	// * @param includeList
	// * @return
	// */
	// private ListViewModel<UserModel> getUserInfos(List<UserModel>userList) {
	// ListViewModel<UserModel> result = new ListViewModel<UserModel>();
	//
	// if (CollectionUtils.isEmpty(userList)) {
	// result.setTotal(0L);
	// result.setItems(new ArrayList<UserModel>());
	// return result;
	// }
	//
	// List<UserModel> userModels=new ArrayList<UserModel>();
	// if (CollectionUtils.isNotEmpty(userList)) {
	// for (UserModel model : userList) {
	// userModels.add(model);
	// }
	// }
	//
	//
	// if (CollectionUtils.isEmpty(userModels)) {
	// result.setTotal(0L);
	// result.setItems(new ArrayList<UserModel>());
	// return result;
	// }
	//
	// result.setTotal(new Long(userModels.size()));
	// result.setItems(userModels);
	// result.setLimit(null);
	// return result;
	// }

}
