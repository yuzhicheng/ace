package com.yzc.message;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component(value = "defaultQueueMessageListener")
public class DefaultQueueMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		try {
			
			String messageBody = new String(message.getBody(),"UTF-8");
			String queueName = message.getMessageProperties().getConsumerQueue();
			System.out.println("queue name:"+queueName);
			System.out.println(" [x] Received '" + messageBody + "'");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
