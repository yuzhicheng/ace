package com.yzc.service;

import java.util.List;

import com.yzc.entity.User;
import com.yzc.models.user.UserModel;
import com.yzc.vos.ListViewModel;

public interface UserService {

	/**
	 * 创建用户
	 * 
	 * @author
	 * @date 2016年9月1日
	 * @param UserModel
	 * @param um
	 * @return UserModel
	 */
	public UserModel createUser(UserModel um);

	/**
	 * 更新用户
	 * 
	 * @author
	 * @date 2016年9月1日
	 * @param UserModel
	 * @param um
	 * @return UserModel
	 */
	public UserModel updateUser(UserModel um);

	/**
	 * 删除用户
	 * 
	 * @author
	 * @date 2016年9月1日
	 * @param id
	 * @return boolean
	 */
	public boolean deleteUser(String id);

	/**
	 * 分页查询用户列表
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 *            查询关键字
	 * @param limit
	 *            查询分页limit(0,10)，第一个参数为页的索引，第二个参数为每页的数量
	 * @return
	 */
	public List<User> getUserListPage(String words, String limit);

	public List<UserModel> queryUser(String id, String include);

	/**
	 * 查询用户列表,，可以根据name或者username进行精确查询。
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 *            查询关键字
	 * @param limit
	 *            查询分页,limit(0,10),第一个参数为记录的索引，第二个参数为偏移量
	 * @return ListViewModel<UserModel>
	 */
	public ListViewModel<UserModel> getUserList(String words, String limit);
	
	/**
	 * 查询用户列表,，可以根据name或者username进行模糊查询。
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 *            查询关键字
	 * @param limit
	 *            查询分页,limit(0,10),第一个参数为记录的索引，第二个参数为偏移量
	 * @return ListViewModel<UserModel>
	 */
	public ListViewModel<UserModel> fuzzyGetUserList(String words, String limit);

	/**
	 * 查询用户信息，可以根据title或者description进行查询。
	 * 
	 * @author yzc
	 * @date 2016年9月2日
	 * @param words
	 * @param limit
	 * @return ListViewModel
	 */
	public ListViewModel<UserModel> queryUserList(String words, String limit);

}
