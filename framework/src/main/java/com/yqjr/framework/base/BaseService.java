package com.yqjr.framework.base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.yqjr.framework.annotation.FrameworkService;
import com.yqjr.framework.component.config.Configuration;
import com.yqjr.framework.component.context.SpringContext;
import com.yqjr.framework.component.context.ThreadContext;
import com.yqjr.framework.component.mapper.BeanMapper;
import com.yqjr.framework.datatype.BizzException;
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.Constants;
import com.yqjr.framework.utils.Reflections;

/**
 * ClassName: BaseService <br>
 * Description: 业务服务基类 <br>
 * Create By: admin <br>
 * Create Date: 2017年4月19日 下午7:00:04 <br>
 * Modified By: <br>
 * Modified Date: <br>
 * Modified Content: <br>
 * Version: 1.0 <br>
 */
@SuppressWarnings("rawtypes")
@FrameworkService
public abstract class BaseService<ID, D extends BaseDao<ID, T, C>, T extends BaseEntity<ID, T>, C extends BaseCondition, BM extends BaseModel<?, BM>> {

	@Autowired
	protected D dao;

	@Autowired
	private SqlSessionFactoryBean sqlSessionFactory;

	private static final int BATCH_SIZE = Configuration.getConfig().getIntValue("framework.db.batchsize");

	/**
	 * 保存数据
	 * 
	 * @param entity
	 */
	@Transactional
	public ID save(BM model) {
		T entity = toEntity(model, getEntityClass());
		entity.preInsert();
		dao.insert(entity);
		return entity.getId();
	}

