package com.yzc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.yzc.config.BaseControllerConfig;

public class TestHomeController extends BaseControllerConfig{
	
	@Test
	public void testHomePage() throws Exception {
	
		HomeController controller = new HomeController();
//		assertEquals("home", controller.home());
		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		mockMvc.perform(get("/home/homepage"))
		                .andExpect(view().name("home"))
		                .andExpect(status().isOk());
	}
	
	@Test
	public void testgetMessagePage() throws Exception {
	
		HomeController controller = new HomeController();

		MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

		mockMvc.perform(get("/home/msg"))
		                .andExpect(view().name("home"))
		                .andExpect(status().isOk())
		                .andExpect(model().attribute("msg","额外信息"));
	}

}
