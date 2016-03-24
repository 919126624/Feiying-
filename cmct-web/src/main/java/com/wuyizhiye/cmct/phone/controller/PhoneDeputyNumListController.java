package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.cmct.phone.model.PhoneDeputyNum;
import com.wuyizhiye.cmct.phone.service.PhoneDeputyNumService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneDeputyNumListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneDeputyNum/*")
public class PhoneDeputyNumListController extends ListController {
	
	@Autowired
	private PhoneDeputyNumService phoneDeputyNumService;
	
	@Override
	protected CoreEntity createNewEntity() {
		PhoneDeputyNum phoneDeputyNum=new PhoneDeputyNum();
		phoneDeputyNum.setBindPerson(getCurrentUser());
		return phoneDeputyNum;
	}

	@Override
	protected String getListView() {
		return "cmct/phone/phoneDeputyNumList";
	}

	@Override
	protected String getEditView() {
		return "cmct/phone/phoneDeputyNumEdit";
	}

	@Override
	protected String getListMapper() {
		return PhoneDeputyNum.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return phoneDeputyNumService;
	}

	@Override
	protected Map<String, Object> getListDataParam() {
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		Map<String,Object> param=super.getListDataParam();
		try{
			if(null!=param.get("startDate")){
				param.put("startDate", df.parse(param.get("startDate").toString()));
			}
			if(null!=param.get("endDate")){
				param.put("endDate", DateUtil.getNextDay(df.parse(param.get("endDate").toString())));
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return param;
	}
}
