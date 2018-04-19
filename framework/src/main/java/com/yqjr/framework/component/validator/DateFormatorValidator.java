/**
 * 
 */
package com.yqjr.framework.component.validator;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName: DateFormatorValidator <br>
 * Description: 日期格式校验器 <br>
 * Create By: admin <br>
 * Create Date: 2017年6月15日 上午11:49:06 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DateFormatorValidator implements ConstraintValidator<DateFormator, String> {

	private String format;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.validation.ConstraintValidator#initialize(java.lang.annotation.
	 * Annotation)
	 */
	@Override
	public void initialize(DateFormator constraintAnnotation) {
		this.format = constraintAnnotation.format();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
	 * javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		if (StringUtils.isBlank(value))
			return false;

		boolean isValid = false;
		SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
		try {
			Date date = dateFormatter.parse(value.trim());
			if (value.equals(dateFormatter.format(date)))
				return true;
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}
}
