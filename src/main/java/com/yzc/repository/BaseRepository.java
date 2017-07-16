package com.yzc.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.yzc.entity.BaseEntity;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.repository.index.AdaptQueryRequest;
import com.yzc.repository.index.QueryRequest;
import com.yzc.repository.index.QueryResponse;

/**
 * 基础接口
 *
 * @author 
 * @version V1.0
 * @param <T>
 *           
 * @Description  基础接口
 * @date 2016年10月10日 下午6:09:12
 */

public interface BaseRepository<T extends BaseEntity> {

	/**
	 * Adds the.
	 *
	 * @param bean
	 *            the bean
	 * @return the t
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public T add(T bean) throws EspStoreException;

	/**
	 * Batch add.
	 *
	 * @param bean
	 *            the bean
	 * @return the list
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public List<T> batchAdd(List<T> bean) throws EspStoreException;

	/**
	 * Delete.
	 *
	 * @param id
	 *            the id
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public void del(String id) throws EspStoreException;

	/**
	 * Batch del.
	 *
	 * @param id
	 *            the id
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public void batchDel(List<String> id) throws EspStoreException;

	/**
	 * Update.
	 *
	 * @param bean
	 *            the bean
	 * @return the t
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public T update(T bean) throws EspStoreException;

	/**
	 * Gets the.
	 *
	 * @param id
	 *            the id
	 * @return the t
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public T get(String id) throws EspStoreException;

	/**
	 * Gets the all.
	 *
	 * @param ids
	 *            the ids
	 * @return the all
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public List<T> getAll(List<String> ids) throws EspStoreException;

	public Page<T> findAll(Pageable pageable);

	public T converterOut(final T bean) throws EspStoreException;

	/**
	 * Gets the list in property.<br>
	 * 
	 * Example:查询名字为"yanglin","yanglin2"的结果<br>
	 * List&lt;Addon&gt; ls =
	 * addonRepository.getListWhereInCondition("name",Arrays
	 * .asList("yanglin","yanglin2"));<br>
	 *
	 * @param propertyName
	 *            the property name
	 * @param propertyValues
	 *            the property values
	 * @return the list in property
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	List<T> getListWhereInCondition(String propertyName, List<?> propertyValues)
			throws EspStoreException;

	/**
	 * 通过bean设置值查询.
	 * 根据entity中存在的不为null的属性进行查询，多个属性之间的查询为and
	 * @param entity
	 *            the entity
	 * @return the by example
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	T getByExample(T entity) throws EspStoreException;

	/**
	 * Gets the all by example.
	 *
	 * @param entity
	 *            the entity
	 * @return the all by example
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	List<T> getAllByExample(T entity) throws EspStoreException;

	/**
	 * Gets the page by example.
	 *
	 * @param entity
	 *            the entity
	 * @param pageable
	 *            the pageable
	 * @return the page by example
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	Page<T> getPageByExample(final T entity, Pageable pageable)
			throws EspStoreException;

	/**
	 * Delete all by example.
	 *
	 * @param entity
	 *            the entity
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	void deleteAllByExample(final T entity) throws EspStoreException;

	/**
	 * Count by example.
	 *
	 * @param entity
	 *            the entity
	 * @return the int
	 * @throws EspStoreException
	 */
	long countByExample(T entity) throws EspStoreException;

	/**
	 * 调用存储过程
	 * 
	 * @param procedure
	 * @return
	 */
	Object callProcedure(String procedure);

	/**
	 * Gets the jdbc temple.
	 *
	 * @return the jdbc temple
	 */
	JdbcTemplate getJdbcTemple();

	TransactionTemplate getTransactionTemplate();

	EntityManager getEntityManager();
	
	/**
	 * Search by example.
	 * 
	 * Example：
	 * AdaptQueryRequest<Shop> queryRequest = new AdaptQueryRequest<Shop>(); 
	 * Shop param = new Shop();
	 * param.setTitle("123"); 表示查询标题中包含123的过滤
	 * queryRequest.setParam(param);
	 * System.out.println(shopRepository.searchByExample(queryRequest));
	 * 
	 * @param queryRequest
	 *            the query request
	 * @return the query response
	 * @throws EspStoreException
	 *             the esp store exception
	 */
	public  QueryResponse<T> searchByExample(AdaptQueryRequest<T> queryRequest) throws EspStoreException;
	
	/**
	 * searchByExample的扩展,支持单个field的模糊匹配
	 * @author xiezy
	 * @date 2016年8月16日
	 * @param queryRequest
	 * @return
	 * @throws EspStoreException
	 */
	public  QueryResponse<T> searchByExampleSupportLike(AdaptQueryRequest<T> queryRequest) throws EspStoreException;
	
	/**
	 * Search.
	 *
	 * @param queryRequest the query request
	 * @return the query response
	 * @throws EspStoreException the esp store exception
	 */
	public  QueryResponse<T> search(QueryRequest queryRequest) throws EspStoreException;
}
