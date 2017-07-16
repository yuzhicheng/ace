package com.yzc.support;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.yzc.exception.extendExceptions.WafSimpleException;

/**
 * @title 业务逻辑校验
 * @Desc 
 * @author liuwx
 * @version 1.0
 * @create 2015年4月14日 下午3:45:26
 */
public class ValidResultHelper {

	private final static Logger LOG = LoggerFactory
			.getLogger(ValidResultHelper.class);

	/**
	 * 获取validResult中的error信息,如果不为空,返回错误信息,并抛出异常
	 * @author yzc
	 * @date 2017年3月24日 
	 * @param validResult
	 */
	public static void valid(BindingResult validResult) {
		Assert.assertNotNull("BindingResult对象不能为空", validResult);
		if (validResult.hasErrors()) {
			StringBuilder errors = new StringBuilder();
			for (ObjectError error : validResult.getAllErrors()) {
				errors.append(error.getDefaultMessage() + ";");
			}
			LOG.warn("检验API参数异常:{}", errors.toString());

			throw new WafSimpleException(errors.toString());
		}

	}

	/**
	 * 获取validResult中的error信息,如果不为空,返回错误信息,并抛出异常
	 * @author yzc
	 * @date 2017年3月24日 
	 * @param validResult
	 * @param code
	 */
	public static void valid(BindingResult validResult, String code) {
		Assert.assertNotNull("BindingResult对象不能为空", validResult);
		if (validResult.hasErrors()) {
			StringBuilder errors = new StringBuilder();
			for (ObjectError error : validResult.getAllErrors()) {
				errors.append(error.getDefaultMessage() + ";");
			}

			LOG.warn("检验API参数异常:{}", errors.toString());

			throw new WafSimpleException(code, errors.toString());
		}

	}

	/**
	 * 获取validResult中的error信息,如果不为空,返回错误信息,并抛出异常
	 * @author yzc
	 * @date 2017年3月24日 
	 * @param validResult
	 * @param code
	 * @param controller
	 */
	public static void valid(BindingResult validResult, String code,
			String controller) {
		Assert.assertNotNull("BindingResult对象不能为空", validResult);
		if (validResult.hasErrors()) {
			StringBuilder errors = new StringBuilder();
			for (ObjectError error : validResult.getAllErrors()) {
				errors.append(error.getDefaultMessage() + ";");
			}

			LOG.warn("检验API参数异常:{}", errors.toString());

			throw new WafSimpleException(code, errors.toString());
		}

	}

	/**
	 * 获取validResult中的error信息,如果不为空,返回错误信息,并抛出异常
	 * @author yzc
	 * @date 2017年3月24日 
	 * @param validResult
	 * @param code
	 * @param controller
	 * @param method
	 */
	public static void valid(BindingResult validResult, String code,
			String controller, String method) {
		Assert.assertNotNull("BindingResult对象不能为空", validResult);
		if (validResult.hasErrors()) {
			List<String> el = new ArrayList<String>();
			StringBuilder errors = new StringBuilder();
			for (ObjectError error : validResult.getAllErrors()) {
				String em = error.getDefaultMessage();
				// 不存在才添加 modify by xuzy 2015-07-20
				if (!el.contains(em)) {
					errors.append(em + ";");
					el.add(em);
				}
			}
			LOG.warn("检验API参数异常:{}", errors.toString());

			throw new WafSimpleException(code, errors.toString());
		}

	}

}
