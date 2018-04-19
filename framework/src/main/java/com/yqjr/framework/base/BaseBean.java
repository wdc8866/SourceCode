/**
 * 
 */
package com.yqjr.framework.base;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yqjr.framework.component.mapper.BeanMapper;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.Reflections;

/**
 * ClassName: BaseBean <br>
 * Description: Bean基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月26日 下午8:34:27 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class BaseBean<T> implements BaseCloneable<T>, Serializable {

	private static final long serialVersionUID = -6670241604310538606L;

	@JsonIgnore
	protected Class<T> clazz;

	@SuppressWarnings("unchecked")
	public BaseBean() {
		this.clazz = Reflections.getClassGenricType(this.getClass(), 1);
	}

	/**
	 * @return the clazz
	 */
	public Class<T> getClazz() {
		return clazz;
	}

	public T clone() {
		try {
			T instance = clazz.newInstance();
			BeanMapper.beanToBean(this, instance, true, false);
			return instance;
		} catch (Exception e) {
			throw new BizzException(e);
		}
	}

	public T newInstance() {
		try {
			T instance = clazz.newInstance();
			return instance;
		} catch (Exception e) {
			throw new BizzException(e);
		}
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
