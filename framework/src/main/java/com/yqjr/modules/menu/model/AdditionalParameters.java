package com.yqjr.modules.menu.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * ClassName: AdditionalParameters <br>
 * Description: 树组件扩展POJO <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月6日 上午10:28:40 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class AdditionalParameters {
	
	/**
	 * 子节点列表
	 */
	@JsonProperty("children")
	private List<Item> children = new ArrayList<Item>();

	/**
	 * 节点的Id
	 */
	private String id;
	
	/**
	 * 上层节点Id
	 */
	private String pid;
	
	/**
	 * 所有上层节点
	 */
	private String pids;

	/**
	 * 是否有选中属性
	 */
	@JsonProperty("item-selected")
	private boolean itemSelected;
	
	public String getPid() {
		return pid;
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<Item> getChildren() {
		return children;
	}

	public void setChildren(List<Item> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isItemSelected() {
		return itemSelected;
	}

	public void setItemSelected(boolean itemSelected) {
		this.itemSelected = itemSelected;
	}

	public String getPids() {
		return pids;
	}

	public void setPids(String pids) {
		this.pids = pids;
	}

}
