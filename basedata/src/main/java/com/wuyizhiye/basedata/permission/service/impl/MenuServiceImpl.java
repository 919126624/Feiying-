package com.wuyizhiye.basedata.permission.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.dao.MenuDao;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.service.MenuService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName MenuServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="menuService")
@Transactional
public class MenuServiceImpl extends DataEntityService<Menu> implements MenuService{
	@Autowired
	private MenuDao menuDao;
	@Override
	protected BaseDao getDao() {
		return menuDao;
	}
	
	@Override
	public void addEntity(Menu entity) {
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null){
			Menu parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			entity.setEnableFlag("Y");
			entity.setMenuType(parent.getMenuType());
			super.updateEntity(parent);
		}
		super.addEntity(entity);
	}
	
	@Override
	public void updateEntity(Menu entity) {
		Menu old = getEntityById(entity.getId());
		List<Menu> child = null;
		String oldLongNumber = old.getLongNumber();
		//if(!old.getNumber().equals(entity.getNumber())){
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("parent", entity.getId());
			child = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
		//}
		entity.setLongNumber(entity.getNumber());
		entity.setLevel(1);
		entity.setLeaf(true);
		if(entity.getParent()!=null &&  StringUtils.isNotNull(entity.getParent().getId())){
			Menu parent = getEntityById(entity.getParent().getId());
			entity.setLongNumber(parent.getLongNumber() + "!" + entity.getNumber());
			entity.setLevel(parent.getLevel() + 1);
			parent.setLeaf(false);
			
			//entity.setMenuType(parent.getMenuType()); 菜单属性由页面指定
			super.updateEntity(parent);
		}
		if(child != null && child.size() > 0){
			for(Menu m : child){
				m.setModuleType(entity.getModuleType());//设置关联模块枚举
				m.setLongNumber(m.getLongNumber().replaceFirst(oldLongNumber, entity.getLongNumber()));
			}
			menuDao.updateBatch(child);
		}
		super.updateEntity(entity);
	}

	@Override
	public List<Menu> getMenuList(Map<String, Object> map) {	
		return this.menuDao.getMenuList(map);
	}
}
