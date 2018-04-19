package com.yqjr.framework.component.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import com.yqjr.framework.component.config.file.FileConfig;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.Constants;

public abstract class Configuration implements IConfig {

	private static final String DEFAULT_PROVIDER = "FILE";

	/**
	 * 用户自定义配置
	 */
	private static Map<String, Object> configuration = new HashMap<String, Object>();

	private static final String[] booleanValue = new String[] { "1", "0", "true", "false", "y", "n" };

	private static IConfig iconfig = null;

	public Configuration() {
	}

	public String getStringValue(String key) {
		Assert.hasText(key);
		Object value = configuration.get(key);
		if (value == null)
			return null;
		return ((String) value).trim();
	}

	public int getIntValue(String key) {
		String value = getStringValue(key);
		Assert.isTrue(NumberUtils.isNumber(value));
		return Integer.parseInt(value);
	}

	public long getLongValue(String key) {
		String value = getStringValue(key);
		Assert.isTrue(NumberUtils.isNumber(value));
		return Long.parseLong(value);
	}

	public boolean getBooleanValue(String key) {
		String value = getStringValue(key);
		Assert.isTrue(ArrayUtils.contains(booleanValue, value.toLowerCase()));
		return NumberUtils.isNumber(value) ? BooleanUtils.toBoolean(Integer.parseInt(value))
				: BooleanUtils.toBoolean(value);
	}

	protected void setProperties(String key, String value) {
		Assert.hasText(key);
		configuration.put(key.trim(), value.trim());
	}

	/**
	 * Description: 获取系统配置加载组件 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月17日 下午3:52:37
	 *
	 * @return
	 */
	public static IConfig getConfig() {
		if (iconfig == null) {
			// 配置方式,默认为文件形式
			String configProvider = DEFAULT_PROVIDER;
			// 判断启动参数中是否指定配置方式
			if (System.getProperty(Constants.CONFIG_PROVIDER) != null) {
				configProvider = System.getProperty(Constants.CONFIG_PROVIDER);
			}
			if ("FILE".equals(configProvider)) {
				iconfig = FileConfig.getInstance();
			}
			// 将来可能会存在数据库方式,ZK等形式,暂时仅支持文件形式
			else {
				throw new BizzException(String.format("尚未支持的配置方式,%s", configProvider));
			}
		}
		return iconfig;
	}
}
