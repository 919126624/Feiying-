package com.wuyizhiye.cmct.phone.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.service.PhoneConfigService;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName PhoneConfigController
 * @Description 话务中心--话伴参数设置控制器
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneconfig/*")
public class PhoneConfigController extends BaseController {
	
	@Autowired
	private PhoneConfigService phoneConfigService ;
	
	/**
	 * 参数设置页面
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("index")
	public String index(ModelMap modelMap){
		if(StringUtils.isNotNull(getString("dc"))){
	    	DataSourceHolder.setDataSource(getString("dc"));
	    }
		List<PhoneConfig> configList = queryExecutor.execQuery(PhoneConfig.MAPPER + ".select", null, PhoneConfig.class);
		modelMap.put("configList", configList);
		return "cmct/phone/phoneConfig" ;
	}
	
	/**
	 * 保存配置
	 * @param response
	 */
	@RequestMapping(value="saveConfig")
	@Transactional
	public void saveConfig(HttpServletResponse response){
		String configJson = getString("configJson","");
		List<PhoneConfig> configList = (List<PhoneConfig>) JSONArray.toCollection(JSONArray.fromObject(configJson), PhoneConfig.class);
		//先删除所有再新增
		queryExecutor.executeDelete(PhoneConfig.MAPPER+".deleteAll", null);
		if(configList!=null && configList.size() > 0){
			phoneConfigService.addBatch(configList);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
}
