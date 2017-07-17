package com.yzc.dao;

import java.util.List;

import com.yzc.entity.User;
import com.yzc.models.user.UserModel;

public interface UserDao {

    /**
     * 根据id查询用户信息
     *
     * @param id      用户id
     * @param include 包括的属性
     * @return 用户列表
     */
    List<UserModel> queryUserById(String id, String include);

    /**
     * 精确查询用户列表
     *
     * @param words 关键字
     * @param limit 分页参数
     * @return 用户列表
     */
    List<User> exactQueryUserItems(String words, String limit);

    /**
     * 模糊查询用户列表
     *
     * @param words 关键字
     * @param limit 分页参数
     * @return 用户列表
     */
    List<User> fuzzyQueryUserItems(String words, String limit);

    /**
     * 精确查询用户数量
     *
     * @param words 关键字
     * @param limit 分页参数
     * @return 用户数量
     */
    Long exactQueryUserTotal(String words, String limit);

    /**
     * 模糊查询用户数量
     *
     * @param words 关键字
     * @param limit 分页参数
     * @return
     */
    Long fuzzyQueryUserTotal(String words, String limit);


}
