package com.wuyizhiye.cmct.wechat.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.wechat.model.WechatCommunication;

/**
 * @ClassName WechatSendUtil
 * @Description 微信发送
 * @author li.biao
 * @date 2015-5-26
 */
public class WechatSendUtil {
	
	private static QueryExecutor queryExecutor ;
	
	public static Map<String, String> sendWechat(WechatCommunication wechat) throws UnsupportedEncodingException, InstantiationException, IllegalAccessException{
		Map<String, String>resultMap=new HashMap<String, String>();
		Map<String, Object>param=new HashMap<String, Object>();
		String type="";
		String picUrl="";
		if("TEXT".equals(wechat.getType().getValue())){
			type="text";
		}else{
			picUrl=SystemUtil.getBase()+"/images/"+wechat.getPicUrl().replace("size", "origin");
			type="news";
		}
		if(queryExecutor==null){
			queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
		}
		String recivePersonArr[]=wechat.getPersonIds().split(";");
		Integer succCount=0;
		Person person=null;
		
		String wechatContent=wechat.getContent();
		
		String regEx_html = "<[^>]+>";
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
		Matcher m_html = p_html.matcher(wechatContent);  
		wechatContent = m_html.replaceAll("");//过滤html标签  
		p_html = Pattern.compile("&[^;]+;", Pattern.CASE_INSENSITIVE);
		m_html = p_html.matcher(wechatContent);  
		wechatContent = m_html.replaceAll("");//过滤html标签
		
		if(!StringUtils.isEmpty(wechatContent) && wechatContent.length()>25){
			wechatContent=wechatContent.substring(0,35)+"...";
		}
		for(String personId:recivePersonArr){
			param.put("id", personId);
			person=queryExecutor.execOneEntity(Person.MAPPER+".select", param, Person.class);	
			String resStr="";
			wechat.setUUID();
			if("news".equals(type)){				
				resStr=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp="+type+"&m_opiden="+ person.getNumber()+"&m_title="+URLEncoder.encode(wechat.getTitle(),"utf-8")+"&m_module=zhushou&m_tourl="+URLEncoder.encode(SystemUtil.getBase()+"/weixinapi/communication/view?id="+wechat.getId()+"&dataCenter="+DataSourceHolder.getDataSource()+"&uid="+person.getId(),"utf-8")+"&m_contten="+URLEncoder.encode(wechatContent+System.getProperty("line.separator")+"(发送人:"+SystemUtil.getCurrentUser().getName()+")","utf-8")+"&m_picurl="+URLEncoder.encode(picUrl,"utf-8"), "");
			}else{
				resStr=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp="+type+"&m_opiden="+ person.getNumber()+"&m_module=zhushou&m_contten="+URLEncoder.encode(wechatContent+System.getProperty("line.separator")+"(发送人:"+SystemUtil.getCurrentUser().getName()+")","utf-8"), "");
			}
			if(!StringUtils.isEmpty(resStr) && resStr.indexOf("STATE")>=0){
				JSONObject jsonObj=JSONObject.fromObject(resStr);
				if("SUCCESS".equals(jsonObj.getString("STATE"))){
					wechat.setReceivePerson(person);
					queryExecutor.executeInsert(WechatCommunication.MAPPER+".insert", wechat);
					succCount+=1;
				}
			}
		}
		
		if(succCount==0){
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "发送失败");	
		}else if(recivePersonArr.length==succCount.intValue()){
			resultMap.put("STATE", "SUCCESS");
			resultMap.put("MSG", "发送成功");
		}else{
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "成功:"+succCount+"条--失败:"+(recivePersonArr.length-succCount.intValue())+"条");
		}
		return resultMap;
	}
	
	public static Map<String, String> sendWechatInfo(Map<String,String> param)  throws UnsupportedEncodingException, InstantiationException, IllegalAccessException{
		Map<String, String>resultMap=new HashMap<String, String>();
		String type=param.get("m_teyp");
		String m_opiden = param.get("m_opiden");
		String m_module = param.get("m_module");
		String m_title = param.get("m_title");
		String m_tourl = param.get("m_tourl");
		String m_picurl = param.get("m_picurl");
		String m_contten= param.get("m_contten");
		String resStr = "";
			if("news".equals(type)){				
				resStr=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()
						+"/weixinapi/sendMessage?m_teyp="+type+(StringUtils.isNotNull(m_picurl)?("&m_picurl="+URLEncoder.encode(m_picurl,"utf-8")):"")+"&m_opiden="+ m_opiden+"&m_title="+URLEncoder.encode(m_title,"utf-8")+"&m_module="+m_module+(StringUtils.isNotNull(m_tourl)?("&m_tourl="+URLEncoder.encode(m_tourl,"utf-8")):"")+"&m_contten="+URLEncoder.encode(m_contten, "utf-8"),"");
			}else{
				resStr=HttpClientUtil.callHttpsUrl(SystemUtil.getBase()
						+"/weixinapi/sendMessage?m_teyp="+type+"&m_opiden="+ m_opiden+"&m_module="+m_module+"&m_contten="+URLEncoder.encode(m_contten,"utf-8"), "");
			}
			if(!StringUtils.isEmpty(resStr) && resStr.indexOf("STATE")>=0){
				JSONObject jsonObj=JSONObject.fromObject(resStr);
				if("SUCCESS".equals(jsonObj.getString("STATE"))){
					resultMap.put("STATE", "SUCCESS");
					resultMap.put("MSG", "发送成功");				
				}else{
					resultMap.put("STATE", "FAIL");
					resultMap.put("MSG", jsonObj.getString("MSG"));	
				}
			}else{
				resultMap.put("STATE", "FAIL");
				resultMap.put("MSG", "发送失败");	
			}
		
		return resultMap;
	}
}
