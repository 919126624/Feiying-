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
import com.wuyizhiye.cmct.phone.model.PhoneMarketBindPerson;
import com.wuyizhiye.cmct.phone.service.PhoneMarketBindPersonService;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneMarketBindPersonListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMb/*")
public class PhoneMarketBindPersonListController extends ListController {

	@Autowired
	private PhoneMarketBindPersonService phoneMarketBindPersonService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new PhoneMarketBindPerson();
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
		return phoneMarketBindPersonService;
	}

	@RequestMapping(value="marketBind")
	public String marketBind(){
		return "cmct/phone/phoneMbBind" ;
	}
	
	@RequestMapping(value="validationMarket")
	public void validationMarket(HttpServletResponse response){
		Map<String, Object>param=new HashMap<String, Object>();
		if(null!=getCurrentUser()){			
			param.put("personId", getCurrentUser().getId());
			List<PhoneMarketBindPerson>mas=queryExecutor.execQuery(PhoneMarketBindPerson.MAPPER+".select", param, PhoneMarketBindPerson.class);
			if(null!=mas && mas.size()>0){
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", mas.get(0));
			}else{
				getOutputMsg().put("STATE", "FAIl");
				getOutputMsg().put("MSG", "没有关联账号");
			}
		}else{
			getOutputMsg().put("STATE", "FAIl");
			getOutputMsg().put("MSG", "当前登录失效.请重新登录");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="validationNumber")
	public void validationNumber(HttpServletResponse response){
		Map<String, String>param=new HashMap<String, String>();
		String marketNumber=getString("marketNumber");
		String marketPassWord=getString("marketPassWord");	
		param.put("marketNumber", marketNumber);
		param.put("marketPassWord", marketPassWord);
		Map<String, Object>res=ProjectMApiRemoteServer.validationMarket(param);
		if(null!=res && "SUCCESS".equals(res.get("STATE"))){
			JSONObject jsonObj=JSONObject.fromObject(res.get("MSG"));
			PhoneMarketBindPerson bindPerson=new PhoneMarketBindPerson();
			bindPerson.setUUID();
			bindPerson.setDjMemberId(jsonObj.getString("MEMBERID"));
			bindPerson.setBindPerson(getCurrentUser());
			bindPerson.setMarketNumber(jsonObj.getString("MARKETNUMBER"));
			bindPerson.setMarketPassWord(jsonObj.getString("USERKEY"));
			bindPerson.setAppid(jsonObj.getString("APPID"));
			bindPerson.setAppKey(jsonObj.getString("APPKEY"));
			//bindPerson.setPassWd(jsonObj.getString("PASSWD"));
			bindPerson.setSpid(jsonObj.getString("SPID"));
			bindPerson.setHttpUrl(jsonObj.getString("HTTPURL"));
			this.phoneMarketBindPersonService.addEntity(bindPerson);
			getOutputMsg().put("MSG", bindPerson);
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			getOutputMsg().put("STATE", "FAIl");
			getOutputMsg().put("MSG", res.get("MSG"));
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="loadMarketAccount")
	public void loadMarketAccount(HttpServletResponse response){
		String djMemberId=getString("djMemberId");
		Map<String, String>param=new HashMap<String, String>();
		param.put("djMemberId", djMemberId);
		Map<String, Object>res=ProjectMApiRemoteServer.marketAccount(param);
		if(null!=res && "SUCCESS".equals(res.get("STATE"))){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("ACCOUNT", res.get("account"));
		}else{
			getOutputMsg().put("STATE", "FAIl");
			getOutputMsg().put("MSG", res.get("MSG"));
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
