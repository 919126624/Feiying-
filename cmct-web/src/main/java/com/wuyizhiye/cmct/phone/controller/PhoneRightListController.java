package com.wuyizhiye.cmct.phone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneRight;
import com.wuyizhiye.cmct.phone.service.PhoneRightService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneRightListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/phoneRight/*")
public class PhoneRightListController extends ListController {

	@Autowired
	private PhoneRightService phoneRightService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return "cmct/phone/phoneRightList";
	}

	@Override
	protected String getEditView() {
		List<PhoneConfig> configList = queryExecutor.execQuery(PhoneConfig.MAPPER + ".select", null, PhoneConfig.class);
		put("configList",configList);
		put("configFlag",configList != null && configList.size() == 1);
		return "cmct/phone/phoneRightEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.cmct.phone.dao.PhoneRightDao.select";
	}

	@Override
	protected BaseService getService() {
		return phoneRightService;
	}

	public void put(String key,Object obj){
		this.getRequest().setAttribute(key, obj);
	}
	
	@RequestMapping(value="setDefault")
	public void setDefault(HttpServletResponse response){
		
		String type = this.getString("type");
		String rightid = this.getString("rightid");
		try{
			PhoneRight pr = phoneRightService.getEntityById(rightid);
			if("setDefault".equals(type)){
				Map<String,Object> queryMap = new HashMap<String,Object>();
				queryMap.put("orgId", pr.getCallOrgId());
				queryMap.put("isDefault", CommonFlagEnum.YES);
				List<PhoneRight> dbList = queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneRightDao.select", queryMap, PhoneRight.class);
				if(dbList!=null && dbList.size() > 0){
					throw new Exception("该核算组织已有默认设置，请先取消默认设置！");
				}
			}
			phoneRightService.updateOrgDefaultRight(pr, type);
			
			this.getOutputMsg().put("STATE", "SUCCESS");
			this.getOutputMsg().put("MSG", "保存成功");	
		}catch(Exception e){
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", e.getMessage());	
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
