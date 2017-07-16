package com.yzc.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.yzc.repository.Impl.BaseRepositoryFactoryBean;

/**
 * @author yzc
 */
@Configuration
@EnableJpaRepositories(basePackages = { "com.yzc" }, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@EnableTransactionManagement
@PropertySource(value = { "classpath:/jdbc.properties" })
public class JpaConfig {

	@Autowired
	private Environment environment;

	public final static String HIBERNATE_CONFIG = "/hibernate.properties";

	@Bean
	public DataSource dataSource() {
		//使用的Apache Commons DBCP连接池
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		dataSource.setInitialSize(5);
		return dataSource;
	}

	//配置实体管理器工厂，此处使用的是容器管理类型
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			JpaVendorAdapter jpaVendorAdapter, Properties hibProperties) {

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setDataSource(dataSource);
		factory.setPackagesToScan("com.yzc.entity");
		factory.setJpaProperties(hibProperties);
		factory.afterPropertiesSet();
		return factory;
	}

	//指明JPA厂商适配器
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return adapter;
	}

	@Bean
	public Properties hibProperties() throws IOException {
		Resource resource = new ClassPathResource(HIBERNATE_CONFIG);
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);
		return properties;
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}

	@Autowired
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		return jt;
	}

	@Autowired
	@Bean(name = "transactionTemplate")
	public TransactionTemplate getTransactionTemplate(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter,
			Properties hibProperties) {

		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = entityManagerFactory(dataSource,
				jpaVendorAdapter, hibProperties);

		PlatformTransactionManager manager = transactionManager(localContainerEntityManagerFactoryBean);
		TransactionTemplate transactionTemplate = new TransactionTemplate(manager);

		return transactionTemplate;
	}

}
