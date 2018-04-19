/**
 * 
 */
package com.yqjr.framework.component.datasource;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.datasource.RouteInfo.RWMode;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.utils.Constants;

/**
 * ClassName: FrameworkDataSource <br>
 * Description: 框架数据源,根据框架参数配置实现如下功能: <br>
 * 1-数据源获取方式:local/jndi <br>
 * 2-数据源类型:多数据源/单一数据源 <br>
 * 3-数据源路由(读写分离) <br>
 * Create By: admin <br>
 * Create Date: 2017年4月18日 上午11:00:08 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
public class FrameworkDataSource extends AbstractRoutingDataSource implements InitializingBean {

	/**
	 * 数据源集合
	 */
	private List<DataSource> dataSources;

	/**
	 * @return the dataSources
	 */
	public List<DataSource> getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources
	 *            the dataSources to set
	 */
	public void setDataSources(List<DataSource> dataSources) {
		this.dataSources = dataSources;
	}

	/**
	 * 写节点组
	 */
	private Map<String, List<String>> writeGroupMap = null;

	/**
	 * 读节点映射
	 */
	private Map<String, List<String>> readGroupMap = null;

	/**
	 * targetDataSources
	 */
	private Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

	/**
	 * targetDataSourceKey
	 */
	private List<String> targetDataSourceKeys = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#
	 * afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
		try {
			init();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		super.afterPropertiesSet();
	}

	/**
	 * 数据源路由逻辑<br>
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		// 单数据源的情况下直接返回路由结果
		if (targetDataSourceKeys.size() == 1) {
			return targetDataSourceKeys.get(0);
		}
		// 否则需要从线程上下文中获取路由信息
		RouteInfo route = ThreadContext.getInstance().getObject(Constants.ROUTEID);
		// 多数据源的情况下路由信息不能为空
		if (route == null) {
			throw new BizzException("配置多数据源的情况下路由信息不能为空");
		}
		// 根据是否配置为读写分离进行不同的路由逻辑
		// 开启情况
		if (Configuration.getConfig().getBooleanValue("framework.db.rw")) {
			// 如果指定数据源节点,则直接返回
			if (StringUtils.isNotBlank(route.getId())) {
				return route.getId();
			}
			// 否则需要先判断路由信息是否有效
			if (StringUtils.isBlank(route.getGroup()) || route.getType() == null)
				throw new BizzException("数据源路由无效");
			// 进行读写节点路由
			List<String> dsList = null;
			if (route.getType() == RWMode.R) {
				dsList = readGroupMap.get(route.getGroup());
			} else if (route.getType() == RWMode.W) {
				dsList = writeGroupMap.get(route.getGroup());
			}
			Random rand = new Random();
			return dsList.get(rand.nextInt(dsList.size()));
		}
		// 非开启情况
		else {
			// 检查路由信息是否有效
			if (StringUtils.isBlank(route.getId()) || !targetDataSourceKeys.contains(route.getId()))
				throw new BizzException("数据源路由无效");
			return route.getId();
		}
	}

	/**
	 * Description: 初始化方法 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月18日 下午2:13:15
	 * 
	 * @throws PropertyVetoException
	 */
	private void init() throws PropertyVetoException {
		// 配置校验
		Assert.notEmpty(dataSources, "数据源配置不能为空");
		if (Configuration.getConfig().getBooleanValue("framework.db.rw"))
			Assert.isTrue(dataSources.size() > 1, "配置读写分离的情况下必须配置多数据源");

		// 判断是否开启读写分离
		if (Configuration.getConfig().getBooleanValue("framework.db.rw")) {
			// 用于存放虚拟节点与实际数据源的映射关系,例如
			// dn1(虚拟节点)->ds1(W)/ds2(R)/ds3(R)
			// dn2(虚拟节点)->ds4(W)/ds5(R)/ds6(R)
			// writeGroupMap -> dn1->ds1,dn2->ds4
			// readGroupMap -> dn1->ds2,ds3,dn2->ds5,ds6
			writeGroupMap = new HashMap<String, List<String>>();
			readGroupMap = new HashMap<String, List<String>>();
			// 加载数据源配置
			for (DataSource dataSource : dataSources) {
				Assert.hasText(dataSource.getType(), "配置读写分离的情况下需指定数据源类型");
				// 初始化数据源
				initDataSource(dataSource);
				if ("R".equalsIgnoreCase(dataSource.getType())) {
					// 判断读节点映射中是否包含该组数据
					List<String> readIds = readGroupMap.get(dataSource.getGroup());
					// 如果为空则初始化
					if (readIds == null) {
						readIds = new ArrayList<String>();
						readGroupMap.put(dataSource.getGroup(), readIds);
					}
					readIds.add(dataSource.getId());
				} else if ("W".equalsIgnoreCase(dataSource.getType())) {
					// 判断写节点映射中是否包含改组数据
					List<String> writeIds = writeGroupMap.get(dataSource.getGroup());
					// 如果为空则初始化
					if (writeIds == null) {
						writeIds = new ArrayList<String>();
						writeGroupMap.put(dataSource.getGroup(), writeIds);
					}
					writeIds.add(dataSource.getId());
				}
			}
		}
		// 如果没有开启读写分离的情况下,选择数据源ID做为路由索引
		else {
			for (DataSource dataSource : dataSources) {
				initDataSource(dataSource);
			}
		}

		// 保存数据源ID,方便索引
		Iterator<Object> iter = targetDataSources.keySet().iterator();
		while (iter.hasNext()) {
			targetDataSourceKeys.add(iter.next().toString());
		}

		// 调用父类方法设置targetDataSource
		super.setTargetDataSources(targetDataSources);
	}

	/**
	 * Description: 根据配置初始化数据源 <br>
	 * Create By: admin <br>
	 * Create Data: 2017年4月18日 下午3:50:01
	 *
	 * @param config
	 * @throws PropertyVetoException
	 */
	private void initDataSource(DataSource dataSource) throws PropertyVetoException {
		// 校验数据源配置
		Assert.notNull(dataSource);
		Assert.hasText(dataSource.getId(), "数据源ID不能为空");
		Assert.isTrue(dataSource.getDataSource() != null || StringUtils.isNotBlank(dataSource.getJndiName()),
				"数据源配置异常");
		// 判断ID是否重复
		if (targetDataSources.containsKey(dataSource.getId())) {
			throw new BizzException("数据源ID重复," + dataSource.getId());
		}
		// 根据数据源配置方式获取数据源
		// 本地数据源的情况下调用数据源工厂
		if (dataSource.getDataSource() != null) {
			targetDataSources.put(dataSource.getId(), dataSource.getDataSource());
		}
		// jndi数据源的情况下传入jndiName
		else if (StringUtils.isNotBlank(dataSource.getJndiName())) {
			targetDataSources.put(dataSource.getId(), dataSource.getJndiName());
		}
	}
}
