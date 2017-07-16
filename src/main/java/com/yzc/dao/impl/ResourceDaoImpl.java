package com.yzc.dao.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.yzc.dao.ResourceDao;
import com.yzc.models.ResourceModel;

@Repository
public class ResourceDaoImpl  implements ResourceDao{
	
	@PersistenceContext(unitName="entityManagerFactory")
	EntityManager defaultEm;

	@Override
	public Long commomQueryCount(String resType, String resCodes, Set<String> categories, Set<String> categoryExclude,
			List<Map<String, String>> relations, List<String> coverages, Map<String, Set<String>> propsMap,
			String words, String limit, boolean reverse, boolean useIn, List<String> tags) {
	
		return null;
	}

	@Override
	public List<ResourceModel> commomQueryByDB(String resType, String resCodes, List<String> includes,
			Set<String> categories, Set<String> categoryExclude, List<Map<String, String>> relations,
			List<String> coverages, Map<String, Set<String>> propsMap, Map<String, String> orderMap, String words,
			String limit, boolean reverse, boolean useIn, List<String> tags) {
		
		return null;
	}

	@Override
	public boolean judgeUseInOrExists(String resType, String resCodes, Set<String> categories,
			Set<String> categoryExclude, List<Map<String, String>> relations, List<String> coverages,
			Map<String, Set<String>> propsMap, String words, boolean reverse, List<String> tags) {
		
		return false;
	}

	public static boolean judgeUseRedisOrNot(String string, List<String> coverages, Map<String, String> orderMap) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int queryResCountByResId(String resourceType, String identifier) {
		
		String sql = "select count(identifier) from resource where identifier = '" + identifier + "' and primary_category = '"+resourceType+"'";
		Query query = defaultEm.createNativeQuery(sql);
		BigInteger num = (BigInteger)query.getSingleResult();
		return num.intValue();
	}

	@Override
	public int queryCodeCountByResId(String resType, String identifier,String code) {
		
		String sql = "select count(identifier) from resource where primary_category = '" + resType + "' and enable = 1 and code = '" + code + "' and identifier <> '"+identifier+"'";
		Query query = defaultEm.createNativeQuery(sql);
		BigInteger num = (BigInteger)query.getSingleResult();
		return num.intValue();
	}

}
