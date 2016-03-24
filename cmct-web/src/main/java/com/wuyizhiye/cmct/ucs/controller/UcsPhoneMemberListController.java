package com.wuyizhiye.cmct.ucs.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.cmct.phone.enums.PhoneEnableEnum;
import com.wuyizhiye.cmct.ucs.enums.UcsPhoneFlagEnum;
import com.wuyizhiye.cmct.ucs.enums.UcsPhoneSortEnum;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneAgent;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneAgentService;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneMemberService;
import com.wuyizhiye.cmct.ucs.util.UcsPhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName UcsPhoneMemberListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneMember/*")
public class UcsPhoneMemberListController extends ListController {

	@Autowired
	private UcsPhoneMemberService ucsPhoneMemberService;
	
	@Autowired
	private UcsPhoneAgentService ucsPhoneAgentService;
	@Autowired
	private PersonService personService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		// TODO Auto-generated method stub
		return "cmct/ucs/ucsPhoneMemberList";
	}

	@Override
	protected String getEditView() {
		this.getRequest().setAttribute("phoneSorts",UcsPhoneSortEnum.values());//电信号码类别
		return "cmct/ucs/ucsPhoneMemberEdit";
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao.select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return ucsPhoneMemberService;
	}
	
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();

		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		
		//坐席的类别
		config.registerJsonValueProcessor(UcsPhoneSortEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UcsPhoneSortEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((UcsPhoneSortEnum)value).getName());
					json.put("value", ((UcsPhoneSortEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UcsPhoneSortEnum){
					return ((UcsPhoneSortEnum)value).getName();
				}
				return null;
			}
		});
		//坐席状态
		config.registerJsonValueProcessor(UcsPhoneFlagEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UcsPhoneFlagEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((UcsPhoneFlagEnum)value).getName());
					json.put("value", ((UcsPhoneFlagEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof UcsPhoneFlagEnum){
					return ((UcsPhoneFlagEnum)value).getName();
				}
				return null;
			}
		});
		
		config.registerJsonValueProcessor(CommonFlagEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof CommonFlagEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((CommonFlagEnum)value).getLabel());
					json.put("value", ((CommonFlagEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof CommonFlagEnum){
					return ((CommonFlagEnum)value).getLabel();
				}
				return null;
			}
		});
		return config;
	}
	
	/**
	 * 坐席示忙
	 */
	@RequestMapping(value="onLine")
	public void onLine(HttpServletResponse response){
		String phoneId=getString("phoneId");
		if(!StringUtils.isEmpty(phoneId)){
			UcsPhoneMember ucs=ucsPhoneMemberService.getEntityById(phoneId);
			if(null!=ucs){
				/**
				 * 设置加密key 为特殊字符+客服电话号码
				 */
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+ucs.getTelNo()).trim()).toLowerCase();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				ucs.setKey(key);
				Map<String, Object>result=UcsPhoneMemberUtil.ucsPhoneUrl(ucs, UcsPhoneMemberUtil.UCSPHONEBUSY_URL);
				if("SUCCESS".equals(result.get("STATE").toString())){
					ucs.setState(UcsPhoneFlagEnum.BUSY);
					ucs.setLastUpdateTime(new Date());
					ucs.setUpdator(getCurrentUser());
					ucsPhoneMemberService.updateEntity(ucs);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "示忙成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSPHONEBUSY_URL,(String)result.get("MSG")));
				}
			}else{
				getOutputMsg().put("STATE", "FAIl");
				getOutputMsg().put("MSG", "暂无数据");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	/**
	 * 坐席示闲
	 */
	@RequestMapping(value="outLine")
	public void outLine(HttpServletResponse response){
		String phoneId=getString("phoneId");
		if(!StringUtils.isEmpty(phoneId)){
			UcsPhoneMember ucs=ucsPhoneMemberService.getEntityById(phoneId);
			if(null!=ucs){
				/**
				 * 设置加密key 为特殊字符+客服电话号码
				 */
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+ucs.getTelNo()).trim()).toLowerCase();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				ucs.setKey(key);
				Map<String, Object>result=UcsPhoneMemberUtil.ucsPhoneUrl(ucs, UcsPhoneMemberUtil.UCSPHONEFREE_URL);
				if("SUCCESS".equals(result.get("STATE").toString())){
					ucs.setState(UcsPhoneFlagEnum.FREE);
					ucs.setLastUpdateTime(new Date());
					ucs.setUpdator(getCurrentUser());
					ucsPhoneMemberService.updateEntity(ucs);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "示闲成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSPHONEFREE_URL,(String)result.get("MSG")));
				}
				
			}else{
				getOutputMsg().put("STATE", "FAIl");
				getOutputMsg().put("MSG", "暂无数据");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	/**
	 * 不掉接口,修改坐席已被释放
	 */
	@RequestMapping(value="outLineByLocal")
	public void outLineByLocal(HttpServletResponse response){
		String phoneId=getString("phoneId");
		if(!StringUtils.isEmpty(phoneId)){
			UcsPhoneMember ucs=ucsPhoneMemberService.getEntityById(phoneId);
			if(null!=ucs){
				ucs.setFlag(CommonFlagEnum.NO);
				ucs.setLastUpdateTime(new Date());
				ucs.setUpdator(getCurrentUser());
				ucs.setCurrentUser(null);
				ucsPhoneMemberService.updateEntity(ucs);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "释放成功");		
			}else{
				getOutputMsg().put("STATE", "FAIl");
				getOutputMsg().put("MSG", "暂无数据");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	/**
	 * 不掉接口,修改坐席已被占用
	 */
	@RequestMapping(value="onLineByLocal")
	public void onLineByLocal(HttpServletResponse response){
		String phoneId=getString("phoneId");
		if(!StringUtils.isEmpty(phoneId)){
			UcsPhoneMember ucs=ucsPhoneMemberService.getEntityById(phoneId);
			if(null!=ucs){
				ucs.setFlag(CommonFlagEnum.YES);
				ucs.setLastUpdateTime(new Date());
				ucs.setUpdator(getCurrentUser());
				ucs.setCurrentUser(getCurrentUser());
				ucsPhoneMemberService.updateEntity(ucs);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "占用成功");		
			}else{
				getOutputMsg().put("STATE", "FAIl");
				getOutputMsg().put("MSG", "暂无数据");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	
	/**
	 * 删除
	 */
	@Override
	public void delete(String id, HttpServletResponse response) {
		String key=getString("key");//特殊字符+坐席电话进行MD5加密
		UcsPhoneMember cs =this.ucsPhoneMemberService.getEntityById(id);
		if(cs != null){
			cs.setKey(key);
			//调用接口，删除话伴成员
			Map<String,Object> result = UcsPhoneMemberUtil.ucsPhoneUrl(cs,UcsPhoneMemberUtil.UCSPHONEDELETE_URL);
			if("SUCCESS".equals(result.get("STATE").toString())){
				ucsPhoneMemberService.deleteEntity(cs);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSPHONEDELETE_URL,(String)result.get("MSG")));
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 绑定用户
	 * @param response
	 */
	@RequestMapping(value="bindRight")
	public void bindRight(HttpServletResponse response){
		
		String personId = this.getString("personId");
		String curbindId = this.getString("curbindId");
		
		UcsPhoneMember member=this.ucsPhoneMemberService.getEntityById(curbindId);
		if(null!=member){
			member.setUpdator(getCurrentUser());
			member.setLastUpdateTime(new Date());
			member.setOnlyUser(personService.getEntityById(personId));
			member.setCurrentUser(personService.getEntityById(personId));
			member.setState(UcsPhoneFlagEnum.FREE);
			member.setFlag(CommonFlagEnum.YES);
			this.ucsPhoneMemberService.updateEntity(member);
			this.getOutputMsg().put("STATE", "SUCCESS");
			this.getOutputMsg().put("MSG", "保存成功");	
		}else{
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", "数据不存在");	
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * stopData  停用 启用
	 */
	@RequestMapping("stopData")
	@ResponseBody
	public void stopData(HttpServletResponse response) {
		String id=getString("id");
		String type=getString("type");
		UcsPhoneMember cs=this.ucsPhoneMemberService.getEntityById(id);
		if(cs != null){
			Map<String,Object> result = new HashMap<String,Object>();
			UcsPhoneAgent agent=ucsPhoneAgentService.getEntityById(cs.getUcsPhoneAgent().getId());
			
			if(agent !=null){
				cs.setUcsPhoneAgent(agent);
				
				//设置key:为特殊字符+用户电话 
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+cs.getTelNo()).trim()).toLowerCase();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				cs.setKey(key);
				if("stop".equals(type)){
					cs.setUserDisable(false);
					cs.setEnable(PhoneEnableEnum.STOP);//停用
				}else{
					cs.setUserDisable(true);
					cs.setEnable(PhoneEnableEnum.USE);//启用
				}
				result=UcsPhoneMemberUtil.ucsPhoneUrl(cs, UcsPhoneMemberUtil.UCSUSERDISABLE_URL);
				if("SUCCESS".equals(result.get("STATE").toString())){
					cs.setCancelDate(new Date());
					this.ucsPhoneMemberService.updateEntity(cs);
					getOutputMsg().put("id", cs.getId());
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "操作成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSPHONEADD_URL,(String)result.get("MSG")));
				}

			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "关联的agent为空");
			}
				
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
