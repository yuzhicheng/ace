package com.yzc.config;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.yzc.config.WebConfig;
import com.yzc.config.WebApplicationInitializer;
import com.yzc.config.JpaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfig.class, WebApplicationInitializer.class,
		JpaConfig.class }, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration//用来声明加载的ApplicationContex是一个WebApplicationContext。属性值指定的是web资源的位置，默认为src/main/webapp
public class BaseControllerConfig extends BaseSpringJunit4Config{
	
	protected final Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Before
	public void before() {
		logger.info("BaseControllerConfig init data..");
	
		logger.info("BaseControllerConfig init data finish");
	}

	@Test
	public void doTest() {
		logger.error("nothing test to do");
	}

	@After
	public void after() {
	}

}
