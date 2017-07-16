package com.yzc.utils;

import org.modelmapper.ModelMapper;

/**
 * @title 将Object进行mapper操作
 * @author yzc
 * @version 1.0
 * @create 2016年10月9日
 */
public class BeanMapperUtils {
	
	 private static ModelMapper modelMapper = new ModelMapper();
	
	public static <T> T beanMapper(Object source,Class<T> target){
		return modelMapper.map(source, target);
	}

}
