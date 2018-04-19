/**
 * Copyright &copy; YQJR All rights reserved.
 */
package com.yqjr.framework.component.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.Assert;

import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.dialect.Dialect;
import com.yqjr.framework.component.dialect.db.AbstractDialect;
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.Constants;
import com.yqjr.framework.utils.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据库分页插件，只拦截查询语句.
 * 
 * @author poplar.yfyang /
 * @version 2013-8-28
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class PaginationInterceptor implements Interceptor {

	protected Log log = LogFactory.getLog(this.getClass());

	private static final String SUFFIX = "_count";

	private static final List<ResultMapping> EMPTY_RESULTMAPPING = new ArrayList<ResultMapping>(0);

	private Dialect dialect;

	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		// 获取分页参数信息
		Page<Object> page = ThreadContext.getInstance().getObject(Constants.PAGE);

		// 数据库不支持分页或分页对象为空或pageSize无效则直接执行后续方法
		if (!dialect.supportsLimit() || page == null || page.getPageSize() <= 0) {
			return invocation.proceed();
		}

		// 获取拦截参数
		Object args[] = invocation.getArgs();
		MappedStatement mappedStatement = (MappedStatement) args[0];
		Object parameter = invocation.getArgs()[1];
		ResultHandler resultHandler = (ResultHandler) invocation.getArgs()[3];

		// 获取后续逻辑中使用的对象
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Object parameterObject = boundSql.getParameterObject();
		Executor executor = (Executor) invocation.getTarget();

		// 数据校验
		Assert.hasText(boundSql.getSql());

		// 查询总条数
		String originalSql = boundSql.getSql().trim();
		// 生成count sql
		String countSql = dialect.getCountSql(originalSql);
		// 构建mybatis参数,执行count sql
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql,
				boundSql.getParameterMappings(), parameterObject);
		MappedStatement countMappedStatement = countMappedStatement(mappedStatement,
				new BoundSqlSqlSource(countBoundSql));
		// 处理foreach
		if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
			MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
			Reflections.setFieldValue(countBoundSql, "metaParameters", mo);
		}
		Object countRes = executor.query(countMappedStatement, parameterObject, RowBounds.DEFAULT, resultHandler);
		// 查询到记录总数
		Long count = ((List<Long>) countRes).get(0);
		page.setCount(count);

		// 查询分页数据,生成分页sql
		// 判断是否存在排序，如果存在排序则添加(待优化,如果集成sql解析器,使用sql解析器添加order by)
		if (page.getOrderBy() != null) {
			originalSql += " order by " + page.getOrderBy();
		}
		String pageSql = dialect.getLimitString(originalSql, page.getFirstResult(), page.getMaxResults());
		BoundSql pageBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql,
				boundSql.getParameterMappings(), boundSql.getParameterObject());
		MappedStatement pageMappedStatement = pageMappedStatement(mappedStatement, new BoundSqlSqlSource(pageBoundSql));
		// 处理foreach
		if (Reflections.getFieldValue(boundSql, "metaParameters") != null) {
			MetaObject mo = (MetaObject) Reflections.getFieldValue(boundSql, "metaParameters");
			Reflections.setFieldValue(pageBoundSql, "metaParameters", mo);
		}

		// 更改参数
		invocation.getArgs()[0] = pageMappedStatement;
		invocation.getArgs()[2] = new RowBounds();

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		dialect = AbstractDialect.getDialect(Configuration.getConfig().getStringValue("framework.db.type"));
	}

	private MappedStatement countMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + SUFFIX,
				newSqlSource, ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null) {
			for (String keyProperty : ms.getKeyProperties()) {
				builder.keyProperty(keyProperty);
			}
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		// 构建空的resultMap
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId() + SUFFIX, Long.class,
				EMPTY_RESULTMAPPING).build();
		resultMaps.add(resultMap);
		builder.resultMaps(resultMaps);
		builder.cache(ms.getCache());
		return builder.build();
	}

	private MappedStatement pageMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());
		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		if (ms.getKeyProperties() != null) {
			for (String keyProperty : ms.getKeyProperties()) {
				builder.keyProperty(keyProperty);
			}
		}
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.cache(ms.getCache());
		return builder.build();
	}

	public static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
}
