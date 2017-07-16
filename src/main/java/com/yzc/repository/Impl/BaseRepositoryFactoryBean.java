package com.yzc.repository.Impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import com.yzc.entity.BaseEntity;

/**
 * @Description
 * @author
 * @date 2016年10月13日 下午6:10:25
 * @version V1.0
 * @param <R>
 * @param <T>
 * @param <I>
 */
public class BaseRepositoryFactoryBean<R extends JpaRepository<T, I>, T extends BaseEntity, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {

	@Autowired
	@Qualifier(value = "jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier(value = "transactionTemplate")
	TransactionTemplate transactionTemplate;

	@PersistenceContext(unitName = "entityManagerFactory")
	EntityManager em;

	@Override
	@PersistenceContext()
	public void setEntityManager(EntityManager entityManager) {
		super.setEntityManager(entityManager);
	}

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
		return new MyRepositoryFactory<T, I>(em, jdbcTemplate,transactionTemplate
				);
	}

	private static class MyRepositoryFactory<T extends BaseEntity, I extends Serializable>
			extends JpaRepositoryFactory {

		private final EntityManager em;

		private JdbcTemplate jdbcTemplate;

		private TransactionTemplate transactionTemplate;

		public MyRepositoryFactory(EntityManager em, JdbcTemplate jdbcTemplate,TransactionTemplate transactionTemplate
				) {
			super(em);
			this.em = em;
			this.jdbcTemplate = jdbcTemplate;
			this.transactionTemplate = transactionTemplate;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new BaseRepositoryImpl<T, I>(
					(Class<T>) metadata.getDomainType(), em, jdbcTemplate,
					transactionTemplate);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return BaseRepositoryImpl.class;
		}

		@SuppressWarnings("unused")
		public EntityManager getEm() {
			return em;
		}
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

}
