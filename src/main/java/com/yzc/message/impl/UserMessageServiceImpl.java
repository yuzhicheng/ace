package com.yzc.message.impl;

import org.springframework.stereotype.Service;

import com.yzc.models.user.UserModel;
import com.yzc.utils.ObjectUtils;

@Service(value ="userMessageServiceImpl" )
public class UserMessageServiceImpl extends AbstractMessageService<UserModel>{
	
	@Override
	public UserModel receive() {
		String message = (String) this.rabbitTemplate
				.receiveAndConvert(queueName());
		return ObjectUtils.fromJson(message, UserModel.class);
	}

	@Override
	String queueName() {
		
		return "ace_queue";
	}

}
