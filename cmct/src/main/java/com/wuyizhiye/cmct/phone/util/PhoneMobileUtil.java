package com.wuyizhiye.cmct.phone.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.constant.FjCtCmctConstant;
import com.wuyizhiye.cmct.phone.model.PhoneDJCostMember;
import com.wuyizhiye.cmct.phone.model.PhonemMobileMember;

/**
 * @ClassName PhoneMobileUtil
 * @Description 获取鼎尖上的计费号码
 * @author li.biao
 * @date 2015-5-26
 */
@Component
@Lazy(value=false)
public class PhoneMobileUtil {

	public static final String TIP_KEY_STATE = "STATE" ;
	public static final String TIP_KEY_MSG = "MSG" ;
	public static final String TIP_KEY_SUCC = "SUCCESS" ;
	public static final String TIP_KEY_FAIL= "FAIL" ;
	public static final String TIP_VALUE_EXCEPTION = "EXCEPTION" ;
	
	
	private static QueryExecutor queryExecutor ;
	
	public volatile static ConcurrentHashMap<String,PhoneDJCostMember> dingjianMember;//存取计费号码
	
	public volatile static ConcurrentHashMap<String,PhonemMobileMember> mobileMember;//存取手机版号码库以及电话号码状态,
	
	static{
		if(queryExecutor==null){
			queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
		}
		if(dingjianMember == null){
			dingjianMember = new ConcurrentHashMap<String,PhoneDJCostMember>();
		}
		if(mobileMember==null){
			mobileMember= new ConcurrentHashMap<String,PhonemMobileMember>(); 
		}
	}
	
	//获取计费号码
	public static PhoneDJCostMember getDingJianCostMember(){
		if(dingjianMember.containsKey(FjCtCmctConstant.memberKey) && null!=dingjianMember.get(FjCtCmctConstant.memberKey)){
			return dingjianMember.get(FjCtCmctConstant.memberKey);
		}else{
			Map<String, String>param=new HashMap<String, String>();
			param.put("customerId", ParamUtils.getParamValueByNumber(PhoneMemberUtil.CMCT_CUSOMETID));
			param.put("isCostNumber", "YES");
			param.put("phoneType", "HW");
			Map<String, Object>resMap=ProjectMApiRemoteServer.getDingJianCostMember(param);
			PhoneDJCostMember member=null;
			if(null!=resMap && "SUCCESS".equals(resMap.get("STATE"))){
				JSONArray jsonArr=JSONArray.fromObject(resMap.get("phones"));
				JSONObject jsonObj1=jsonArr.getJSONObject(0);
				member=new PhoneDJCostMember();
				member.setUserId(jsonObj1.getString("USERID"));
				member.setUserKey(jsonObj1.getString("USERKEY"));
				member.setAppid(jsonObj1.getString("APPID"));
				member.setHttpUrl(jsonObj1.getString("HTTPURL"));
				member.setSpid(jsonObj1.getString("SPID"));
				member.setPassWd(jsonObj1.getString("PASSWD"));
				dingjianMember.put(FjCtCmctConstant.memberKey, member);
			}
			return member;
		}
	}
	
	/**
	 * 系统启动时.将手机线路的数据加载到mobileMember内存之中.....
	 */
	@PostConstruct
	public static void getMobileMemeber(){
		try{
			List<String> dslist = SystemUtil.getDataSourceSingleList();
			for(int j=0;j<dslist.size();j++){
				DataSourceHolder.setDataSource(dslist.get(j));				
				List<PhonemMobileMember>mobiles=queryExecutor.execQuery(PhonemMobileMember.MAPPER+".select", null, PhonemMobileMember.class);
				if(null!=mobiles && mobiles.size()>0){				
					for(PhonemMobileMember m:mobiles){
						mobileMember.put(m.getPhoneNum(), m);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//判断当前登录人的号码是否存在号码库并且号码是开通状态
	public static Map<String, Object> judgeMobileMemberStatus(){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		Person person=SystemUtil.getCurrentUser();
		if(null==person){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "你还未登录系统");
			return msgRst;
		}
		return judgeMobileMemberStatus(person.getId());
	}
	
	public static Map<String, Object> judgeMobileMemberStatus(String personId){
		Map<String,Object> msgRst = new HashMap<String,Object>();
		if(StringUtils.isEmpty(personId)){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "未找到系统人员");
			return msgRst;
		}
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("id", personId);
		Person person=queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.select", param, Person.class);
		if(null==person){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "未找到系统人员");
			return msgRst;
		}
		if(StringUtils.isEmpty(person.getPhone())){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "你所在的系统的电话号码为空,请在系统修改你的个人号码");
			return msgRst;
		}
		if(mobileMember.size()==0){
			putMobileMember();
		}
		if(!mobileMember.containsKey(person.getPhone())){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "当前系统没有您的电话,请联系管理员维护你的手机号码");
			return msgRst;
		}
		PhonemMobileMember member=mobileMember.get(person.getPhone());
		if("STOP".equals(member.getStatus().getValue())){
			msgRst.put(TIP_KEY_STATE, TIP_KEY_FAIL);
			msgRst.put(TIP_KEY_MSG, "你的手机号码未开通,请联系管理员开通你的号码");
			return msgRst;
		}
		msgRst.put(TIP_KEY_STATE, TIP_KEY_SUCC);
		msgRst.put("phone", person.getPhone());
		return msgRst;
	}
	
	public static boolean judgeMobile(){
		return false;
	}
	
	//存放mobileMember
	public static void putMobileMember(PhonemMobileMember member){
		mobileMember.put(member.getPhoneNum(), member);
	}
	
	//清除mobileMember
	public static void removeMobileMember(PhonemMobileMember member){
		mobileMember.remove(member.getPhoneNum(), member);
	}
	
	private static void putMobileMember(){
		List<PhonemMobileMember>mobiles=queryExecutor.execQuery(PhonemMobileMember.MAPPER+".select", null, PhonemMobileMember.class);
		if(null!=mobiles && mobiles.size()>0){			
			for(PhonemMobileMember m:mobiles){
				mobileMember.put(m.getPhoneNum(), m);
			}
		}
	}
}
