package com.yzc.mongo.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 *
 * Created by yzc on 2017/7/16.
 */
@Service
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        SpringContextHolder.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {

        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {

        return applicationContext.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz){

        return applicationContext.getBean(clazz);
    }

}
