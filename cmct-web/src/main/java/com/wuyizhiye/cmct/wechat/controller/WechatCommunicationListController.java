package com.wuyizhiye.cmct.wechat.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.wechat.enums.WechatCommStatusEnum;
import com.wuyizhiye.cmct.wechat.model.WechatCommunication;
import com.wuyizhiye.cmct.wechat.service.WechatCommunicationService;
import com.wuyizhiye.cmct.wechat.util.WechatSendUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName WechatCommunicationListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/wechatCommunication/*")
public class WechatCommunicationListController extends ListController {

	@Autowired
	private WechatCommunicationService wechatCommunicationService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new WechatCommunication();
	}

	@Override
	protected String getListView() {
		this.getRequest().setAttribute("personFlag", getString("personFlag"));//是否根据当前人过滤
		this.getRequest().setAttribute("isShowpn", getString("isShowpn","Y"));//是否显示发送人
		return "cmct/wechat/wechatCommunicationList";
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return WechatCommunication.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return wechatCommunicationService;
	}

	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
		
		config.registerJsonValueProcessor(WechatCommStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof WechatCommStatusEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((WechatCommStatusEnum)value).getName());
					json.put("value", ((WechatCommStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof WechatCommStatusEnum){
					return ((WechatCommStatusEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	/**
	 * 发送微信总页面
	 */
	@RequestMapping("totalPage")
	public String totalPage(ModelMap model){
		return "cmct/wechat/wechatTotal";
	}
	
	/**
	 * 发送微信 详细页面 (可 发送 自己/同事/外界)  (首页 工具 点击进入 )
	 */
	@RequestMapping("detailPage")
	public String detailPage(ModelMap model){
		return "cmct/wechat/wechatDetail";
	}
	@RequestMapping("weiXinPage")
	public String weiXinPage(ModelMap model){
		String personId=getString("personId");
		if(personId!=null&!StringUtils.isEmpty(personId)){
			Map<String, Object> param=new HashMap<String, Object>();
			param.put("id", personId);
			Person person=queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.select", param, Person.class);
			if(person!=null){
				this.getRequest().setAttribute("person", person);
			}
		}
		return "cmct/wechat/weiXinDetail";
	}
	/**
	 * 选 人员
	 */
	@RequestMapping("getPersons")
	@ResponseBody
	public void getPersons(HttpServletResponse response){
		response.setContentType("text/html");
		String key  = getString("term");
		int maxRows  = getInt("maxRows");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("key", key);
		Pagination<Person> pagination=queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select",new Pagination<Person>(maxRows, 0),map);
		for(Person p:pagination.getItems()){
			p.setId(p.getId()+","+p.getNumber());
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 微信发送
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@RequestMapping(value="send")
	public void send(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String content=getString("content");
		String personIds=getString("personIds");
		String type=getString("type");
		String picUrl=getString("picUrl");
		String title=getString("title");
		
		WechatCommunication wechat=new WechatCommunication();
		wechat.setContent(content);
		wechat.setPersonIds(personIds);
		wechat.setType(WechatCommStatusEnum.valueOf(type));
		wechat.setPicUrl(picUrl);
		wechat.setTitle(title);
		wechat.setSendTime(new Date());
		wechat.setSendPerson(SystemUtil.getCurrentUser());
		try {
			Map<String, String>res=WechatSendUtil.sendWechat(wechat);
			getOutputMsg().put("STATE", res.get("STATE"));
			getOutputMsg().put("MSG", res.get("MSG"));
		} catch (UnsupportedEncodingException e) {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
			e.printStackTrace();
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@RequestMapping(value="turnSend")
	public void turnSend(@RequestParam(required=true,value="id")String id,HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String personId=getString("personId");
		WechatCommunication wechat=this.wechatCommunicationService.getEntityById(id);
		wechat.setId(null);
		wechat.setPersonIds(personId);
		try {
			Map<String, String>res=WechatSendUtil.sendWechat(wechat);
			getOutputMsg().put("STATE", res.get("STATE"));
			getOutputMsg().put("MSG", res.get("MSG"));
		} catch (UnsupportedEncodingException e) {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
			e.printStackTrace();
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String, Object>param=super.getListDataParam();
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
       
        if(null!=param.get("startDate") && !StringUtils.isEmpty(param.get("startDate").toString())){
        	try {
				param.put("startDate", df.parse(param.get("startDate").toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
        if(null!=param.get("endDate") && !StringUtils.isEmpty(param.get("endDate").toString())){
        	try {
				param.put("endDate", DateUtil.getNextDay(df.parse(param.get("endDate").toString())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
        }
		if(param.containsKey("personFlag") && "YES".equals(param.get("personFlag"))){
			param.put("sendPersonId", getCurrentUser().getId());
		}
		return param;
	}
	
	@RequestMapping(value="listBySearch")
	public String listBySearch(){
		this.getRequest().setAttribute("personFlag", getString("personFlag"));//是否根据当前人过滤
		this.getRequest().setAttribute("isShowpn", getString("isShowpn","Y"));//是否显示发送人
		return "cmct/wechat/wechatCommunicationList";
	}
}
