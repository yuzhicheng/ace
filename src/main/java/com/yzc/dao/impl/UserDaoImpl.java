package com.yzc.dao.impl;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.yzc.dao.UserDao;
import com.yzc.entity.User;
import com.yzc.models.user.UserModel;
import com.yzc.repository.UserRepository;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger LOG = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Qualifier(value = "jdbcTemplate")
	@Autowired
	private JdbcTemplate defaultJdbcTemplate;

	@Override
	public List<UserModel> queryUserById(String id, String include) {
		final List<UserModel> result = new ArrayList<UserModel>();
		String querySql = "SELECT u.identifier AS identifier,u.card_id AS username,u.password as password FROM ace_user u WHERE u.identifier=:identifier";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("identifier", id);
		LOG.info("查询的SQL语句：" + querySql.toString());
		LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
		namedJdbcTemplate.query(querySql, params, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserModel um = new UserModel();
				um.setIdentifier(rs.getString("identifier"));
				um.setUsername(rs.getString("username"));
				um.setPassword(rs.getString("password"));
				result.add(um);
				return null;
			}
		});
		return result;
	}

	@Override
	public List<User> exactQueryUserItems(String words, String limit) {
		final List<User> resultList = new ArrayList<User>();
		Map<String, Object> params = new HashMap<String, Object>();
		Integer result[] = ParamCheckUtil.checkLimit(limit);
		String sqlLimit = " LIMIT " + result[0] + "," + result[1];
		String querySql = "SELECT *  FROM ace_user u";

		if (StringUtils.hasText(words)) {
			querySql += " WHERE u.name like :words or u.username like :words";
			params.put("words", words);
			LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
		}
		querySql += sqlLimit;
		LOG.info("查询的SQL语句：" + querySql.toString());
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
		namedJdbcTemplate.query(querySql, params, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = new User();
				u.setIdentifier(rs.getString("identifier"));
				u.setCardId(rs.getString("card_id"));
				u.setCreateTime(rs.getDate("create_time"));
				u.setUpdateTime(rs.getDate("update_time"));
				u.setDescription(rs.getString("description"));
			    u.setTitle(rs.getString("title"));
			    u.setCount(rs.getDouble("count"));
			    u.setPassword(rs.getString("password"));
				u.setUsername(rs.getString("username"));
				u.setName(rs.getString("name"));
				u.setTel(rs.getString("tel"));
				u.setPermission(rs.getInt("permission"));
				u.setSex(rs.getInt("sex"));
				resultList.add(u);
				return null;
			}
		});
		return resultList;
	}

	@Override
	public Long exactQueryUserTotal(String words, String limit) {

		String querySql = "select count(*) as total FROM ace_user u";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.hasText(words)) {
			querySql += " WHERE u.name like :words or u.username like :words";
			params.put("words", words);
			LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
		}
		LOG.info("查询的SQL语句：" + querySql.toString());
		NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
		@SuppressWarnings("deprecation")
		long total = namedJdbcTemplate.queryForLong(querySql, params);
		return total;
	}

	@Override
	public Long fuzzyQueryUserTotal(String words, String limit) {

		String querySql = "select count(*) as total FROM ace_user u";
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.hasText(words)) {
			querySql += " WHERE u.name like :words or u.username like :words";
			params.put("words", "%" + words + "%");
			LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
		}
		LOG.info("查询的SQL语句：" + querySql.toString());

		Query query = userRepository.getEntityManager().createNativeQuery(querySql);
		if (CollectionUtils.isNotEmpty(params)) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		BigInteger c = (BigInteger) query.getSingleResult();
		if (c != null) {
			return c.longValue();
		}

		return 0L;
	}

	@Override
	public List<User> fuzzyQueryUserItems(String words, String limit) {
	
		Map<String, Object> params = new HashMap<String, Object>();
		Integer result[] = ParamCheckUtil.checkLimit(limit);
		String sqlLimit = " LIMIT " + result[0] + "," + result[1];
		String querySql = "SELECT * FROM ace_user u";

		if (StringUtils.hasText(words)) {
			querySql += " WHERE u.name like :words or u.username like :words";
			params.put("words", "%" + words + "%");
			LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
		}
		querySql += sqlLimit;
		LOG.info("查询的SQL语句：" + querySql.toString());
		Query query = userRepository.getEntityManager().createNativeQuery(querySql, User.class);
		if (CollectionUtils.isNotEmpty(params)) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		return list;

	}

}
