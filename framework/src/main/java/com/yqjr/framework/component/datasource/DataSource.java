/**
 * 
 */
package com.yqjr.framework.component.datasource;

/**
 * ClassName: DataSource <br>
 * Description: 框架数据源<br>
 * 对原有datasource进行包装,为后续读写分离做准备<br>
 * Create By: admin <br>
 * Create Date: 2017年5月3日 下午6:18:41 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class DataSource {

	/**
	 * 逻辑分组名称
	 */
	private String group = "default";

	/**
	 * 数据源ID
	 */
	private String id;

	/**
	 * 数据源类型<br>
	 * R-读节点<br>
	 * W-写节点<br>
	 */
	private String type;

	/**
	 * jndi名称
	 */
	private String jndiName;

	/**
	 * 引用数据源
	 */
	private javax.sql.DataSource dataSource;

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the jndiName
	 */
	public String getJndiName() {
		return jndiName;
	}

	/**
	 * @param jndiName
	 *            the jndiName to set
	 */
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	/**
	 * @return the dataSource
	 */
	public javax.sql.DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(javax.sql.DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
