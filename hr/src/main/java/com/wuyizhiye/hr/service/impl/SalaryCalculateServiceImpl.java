package com.wuyizhiye.hr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.hr.enums.SchemeObjectTypeEnum;
import com.wuyizhiye.hr.salary.dao.DurationDao;
import com.wuyizhiye.hr.salary.dao.SalarySchemeDao;
import com.wuyizhiye.hr.salary.model.Duration;
import com.wuyizhiye.hr.salary.model.SalaryScheme;
import com.wuyizhiye.hr.service.SalaryCalculateService;
/**
 * 薪酬计算
 * @author hlz
 * @since 2014-02-21
 */
@Component(value="salaryCalculateService")
public class SalaryCalculateServiceImpl implements SalaryCalculateService {
	
	@Autowired
	private SalarySchemeDao salarySchemeDao;
	@Autowired
	private DurationDao durationDao;
	
	public void calculate(String schemeId,String durationId){
		SalaryScheme salaryScheme = salarySchemeDao.getEntityById(schemeId);
		Duration duration = durationDao.getEntityById(durationId);
		if(SchemeObjectTypeEnum.COMPANY == salaryScheme.getObjectType()){
			
		}
	}
}
