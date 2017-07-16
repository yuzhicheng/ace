package com.yzc.dao;

import java.util.List;

import com.yzc.entity.User;
import com.yzc.models.user.UserModel;

public interface UserDao {

	List<UserModel> queryUserById(String id, String include);

	List<User> exactQueryUserItems(String words, String limit);
	
	List<User> fuzzyQueryUserItems(String words, String limit);

	Long exactQueryUserTotal(String words, String limit);
	
	Long fuzzyQueryUserTotal(String words, String limit);

	
}
