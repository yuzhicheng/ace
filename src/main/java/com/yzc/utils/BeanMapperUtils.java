package com.yzc.utils;

import org.modelmapper.ModelMapper;

/**
 * 将Object进行mapper操作
 *
 * @author yzc
 * @version 1.0
 */
public class BeanMapperUtils {

    private static ModelMapper modelMapper = new ModelMapper();

    public static <T> T beanMapper(Object source, Class<T> target) {
        return modelMapper.map(source, target);
    }

}
