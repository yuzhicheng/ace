package com.yzc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.yzc.service.EmailService;
import com.yzc.utils.email.EmailUtils;

@Service("EmailService")
public class EmailServiceImpl implements EmailService{

	@Autowired
	JavaMailSender mailSender;
	
	@Override
	public void sendSimpleEmail(String to) {
		SimpleMailMessage message=new SimpleMailMessage();	
		message.setFrom("1799281706@qq.com");
		message.setTo("582771729@qq.com");
//		mailSender.send(message);
		EmailUtils.sendTextEmailByQQ("582771729@qq.com","https://esp-lifecycle.sdp.101.com//v0.6/assets/actions/query?words&limit=(0,20)&coverage=Org/nd/&include=TI,LC,CG,CR,EDU");
		
	}

}
