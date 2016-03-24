package com.wuyizhiye.cmct.sms.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.person.enums.PositionChangeTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CmctMessageReportController
 * @Description 短信统计
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/messageReport/*")
public class CmctMessageReportController extends ListController{
	
	@Autowired
	private OrgService orgService;

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		put("startDay",(df.format(new Date()).substring(0, 7)+"-01").replaceAll("-", "/"));
		put("endDay",df.format(new Date()).replaceAll("-", "/"));
		return "cmct/sms/shortMessageReport";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("getData")
	@ResponseBody
	public void getData(HttpServletResponse response){
		Map<String,Object> json=new HashMap<String, Object>();
		Map<String,Object> param=new HashMap<String, Object>();//参数
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		String orgId=getString("orgId");//组织ID
		boolean flag=false;//标示 是否 页面第一次 初始数据
		if(null == orgId || "".equals(orgId)){
			orgId=SystemUtil.getCurrentOrg().getId();
			flag=true;
		}
		String startTime=getString("startTime");
		String endTime=getString("endTime");
        	if(null != startTime && !"".equals(startTime)){
        		startTime=startTime.replaceAll("/", "-");
        	}
        	if(null != endTime && !"".equals(endTime)){
        		endTime=endTime.replaceAll("/", "-");
        	}
        try {
        	if(!"".equals(startTime)){
        		param.put("startTime", df.parse(startTime));
        	}
			if(!"".equals(endTime)){
				param.put("endTime", DateUtil.getNextDay(df.parse(endTime)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Org org=this.orgService.getEntityById(orgId);
		List<Map> result=new ArrayList<Map>();
		if(!flag){//第一次加载数据  求和 不需要读人员数据
			List<Map> plist=this.getPersonData(org, param);
			if(null != plist && plist.size()>0){
				result.addAll(plist);
			}
		}
		List<Map> olist=this.getOrgData(org, param,flag);
		if(null != olist && olist.size()>0){
			result.addAll(olist);
		}
		json.put("showList", result);
		outPrint(response,JSONObject.fromObject(json, getDefaultJsonConfig()));
	}
	
	/**
	 * 查找人员数据
	 * @param orgId
	 * @param start
	 * @param end
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getPersonData(Org org,Map<String,Object> param){
		/**
		 * 先取出该组织下 所有人员
		 */
		param.put("orgId", org.getId());
		param.put("isPrimary", "yes");//查询主要职位
		param.put("changeTypeNotIn", "'"+PositionChangeTypeEnum.LEAVE.getValue()+"','"+PositionChangeTypeEnum.DELRUNDISK.getValue()+"'");
		List<Person> plist=queryExecutor.execQuery("com.wuyizhiye.fastsale.dao.AchieveTargetDao.getHistoryPersonByOrg", param, Person.class);
		param.put("personIds", getPersonIds(plist));
		List<Map> mlist=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getMessagePersonData", param, Map.class);
		
		/**
		 * 充值数据
		 */
		List<Map> charge=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao.getChargePersonData", param, Map.class);
		for(Map map:mlist){
			map.put("URL", "no");
			map.put("ORG_NUMBER", map.get("ORG_LONGNUMBER"));
			map.put("ORG_LONGNUMBER", org.getLongNumber()+"_"+map.get("ORG_LONGNUMBER"));
			map.put("ORG_LEVEL", org.getLevel()+3);
			boolean flag=false;
			for(Map p:charge){
				if(map.get("ORG_ID").equals(p.get("PERSON_ID"))){
					map.put("PERSON_FOUR", p.get("PERSON_ONE"));
					flag=true;
				}
			}
			if(!flag){
				map.put("PERSON_FOUR", 0);
			}
		}
		return mlist;
	}
	
	/**
	 * 查找下级组织数据
	 * @param orgId
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Map> getOrgData(Org org,Map<String,Object> param,boolean flag){
		Map<String,Object> oparam=new HashMap<String, Object>();
		oparam.put("parentid", org.getId());
		oparam.put("begDate", param.get("startTime"));
		oparam.put("endDate", param.get("endTime"));
		/**
		 * 取下级组织
		 */
		List<Map> result=new ArrayList<Map>();
		List<Org> olist=new ArrayList<Org>();
		if(!flag){
			olist=OrgUtils.getHistoryOrgList(oparam, false);
		}else{
			olist.add(org);
		}
		if(null != olist && olist.size()>0){//有下级组织
			oparam.remove("parentid");//移除父节点
			oparam.put("longnumber", org.getLongNumber());//根据该组织的长编码 找到所有的下级组织
			List<Org> child=OrgUtils.getHistoryOrgList(oparam, false);
			param.put("orgIds", getOrgIds(child));
			/**
			 * 短信数据
			 */
			List<Map> temp=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getMessageOrgData", param, Map.class);
			/**
			 * 充值数据
			 */
			List<Map> charge=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao.getChargeOrgData", param, Map.class);
			for(Org o:olist){
				Map<String,Object> one=new HashMap<String, Object>();
				one.put("ORG_NAME", o.getName());
				one.put("ORG_NUMBER", o.getNumber());
				one.put("ORG_ID", o.getId());
				one.put("ORG_LONGNUMBER", o.getLongNumber());
				one.put("ORG_LEVEL", o.getLevel());
				one.put("URL", "yes");//有点击查看下级 图标  
				int first=0;
				int two=0;
				int three=0;
				int four=0;
				for(Map m:temp){
					if(m.get("ORG_LONGNUMBER").toString().contains(o.getLongNumber())){//是该组织的下级
						if(null != m.get("PERSON_ONE")){
							first+=Integer.parseInt(m.get("PERSON_ONE").toString());
						}
						if(null != m.get("PERSON_TWO")){
							two+=Integer.parseInt(m.get("PERSON_TWO").toString());
						}
						if(null != m.get("PERSON_THREE")){
							three+=Integer.parseInt(m.get("PERSON_THREE").toString());
						}
					}
				}
				for(Map map:charge){
					if(map.get("ORG_LONGNUMBER").toString().contains(o.getLongNumber())){//是该组织的下级
						if(null != map.get("PERSON_ONE")){
							four+=Integer.parseInt(map.get("PERSON_ONE").toString());
						}
					}
				}
				one.put("PERSON_ONE", first);
				one.put("PERSON_TWO", two);
				one.put("PERSON_THREE", three);
				one.put("PERSON_FOUR", four);
				result.add(one);
			}
		}
		return result;
	}
	
	/**
	 * 拼组织ID
	 * @param olist
	 * @return
	 */
	public String getOrgIds(List<Org> olist){
		String str="(";
		for(Org o:olist){
			str+="'"+o.getId()+"',";
		}
		if(!"(".equals(str)){
			str=str.substring(0, str.length()-1)+")";
		}else{
			str="('')";
		}
		return str;
	}
	
	public String getPersonIds(List<Person> olist){
		String str="(";
		for(Person o:olist){
			str+="'"+o.getId()+"',";
		}
		if(!"(".equals(str)){
			str=str.substring(0, str.length()-1)+")";
		}else{
			str="('')";
		}
		return str;
	}
	
	public void put(String key,Object o){
		this.getRequest().setAttribute(key, o);
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
