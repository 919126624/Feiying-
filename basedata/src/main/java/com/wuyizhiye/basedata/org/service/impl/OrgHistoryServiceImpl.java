package com.wuyizhiye.basedata.org.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.dao.OrgHistoryDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgHistoryService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName OrgHistoryServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value = "orgHistoryService")
@Transactional
public class OrgHistoryServiceImpl extends DataEntityService<Org> implements
		OrgHistoryService {
	@Autowired
	private OrgHistoryDao orgHistoryDao;
	
	@Autowired
	private OrgDao orgDao;
	
	@Override
	protected BaseDao getDao() {
		return orgHistoryDao;
	}

	@Override
	public void addEntity(Org entity) {
		getOrgList(entity.getId()); 
		insertOrg(entity); //新增组织历史记录
	}

	/**
	 * 递归查询树结构并且将数据插入组织历史表中
	 * @param orgId  组织ID
	 */
	public void getOrgList(String orgId) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("parent", orgId);
		List<Org> orgItems = (List<Org>)orgDao.getOrgByOrg(param);
		if (orgItems.size()>0) {
			for (int i = 0; i < orgItems.size(); i++) {
				Org org = orgItems.get(i);
				if(!org.isLeaf()){
					getOrgList(org.getId());
				}
				insertOrg(org); //新增子节点
			}
		}
	}
	/**
	 * 新增组织历史记录
	 * @param entity
	 */
	private void insertOrg(Org entity){
		if (entity != null) {
			entity.setId(entity.getId());
			entity.setDisabledDate(new Date());
			entity.setCreator(SystemUtil.getCurrentUser());
		}
		super.addEntity(entity);
	}

}
