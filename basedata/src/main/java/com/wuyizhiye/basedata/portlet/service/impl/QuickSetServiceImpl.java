package com.wuyizhiye.basedata.portlet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.portlet.dao.QuickJobItemDao;
import com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao;
import com.wuyizhiye.basedata.portlet.dao.QuickSetDao;
import com.wuyizhiye.basedata.portlet.model.QuickJobItem;
import com.wuyizhiye.basedata.portlet.model.QuickMenuItem;
import com.wuyizhiye.basedata.portlet.model.QuickSet;
import com.wuyizhiye.basedata.portlet.service.QuickSetService;

/**
 * @ClassName QuickSetServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="quickSetService")
@Transactional
public class QuickSetServiceImpl extends BaseServiceImpl<QuickSet> implements QuickSetService {
	@Autowired
	private QuickSetDao quickSetDao;
	
	@Autowired
	private QuickMenuItemDao quickMenuItemDao;
	
	@Autowired
	private QuickJobItemDao quickJobItemDao;
	
	@Override
	protected BaseDao getDao() {
		return quickSetDao;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addEntity(QuickSet entity) {
		entity.setId(UUID.randomUUID().toString());
		if(null == entity.getIsDefault() || "".equals(entity.getIsDefault())){
			entity.setIsDefault(CommonFlagEnum.NO);
		}
		String menuJson=entity.getMenuJson();
		String menuName=entity.getName()+":";
		if(!StringUtils.isEmpty(menuJson)){
			JSONArray arry=JSONArray.fromObject(menuJson);
			List<QuickMenuItem> qlist=(List<QuickMenuItem>) JSONArray.toCollection(arry, QuickMenuItem.class);
			for(QuickMenuItem qm:qlist){
				qm.setQuickSet(entity);
				menuName+=qm.getMenu().getName()+",";
				qm.setId(UUID.randomUUID().toString());
				quickMenuItemDao.addEntity(qm);
			}
			if(menuName.contains(",")){
				menuName=menuName.substring(0, menuName.length()-1);
			}
		}
		entity.setMenuName(menuName);
		quickSetDao.addEntity(entity);
	}
	
	
	
	/**
	 * 修改
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateEntity(QuickSet entity) {
		if(null == entity.getIsDefault() || "".equals(entity.getIsDefault())){
			entity.setIsDefault(CommonFlagEnum.NO);
		}
		String menuJson=entity.getMenuJson();
		String menuName=entity.getName()+":";
		/**
		 * 先删除 之前的 所有 菜单明细数据
		 */
		quickMenuItemDao.deleteById(entity.getId());
		if(!StringUtils.isEmpty(menuJson)){
			JSONArray arry=JSONArray.fromObject(menuJson);
			List<QuickMenuItem> qlist=(List<QuickMenuItem>) JSONArray.toCollection(arry, QuickMenuItem.class);
			for(QuickMenuItem qm:qlist){
				qm.setQuickSet(entity);
				menuName+=qm.getMenu().getName()+",";
				qm.setId(UUID.randomUUID().toString());
				quickMenuItemDao.addEntity(qm);
			}
			if(menuName.contains(",")){
				menuName=menuName.substring(0, menuName.length()-1);
			}
		}
		entity.setMenuName(menuName);
		quickSetDao.updateEntity(entity);
	}
	
	
	@Override
	public void deleteById(String id) {
		//删除3个表中的关联数据
		quickSetDao.deleteById(id);
		quickJobItemDao.deleteById(id);
		quickMenuItemDao.deleteById(id);
	}
	@Override
	public QuickSet getEntityById(String id) {
		QuickSet qs=quickSetDao.getEntityById(id);
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("quickId", id);
		List<QuickMenuItem> mlist=queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao.select", param, QuickMenuItem.class);
		qs.setMenuList(mlist);
		List<QuickJobItem> jlist=queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.QuickJobItemDao.select", param, QuickJobItem.class);
		qs.setJobList(jlist);
		return qs;
	}
	
	
	
	
}