package com.wuyizhiye.basedata.access.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.access.service.MacAddressService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName MacAddressEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/access/address/*")
public class MacAddressEditController extends EditController {
	@Autowired
	private MacAddressService macAddressService;
	@Override
	protected Class<MacAddress> getSubmitClass() {
		return MacAddress.class;
	}

	@Override
	protected MacAddressService getService() {
		return macAddressService;
	}
	
	@Override
	protected Object getSubmitEntity() {
		MacAddress address = (MacAddress) super.getSubmitEntity();
		if(StringUtils.isEmpty(address.getId())){
			address.setCreateTime(new Date());
			address.setEnable(true);
		}
		return address;
	}
	
	@Override
	protected boolean validate(Object data) {
		MacAddress address = (MacAddress)data;
		Map<String,Object> param = new HashMap<String, Object>();
		List<MacAddress> addresses;
		param.put("mac", address.getMac());
		addresses = queryExecutor.execQuery("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param, MacAddress.class);
		for(MacAddress mac : addresses){
			if(!mac.getId().equals(address.getId())){
				getOutputMsg().put("MSG", "该识别码己存在");
				return false;
			}
		}
	/*	param.clear();
		param.put("ip", address.getIp());
		addresses = queryExecutor.execQuery("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param, MacAddress.class);
		for(MacAddress mac : addresses){
			if(!mac.getId().equals(address.getId())){
				getOutputMsg().put("MSG", "该IP地址己存在");
				return false;
			}
		}*/
		return super.validate(data);
	}
}
