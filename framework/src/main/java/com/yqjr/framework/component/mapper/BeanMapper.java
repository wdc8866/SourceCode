/**
 * Copyright (c) 2005-2012 springside.org.cn
 */
package com.yqjr.framework.component.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.util.Assert;

import com.yqjr.framework.annotation.Dict;
import com.yqjr.framework.base.BaseCloneable;
import com.yqjr.framework.component.bean.NoSetterBeanCopier;
import com.yqjr.framework.component.cache.AbstractCache;
import com.yqjr.framework.component.cache.ICache;
import com.yqjr.framework.component.dict.DictLoader;
import com.yqjr.framework.component.log.Logger;
import com.yqjr.framework.datatype.Amount;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Date;
import com.yqjr.framework.utils.Reflections;
import com.yqjr.framework.utils.StringUtils;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

/**
 * ClassName: BeanMapper <br>
 * Description: 基于cglib实现 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月2日 下午5:21:49 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class BeanMapper {

	private static final String CACHE_NAME = "framework.beanmapper.cache";

	private static ICache beanCopierCache = AbstractCache.getCache();

	private static Map<Class<?>, Class<?>> primitiveMap = new HashMap<Class<?>, Class<?>>();

	private static Logger logger = Logger.getLogger();

	static {
		primitiveMap.put(byte.class, Byte.class);
		primitiveMap.put(boolean.class, Boolean.class);
		primitiveMap.put(short.class, Short.class);
		primitiveMap.put(char.class, Character.class);
		primitiveMap.put(int.class, Integer.class);
		primitiveMap.put(long.class, Long.class);
		primitiveMap.put(float.class, Float.class);
		primitiveMap.put(double.class, Double.class);
	}

	private static Object lock = new Object();

	/**
	 * Description: bean复制,基于cglib beanCopier实现<br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月26日 上午11:44:43
	 *
	 * @param source
	 *            源bean实例
	 * @param target
	 *            目标bean实例
	 * @param isDeepCopy
	 *            是否为深拷贝
	 * @param isDict
	 *            是否包含数据字典转换
	 * @return
	 */
	public static void beanToBean(Object source, final Object target, final boolean isDeepCopy, final boolean isDict) {
		Assert.notNull(source);
		Assert.notNull(target);
		// 获取bean copier
		String cacheKey = source.getClass().getName() + target.getClass().getName();
		BeanCopier beanCopier = beanCopierCache.get(CACHE_NAME, cacheKey);
		if (beanCopier == null) {
			synchronized (lock) {
				if (beanCopierCache.get(CACHE_NAME, cacheKey) == null) {
					beanCopier = NoSetterBeanCopier.create(source.getClass(), target.getClass(), true);
					beanCopierCache.put(CACHE_NAME, cacheKey, beanCopier);
				}
			}
		}
		// bean复制
		beanCopier.copy(source, target, new Converter() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public Object convert(Object value, Class targetClass, Object context) {
				if (value == null)
					return value;
				if (targetClass.isPrimitive() && primitiveMap.get(targetClass).isInstance(value)) {
					return value;
				}
				if (targetClass.isInstance(value)) {
					// 深拷贝
					if (isDeepCopy && value instanceof BaseCloneable) {
						return ((BaseCloneable) value).clone();
					}
					// 浅拷贝则直接返回值
					else
						return value;
				}
				if (value instanceof Date)
					return ((Date) value).toString();
				if (value instanceof Amount)
					return ((Amount) value);
				if (targetClass == Date.class && value instanceof String ){
					if(StringUtils.isBlank((String)value)){
						return null;
					}else{
						return new Date((String) value);
					}
				}
				if (targetClass == Amount.class && value instanceof Double)
					return new Double((Double) value);
				// 判断是否包含数据字典转换,目标必须为string类型
				if (isDict && targetClass == String.class) {
					DictLoader dictLoader = DictLoader.getInstance();
					String fieldName = Reflections.getFieldName(context.toString());
					try {
						Dict dict = target.getClass().getDeclaredField(fieldName).getAnnotation(Dict.class);
						// 如果存在注解
						if (dict != null) {
							Assert.hasText(dict.name());
							// 通过数据字典组件获取标签值
							String label = dictLoader.getLabel(dict.name(), String.valueOf(value));
							if (label == null)
								throw new BizzException("数据字典转换异常");
							return label;
						}
					} catch (Exception e) {
						logger.warn("获取属性信息异常,将返回原值,属性名称:" + fieldName + "属性值:" + value);
					}
					return String.valueOf(value);
				}
				return ConvertUtils.convert(value, targetClass);
			}
		});
	}
	
	/**
	 * Description: bean复制,基于cglib beanCopier实现<br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月26日 上午11:44:43
	 *
	 * @param source
	 *            源bean实例
	 * @param target
	 *            目标bean实例
	 * @return
	 */
	public static void beanToBean(Object source, final Object target) {
		beanToBean(source,target,false,false);
	}
	
}