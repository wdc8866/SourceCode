/**
 * 
 */
package com.yqjr.framework.component.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.yqjr.framework.component.cache.AbstractCache;
import com.yqjr.framework.component.cache.ICache;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.modules.dict.model.DictModel;
import com.yqjr.modules.dict.service.DictService;

/**
 * ClassName: DictLoader <br>
 * Description: 数据字典组件 <br>
 * Create By: admin <br>
 * Create Date: 2017年5月4日 上午10:30:02 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@Component
public class DictLoader {

	private static final String CACHE_DICT_MAP = "framework.dict.cache";

	private ICache cache = AbstractCache.getCache();

	private static DictLoader instance = null;

	@Autowired
	private DictService dictService;

	private DictLoader() {
	}

	public static DictLoader getInstance() {
		if (instance == null) {
			synchronized (DictLoader.class) {
				if (instance == null) {
					instance = SpringContext.getInstance().getBeanWithName("dictLoader");
				}
			}
		}
		return instance;
	}

	/**
	 * Description: 数据字典初始化 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月4日 上午10:30:41
	 */
	public void init() {
		Map<String, List<DictModel>> dictMap = new HashMap<String, List<DictModel>>();
		for (DictModel dict : dictService.findList(new DictModel())) {
			List<DictModel> dictList = dictMap.get(dict.getType());
			if (dictList == null) {
				dictList = new ArrayList<DictModel>();
				dictMap.put(dict.getType(), dictList);
			}
			dictList.add(dict);
		}
		cache.put(CACHE_DICT_MAP, dictMap);
	}

	/**
	 * Description: 获取标签 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月8日 上午9:05:16
	 *
	 * @param type
	 *            字典类型
	 * @param value
	 *            字典值
	 * @return
	 */
	public String getLabel(String type, String value) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			for (DictModel dict : getDicts(type)) {
				if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
					return dict.getLabel();
				}
			}
		}
		return null;
	}

	/**
	 * Description: 获取值 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月8日 上午9:05:31
	 *
	 * @param type
	 *            字典类型
	 * @param label
	 *            字典标签
	 * @return
	 */
	public String getValue(String type, String label) {
		Assert.hasText(type);
		Assert.hasText(label);
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			for (DictModel dict : getDicts(type)) {
				if (type.equals(dict.getType()) && label.equals(dict.getLabel())) {
					return dict.getValue();
				}
			}
		}
		throw new BizzException("无效的数据字典值");
	}

	/**
	 * Description: 获取字典列表 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月8日 上午9:06:13
	 *
	 * @param type
	 *            字典类型
	 * @return
	 */
	public List<DictModel> getDicts(String type) {
		Map<String, List<DictModel>> dictMap = cache.get(CACHE_DICT_MAP);
		if (dictMap == null) {
			synchronized (this) {
				if (cache.get(CACHE_DICT_MAP) == null) {
					init();
					dictMap = cache.get(CACHE_DICT_MAP);
				}
			}
		}
		return dictMap.get(type);
	}
	
	/**
	 * 
	 * Description: 用于页面TLD调用的获取字典列表 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月3日 下午3:39:01
	 *
	 * @param type
	 * @return
	 */
	public static List<DictModel> getDictsStatic(String type) {
		return getInstance().getDicts(type);
	}
	
	/**
	 * 
	 * Description: 获取字典表对应关系
	 *  <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月3日 下午3:40:04
	 *
	 * @param type
	 * @return
	 */
	public static String getDictsForJQGird(String type){
		StringBuffer sb = new StringBuffer();
		List<DictModel> list = getInstance().getDicts(type);
		if(!list.isEmpty()){
			for(DictModel dict : list){
				sb.append(dict.getValue()).append(":").append(dict.getLabel()).append(";");
			}
		}
		return sb.toString().substring(0, sb.toString().length()-1);
	}
}
