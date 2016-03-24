package com.wuyizhiye.basedata.service.impl;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.BillEntity;

/**
 * @ClassName BillEntityService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 * @param <T>
 */
public abstract class BillEntityService<T extends BillEntity> extends DataEntityService<T> {
	
	public void submit(T bill){
		String processName = bill.getProcessName();
		if(!StringUtils.isEmpty(processName)){
			//TODO
		}
	}
	
	public void saveAndSubmit(T bill){
		if(StringUtils.isEmpty(bill.getId())){
			super.addEntity(bill);
		}else{
			super.updateEntity(bill);
		}
		submit(bill);
	}
}
