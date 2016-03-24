package com.wuyizhiye.hr.utils;

import java.util.Date;

import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.hr.affair.model.HrBillBase;

public class BaseUtils {

	/**
	 *  设置对象基本属性
	 * @param obj
	 * @param isUpdate
	 */
	public static void setWho(HrBillBase obj,boolean isUpdate){
		try{
		Date now = new Date();
		if(!isUpdate){
			obj.setCreateTime(now);
			obj.setCreateName(SystemUtil.getCurrentUser().getName());
			obj.setCreator(SystemUtil.getCurrentUser());
		}
		obj.setControlUnit(SystemUtil.getCurrentControlUnit());
		obj.setOrg(SystemUtil.getCurrentOrg());
		obj.setCreateOrgName(SystemUtil.getCurrentOrg().getName());
		obj.setCreatePositionName(SystemUtil.getCurrentPosition().getName());
		obj.setLastUpdateTime(now);
		obj.setUpdator(SystemUtil.getCurrentUser());
		}catch(Exception e){
			
		}
	}
}
