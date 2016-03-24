package com.wuyizhiye.cmct.ucs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.wuyizhiye.cmct.ucs.model.UcsPhoneConfig;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneConfigService;
import com.wuyizhiye.cmct.ucs.util.UcsPhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName UcsPhoneConfigController
 * @Description ucs参数配置
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneconfig/*")
public class UcsPhoneConfigController extends BaseController {
	
	@Autowired
	private UcsPhoneConfigService ucsPhoneConfigService ;
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
		List<UcsPhoneConfig> configList = queryExecutor.execQuery(UcsPhoneConfig.MAPPER + ".select", null, UcsPhoneConfig.class);
		modelMap.put("configList", configList);
		return "cmct/ucs/ucsPhoneConfig" ;
	}
	
	/**
	 * 
	 */
	@RequestMapping(value="getphoneUrl")
	public void getphoneUrl(HttpServletResponse response){
		List<UcsPhoneConfig> configList = queryExecutor.execQuery(UcsPhoneConfig.MAPPER + ".select", null, UcsPhoneConfig.class);
		if(null!=configList && configList.size()>0){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("config", configList.get(0));
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	/**
	 * 保存配置
	 * @param response
	 */
	@RequestMapping(value="saveConfig")
	@Transactional
	public void saveConfig(HttpServletResponse response){
		String configJson = getString("configJson","");
		List<UcsPhoneConfig> configList = (List<UcsPhoneConfig>) JSONArray.toCollection(JSONArray.fromObject(configJson), UcsPhoneConfig.class);
		//先删除所有再新增
		queryExecutor.executeDelete(UcsPhoneConfig.MAPPER+".deleteAll", null);
		if(configList!=null && configList.size() > 0){
			ucsPhoneConfigService.addBatch(configList);
		}
		
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 拨打电话
	 */
	@RequestMapping(value="ucsCallUrl")
	public void ucsCallUrl(HttpServletResponse response){
		Map<String, Object>param=new HashMap<String, Object>();
		UcsPhoneMember member=new UcsPhoneMember();
		member.setTelNo( getString("userid"));
		member.setCallNo(getString("called"));
		member.setKey( getString("enckey"));
		/*member.setCallNo("15891748321");
		String key="";
		try {
			key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+member.getTelNo()+member.getCallNo()).trim()).toLowerCase();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		member.setKey(key);*/
		Map<String, Object> result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSCALL_URL);
		outPrint(response, JSONObject.fromObject(result, getDefaultJsonConfig()));
	}
	
	/**
	 * 挂断电话
	 */
	@RequestMapping(value="hookUrl")
	public void hookUrl(HttpServletResponse response){
		UcsPhoneMember member=new UcsPhoneMember();
		member.setTelNo( getString("phone"));
		member.setKey( getString("enckey"));
		Map<String, Object> result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSPHONEHANG_URL);
		outPrint(response, JSONObject.fromObject(result, getDefaultJsonConfig()));
	} 
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
	}
}
