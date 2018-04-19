package com.yqjr.modules.menu.model;

/**
 * 
 * ClassName: Item <br>
 * Description: 树组件项POJO <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月6日 上午10:29:14 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class Item {
	//节点名字
	private String text;
	//节点类型: item 文件     folder 目录
	private String type;
	//节点信息
	private AdditionalParameters additionalParameters;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AdditionalParameters getAdditionalParameters() {
		return additionalParameters;
	}

	public void setAdditionalParameters(AdditionalParameters additionalParameters) {
		this.additionalParameters = additionalParameters;
	}

}
