package com.yqjr.modules.dict.service;

import com.yqjr.framework.base.BaseService;
import com.yqjr.framework.component.sequence.AbstractSequenceManager;
import com.yqjr.framework.datatype.OrderBy;
import com.yqjr.framework.datatype.Page;
import com.yqjr.framework.utils.CommonUtils;
import com.yqjr.framework.utils.Constants;
import com.yqjr.framework.utils.StringUtils;
import com.yqjr.modules.dict.condition.DictCondition;
import com.yqjr.modules.dict.dao.DictDao;
import com.yqjr.modules.dict.entity.Dict;
import com.yqjr.modules.dict.model.DictModel;
import com.yqjr.modules.menu.model.Item;
import com.yqjr.modules.menu.model.TreeModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class DictService extends BaseService<Integer, DictDao, Dict, DictCondition, DictModel> {

	public void saveModel(DictModel model) {
		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = model.getParentIds();
		model.setParent(dao.id(model.getParent().getId()));
		// 设置新的父节点串
		model.setParentIds(model.getParent().getParentIds() + model.getParent().getId() + ",");

		// 保存或更新实体
		if (model.getId() == null) {
			model.setId(AbstractSequenceManager.getSequenceManager().generateIntSeq(Constants.DEFAULT_SEQ_NAME));
			super.save(model);
		} else {
			super.update(model);
		}

		// 更新子节点 parentIds
		Dict d = new Dict();
		d.setParentIds("%," + model.getId() + ",%");
		List<Dict> list = dao.findByParentIdsLike(d);
		for (Dict e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, model.getParentIds()));
			dao.updateParentIds(e);
		}
	}

	/**
	 * 
	 * Description: 重新单查数据字典方法 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月7日 下午1:27:30
	 *
	 * @param dictModel
	 * @return
	 */
	@Override
	public Page<DictModel> findPage(Page<DictModel> conditionPage, DictCondition dictCondition) {
		OrderBy orderSort = new OrderBy("sort", "asc");
		OrderBy orderType = new OrderBy("type", "asc");
		OrderBy[] orderBy = new OrderBy[] { orderType,orderSort };
		conditionPage.setOrderBy(orderBy);
		DictCondition parent = new DictCondition();
		parent.setId(dictCondition.getParentId());
		dictCondition.setParent(parent);
		Page<DictModel> page = super.findPage(conditionPage,dictCondition);
		return page;
	}

	/**
	 * 
	 * Description: 重新单查数据字典方法 <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月7日 下午1:27:30
	 *
	 * @param dictModel
	 * @return
	 */
	@Override
	public DictModel get(DictModel dictModel) {
		if(dictModel.getId() != null){
			dictModel = super.get(dictModel);
		}
		Dict parent = dao.get(dictModel.getParent());
		dictModel.setParent(parent);
		return dictModel;
	}

	/**
	 * 
	 * Description: 查询数据字典信息（树形） <br>
	 * Create By: Wanglei <br>
	 * Create Date: 2017年7月6日 上午9:30:54
	 *
	 * @param menuId
	 * @param parentId
	 * @return
	 */
	
	public List<Item> findDictTree(String menuId, String parentId) {
		Set<String> checkId = new HashSet<String>();
		if (StringUtils.isNoneBlank(menuId)) {
			Dict condition = new Dict();
			condition.setId(Integer.valueOf(menuId));
			List<Dict> menuList = dao.findList(condition);
			for (Dict dict : menuList) {
				checkId.add(String.valueOf(dict.getId()));
			}
		}
		List<Map<String, Object>> treeList = dao.findForTree(parentId);
		List<TreeModel> tree = new ArrayList<TreeModel>();
		for (Map<String, Object> row : treeList) {

			TreeModel treeModel = new TreeModel();
			treeModel.setNodeId(row.get("ID").toString());
			if(row.get("TYPE") == null){
				treeModel.setNodeName(row.get("LABEL").toString());
			}else{
				treeModel.setNodeName(row.get("TYPE").toString()+" | "+row.get("LABEL").toString());
			}
			treeModel.setParentId(row.get("PARENTID").toString());
			treeModel.setParentIds(row.get("PARENTIDS").toString());
			long count = Long.parseLong(row.get("COUNTS").toString());
			treeModel.setParent(count > 0);
			treeModel.setNodeData(checkId.contains(row.get("ID").toString()) || treeModel.isParent());
			//treeModel.setNodeData(false);
			tree.add(treeModel);
		}
		return CommonUtils.buildTree(tree);
	}
}