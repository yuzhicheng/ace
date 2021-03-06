package com.yzc.dao.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.yzc.dao.CommonDao;
import com.yzc.entity.ResCategory;
import com.yzc.entity.TechInfo;
import com.yzc.utils.CollectionUtils;

@Repository
public class CommonDaoImpl implements CommonDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	EntityManager em;

	@Override
	public int deleteTechInfoByResource(String resType, String resourceId) {

		Query query = em.createNamedQuery("deleteTechInfoByResource");
		query.setParameter("resourceId", resourceId);
		return query.executeUpdate();
	}

	@Override
	public int deleteResourceCategoryByResource(String resType, String resourceId) {

		Query query = em.createNamedQuery("deleteResourceCategoryByResource");
		query.setParameter("resourceId", resourceId);
		query.setParameter("rts", resType);
		return query.executeUpdate();
	}

	/**
	 * 查询技术属性（批量resource id）
	 * 
	 * @author yzc
	 * @param resTypes
	 * @param keySet
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TechInfo> queryTechInfosByResourceSet(List<String> resTypes, Set<String> keySet) {

		if (CollectionUtils.isEmpty(resTypes)) {
			return null;
		}

		Query query = em.createNamedQuery("commonQueryGetTechInfos");
		query.setParameter("rts", resTypes);
		query.setParameter("sids", keySet);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResCategory> queryCategoriesUseHql(List<String> resTypes, Set<String> keySet) {
		
	       if(CollectionUtils.isEmpty(resTypes)){
	        	return null;
	        }
	    	
	    	Query query = em.createNamedQuery("commonQueryGetCategories");    
	        query.setParameter("rts", resTypes);
	        query.setParameter("sids", keySet);
	        
	        return query.getResultList();
	}

}
