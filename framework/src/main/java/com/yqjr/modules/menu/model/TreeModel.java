package com.yqjr.modules.menu.model;

/**
 * 
 * ClassName: TreeModel <br>
 * Description: 树组件POJO <br>
 * Create By: Wanglei <br>
 * Create Date: 2017年6月6日 上午10:29:44 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 *
 */
public class TreeModel implements Comparable<TreeModel> {

	/**
	 * nodeId
	 */
	private String nodeId;

	/**
	 * node名称
	 */
	private String nodeName;
	
	/**
	 * 是否为父节点
	 */
	private boolean isParent;

	/**
	 * node层级
	 */
	private int level;

	/**
	 * node父节点Id
	 */
	private String parentId;
	
	/**
	 * node所有父节点
	 */
	private String parentIds;

	/**
	 * 数据,通过此字段与应用集成
	 */
	private Object nodeData;

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		/*this.level = nodeId.length() / DIGIT;
		if (level == 1) {
			this.parentId = "0";
		} else {
			this.parentId = nodeId.substring(0, (level - 1) * DIGIT);
		}*/
		this.nodeId = nodeId;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}
	
	/**
	 * @return the isParent
	 */
	public boolean isParent() {
		return isParent;
	}

	/**
	 * @param isParent the isParent to set
	 */
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	/**
	 * @param nodeName
	 *            the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the nodeData
	 */
	public Object getNodeData() {
		return nodeData;
	}

	/**
	 * @param nodeData
	 *            the nodeData to set
	 */
	public void setNodeData(Object nodeData) {
		this.nodeData = nodeData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TreeModel model) {

		/*// 如果菜单等级相同则直接判断Id大小即可
		if (this.level == model.level) {
			return this.nodeId.compareTo(model.getNodeId());
		}

		// 如果等级不同进行补位后再判断
		else if (this.level > model.level) {
			return this.nodeId.compareTo((StringUtils.rightPad(
					model.getNodeId(), this.level * DIGIT, "0")));
		} else {
			return StringUtils.rightPad(this.nodeId, model.getLevel() * DIGIT,
					"0").compareTo(model.getNodeId());
		}*/
		return 0;
	}

}
