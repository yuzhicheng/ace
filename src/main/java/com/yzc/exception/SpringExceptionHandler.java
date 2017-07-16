package com.yzc.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.yzc.exception.extendExceptions.WafSimpleException;

@RestController
//控制器通知注解
@ControllerAdvice
public class SpringExceptionHandler {
	/**
	 * 全局处理WafSimpleException 错误的情况下返回500
	 * 
	 * @param ex
	 * @param req
	 * @return
	 */
	@ExceptionHandler(value = { WafSimpleException.class })
	public ResponseEntity<Object> handleOtherExceptions(final WafSimpleException ex, final HttpServletRequest req,
			Throwable throwable) {

		Map<String, Object> m = new HashMap<String, Object>();
		String code = ex.getErrorMessage().getCode();
		String message = ex.getErrorMessage().getMessage();
		String host_id = req.getServerName();
		String request_id = ex.getErrorMessage().getRequestId();
		String stack_track = getStackTrace(throwable);
		String service_time = ex.getErrorMessage().getServerTime().toString();
		m.put("message", message);
		m.put("code", code);
		m.put("host_id", host_id);
		m.put("request_id", request_id);
		m.put("stack_track", stack_track);
		m.put("service_time", service_time);
		return new ResponseEntity<Object>(m, ex.getStatus());
	}

	protected String getStackTrace(Throwable throwable) {
		StringWriter errors = new StringWriter();
		throwable.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST)//将异常映射为HTTP状态码注解
	@ExceptionHandler(value = { BindException.class, HttpMessageNotReadableException.class,
			MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,TypeMismatchException.class})//处理异常的注解
	public ResponseEntity<Object> handle400Exceptions(final Exception ex, final HttpServletRequest req,
			Throwable throwable) {

		Map<String, Object> m=getReturnMap(ex, req, throwable);
		return new ResponseEntity<Object>(m, HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(value=HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
	public ResponseEntity<Object> handle415Exception(final Exception ex, final HttpServletRequest req,
			Throwable throwable) {

		Map<String, Object> m=getReturnMap(ex, req, throwable);
		return new ResponseEntity<Object>(m, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
	
	//使用ResponseEntity的唯一原因是因为能够设置状态码，通过@ResponseStatus注解可以达到相同的效果
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
	public Map<String, Object> handle405Exception(final Exception ex, final HttpServletRequest req,
			Throwable throwable) {

		Map<String, Object> m=getReturnMap(ex, req, throwable);
		return m;
	}
	
	@ResponseStatus(value=HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(value = {NoSuchRequestHandlingMethodException.class})
	public ResponseEntity<Object> handle404Exception(final Exception ex, final HttpServletRequest req,
			Throwable throwable) {

		Map<String, Object> m=getReturnMap(ex, req, throwable);
		return new ResponseEntity<Object>(m, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleExceptions(final Exception ex, final HttpServletRequest req,
			Throwable throwable) {
		
		Map<String, Object> m=getReturnMap(ex, req, throwable);
		return new ResponseEntity<Object>(m, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private Map<String, Object> getReturnMap(final Exception ex, final HttpServletRequest req,
			Throwable throwable){
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("message", ex.getMessage());
		m.put("host_id", req.getServerName());
		m.put("request_id", UUID.randomUUID().toString());
		m.put("stack_track", getStackTrace(throwable));
		m.put("service_time", new Date().toString());	
		return m;
		
	}
	
	//全局的@RequestMapping都能获得在此处设置的键值对
	@ModelAttribute
	public void addAttributes(Model model) {
		
		model.addAttribute("msg","额外信息");
	}

}