package com.yzc.message.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yzc.message.MessageService;
import com.yzc.utils.ObjectUtils;


@Component
abstract class AbstractMessageService<T> implements MessageService<T>{
    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Override
    public void send(Object message) {
    	
        this.rabbitTemplate.convertAndSend("", queueName(),ObjectUtils.toJson(message));
    }
    
    
    String queueName() {
		return "ace_queue";
	}
    
	@Override
	public T receive() {
		this.rabbitTemplate.receive();
		return null;
	}
}
