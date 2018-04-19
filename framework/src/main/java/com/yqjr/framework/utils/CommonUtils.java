package com.yqjr.framework.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.yqjr.modules.menu.model.AdditionalParameters;
import com.yqjr.modules.menu.model.Item;
import com.yqjr.modules.menu.model.TreeModel;

/**
 * 
 * ClassName: CommonUtils <br>
 * Description: 通用工具类 <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月6日 上午10:25:48 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class CommonUtils {

	/**
	 * 
	 * Description: 构建树状组件 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年6月6日 上午10:26:08
	 *
	 * @param list
	 * @return
	 */
	public static List<Item> buildTree(List<TreeModel> list) {
		Assert.notNull(list);
		Assert.notEmpty(list);
		List<Item> tmp = new ArrayList<Item>();
		tmp.add(new Item());
		List<Item> voItemList = new ArrayList<Item>();
		for (TreeModel node : list) {
			Item item = new Item();
			item.setText(node.getNodeName());
			AdditionalParameters adp = new AdditionalParameters();
			adp.setId(node.getNodeId());
			adp.setPid(node.getParentId());
			adp.setPids(node.getParentIds());
			adp.setItemSelected((Boolean)node.getNodeData());
			adp.setChildren(tmp);
			item.setAdditionalParameters(adp);
			item.setType(node.isParent() ? "folder" : "item");
			voItemList.add(item);
		}
		return voItemList;
	}
	
	/**
	 * 
	 * Description: 金额转换 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月10日 下午2:57:56
	 *
	 * @param money
	 * @return
	 */
	public static String formatMoney(BigDecimal amount) {
		if (null == amount) {
			amount = new BigDecimal(0.00);
		}
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("###,##0.00");
		return myformat.format(amount);
	}
}
