package com.yzc.config;

import com.yzc.support.CommonServiceHelper;
import com.yzc.support.interceptors.AllInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.util.Properties;
import java.util.concurrent.Executors;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.yzc")
public class WebConfig extends WebMvcConfigurerAdapter implements SchedulingConfigurer{

	@Autowired
	private AllInterceptor allInterceptor;
	
	// 配置jsp视图解析器
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);// 将试图解析为JSTL视图
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setExposeContextBeansAsAttributes(true);
		return viewResolver;
	}

	/**
	 * springMVC对静态资源的处理方法：
	 * 方法1: 使用DefaultServletHandlerConfigurer,一般Web应用服务器默认的Servlet名称是"default"，所以这里我们激活Tomcat的defaultServlet来处理静态文件
	 *    具体操作：static资源包放在webapp下，配置见web.xml
	 * 方法2: 使用  ResourceHandlerRegistry 为静态资源添加映射路径，当映射资源在WEB-INF下时，在web.xml中对应的资源类型的
	 *    默认映射需要注释，否则这里的映射会失效
	 *    具体操作：static资源包放在WEB-INF下，jsp、html等页面对资源的引用路径为：
	 *   <script src="./static/javascripts/jquery-2.1.4.min.js"></script>
	 *   <script src="static/javascripts/jquery.cookie.js"></script>
	 *   <script src="/ace/static/javascripts/bootstrap.min.js"></script>
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
	}

	// 配置静态资源的处理(对静态资源的请求转发到Servlet容器中默认的Servlet上，而不是使用@DispatcherServlet)
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

		configurer.enable();

	}

	@Bean
	public static ResourceBundleMessageSource getResourceBundleMessageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setUseCodeAsDefaultMessage(true);// 如果找不到属性值,则使用key作为值返回
		source.setBasenames(new String[] { "valid/messages" });
		source.setDefaultEncoding("utf-8");
		source.setFallbackToSystemLocale(true);
		return source;
	}

	@Bean
	public LocalValidatorFactoryBean getLocalValidatorFactoryBean() {
		LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
		factoryBean.setValidationMessageSource(getResourceBundleMessageSource());
		return factoryBean;
	}

	@Override
	public Validator getValidator() {

		return getLocalValidatorFactoryBean();
	}

	/**
	 * 根据环境变理加载 properties
	 *
	 * @return PropertySourcesPlaceholderConfigurer
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
    /**
     * 加载CommonServiceHelper
     * 
     * @return CommonServiceHelper
     * @since
     */
    @Bean
    public CommonServiceHelper getCommonServiceHelper() {

        return new CommonServiceHelper();
    }
     
    //拦截器配置
     @Override
    	public void addInterceptors(InterceptorRegistry registry) {
    	 registry.addInterceptor(allInterceptor);
    		super.addInterceptors(registry);
    	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("UTF-8");
		resolver.setMaxUploadSize(50 * 1024 * 1024);
		resolver.setMaxInMemorySize(512 * 1024);
		resolver.setResolveLazily(true);
		return resolver;
	}
	
	/**
	 * 开启异步任务
	 */
    @Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
    	
		configurer.setDefaultTimeout(30*1000L);
		configurer.setTaskExecutor(getAsyncExecutor());
	}
	
	@Bean
    public ThreadPoolTaskExecutor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ronnie-task-");
        executor.initialize();
        return executor;
    }

	/**
	 * 配置定时任务线程池（spring 默认单线程，容易受其它任务影响）
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
	}
	
	@Bean(name="simpleMappingExceptionResolver")
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
		
		SimpleMappingExceptionResolver smer=new SimpleMappingExceptionResolver();
		Properties mappings=new Properties();
		//定义需要特殊处理的异常，用类名或完全路径名作为key，异常页页名作为值
		mappings.setProperty("MessageException", "home");
		mappings.setProperty("EspStoreException", "test");
		//springmvc 要处理的异常类型和对应的错误页面
		smer.setExceptionMappings(mappings);  // 默认为空
		//定义默认错误显示页面，表示处理不了的异常都显示该页面
		smer.setDefaultErrorView("home");    // 默认没有
		//定义异常处理页面用来获取异常信息的变量名，默认名为exception
		smer.setExceptionAttribute("ex"); 
		smer.setDefaultStatusCode(500);
		smer.setWarnLogCategory("example.MvcLogger"); 
	    return smer;
	}
	
//	@Bean(name="ajaxSimpleMappingExceptionResolver")
//	public AjaxSimpleMappingExceptionResolver ajaxSimpleMappingExceptionResolver(){
//		
//		AjaxSimpleMappingExceptionResolver smer=new AjaxSimpleMappingExceptionResolver();
//		Properties mappings=new Properties();
//		//定义需要特殊处理的异常，用类名或完全路径名作为key，异常页页名作为值
//		mappings.setProperty("MessageException", "home");
//		mappings.setProperty("EspStoreException", "test");
//		//springmvc 要处理的异常类型和对应的错误页面
//		smer.setExceptionMappings(mappings);  // 默认为空
//		//定义默认错误显示页面，表示处理不了的异常都显示该页面
////		smer.setDefaultErrorView("error");    // 默认没有
////		//定义异常处理页面用来获取异常信息的变量名，默认名为exception
////		smer.setExceptionAttribute("ex"); 
//		smer.setDefaultStatusCode(500);
//		smer.setWarnLogCategory("example.MvcLogger"); 
//	    return smer;
//	}
}
