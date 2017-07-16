package com.yzc.config;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import com.yzc.core.fillter.LogMDCFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private final static Logger LOG = LoggerFactory.getLogger(WebApplicationInitializer.class);
	public static Properties redis_properties = null;
	public static Properties log4j_properties = null;
	public static Properties message_properties=null;
	public static Properties props_properties_db=null;

	static {
		try {
			redis_properties = PropertiesLoaderUtils.loadAllProperties("redis.properties");
			log4j_properties = PropertiesLoaderUtils.loadAllProperties("log4j.properties");
			message_properties=PropertiesLoaderUtils.loadAllProperties("exception_message.properties");
			props_properties_db = PropertiesLoaderUtils.loadAllProperties("props/resource_props_range_db.properties");
		} catch (IOException e) {
			LOG.warn("加载配置文件失败", e);
		}
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class,JpaConfig.class,RedisCacheConfig.class,WebSocketStompConfig.class,MailConfig.class,AspectJConfig.class,MongoConfig.class,RabbitmqConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
	    /**
	     * 設置当spring没有找到Handler的时候，抛出NoHandlerFoundException异常。并且被异常捕获到。统一进行处理
	     */
		registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
		
		registration.setAsyncSupported(true);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		this.initLogMDCFilter(servletContext);
		super.onStartup(servletContext);
	}

	protected void initLogMDCFilter(ServletContext servletContext) {
		LogMDCFilter logMDCFilter = new LogMDCFilter();
		javax.servlet.FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("logMDCFilter", logMDCFilter);
		filterRegistration.setAsyncSupported(this.isAsyncSupported());
	}

}