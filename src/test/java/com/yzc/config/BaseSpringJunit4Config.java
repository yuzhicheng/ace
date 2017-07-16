package com.yzc.config;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @Title: BaseSpringJunit4Config
 * @Description: BaseSpringJunit4Config
 * @author yzc
 * @version 0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class BaseSpringJunit4Config {
	
	protected MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	public BaseSpringJunit4Config() {
	}

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

	}
}
