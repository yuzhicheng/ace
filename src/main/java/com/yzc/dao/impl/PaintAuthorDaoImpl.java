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
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.yzc.dao.PaintAuthorDao;
import com.yzc.entity.PaintAuthor;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.repository.PaintAuthorRepository;
import com.yzc.repository.index.AdaptQueryRequest;
import com.yzc.repository.index.Hits;
import com.yzc.repository.index.QueryResponse;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ListViewModel;

@Repository
public class PaintAuthorDaoImpl implements PaintAuthorDao {

    private static final Logger LOG = LoggerFactory.getLogger(PaintAuthorDaoImpl.class);
    @Autowired
    private PaintAuthorRepository paintAuthorRepository;

    @Qualifier(value = "jdbcTemplate")
    @Autowired
    private JdbcTemplate defaultJdbcTemplate;

    @Override
    public PaintAuthor savePaintAuthor(PaintAuthor paintAuthor) {
        try {
            return paintAuthorRepository.add(paintAuthor);
        } catch (EspStoreException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public PaintAuthor updatePaintAuthor(PaintAuthor paintAuthor) {
        try {
            return paintAuthorRepository.update(paintAuthor);
        } catch (EspStoreException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public void deletePaintAuthor(String id) {
        try {
            paintAuthorRepository.del(id);
        } catch (EspStoreException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public PaintAuthor getPaintAuthor(String id) {
        try {
            return paintAuthorRepository.get(id);
        } catch (EspStoreException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public ListViewModel<PaintAuthor> queryPaintAuthorList(String authorName, String limit) {
        ListViewModel<PaintAuthor> returnValue = new ListViewModel<>();
        returnValue.setLimit(limit);
        try {
            AdaptQueryRequest<PaintAuthor> adaptQueryRequest = new AdaptQueryRequest<>();
            Integer result[] = ParamCheckUtil.checkLimit(limit);
            adaptQueryRequest.setOffset(result[0]);
            adaptQueryRequest.setLimit(result[1]);
            if (StringUtils.hasText(authorName)) {
                adaptQueryRequest.and("authorName", authorName);
            }

            QueryResponse<PaintAuthor> response = paintAuthorRepository.searchByExampleSupportLike(adaptQueryRequest);
            if (response != null) {
                Hits<PaintAuthor> hits = response.getHits();
                if (hits != null) {
                    returnValue.setTotal(hits.getTotal());
                    returnValue.setItems(hits.getDocs());
                }
            }
            return returnValue;
        } catch (EspStoreException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public List<PaintAuthor> batchGetPaintAuthor(List<String> ids) {
        try {
            return paintAuthorRepository.getAll(ids);
        } catch (EspStoreException e) {
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
                    e.getLocalizedMessage());
        }
    }

    @Override
    public List<String> getAuthorIdByNameAndNationality(String authorName, String nationality) {
        Map<String, Object> params = new HashMap<>();
        String querySql = "select identifier as identifier from author where ";

        if (StringUtils.hasText(authorName)) {
            querySql += "author_name like :name";
            params.put("name", "%" + authorName + "%");
        }
        if (StringUtils.hasText(nationality)) {
            querySql += "nationality=:nat";
            params.put("nat", nationality);
        }
        if (StringUtils.hasText(authorName) && StringUtils.hasText(nationality)) {
            querySql += "author_name like :name and nationality=:nat";
            params.put("name", "%" + authorName + "%");
            params.put("nat", nationality);
        }

        final List<String> userIdList = new ArrayList<>();
        NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(paintAuthorRepository.getJdbcTemple());
        npjt.query(querySql, params, new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                userIdList.add(rs.getString("identifier"));
                return null;
            }
        });
        return userIdList;
    }

    @Override
    public List<PaintAuthor> queryAuthorByNameAndNationality(String authorName, String nationality, String limit) {

        Integer result[] = ParamCheckUtil.checkLimit(limit);
        String sqlLimit = " LIMIT " + result[0] + "," + result[1];
        final List<PaintAuthor> resultList = new ArrayList<>();
        String querySql = "SELECT a.identifier AS identifier,a.author_name AS author_name,a.birthdate as birthdate FROM author  a WHERE a.author_name=:name and a.nationality=:nationality" + sqlLimit;
        Map<String, Object> params = new HashMap<>();
        params.put("name", authorName);
        params.put("nationality", nationality);
        LOG.info("查询的SQL语句：" + querySql);
        LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
        NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(defaultJdbcTemplate);
        namedJdbcTemplate.query(querySql, params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                PaintAuthor uim = new PaintAuthor();
                uim.setIdentifier(rs.getString("identifier"));
                uim.setAuthorName(rs.getString("author_name"));
                uim.setBirthdate(rs.getString("birthdate"));
                resultList.add(uim);
                return null;
            }
        });
        return resultList;
    }

    @Override
    public Long queryCountByNameAndNationality(String authorName, String nationality, String limit) {

        String querySql = "select count(*) as total FROM author a where a.author_name=:name and a.nationality=:nationality";
        Map<String, Object> params = new HashMap<>();
        params.put("name", authorName);
        params.put("nationality", nationality);
        LOG.info("查询的SQL语句：" + querySql);
        LOG.info("查询的SQL参数:" + ObjectUtils.toJson(params));
        Query query = paintAuthorRepository.getEntityManager().createNativeQuery(querySql);
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

}
