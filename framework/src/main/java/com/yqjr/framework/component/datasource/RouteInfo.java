/**
 * 
 */
package com.yqjr.framework.component.datasource;

/**
 * ClassName: RouteInfo <br>
 * Description: 数据源路由信息 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月18日 下午6:44:17 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class RouteInfo {

	/**
	 * 数据访问组(虚拟节点ID)
	 */
	private String group;

	/**
	 * 访问读写节点
	 */
	private RWMode type;

	/**
	 * 数据源访问节点
	 */
	private String id;

	/**
	 * 横向切分的情况下需要指定虚拟节点
	 * 
	 * @param group
	 * @param type
	 */
	public RouteInfo(String group, RWMode type) {
		this.group = group;
		this.type = type;
	}

	/**
	 * 指定数据源ID访问
	 * 
	 * @param id
	 */
	public RouteInfo(String id) {
		this.id = id;
	}

	public enum RWMode {
		R, W;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return the type
	 */
	public RWMode getType() {
		return type;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
