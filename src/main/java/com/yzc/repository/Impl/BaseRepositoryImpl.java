package com.yzc.repository.Impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yzc.entity.BaseEntity;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.repository.BaseRepository;
import com.yzc.repository.index.AdaptQueryRequest;
import com.yzc.repository.index.Hits;
import com.yzc.repository.index.OffsetPageRequest;
import com.yzc.repository.index.QueryRequest;
import com.yzc.repository.index.QueryResponse;

/**
 * @Description
 * @author yzc
 * @date 2015年5月27日 下午9:20:04
 * @version V1.0
 * @param <T>
 * @param <ID>
 */

@NoRepositoryBean
public class BaseRepositoryImpl<T extends BaseEntity, ID> extends
		SimpleJpaRepository<T, String> implements BaseRepository<T> {

	private static final Logger logger = LoggerFactory
			.getLogger(BaseRepositoryImpl.class);

	private final EntityManager entityManager;

	private final Class<T> domainClass;

	private JdbcTemplate jdbcTemplate;

	private TransactionTemplate transactionTemplate;

	/**
	 * 实例化一个新的代理仓库实现
	 * 
	 * @param domainClass
	 * @param em
	 * @param jdbcTemplate
	 * @param transactionTemplate
	 */
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em,
			JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
		super(domainClass, em);
		entityManager = em;
		this.domainClass = domainClass;
		this.jdbcTemplate = jdbcTemplate;
		this.transactionTemplate = transactionTemplate;
	}

	@Override
	public JdbcTemplate getJdbcTemple() {
		return jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * 自定义添加方法add
	 * 
	 * @param bean
	 * @return T
	 * @throws EspStoreException
	 */
	@Override
	public T add(T bean) throws EspStoreException {
		converterIn(bean);
		T result = save(bean);
		if (logger.isDebugEnabled()) {
			logger.debug(">>>doe add!@");
		}
		return converterOut(result);
	}

	/**
	 * 自定义删除方法delete
	 * 
	 * @param id
	 * @return void
	 */
	// 不鼓励程序员使用这样的元素，通常是因为它很危险或存在更好的选择。在使用不被赞成的程序元素
	// 或在不被赞成的代码中执行重写时，编译器会发出警告
	@Deprecated
	@Override
	public void delete(String id) {
		super.delete(id);
	}

	/**
	 * 自定义删除方法del
	 * 
	 * @param id
	 * @return void
	 */
	@Override
	public void del(String id) throws EspStoreException {
		try {
			if (logger.isInfoEnabled()) {
				logger.info("delete res {} : {}", domainClass.getName(), id);
			}
			super.delete(id);
			List<String> ids = new ArrayList<String>();
			ids.add(id);
		} catch (Exception e) {
			if (e instanceof EmptyResultDataAccessException) {
				if (logger.isErrorEnabled()) {
					logger.error("被删除的数据：{}不存在！{}", id, e);
				}
				throw new EspStoreException("被删除的数据：" + id + "不存在！");
			}
			if (logger.isErrorEnabled()) {
				logger.error("删除的数据异常{}", e);
			}
			throw new EspStoreException(e);
		}
	}

	/**
	 * 自定义更新方法update
	 * 
	 * @param bean
	 * @return T
	 * @throws EspStoreException
	 */
	@Override
	public T update(T bean) throws EspStoreException {
		converterIn(bean);
		T result = super.save(bean);
		if (logger.isDebugEnabled()) {
			logger.debug(">>>doe update!@");
		}
		return converterOut(result);
	}

	/**
	 * 自定义获取单个bean方法get
	 * 
	 * @param id
	 * @return T
	 * @throws EspStoreException
	 */
	@Override
	public T get(String id) throws EspStoreException {
		return converterOut(findOne(id));
	}

	/**
	 * 自定义批量获取方法getAll
	 * 
	 * @param ids
	 * @return List<T>
	 * @throws EspStoreException
	 */
	@Override
	public List<T> getAll(List<String> ids) throws EspStoreException {
		if (ids != null && ids.size() != 0) {
			return batchConverterOut(getListWhereInCondition("id", ids));
		} else {
			return Lists.newArrayList();
		}
	}

	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 自定义批量添加方法batchAdd
	 * 
	 * @param bean
	 * @return List<t>
	 * @throws EspStoreException
	 */
	@Override
	public List<T> batchAdd(List<T> bean) throws EspStoreException {
		batchConverterIn(bean);
		List<T> result = this.save(bean);
		return batchConverterOut(result);
	}

	/**
	 * 自定义批量删除方法batchDel
	 * 
	 * @param ids
	 * @throws EspStoreException
	 * @return void
	 */
	@Override
	public void batchDel(List<String> ids) throws EspStoreException {
		if (logger.isInfoEnabled()) {
			logger.info("batch delete res {} : {}", domainClass.getName(), ids);
		}
		List<T> beans = findAll(ids);
		try {
			for (T t : beans) {
				getEntityManager().remove(t);
			}
		} catch (Exception e) {
			if (e instanceof EmptyResultDataAccessException) {
				if (logger.isErrorEnabled()) {
					logger.error("被删除的数据：{}不存在！{}", ids, e);
				}
				throw new EspStoreException("被删除的数据：" + ids + "不存在！");
			}
			throw new EspStoreException(e);
		}
	}

	/**
	 * 自定义根据entity获取T方法getByExample
	 * 
	 * @param entity
	 * @return T
	 * @throws EspStoreException
	 */
	public T getByExample(T entity) throws EspStoreException {
		Pageable page = new PageRequest(0, 1);
		Page<T> list = findAll(exampleSpecification(entity), page);
		return list.getContent().size() > 0 ? converterOut(list.getContent()
				.get(0)) : null;
	}

	/**
	 * 自定义根据entity获取List<T>方法getAllByExample
	 * 
	 * @param entity
	 * @return List<T>
	 * @throws EspStoreException
	 */
	public List<T> getAllByExample(T entity) throws EspStoreException {
		return batchConverterOut(findAll(exampleSpecification(entity)));
	}

	/**
	 * 自定义分页查询
	 * 
	 * @param entity
	 * @param pageable
	 * @return Page<T>
	 * @throws EspStoreException
	 */
	public Page<T> getPageByExample(final T entity, Pageable pageable)
			throws EspStoreException {
		if (entity == null)
			return null;
		Page<T> page = findAll(exampleSpecification(entity), pageable);
		batchConverterOut(page.getContent());
		return page;
	}

	/**
	 * 根据条件查询获取List
	 * 
	 * @param propertyName
	 * @param propertyValues
	 * @return List
	 * @throws EspStoreException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListWhereInCondition(final String propertyName,
			List<?> propertyValues) throws EspStoreException {
		if (logger.isInfoEnabled()) {
			logger.info("DOSQL2:propertyName is {}  propertyValues {}",
					propertyName, propertyValues);
		}
		String dbName = HibernateParter
				.getColumnName(domainClass, propertyName);

		if (logger.isInfoEnabled()) {
			logger.info("where property name is {}", dbName);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder
				.createQuery(domainClass);
		Root<T> root = criteriaQuery.from(domainClass);
		root.get(propertyName);
		StringBuffer sb = new StringBuffer();
		sb.append("select a from ").append(domainClass.getName())
				.append(" as a").append(" where ").append(dbName)
				.append(" in (:values)").append(" order by ").append(" field(")
				.append(dbName).append(",").append(" :values").append(")");
		if (logger.isInfoEnabled()) {
			logger.info("DOSQL: {}", sb.toString());
		}
		Query query = entityManager.createQuery(sb.toString(), domainClass);
		query.setParameter("values", propertyValues);
		return batchConverterOut(query.getResultList());
	}

	/**
	 * Description .
	 * 
	 * @param entity
	 * @throws EspStoreException
	 * @see com.yzc.repository.BaseRepository
	 */
	public void converterIn(final T bean) throws EspStoreException {
		if (bean == null)
			return;
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void doWith(java.lang.reflect.Field field)
					throws IllegalArgumentException, IllegalAccessException {

				DataConverter dataConverter = field
						.getAnnotation(DataConverter.class);
				if (dataConverter != null) {
					String targetProp = dataConverter.target();
					try {
						Object targetPropValue = PropertyUtils.getProperty(
								bean, targetProp);
						if (targetPropValue != null) {
							if (targetPropValue instanceof List) {
								String v = JSON
										.toJSONString((List<String>) targetPropValue);
								PropertyUtils.setProperty(bean,
										field.getName(), v);
							} else if (targetPropValue instanceof Map) {

								String v = JSON
										.toJSONString((Map<String, String>) targetPropValue);
								PropertyUtils.setProperty(bean,
										field.getName(), v);
							}
						}
					} catch (InvocationTargetException | NoSuchMethodException e) {

						if (logger.isErrorEnabled()) {

							logger.error("添加实体:{}异常{}", bean, e);

						}

						throw new RuntimeException("添加实体" + bean + "异常", e);
					}
				}
			}
		});
	}

	/**
	 * Description .
	 * 
	 * @param entity
	 * @throws EspStoreException
	 * @see com.yzc.repository.BaseRepository
	 */
	public T converterOut(final T bean) throws EspStoreException {
		if (bean == null)
			return null;
		ReflectionUtils.doWithFields(bean.getClass(), new FieldCallback() {
			@SuppressWarnings("unchecked")
			@Override
			public void doWith(java.lang.reflect.Field field)
					throws IllegalArgumentException, IllegalAccessException {
				DataConverter dataConverter = field
						.getAnnotation(DataConverter.class);
				if (dataConverter != null) {
					String targetProp = dataConverter.target();
					Class<?> targetType = dataConverter.type();
					try {
						Object propValue = PropertyUtils.getProperty(bean,
								field.getName());
						if (propValue != null) {
							if (targetType == List.class) {
								List<String> v = JSON.parseArray(
										(String) propValue, String.class);
								PropertyUtils.setProperty(bean, targetProp, v);
							} else if (targetType == Map.class) {
								Map<String, String> v = (Map<String, String>) JSON
										.parse((String) propValue);
								PropertyUtils.setProperty(bean, targetProp, v);
							}
						}
					} catch (InvocationTargetException | NoSuchMethodException e) {
						if (logger.isErrorEnabled()) {
							logger.error("添加实体{}异常{}", bean, e);
						}
						throw new RuntimeException("添加实体" + bean + "异常", e);
					}
				}
			}
		});
		return bean;
	}

	public void batchConverterIn(List<T> beans) throws EspStoreException {
		if (beans != null) {
			for (T item : beans) {
				converterIn(item);
			}
		}
	}

	public List<T> batchConverterOut(List<T> beans) throws EspStoreException {
		if (beans != null) {
			for (T item : beans) {
				converterOut(item);
			}
		}
		return beans;
	}

	/**
	 * 自定义根据entity获取数量
	 * 
	 * @param entity
	 * @return long
	 * @throws EspStoreException
	 */
	@Override
	public long countByExample(T entity) throws EspStoreException {
		return super.count(exampleSpecification(entity));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Object callProcedure(String procedure) {
		Query query = entityManager.createNativeQuery("{call " + procedure
				+ " }");
		query.executeUpdate();
		return null;
	}

	@Override
	public void deleteAllByExample(T entity) throws EspStoreException {
		List<T> beans = findAll(exampleSpecification(entity));
		List<String> ids = Lists.newArrayList();
		for (T bean : beans) {
			ids.add(bean.getIdentifier());
		}
		super.delete(beans);
	}

	/**
	 * specification查询
	 * 
	 * @param entity
	 * @return the specification
	 */
	private Specification<T> exampleSpecification(final T entity)
			throws EspStoreException {
		// 判断组合条件是否有错误
		final Boolean[] isErr = { false };
		Specification<T> spec = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				try {
					Predicate p = cb.conjunction();
					Metamodel mm = entityManager.getMetamodel();

					EntityType<T> et = mm.entity(domainClass);
					Set<Attribute<? super T, ?>> attrs = et.getAttributes();
					for (Attribute<? super T, ?> a : attrs) {
						String name = a.getName();
						String javaName = a.getJavaMember().getName();
						Object value;
						try {
							value = PropertyUtils.getProperty(entity, javaName);
						} catch (IllegalAccessException
								| InvocationTargetException
								| NoSuchMethodException e) {

							if (logger.isErrorEnabled()) {

								logger.error(
										"获取读取exampleSpecification[{};id={}, {}] 值错误{}",
										entity.getClass(), entity.getIdentifier(),
										javaName, e);
							}
							throw e;
						}
						if (value == null)
							continue;
						if (value instanceof String) {
							if (StringUtils.isEmpty((String) value)) {
								continue;
							}
						}
						p = cb.and(p, cb.equal(root.get(name), value));
					}
					return p;
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error("exampleSpecification{}", e);
					}
					isErr[0] = true;
					return null;
				}
			}
		};
		if (isErr[0])
			throw new EspStoreException("exampleSpecification 构造条件错误");
		return spec;
	}

	/**
	 * Description.
	 *
	 * @param queryRequest
	 *            the query request
	 * @return the query response
	 * @throws EspStoreException
	 *             the esp store exception
	 * @see com.nd.esp.repository.IndexRepository#search(com.nd.esp.repository.index.QueryRequest)
	 */

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public QueryResponse<T> search(QueryRequest queryRequest)
			throws EspStoreException {

		AdaptQueryRequest<T> adaptQueryRequest = new AdaptQueryRequest<T>();
		ModelMapper modelMapper = new ModelMapper();
		adaptQueryRequest = modelMapper.map(queryRequest,
				AdaptQueryRequest.class);

		QueryResponse<T> response = searchByExample(adaptQueryRequest);

		return response;
	}

	/**
	 * Description.
	 *
	 * @param queryRequest
	 *            the query request
	 * @return the query response
	 * @throws EspStoreException
	 *             the esp store exception
	 * @see com.nd.esp.repository.IndexRepository#searchByExample(com.nd.esp.repository.index.AdaptQueryRequest)
	 */
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public QueryResponse<T> searchByExample(AdaptQueryRequest<T> queryRequest)
			throws EspStoreException {
		return searchByExample(queryRequest, false);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public QueryResponse<T> searchByExampleSupportLike(
			AdaptQueryRequest<T> queryRequest) throws EspStoreException {
		return searchByExample(queryRequest, true);
	}

	private QueryResponse<T> searchByExample(AdaptQueryRequest<T> queryRequest,
			boolean supportLike) throws EspStoreException {
		if (queryRequest.getOffset() < 0) {
			queryRequest.setOffset(0);
		}
		if (queryRequest.getLimit() <= 0) {
			queryRequest.setLimit(20);
		}

		QueryResponse<T> response = new QueryResponse<T>();
		Pageable pageable = null;
		T bean = queryRequest.getParam();
		converterIn(bean);

		if (bean != null) {
			pageable = new PageRequest(queryRequest.getOffset()
					/ queryRequest.getLimit(), queryRequest.getLimit(),
					Sort.Direction.ASC, "createTime");
		} else {
			String column = HibernateParter.getColumnName(domainClass,
					"createTime");
			if (null != column) {
				pageable = new OffsetPageRequest(queryRequest.getOffset(),
						queryRequest.getLimit(), Sort.Direction.DESC,
						"createTime");
			} else {
				pageable = new OffsetPageRequest(queryRequest.getOffset(),
						queryRequest.getLimit());
			}
		}
		Page<T> page = findAll(
				searchExampleSpecification(queryRequest, supportLike), pageable);
		batchConverterOut(page.getContent());
		Hits<T> hits = new Hits<T>();
		hits.setTotal(page.getTotalElements());
		hits.setDocs(page.getContent());
		response.setHits(hits);
		return response;
	}

	/**
	 * 改造后的searchExampleSpecification,可支持单个field的模糊匹配
	 * 
	 * @author xiezy
	 * @date 2016年8月16日
	 * @param queryRequest
	 * @param supportLike
	 * @return
	 * @throws EspStoreException
	 */
	private Specification<T> searchExampleSpecification(
			final AdaptQueryRequest<T> queryRequest, final boolean supportLike)
			throws EspStoreException {
		// 判断组合条件是否有错误
		final Boolean[] isErr = { false };
		Specification<T> spec = new Specification<T>() {
			@SuppressWarnings("rawtypes")
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				try {
					Predicate p;
					if (StringUtils.isNotEmpty(queryRequest.getKeyword())) {
						Predicate p1 = cb.like(
								root.get("title").as(String.class), "%"
										+ queryRequest.getKeyword() + "%");
						Predicate p2 = cb.like(
								root.get("description").as(String.class), "%"
										+ queryRequest.getKeyword() + "%");
						p = cb.or(p1, p2);
						String dbKey = HibernateParter.getColumnName(
								domainClass, "dbkeywords");
						if (null != dbKey) {
							Predicate p3 = cb.like(
									root.get("dbkeywords").as(String.class),
									"%" + queryRequest.getKeyword() + "%");
							p = cb.or(p, p3);
						}
						String crDes = HibernateParter.getColumnName(
								domainClass, "crDescription");
						if (null != crDes) {
							Predicate p4 = cb.like(root.get("crDescription")
									.as(String.class),
									"%" + queryRequest.getKeyword() + "%");
							p = cb.or(p, p4);
						}
						String dbEduDes = HibernateParter.getColumnName(
								domainClass, "dbEduDescription");
						if (null != dbEduDes) {
							Predicate p5 = cb.like(root.get("dbEduDescription")
									.as(String.class),
									"%" + queryRequest.getKeyword() + "%");
							p = cb.or(p, p5);
						}

					} else {
						p = cb.conjunction();
					}

					Map<String, Object> extraParam = queryRequest
							.getExtraParam();
					if (null != extraParam) {
						for (Entry<String, Object> entry : extraParam
								.entrySet()) {
							String field = entry.getKey();
							Object value = entry.getValue();

							if (value instanceof Collection) {
								if (((Collection) value).size() > 0) {
									p = cb.and(
											p,
											root.get(field).in(
													(Collection) value));
								}
							} else if (value instanceof String) {
								if (StringUtils.isNotEmpty((String) value)) {
									if (supportLike) {
										p = cb.and(
												p,
												cb.like(root.get(field).as(
														String.class), "%"
														+ value + "%"));
									} else {
										p = cb.and(p, cb.equal(root.get(field),
												value));
									}
								}
							} else {
								p = cb.and(p, cb.equal(root.get(field), value));
							}
						}
					}

					T entity = queryRequest.getParam();
					if (entity != null) {
						Metamodel mm = entityManager.getMetamodel();

						EntityType<T> et = mm.entity(domainClass);
						Set<Attribute<? super T, ?>> attrs = et.getAttributes();
						for (Attribute<? super T, ?> a : attrs) {
							String name = a.getName();
							String javaName = a.getJavaMember().getName();
							Object value;
							try {
								value = PropertyUtils.getProperty(entity,
										javaName);
							} catch (IllegalAccessException
									| InvocationTargetException
									| NoSuchMethodException e) {

								if (logger.isErrorEnabled()) {

									logger.error(
											"获取读取exampleSpecification[{};id={}, {}] 值错误{}",
											entity.getClass(), entity.getIdentifier(),
											javaName, e);

								}

								throw e;
							}
							if (value == null)
								continue;
							if (value instanceof String) {
								if (StringUtils.isEmpty((String) value)
										|| ((String) value).equals("{}")
										|| ((String) value).equals("[]")) {
									continue;
								}
							}
							if (value instanceof Collection) {
								if (((Collection) value).isEmpty()) {
									continue;
								}
							}
							if (javaName.equals("dbcategories")
									&& value instanceof String) {
								List<String> categories = JSON.parseArray(
										(String) value, String.class);
								if (null != categories) {
									Predicate pPath = cb.disjunction();
									Predicate pCode = cb.disjunction();
									boolean bHasPath = false;
									boolean bHasCode = false;
									for (String category : categories) {
										if (category.contains("/")) {
											category = category.replace('*',
													'%');
											pPath = cb.or(
													pPath,
													cb.like(root.get(name).as(
															String.class), "%"
															+ category + "%"));
											bHasPath = true;
										} else {
											pCode = cb.or(
													pCode,
													cb.like(root.get(name).as(
															String.class), "%"
															+ category + "%"));
											bHasCode = true;
										}
									}
									if (bHasPath) {
										p = cb.and(p, pPath);
									}
									if (bHasCode) {
										p = cb.and(p, pCode);
									}
								}
							} else {
								p = cb.and(p, cb.equal(root.get(name), value));
							}
						}
					}
					return p;
				} catch (Exception e) {

					if (logger.isErrorEnabled()) {

						logger.error("exampleSpecification{}", e);

					}

					isErr[0] = true;
					return null;
				}
			}
		};
		if (isErr[0])
			throw new EspStoreException("exampleSpecification 构造条件错误");
		return spec;
	}

}
