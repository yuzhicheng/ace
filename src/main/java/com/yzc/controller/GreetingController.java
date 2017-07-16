package com.yzc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yzc.models.Greeting;
import com.yzc.models.HelloMessage;
import com.yzc.service.EmailService;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.utils.MessageConvertUtil;

@Controller
public class GreetingController {
	
	@Autowired
	private EmailService emailService;

	// @MessageMapping defines the sending addr for client.
	// 消息发送地址: /server/app/hello
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		System.out.println("receiving " + message.getName());
		System.out.println("connecting successfully.");
		return new Greeting("Hello, " + message.getName() + "!");
	}

	@SubscribeMapping("/macro")
	public Greeting handleSubscription() {
		System.out.println("this is the @SubscribeMapping('/marco')");
		Greeting greeting = new Greeting("i am a msg from SubscribeMapping('/macro').");
		return greeting;
	}
	
//	@MessageMapping("/feed")
//	@SendTo("/topic/feed")
//	public Greeting greetingForFeed(HelloMessage message) throws Exception {
//		System.out.println("receiving " + message.getName());
//		System.out.println("connecting successfully.");
//		return new Greeting("i am /topic/feed, hello " + message.getName() + "!");
//	}

	private SimpMessageSendingOperations template;

	@Autowired
	public GreetingController(SimpMessageSendingOperations template) {
		this.template = template;
	}

	@RequestMapping(value = "/feed", method = RequestMethod.GET)
	public @ResponseBody void greet(@RequestParam String greeting) {
		String text = "you said just now "+greeting;
		System.out.println(text);
		this.template.convertAndSend("/topic/feed", text);
	}
	
	@RequestMapping(value = "/mail", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> send(@RequestParam String greeting) {
		String to="582771729@qq.com";
		emailService.sendSimpleEmail(to);
		return MessageConvertUtil.getMessageString(ErrorMessageMapper.DeleteStudentSuccess);
	}
}
