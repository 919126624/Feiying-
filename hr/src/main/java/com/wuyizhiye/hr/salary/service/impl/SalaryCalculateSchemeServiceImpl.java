/**
 * com.wuyizhiye.hr.salary.service.impl.SalaryCalculateSchemeServiceImpl.java
 */
package com.wuyizhiye.hr.salary.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.hr.salary.dao.SalaryCalculateItemDao;
import com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao;
import com.wuyizhiye.hr.salary.dao.SalaryCalculateSchemeDao;
import com.wuyizhiye.hr.salary.model.SalaryCalculateItem;
import com.wuyizhiye.hr.salary.model.SalaryCalculatePerson;
import com.wuyizhiye.hr.salary.model.SalaryCalculateScheme;
import com.wuyizhiye.hr.salary.service.SalaryCalculateSchemeService;

/**
 * 核算方案service
 * @author hlz
 * 
 * @since 2014-02-19
 */
@Component(value="salaryCalculateSchemeService")
@Transactional
public class SalaryCalculateSchemeServiceImpl extends BaseServiceImpl<SalaryCalculateScheme> implements SalaryCalculateSchemeService {
	@Autowired
	private SalaryCalculateSchemeDao salaryCalculateSchemeDao;
	@Autowired
	private SalaryCalculatePersonDao salaryCalculatePersonDao;
	@Autowired
	private SalaryCalculateItemDao salaryCalculateItemDao;
	@Override
	protected BaseDao getDao() {
		return salaryCalculateSchemeDao;
	}	
	
	@Override
	public void addEntity(SalaryCalculateScheme entity) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("durationId", entity.getDuration().getId());
		param.put("schemeId", entity.getSalaryScheme().getId());
		List<SalaryCalculateScheme> list = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryCalculateSchemeDao.select", param, SalaryCalculateScheme.class);
		if(list.size()==0){
			entity.setState("0");
			getDao().addEntity(entity);
		}else{
			entity.setId(list.get(0).getId());
		}
		for(SalaryCalculatePerson person : entity.getPersons()){
			param.clear();
			param.put("personId", person.getPerson().getId());
			param.put("calculateSchemeId", entity.getId());
			List<SalaryCalculatePerson> rePersonList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao.select", param, SalaryCalculatePerson.class);
			person.setCalculateDate(new Date());
			person.setSalaryCalculateScheme(entity);
			if(rePersonList.size()==0){
				person.setUUID();
				salaryCalculatePersonDao.addEntity(person);
			}else{
				person.setId(rePersonList.get(0).getId());
				salaryCalculatePersonDao.updateEntity(person);
			}
			
			for(SalaryCalculateItem item : person.getItems()){
				param.clear();
				param.put("calculatePersonId", person.getId());
				param.put("itemId", item.getSalaryItem().getId());
				List<SalaryCalculateItem> itemList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryCalculateItemDao.select", param, SalaryCalculateItem.class);
				item.setSalaryCalculatePerson(person);
				if(itemList.size()==0){
					item.setUUID();
					salaryCalculateItemDao.addEntity(item);
				}else{
					item.setId(itemList.get(0).getId());
					salaryCalculateItemDao.updateEntity(item);
				}
				
			}
		}
	}
}
