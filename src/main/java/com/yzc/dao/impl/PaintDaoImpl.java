package com.yzc.dao.impl;

import com.yzc.dao.PaintDao;
import com.yzc.entity.Paint;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.repository.PaintRepository;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PaintDaoImpl implements PaintDao {

	@Autowired
	private PaintRepository paintRepository;

	@Override
	public Paint savePaint(Paint paint) {
		try {
			return paintRepository.add(paint);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getLocalizedMessage());
		}
	}

	@Override
	public Paint updatePaint(Paint paint) {
		try {
			return paintRepository.update(paint);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getLocalizedMessage());
		}
	}

	@Override
	public void deletePaint(String id) {
		try {
			paintRepository.del(id);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getLocalizedMessage());
		}
	}

	@Override
	public Paint getPaint(String id) {
		try {
			return paintRepository.get(id);
		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getLocalizedMessage());
		}
	}

	@Override
	public long getCountByCond(String title, List<String> tags, String creator, List<String> authorIds) {
		Map<String, Object> params = new HashMap<>();
		String sql = "select count(identifier) from paints where state = 1";

		if (StringUtils.hasText(title)) {
			sql += " and (title like :title or title_cn like :titleCn)";
			params.put("title", "%" + title + "%");
			params.put("titleCn", "%" + title + "%");
		}
		if (CollectionUtils.isNotEmpty(tags)) {
			int i = 0;
			for (String tag : tags) {
				sql += " and tags like :tags" + i;
				params.put("tags" + i, "%" + tag + "%");
				i++;
			}
		}
		if (StringUtils.hasText(creator)) {
			sql += " and creator=:creator";
			params.put("creator", creator);
		}
		if (CollectionUtils.isNotEmpty(authorIds)) {
			sql += " and author_id in (:userIds)";
			params.put("userIds", authorIds);
		}

		Query query = paintRepository.getEntityManager().createNativeQuery(sql);
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
	public List<Paint> queryListByCond(String title, List<String> tags, String creator, List<String> authorIds,
			int offset, int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select * from paints where state = 1";
		if (StringUtils.hasText(title)) {
			sql += " and (title like :title or title_cn like :titleCn)";
			params.put("title", "%" + title + "%");
			params.put("titleCn", "%" + title + "%");
		}
		if (CollectionUtils.isNotEmpty(tags)) {
			int i = 0;
			for (String tag : tags) {
				sql += " and tags like :tags" + i;
				params.put("tags" + i, "%" + tag + "%");
				i++;
			}
		}
		if (StringUtils.hasText(creator)) {
			sql += " and creator=:creator";
			params.put("creator", creator);
		}
		if (CollectionUtils.isNotEmpty(authorIds)) {
			sql += " and author_id in (:userIds)";
			params.put("userIds", authorIds);
		}

		sql += " limit " + offset + "," + pageSize;
		Query query = paintRepository.getEntityManager().createNativeQuery(sql, Paint.class);
		if (CollectionUtils.isNotEmpty(params)) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}
		@SuppressWarnings("unchecked")
		List<Paint> list = query.getResultList();
		return list;
	}

	@Override
	public int queryCountByResId(String identifier) {
		String sql = "select count(identifier) from paints where identifier = :identifier";
		Query query = paintRepository.getEntityManager().createNativeQuery(sql);
		query.setParameter("identifier", identifier);
		BigInteger num = (BigInteger) query.getSingleResult();
		return num.intValue();
	}
}
