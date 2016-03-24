/**
 * com.wuyizhiye.hr.salary.service.impl.SalaryStandardServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.wuyizhiye.hr.salary.dao.SalaryStandardDao;
import com.wuyizhiye.hr.salary.dao.StandardItemDao;
import com.wuyizhiye.hr.salary.model.SalaryStandard;
import com.wuyizhiye.hr.salary.model.StandardItem;
import com.wuyizhiye.hr.salary.service.SalaryStandardService;

/**
 * 组织service
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="salaryStandardService")
@Transactional
public class SalaryStandardServiceImpl extends BaseServiceImpl<SalaryStandard> implements SalaryStandardService {
	@Autowired
	private SalaryStandardDao salaryStandardDao;
	@Autowired
	private StandardItemDao standardItemDao;
	@Override
	protected BaseDao getDao() {
		return salaryStandardDao;
	}
	public void addEntity(SalaryStandard entity) {
		entity.setId(UUID.randomUUID().toString());
		String detailJson=entity.getDetailJson();
		if(!StringUtils.isEmpty(detailJson)){
			JSONArray array=JSONArray.fromObject(detailJson);
			List<StandardItem> alist=(List<StandardItem>) JSONArray.toCollection(array, StandardItem.class);
			for(StandardItem si:alist){
				si.setId(UUID.randomUUID().toString());
				si.setSalarayStandard(entity);
				standardItemDao.addEntity(si);
			}
		}
		super.addEntity(entity);
	}
	/**
	 * 数据更新
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateEntity(SalaryStandard entity) {
		/**
		 * 删除 明细  
		 */
		this.standardItemDao.deleteById(entity.getId());
		String detailJson=entity.getDetailJson();
		if(!StringUtils.isEmpty(detailJson)){
			JSONArray array=JSONArray.fromObject(detailJson);
			List<StandardItem> alist=(List<StandardItem>) JSONArray.toCollection(array, StandardItem.class);
			for(StandardItem si:alist){
				si.setId(UUID.randomUUID().toString());
				si.setSalarayStandard(entity);
				standardItemDao.addEntity(si);
			}
		}
		super.updateEntity(entity);
	}
	@Override
	public SalaryStandard getEntityById(String id) {
		SalaryStandard si=super.getEntityById(id);
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("salarayStandardId", id);
		List<StandardItem> alist=queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.StandardItemDao.select", param, StandardItem.class);
		si.setMinLength(alist.size());
		si.setStandardItemList(alist);
		return si;
	}
}
