package com.yzc.repository.Impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import com.google.common.collect.Maps;
import com.yzc.entity.BaseEntity;

/**
 * @Description
 * @author Rainy(yang.lin)
 * @date 2015年6月3日 上午10:16:34
 * @version V1.0
 */

public class HibernateParter {

	@SuppressWarnings("rawtypes")
	public static final Map<Class, Map<String, String>> cached = Maps
			.newConcurrentMap();

	/**
	 * Logging
	 */
	private static Logger logger = LoggerFactory
			.getLogger(HibernateParter.class);

	/**
	 * 获得表名
	 * 
	 * @param clazz
	 *            映射到数据库的po类
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getTableName(Class clazz) {
		Table annotation = (Table) clazz.getAnnotation(Table.class);
		if (annotation != null) {
			return annotation.name();
		}

		return null;
	}

	/**
	 * 获得列名
	 * 
	 * @param clazz
	 *            映射到数据库的po类(注意：po类对应的属性建表的注解必须为@column(name="名字")，如果使用@column，该方法会找不到)
	 * @param icol
	 *            第几列
	 * @return String
	 */
	public static String getColumnName(final Class<?> clazz, final String name) {
		try {
			if (cached.get(clazz) == null) {
				synchronized (HibernateParter.class) {
					if (cached.get(clazz) != null) {
						return cached.get(clazz).get(name);
					} else {
						final Map<String, String> map = new HashMap<String, String>();
						ReflectionUtils.doWithFields(clazz,
								new FieldCallback() {

									@Override
									public void doWith(Field field)
											throws IllegalArgumentException,
											IllegalAccessException {
										Column column = field
												.getAnnotation(Column.class);
										Transient transient1 = field
												.getAnnotation(Transient.class);
										if (transient1 == null) {
											map.put(field.getName(),
													column != null ? column
															.name() : field
															.getName());
										}
									}

								});
						cached.put(clazz, map);
					}
				}
			} else {
				return cached.get(clazz).get(name);
			}
		} catch (Exception e) {

			if (logger.isErrorEnabled()) {

				logger.error("获取列错误:{}", e);

			}

			throw e;
		}

		return cached.get(clazz).get(name);
	}

	public static Map<String, Object> getParam(final BaseEntity entity) {
		final Map<String, Object> param = Maps.newLinkedHashMap();
		ReflectionUtils.doWithFields(entity.getClass(), new FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				if (Modifier.isStatic(field.getModifiers()))
					return;
				Column column = field.getAnnotation(Column.class);
				Transient transient1 = field.getAnnotation(Transient.class);
				if (transient1 == null) {
					field.setAccessible(true);
					Object value = null;
					try {
						value = field.get(entity);
						field.setAccessible(false);
						if (value instanceof String) {
							if (StringUtils.isEmpty((String) value)) {
								return;
							}
						}
						if (value == null)
							return;
						param.put(
								column != null ? column.name() : field
										.getName(), value);
					} catch (IllegalArgumentException | IllegalAccessException e) {

						if (logger.isWarnEnabled()) {

							logger.warn("获取属性值异常:{}", e);

						}

					}
				}
			}
		});
		return param;
	}
}
