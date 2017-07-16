package com.yzc.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.yzc.config.WebApplicationInitializer;
import com.yzc.config.JpaConfig;
import com.yzc.config.WebConfig;
import com.yzc.models.StudentModel;
import com.yzc.service.StudentService;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.vos.student.StudentViewModel;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebConfig.class, WebApplicationInitializer.class,
		JpaConfig.class }, loader = AnnotationConfigWebContextLoader.class)
public class TestUserController {

	private MockMvc mockMvc;

	@Mock
	private StudentService studentService;

	@InjectMocks
	private StudentController studentController;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
	}

	@Test
    public void testCreateStudent() throws Exception {  
        
        StudentViewModel svm =getDefaultStudentViewModel();
        String uuid=UUID.randomUUID().toString();
        svm.setIdentifier(uuid);
        String param=ObjectUtils.toJson(svm);
        StudentModel sm = BeanMapperUtils.beanMapper(svm, StudentModel.class);
        StudentModel result=sm;
        when(studentService.createStudent(sm)).thenReturn(result);   
	
        mockMvc.perform(post("/student/"+uuid).contentType(MediaType.APPLICATION_JSON)
        		.content(param))  
        .andDo(print())  
        .andExpect(status().isOk());

        assertEquals("测试创建student不通过",sm,studentService.createStudent(sm));
    }
	
	@Test
    public void testGetDetailStudent() throws Exception {  
        
        StudentViewModel svm =getDefaultStudentViewModel();    
        String id="fcef3e4c-9da3-11e6-abbb-dc0ea1e2276c";
        StudentModel sm = BeanMapperUtils.beanMapper(svm, StudentModel.class);
        when(studentService.queryStudentById(id)).thenReturn(sm);     
        mockMvc.perform(get("/student/{content}",id)
        		.contentType(MediaType.APPLICATION_JSON))  
        .andDo(print())  
        .andExpect(status().isOk());  
        assertEquals("测试根据id获取学生详情不通过",sm,studentService.queryStudentById(id));
        
        String username="111111";
        when(studentService.queryStudentByUsername(username)).thenReturn(sm); 
        mockMvc.perform(get("/student/{content}",username)
        		.contentType(MediaType.APPLICATION_JSON))  
        .andDo(print())  
        .andExpect(status().isOk()); 
        assertEquals("测试根据username获取学生详情不通过",sm,studentService.queryStudentByUsername(username));
        
        when(studentService.queryStudentByUsername(username)).thenReturn(null); 
        mockMvc.perform(get("/student/{content}",username)
        		.contentType(MediaType.APPLICATION_JSON))  
        .andDo(print())  
        .andExpect(status().isOk()); 
        assertEquals("测试根据username获取学生详情不通过",null,studentService.queryStudentByUsername(username));
    }

	private StudentViewModel getDefaultStudentViewModel() {
		StudentViewModel svm = new StudentViewModel();
		svm.setClasses("一班");
		svm.setDescription("2000年9月1日入学");
		svm.setGrade("一年级");
		svm.setName("yzc");
		svm.setPassword("123456");
		svm.setSchool("万福小学");
		svm.setSex(0);
		svm.setTitle("yzc");
		svm.setUsername("13121155");
		return svm;
	}
}
