/**
 * com.wuyizhiye.hr.salary.service.impl.SalarySchemeServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalarySchemeDao;
import com.wuyizhiye.hr.salary.model.SalaryScheme;
import com.wuyizhiye.hr.salary.model.SalarySchemeItem;
import com.wuyizhiye.hr.salary.model.SalarySchemeObject;
import com.wuyizhiye.hr.salary.service.SalarySchemeItemService;
import com.wuyizhiye.hr.salary.service.SalarySchemeObjectService;
import com.wuyizhiye.hr.salary.service.SalarySchemeService;

/**
 * 薪酬方案service
 * @author hlz
 * 
 * @since 2014-02-13
 */
@Component(value="salarySchemeService")
@Transactional
public class SalarySchemeServiceImpl extends BaseServiceImpl<SalaryScheme> implements SalarySchemeService {
	@Autowired
	private SalarySchemeDao salarySchemeDao;
	@Autowired
	private SalarySchemeObjectService salarySchemeObjectService;
	@Autowired
	private SalarySchemeItemService salarySchemeItemService;
	@Override
	protected BaseDao getDao() {
		return salarySchemeDao;
	}	
	
	@Override
	public void addEntity(SalaryScheme entity) {
		getDao().addEntity(entity);
		List<SalarySchemeObject> objectList = entity.getSalarySchemeObjectList();
		for(SalarySchemeObject obj : objectList){
			obj.setSalaryScheme(entity);
			salarySchemeObjectService.addEntity(obj);
		}
		List<SalarySchemeItem> itemList = entity.getSalarySchemeItem();
		for(SalarySchemeItem item : itemList){
			item.setSalaryScheme(entity);
			salarySchemeItemService.addEntity(item);
		}
	}
	
	@Override
	public void updateEntity(SalaryScheme entity) {
		getDao().updateEntity(entity);
		queryExecutor.executeDelete("com.wuyizhiye.hr.salary.dao.SalarySchemeObjectDao.deleteBySchemeId", entity.getId());
		queryExecutor.executeDelete("com.wuyizhiye.hr.salary.dao.SalarySchemeItemDao.deleteBySchemeId", entity.getId());
		List<SalarySchemeObject> objectList = entity.getSalarySchemeObjectList();
		for(SalarySchemeObject obj : objectList){
			obj.setSalaryScheme(entity);
			salarySchemeObjectService.addEntity(obj);
		}
		List<SalarySchemeItem> itemList = entity.getSalarySchemeItem();
		for(SalarySchemeItem item : itemList){
			item.setSalaryScheme(entity);
			salarySchemeItemService.addEntity(item);
		}
	}
}
