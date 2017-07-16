package com.yzc.utils;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;

/**
 * @title 参数校验工具类
 * @author liuwx
 * @version 1.0
 * @create 2015年1月28日 下午8:36:12
 */
public class ParamCheckUtil {

	/**
	 * 校验limit的合法性 标准格式 (0,1)
	 * 
	 * */
	public static Integer[] checkLimit(String limit) {
		if (!StringUtils.hasText(limit)) {
			throw new MessageException(HttpStatus.BAD_REQUEST,
					ErrorMessageMapper.LimitParamMissing);
		}
		Integer queryFirst;
		Integer queryLast;
		try {
			String[] pageInfo = limit.replaceAll(Pattern.quote("("), "")
					.replaceAll(Pattern.quote(")"), "").split(",");
			queryFirst = Integer.parseInt(pageInfo[0]);
			queryLast = Integer.parseInt(pageInfo[1]);
			if(queryFirst < 0 || queryLast <= 0){
				throw new MessageException(HttpStatus.BAD_REQUEST,
						ErrorMessageMapper.LimitParamIllegal);
			}		

		} catch (Exception e) {
			throw new MessageException(HttpStatus.BAD_REQUEST,
					ErrorMessageMapper.LimitParamIllegal);
		}
		
		return new Integer[]{queryFirst,queryLast};
	}
}
