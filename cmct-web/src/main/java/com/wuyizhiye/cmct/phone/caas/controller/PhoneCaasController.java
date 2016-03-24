package com.wuyizhiye.cmct.phone.caas.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.caas.util.OmpRestClient;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneCaasController
 * @Description 华为caas平台控制器
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCaas/*")
public class PhoneCaasController extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return null;
	}

	@Override
	protected BaseService getService() {
		return null;
	}

	@RequestMapping(value="dialPhone")
	public void dialPhone(HttpServletResponse response){
		try{
			String server=getString("server");
			String redirectUrl=getString("redirectUrl");
			String appKey=getString("appKey");
			String appSecret=getString("appSecret");
			String userId=getString("userId");
			String userKey=getString("userKey");
			String caller=getString("caller");
			String callee=getString("callee");
			if(StringUtils.isEmpty(server)){
				throw new Exception("环境地址为空");
			}
			if(StringUtils.isEmpty(appKey)){
				throw new Exception("应用密匙为空");
			}
			if(StringUtils.isEmpty(appSecret)){
				throw new Exception("无效参数");
			}
			if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(userKey)){
				throw new Exception("计费号码为空");
			}
			if(StringUtils.isEmpty(caller)){
				throw new Exception("主叫号码为空");
			}
			if(StringUtils.isEmpty(callee)){
				throw new Exception("被叫号码为空");
			}
			OmpRestClient client = new OmpRestClient(server, redirectUrl, appKey, appSecret);
			String token = client.getFastloginToken("+"+userId, userKey);
			Map<String, Object> result = client.triggerClick2call(token, conversionPhone(caller), conversionPhone(callee), userId);
			if(null!=result && "SUCCESS".equals(result.get("STATE"))){
				this.getOutputMsg().put("STATE", "SUCCESS");
				this.getOutputMsg().put("MSG", result.get("sessionId").toString());
				FjCtCmctMemberUtil.writeRecordDataBase(caller, callee, "", "", result.get("sessionId").toString(),"");
			}else{
				this.getOutputMsg().put("STATE", "FAIL");
				this.getOutputMsg().put("MSG", result.get("MSG"));	
			}
		}catch(Exception e){
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", e.getMessage());	
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	
	public String conversionPhone(String phone){
		if(null == phone || phone.length() < 1) {
			return phone;
		}
		phone = phone.replaceAll("[+]", "");
		phone = phone.replaceAll(" ", "");
		phone = phone.replaceAll("-", "");
		if(null != phone && phone.length() == 15 && phone.startsWith("00")) {
			phone = phone.substring(2);
		}
		if(null != phone && phone.length() == 11 && phone.startsWith("1")) {
			phone = "86" + phone;
		}
		return phone;
	}
}
