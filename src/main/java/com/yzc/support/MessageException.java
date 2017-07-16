package com.yzc.support;

import org.springframework.http.HttpStatus;

import com.yzc.exception.extendExceptions.WafSimpleException;

/**
 * @title 生命管理周期业务异常处理
 * @version 1.0
 * @create 2016年9月26日 上午11:33:38
 */
public class MessageException extends WafSimpleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4549214462211402970L;

	public MessageException(HttpStatus status, String code, String message) {
		super(status, code, message);

	}

	public MessageException(HttpStatus status, MessageMapper messageMapper) {
		super(status, messageMapper.getCode(), messageMapper.getMessage());

	}

	public MessageException(String code, String message) {
		this(HttpStatus.OK, code, message);

	}

	public MessageException(String message) {
		super(message);

	}

	public MessageException(MessageMapper messageMapper) {
		this(messageMapper.getCode(), messageMapper.getMessage());

	}

}