	/**
	 * Description: 批量保存 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月2日 上午11:30:54
	 *
	 * @param entities
	 *            待保存数据
	 */
	@Transactional
	public void batchSave(List<T> entities) {
		Assert.notEmpty(entities);
		String mapperName = getDaoClass().getName() + ".insert";
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.getObject().openSession(ExecutorType.BATCH);
			for (int i = 1; i <= entities.size(); i++) {
				T entity = entities.get(i - 1);
				entity.preInsert();
				sqlSession.insert(mapperName, entity);
				if (i % BATCH_SIZE == 0 || i == entities.size()) {
					sqlSession.commit();
					sqlSession.clearCache();
				}
			}
		} catch (Exception e) {
			throw new BizzException("批量保存数据异常", e);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	/**
	 * Description: 更新数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月27日 上午8:32:07
	 *
	 * @param entity
	 */
	@Transactional
	public void update(BM model) {
		T entity = toEntity(model, getEntityClass());
		entity.preUpdate();
		int res = dao.update(entity);
		if (res == 0)
			throw new BizzException("影响记录数为0,更新失败");
	}

	/**
	 * Description: 批量更新 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月2日 上午11:30:54
	 *
	 * @param entities
	 *            待更新数据
	 */
	@Transactional
	public void batchUpdate(List<T> entities) {
		Assert.notEmpty(entities);
		String mapperName = getDaoClass().getName() + ".update";
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.getObject().openSession(ExecutorType.BATCH);
			for (int i = 1; i <= entities.size(); i++) {
				T entity = entities.get(i - 1);
				entity.preUpdate();
				sqlSession.update(mapperName, entity);
				// 达到批量大小或者循环结束时提交数据
				if (i % BATCH_SIZE == 0 || i == entities.size()) {
					sqlSession.commit();
					sqlSession.clearCache();
				}
			}
		} catch (Exception e) {
			throw new BizzException("批量更新数据异常", e);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param entity
	 */
	@Transactional
	public void delete(BM model) {
		T entity = toEntity(model, getEntityClass());
		entity.setDeleteStatus(T.DELETE);
		entity.preUpdate();
		int res = dao.delete(entity);
		if (res == 0)
			throw new BizzException("影响记录数为0,删除失败");
	}

	/**
	 * Description: 批量删除 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月2日 上午11:30:54
	 *
	 * @param entities
	 *            待删除数据
	 */
	@Transactional
	public void batchDelete(List<T> entities) {
		Assert.notEmpty(entities);
		String mapperName = getDaoClass().getName() + ".delete";
		SqlSession sqlSession = null;
		try {
			sqlSession = sqlSessionFactory.getObject().openSession(ExecutorType.BATCH);
			for (int i = 1; i <= entities.size(); i++) {
				T entity = entities.get(i - 1);
				entity.setDeleteStatus(T.DELETE);
				entity.preUpdate();
				sqlSession.update(mapperName, entity);
				// 达到批量大小或者循环结束时提交数据
				if (i % BATCH_SIZE == 0 || i == entities.size()) {
					sqlSession.commit();
					sqlSession.clearCache();
				}
			}
		} catch (Exception e) {
			throw new BizzException("批量删除数据异常", e);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	/**
	 * Description: 批量删除数据 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月28日 上午10:06:21
	 *
	 * @param ids
	 *            数据ID,使用,分割
	 */
	@SuppressWarnings("unchecked")
	public void batchDelete(String ids) {
		Assert.hasText(ids);
		List<T> deleteIds = new ArrayList<T>();
		for (String id : ids.split(",")) {
			T t = null;
			try {
				t = getEntityClass().newInstance();
			} catch (Exception e) {
				throw new BizzException("批量删除数据异常", e);
			}
			Class idClass = getIDClass();
			Object tmp = null;
			if (idClass == Long.class) {
				tmp = Long.valueOf(id);
			} else if (idClass == Integer.class) {
				tmp = Integer.valueOf(id);
			} else {
				tmp = id;
			}
			t.setId((ID) tmp);
			deleteIds.add(t);
		}
		batchDelete(deleteIds);
	}

	/**
	 * 获取单条数据
	 * 
	 * @param id
	 * @return
	 */
	public BM id(ID id) {
		T entity = dao.id(id);
		return toModel(entity, getModelClass(), false);
	}

	/**
	 * 获取单条数据
	 * 
	 * @param entity
	 * @return
	 */
	public BM get(BM model) {
		T entity = toEntity(model, getEntityClass());
		entity = dao.get(entity);
		return toModel(entity, getModelClass(), false);
	}

	/**
	 * 查询列表数据
	 * 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <M extends BaseModel> List<M> findList(M model) {
		T entity = toEntity(model, getEntityClass());
		List<T> list = dao.findList(entity);
		return toModels(list, model.getClazz(), false);
	}

	/**
	 * Description: 单表分页查询 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月21日 下午1:02:01
	 *
	 * @param page
	 * @param condition
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<BM> findPage(Page<BM> page, C condition) {
		Assert.notNull(page);
		ThreadContext.getInstance().put(Constants.PAGE, page);
		List<?> result = dao.findByCondition(condition);
		List<BM> models = new ArrayList<BM>();
		for (Object object : result) {
			// 如果查询结果为model则直接存入结果集
			if (object instanceof BaseModel) {
				models.add((BM) object);
			}
			// 如果为entity则进行属性复制
			else if (object instanceof BaseEntity) {
				models.add(toModel((T) object, page.getClazz(), true));
			}
		}
		page.setList(models);
		return page;
	}

	/**
	 * 查询分页数据
	 * 
	 * @param page
	 *            分页对象
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <M extends BaseModel> Page<M> findPage(Page<M> page, String methodName, Object... parameter) {
		Assert.notNull(page);
		Assert.hasText(methodName);
		ThreadContext.getInstance().put(Constants.PAGE, page);
		Method method = SpringContext.getInstance().getMethodWithName(dao.getClass(), methodName);
		List<?> result = null;
		try {
			result = (List<?>) method.invoke(dao, parameter);
		} catch (Exception e) {
			throw new BizzException(e);
		}
		List<M> models = new ArrayList<M>();
		for (Object object : result) {
			// 如果查询结果为model则直接存入结果集
			if (object instanceof BaseModel) {
				models.add((M) object);
			}
			// 如果为entity则进行属性复制
			else if (object instanceof BaseEntity) {
				models.add(toModel((T) object, page.getClazz(), true));
			}
		}
		page.setList(models);
		return page;
	}

	/**
	 * Description: 实体对象转模型 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月24日 下午6:56:40
	 *
	 * @param entity
	 * @param clazz
	 * @param transferDict
	 *            是否需要转换数据字典
	 * @return
	 */
	public <M extends BaseModel, E extends BaseEntity> M toModel(E entity, Class<M> clazz, boolean transferDict) {
		if (entity == null)
			return null;
		Assert.notNull(clazz);
		try {
			M modelInstance = clazz.newInstance();
			BeanMapper.beanToBean(entity, modelInstance, false, transferDict);
			return modelInstance;
		} catch (Exception e) {
			throw new BizzException(e);
		}
	}

	public <M extends BaseModel, E extends BaseEntity> M toModel(E entity, Class<M> clazz) {
		return toModel(entity, clazz);
	}

	/**
	 * Description: 实体对象转模型 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年5月3日 上午9:42:27
	 *
	 * @param entities
	 * @param clazz
	 * @return
	 */
	public <M extends BaseModel, E extends BaseEntity> List<M> toModels(List<E> entities, Class<M> clazz,
			boolean transferDict) {
		if (entities == null)
			return null;
		Assert.notNull(clazz);
		try {
			List<M> list = new ArrayList<M>();
			for (E entity : entities) {
				list.add(toModel(entity, clazz, transferDict));
			}
			return list;
		} catch (Exception e) {
			throw new BizzException(e);
		}
	}

	public <M extends BaseModel, E extends BaseEntity> List<M> toModels(List<E> entities, Class<M> clazz) {
		return toModels(entities, clazz, false);
	}

	/**
	 * Description: 模型转实体对象 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年4月24日 下午6:56:40
	 *
	 * @param model
	 * @param clazz
	 * @return
	 */
	public <M extends BaseModel, E extends BaseEntity> E toEntity(M model, Class<E> clazz) {
		Assert.notNull(model);
		Assert.notNull(clazz);
		try {
			E entityInstance = clazz.newInstance();
			BeanMapper.beanToBean(model, entityInstance, false, false);
			return entityInstance;
		} catch (Exception e) {
			throw new BizzException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return Reflections.getClassGenricType(this.getClass(), 2);
	}

	@SuppressWarnings("unchecked")
	private Class<BM> getModelClass() {
		return Reflections.getClassGenricType(this.getClass(), 4);
	}

	@SuppressWarnings("unchecked")
	private Class<D> getDaoClass() {
		return Reflections.getClassGenricType(this.getClass(), 1);
	}

	@SuppressWarnings("unchecked")
	private Class<ID> getIDClass() {
		return Reflections.getClassGenricType(this.getClass(), 0);
	}

	/**
	 * Description: 校验属性是否重复 <br>
	 * Create By: admin <br>
	 * Create Date: 2017年6月28日 下午4:46:36
	 *
	 * @param oldName
	 * @param name
	 * @return
	 */
	public String checkUnique(String old, String cur, BM model) {
		if (cur != null && cur.equals(old)) {
			return "true";
		} else if (cur != null && get(model) == null) {
			return "true";
		}
		return "false";
	}

}
