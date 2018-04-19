/**
 * 
 */
package com.yqjr.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Dict <br>
 * Description: 数据字典注解,用于在Bean拷贝时实现数据字典转换 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月4日 上午9:43:09 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

	/**
	 * Description: 字典名称 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 上午9:49:40
	 *
	 * @return
	 */
	String name();

}
