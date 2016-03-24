package com.wuyizhiye.basedata.access.controller;


import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.access.enums.AccessTypeEnum;
import com.wuyizhiye.basedata.access.enums.TerminalTypeEnum;
import com.wuyizhiye.basedata.access.model.MacAddress;
import com.wuyizhiye.basedata.access.service.MacAddressService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName MacAddressListController
 * @Description Mac地址列表
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/access/address/*")
public class MacAddressListController extends ListController {
	@Autowired
	private MacAddressService macAddressService;
	@Override
	protected CoreEntity createNewEntity() {
		MacAddress mac = new MacAddress();
		mac.setPerson(SystemUtil.getCurrentUser());
		mac.setOrg(SystemUtil.getCurrentOrg());
		return mac;
	}

	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		//权限类型
		config.registerJsonValueProcessor(AccessTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof AccessTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((AccessTypeEnum)value).getName());
					json.put("value", ((AccessTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof AccessTypeEnum){
					return ((AccessTypeEnum)value).getName();
				}
				return null;
			}
		});
		//终端设备类型
		config.registerJsonValueProcessor(TerminalTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof TerminalTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((TerminalTypeEnum)value).getName());
					json.put("value", ((TerminalTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof TerminalTypeEnum){
					return ((TerminalTypeEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	@Override
	protected String getListView() {
		return "basedata/access/macAddressList";
	}

	@Override
	protected String getEditView() {
		this.getRequest().setAttribute("accessType", AccessTypeEnum.values());//权限类型
		this.getRequest().setAttribute("terminalType", TerminalTypeEnum.values());//终端设备类型
		return "basedata/access/macAddressEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.access.dao.MacAddressDao.select";
	}

	@Override
	protected MacAddressService getService() {
		return macAddressService;
	}
	
	@RequestMapping(value="enable",method=RequestMethod.POST)
	public void enable(@RequestParam(value="id")String id,@RequestParam(value="enable")String enable,HttpServletResponse response){
		MacAddress address = getService().getEntityById(id);
		if(address != null){
			address.setEnable("1".equals(enable)?true:false);
			getService().updateEntity(address);
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}

}
