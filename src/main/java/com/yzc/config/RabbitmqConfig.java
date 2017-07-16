package com.yzc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitmqConfig {
	
	private final static Logger LOG = LoggerFactory.getLogger(RabbitmqConfig.class);

	@Qualifier(value = "defaultQueueMessageListener")
	@Autowired
	private MessageListener messageListener;
	
	@Bean
	public ConnectionFactory connectionFactory() throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(new Integer(5672));
		connectionFactory.setVirtualHost("/");
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		initQueue(connectionFactory);
		return connectionFactory;
	}
	
	/**
	 * 声明队列
	 *
	 * @param connectionFactory
	 * @throws Exception
	 */
	private static void initQueue(ConnectionFactory connectionFactory){
		try{
			Connection connection = connectionFactory.newConnection();
			Channel channel = connection.createChannel();
			//声明队列
			channel.queueDeclare("ace_queue", false, false, false, null);
			//关闭频道和资源
			channel.close();
			connection.close();
		}catch(Exception exception){
			LOG.error("init MQ queue failed");
		}

	}
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() throws Exception {
		return new CachingConnectionFactory(connectionFactory());
	}

	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

		try {
			CachingConnectionFactory factory = cachingConnectionFactory();
			factory.setConnectionCacheSize(100);
			container.setConnectionFactory(cachingConnectionFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		container.setQueueNames("ace_queue");
		container.setConcurrentConsumers(50);
		container.setMaxConcurrentConsumers(200);
		container.setMessageListener(messageListener);
		return container;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
		rabbitTemplate.setChannelTransacted(true);
		return rabbitTemplate;
	}

}



//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.concurrent.TimeoutException;
//
//
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageListener;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//
//@Configuration
//public class RabbitmqConfig {
//
//	private static final String exchangeName = "exchange";
//	private static String queueName="queueNaem";
//
//	private ConnectionFactory getConnectionFactory() {
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setHost("localhost");
//		factory.setUsername("guest");
//		factory.setPassword("guest");
//		factory.setVirtualHost("/");
//		initQueue(factory);
//		return factory;
//	}
//
//	/**
//	 * @param fac
//	 * @description 初始化队列
//	 */
//	private void initQueue(ConnectionFactory fac) {
//		try {
//			Connection c = null;
//			Channel channel = null;
//			try {
//				c = fac.newConnection();
//				channel = c.createChannel();
//				// channel.queueDeclare("queue", false, false, false, null);
//				// 尝试binding exchange和queue
//				dealQueue(channel);
//			} finally {
//				channel.close();
//				c.close();
//			}
//		} catch (IOException | TimeoutException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 创建队列并进行binding
//	 * 
//	 * @param channel
//	 * @throws IOException
//	 */
//	private void dealQueue(Channel channel) throws IOException {
//		channel.exchangeDeclare(exchangeName, "topic");
////		queueName = channel.queueDeclare().getQueue();
//		channel.queueBind(queueName, exchangeName, "routekey");
//	}
//
//	/**
//	 * @return
//	 * @description spring唯一实现的工厂
//	 */
//	@Bean
//	public CachingConnectionFactory getCachingConnectionFactory() {
//		CachingConnectionFactory factory = new CachingConnectionFactory(getConnectionFactory());
//		return factory;
//	}
//
//	/**
//	 * @param listener
//	 * @return
//	 * @description listener-container,为了实现消息驱动的 amqp （异步)
//	 */
//	@Bean
//	@Autowired
//	public SimpleMessageListenerContainer container(CachingConnectionFactory factory,@Qualifier("anonymousListener") MessageListener AnonymousListener) {
//		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//		container.setConnectionFactory(factory);
//		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
//		container.setConcurrentConsumers(50);
//		container.setMaxConcurrentConsumers(200);
//		container.addQueueNames(queueName);
//		container.setMessageListener(AnonymousListener);
//		return container;
//	}
//
//	@Bean
//	@Autowired
//	public RabbitTemplate getRabbitTemplate(CachingConnectionFactory factory) {
//		RabbitTemplate template = new RabbitTemplate(factory);
//		return template;
//	}
//
//	@Bean(name="anonymousListener")
//	public MessageListener getMessageListener() {
//		return new MessageListener() {
//
//			@Override
//			public void onMessage(Message msg) {
//				String s = "";
//				try {
//					s = new String(msg.getBody(), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				System.out.println(s);
//			}
//		};
//	}
//}

