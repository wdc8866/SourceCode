/**
 * 
 */
package com.yqjr.framework.component.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * ClassName: DateFormator <br>
 * Description: 日期格式校验 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月15日 上午11:43:54 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DateFormatorValidator.class })
@Documented
public @interface DateFormator {

	String message() default "时间校验非法";

	String format();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}