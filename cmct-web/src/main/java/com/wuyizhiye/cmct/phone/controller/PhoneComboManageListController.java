package com.wuyizhiye.cmct.phone.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhoneStateEnum;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneComboManageListController
 * @Description 线路管理--套餐管理
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneComboManage/*")
public class PhoneComboManageListController extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		put("currentOrg",SystemUtil.getCurrentOrg());
		put("stateList",PhoneStateEnum.values());
		put("today",new Date());
		return "cmct/phone/phoneComboManageList";
	}

	public void put(String key,Object obj){
		this.getRequest().setAttribute(key, obj);
	}
	
	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		return PhoneMember.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
