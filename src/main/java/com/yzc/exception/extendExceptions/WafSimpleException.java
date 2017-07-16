package com.yzc.exception.extendExceptions;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.yzc.exception.messages.ErrorMessage;

/**
 * 定义简单的异常类对象。此对象实现了{@link WafExceptionSupport}接口<br>
 * 并且集成了RuntimeException。<br>
 * <p/>
 * 此异常类主要标注服务器内部的异常信息，所有的异常信息抛出后，其响应码统一为500<br>
 * 开发人员可以通过设置异常的编号和异常信息进行定义业务系统的异常信息。
 *
 * @author johnny
// * @deprecated 请使用 {@link com.nd.gaea.WafException}
 */
@SuppressWarnings("serial")
//@Deprecated
public class WafSimpleException extends WafException implements
		WafExceptionSupport {

	public WafSimpleException(String message) {
		super("WAF/INTERNAL_SERVER_ERROR", message);
	}

	public WafSimpleException(String code, String message) {
		super(code, message);
	}

	public WafSimpleException(HttpStatus status, String code, String message) {
		super(code, message, status);
	}

	@Override
	public ErrorMessage getErrorMessage() {
		ErrorMessage em = new ErrorMessage();
		em.setCode(super.getError().getCode());
		em.setMessage(super.getError().getMessage());
		em.setServerTime(new Date());
		em.setRequestId(UUID.randomUUID().toString());
		return em;
	}

	@Override
	public HttpStatus getStatus() {
		return super.getResponseEntity().getStatusCode();
	}

}
