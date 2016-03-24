package com.wuyizhiye.cmct.wechat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.model.BaseConfig;
import com.wuyizhiye.basedata.basic.service.BaseConfigService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.LoginHolder;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.wechat.model.WechatPromote;
import com.wuyizhiye.cmct.wechat.service.WechatPromoteService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName WechatPromoteListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/wechatPromote/*")
public class WechatPromoteListController extends ListController {

	@Autowired
	private WechatPromoteService wechatPromoteService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private BaseConfigService baseConfigService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new WechatPromote();
	}

	@Override
	protected String getListView() {
		List<BaseConfig>bcs=queryExecutor.execQuery("com.wuyizhiye.basedata.basic.dao.BaseConfigDao.select", null, BaseConfig.class);
		for(BaseConfig bs:bcs){
			if("gzPic".equals(bs.getNumber())){
				this.getRequest().setAttribute("gzPic", bs.getParamValue());
				continue;
			}
			if("dbPic".equals(bs.getNumber())){
				this.getRequest().setAttribute("dbPic", bs.getParamValue());
				continue;
			}
			if("gzhName".equals(bs.getNumber())){
				this.getRequest().setAttribute("gzhName", bs.getParamValue());
				continue;
			}
			if("gzhUrl".equals(bs.getNumber())){
				this.getRequest().setAttribute("gzhUrl", bs.getParamValue());
				continue;
			}
			if("zcUrl".equals(bs.getNumber())){
				this.getRequest().setAttribute("zcUrl", bs.getParamValue());
				continue;
			}
			if("qrUrl".equals(bs.getNumber())){
				this.getRequest().setAttribute("qrUrl", bs.getParamValue());
			}
		}
		
		return "cmct/wechat/wechatPromoteList";
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
		return config;
	}

	@Override
	protected String getEditView() {
		return "cmct/wechat/wechatPromoteEdit";
	}

	@Override
	protected String getListMapper() {
		return WechatPromote.MAPPER+".selectList";
	}

	@Override
	protected BaseService getService() {
		return wechatPromoteService;
	}
	
	@Override
	protected Map<String, Object> getListDataParam() {
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
        Map<String,Object> param=super.getListDataParam();
       
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
        return param;
        
	}
	
	@RequestMapping(value="viewDetail")
	public String viewDetail(){
		this.getRequest().setAttribute("currentOrg", SystemUtil.getCurrentOrg());
		return "cmct/wechat/wechatPromoteView";
	}
	
	@RequestMapping(value="listViewData")
	public void listViewData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.cmct.wechat.dao.WechatAmountDao.selectPersonRank", pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="sendWxMessage")
	public void sendWxMessage(HttpServletResponse response) throws UnsupportedEncodingException{
		String personId=getString("personId");
		String dataId=getString("dataId");
		String type=getString("type","");
		if(!StringUtils.isEmpty(dataId)){
			WechatPromote wechatPromote=this.wechatPromoteService.getEntityById(dataId);
			String wxContent="";	
			String picUrl="";
			if(StringUtils.isEmpty(wechatPromote.getPicUrl())){
				picUrl=getBasePath()+"/default/style/images/wxNoticeDefault.jpg";
			}else{
				picUrl=getBasePath()+"/images/"+wechatPromote.getPicUrl().replace("size", "origin");
			}
			String regEx_html = "<[^>]+>";
			Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
			Matcher m_html = p_html.matcher(wechatPromote.getContent());  
			wxContent = m_html.replaceAll("");//过滤html标签  
			p_html = Pattern.compile("&[^;]+;", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(wxContent);  
			wxContent = m_html.replaceAll("");//过滤html标签
			if(StringUtils.isNotNull(wxContent)){
				wxContent = wxContent.length()>100?wxContent.substring(0, 100):wxContent;
			}
			String result="";
			if(!StringUtils.isEmpty(personId)){		
				Person person=personService.getEntityById(personId);
				result=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp=news&m_opiden="+ person.getNumber() +"&m_picurl="+URLEncoder.encode(picUrl,"utf-8")+"&m_title="+URLEncoder.encode(wechatPromote.getTitle(),"utf-8")+"&m_module=promote&m_tourl="+URLEncoder.encode(getBasePath()+"/weixinapi/promote/view?id="+wechatPromote.getId()+"&dataCenter="+DataSourceHolder.getDataSource()+"&uid="+person.getId(),"utf-8")+"&m_contten="+URLEncoder.encode(wxContent,"utf-8"), "");
			}else{
				//取系统所有人,发送
				Map<String, Object>param=new HashMap<String, Object>();
				param.put("jobStatusIn", "'ONDUTY','RUNDISK','PROBATION'");
				List<Person>ps=queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrg", param, Person.class);
				for(Person person:ps){
					//result=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp=news&m_opiden="+ person.getNumber() +"&picurl="+URLEncoder.encode(picUrl,"utf-8")+"&m_title="+URLEncoder.encode(wechatPromote.getTitle(),"utf-8")+"&m_module=promote&m_tourl="+URLEncoder.encode(getBasePath()+"/mobile/promote/view?id="+wechatPromote.getId()+"&uid="+person.getId(),"utf-8")+"&m_contten="+URLEncoder.encode(wxContent,"utf-8"), "");
					result=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp=news&m_opiden="+ person.getNumber() +"&m_picurl="+URLEncoder.encode(picUrl,"utf-8")+"&m_title="+URLEncoder.encode(wechatPromote.getTitle(),"utf-8")+"&m_module=promote&m_tourl="+URLEncoder.encode(getBasePath()+"/weixinapi/promote/view?id="+wechatPromote.getId()+"&dataCenter="+DataSourceHolder.getDataSource()+"&uid="+person.getId(),"utf-8")+"&m_contten="+URLEncoder.encode(wxContent,"utf-8"), "");

				}
				//循环发默认是成功的
				result="{\"MSG\":\"发送成功\",\"STATE\":\"SUCCESS\"}";
			}
			if(!StringUtils.isEmpty(result) && result.indexOf("STATE")>=0){
				JSONObject jsonObj=JSONObject.fromObject(result);
				if("SUCCESS".equals(jsonObj.getString("STATE"))){
					if("publish".equals(type)){//更新数据
						wechatPromote.setIsPublish("Y");
						wechatPromote.setLastUpdateTime(new Date());
						wechatPromote.setUpdator(getCurrentUser());
						this.wechatPromoteService.updateEntity(wechatPromote);
					}
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "发送成功");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", jsonObj.getString("MSG"));
				}
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "返回数据为空,发送失败");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "无数据");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="saveWechatConfig")
	public void saveWechatConfig(HttpServletResponse response){
		String gzPic=getString("gzPic");
		String dbPic=getString("dbPic");
		String gzhName=getString("gzhName");
		String gzhUrl=getString("gzhUrl");
		String zcUrl=getString("zcUrl");
		String qrUrl = getString("qrUrl");
		
		List<BaseConfig>  bclist = new ArrayList<BaseConfig>();
		BaseConfig gzPicb = new BaseConfig("gzPic","关注图片",gzPic);
		BaseConfig dbPicb = new BaseConfig("dbPic","底部图片",dbPic);
		BaseConfig gzhNameb = new BaseConfig("gzhName","公众号名称",gzhName);
		BaseConfig gzhUrlb = new BaseConfig("gzhUrl","公众号地址",gzhUrl);
		BaseConfig zcUrlb = new BaseConfig("zcUrl","注册地址",zcUrl);
		BaseConfig qrUrlb= new BaseConfig("qrUrl","二维码请求地址",qrUrl);
		bclist.add(gzPicb);
		bclist.add(dbPicb);
		bclist.add(gzhNameb);
		bclist.add(gzhUrlb);
		bclist.add(zcUrlb);
		bclist.add(qrUrlb);
		this.baseConfigService.updateList(bclist);
		if(null==LoginHolder.getCurrBaseConfig()){
			LoginHolder.getCurrBaseConfig().put("gzPic", gzPic);
			LoginHolder.getCurrBaseConfig().put("dbPic", dbPic);
			LoginHolder.getCurrBaseConfig().put("gzhName", gzhName);
			LoginHolder.getCurrBaseConfig().put("gzhUrl", gzhUrl);
			LoginHolder.getCurrBaseConfig().put("zcUrl", zcUrl);
			LoginHolder.getCurrBaseConfig().put("qrUrl", qrUrl);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="getEachData")
	public void getEachData(HttpServletResponse response){
		Map map=queryExecutor.execOneEntity("com.wuyizhiye.cmct.wechat.dao.WechatAmountDao.getEachData", super.getListDataParam(), Map.class);
		outPrint(response, JSONObject.fromObject(map, getDefaultJsonConfig()));
	}
}
