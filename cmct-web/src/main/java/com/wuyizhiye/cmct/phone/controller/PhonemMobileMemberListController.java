package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.enums.PhoneMobileStatusEnum;
import com.wuyizhiye.cmct.phone.model.PhonemMobileMember;
import com.wuyizhiye.cmct.phone.service.PhonemMobileMemberService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMobileUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhonemMobileMemberListController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/mobileMember/*")
public class PhonemMobileMemberListController extends ListController {

	@Autowired
	private PhonemMobileMemberService phonemMobileMemberService;
	
	@Autowired
	private PersonService personService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new PhonemMobileMember();
	}

	@Override
	protected String getListView() {
//		Map<String, Object>res=FjCtCmctMemberUtil.dailMobilePhone("13510628424","wyzy","");
		return "cmct/phone/mobileMemberList";
	}

	@RequestMapping(value="manage")
	public String manage(){
		return "cmct/phone/mobileMemberManager";
	}
	
	@Override
	protected String getEditView() {
		return "cmct/phone/mobileMemberEdit";
	}

	@Override
	protected String getListMapper() {
		return PhonemMobileMember.MAPPER+".selectList";
	}

	@Override
	protected BaseService getService() {
		return phonemMobileMemberService;
	}
	
	@RequestMapping(value="updateStatus")
	public void updateFlag(HttpServletResponse response){
		String dataId=getString("dataId");
		String personId=getString("personId");
		Person person=personService.getEntityById(personId);
		PhonemMobileMember member=null;
		if(!StringUtils.isEmpty(dataId)){			
			member=phonemMobileMemberService.getEntityById(dataId);
		}
		if(null!=member){
			if("OPEN".equals(member.getStatus().getValue())){
				member.setStatus(PhoneMobileStatusEnum.STOP);
			}else{
				member.setStatus(PhoneMobileStatusEnum.OPEN);
			}
			phonemMobileMemberService.updateEntity(member);
			PhoneMobileUtil.putMobileMember(member);
			getOutputMsg().put("MSG", "操作成功！");
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			if(StringUtils.isEmpty(person.getPhone())){
				getOutputMsg().put("MSG", "当前开通人员的电话号码是空...！");
				getOutputMsg().put("STATE", "FAIL");
			}else{				
				member=new PhonemMobileMember();
				member.setPerson(person);
				member.setPhoneNum(person.getPhone());
				member.setStatus(PhoneMobileStatusEnum.OPEN);
				this.phonemMobileMemberService.addEntity(member);
				PhoneMobileUtil.putMobileMember(member);
				getOutputMsg().put("MSG", "操作成功！");
				getOutputMsg().put("STATE", "SUCCESS");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
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
		config.registerJsonValueProcessor(PhoneMobileStatusEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhoneMobileStatusEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((PhoneMobileStatusEnum)value).getName());
					json.put("value", ((PhoneMobileStatusEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhoneMobileStatusEnum){
					return ((PhoneMobileStatusEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	@RequestMapping(value="mobileCostList")
	public String mobileCostList(){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		this.getRequest().setAttribute("showMonth",df.format(new Date()).substring(0, 7));
		return "cmct/phone/mobileCostList" ;
	}
	
	@RequestMapping(value="listCostData")
	public void listCostData(Pagination<?> pagination,HttpServletResponse response){
		String showMonth=getString("yearMonth");
		Map<String, Object>param=super.getListDataParam();
		if(null != showMonth && !"".equals(showMonth)){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    		try {
				param.put("startTime", df.parse(showMonth+"-01"));
				param.put("endTime", DateUtil.getNextDay(df.parse(getMonthEnd(showMonth))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			param.put("suffix", "_"+showMonth.replace("-", "_"));
    	}
		
		pagination = queryExecutor.execQuery(PhonemMobileMember.MAPPER+".selectCost", pagination, param);
		if(null!=pagination.getItems() && pagination.getItems().size()>0){			
			Map<String, String>queryParam=new HashMap<String, String>();
			queryParam.put("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));
			queryParam.put("year", getString("year"));
			queryParam.put("month", getString("month"));
			queryParam.put("version", "SYNCVERSION");
			queryParam.put("partners", "HW");
			Map<String, Object>resMap=ProjectMApiRemoteServer.getDingJianDurationMember(queryParam);
			Map<String, Integer>memberDuration=new HashMap<String, Integer>();
			if(null!=resMap && "SUCCESS".equals(resMap.get("STATE"))){
				JSONArray jsonArr=JSONArray.fromObject(resMap.get("phones"));
				for(int i=0;i<jsonArr.size();i++){
					JSONObject jsonObj=jsonArr.getJSONObject(i);
					if(jsonObj.containsKey("MEMBERCOUNT") && null!=jsonObj.getString("MEMBERCOUNT")){
						memberDuration.put(jsonObj.getString("PHONENUM"), jsonObj.getInt("MEMBERCOUNT"));
					}
				}
			}
			List<Map>maps=(List<Map>) pagination.getItems();
			for(Map map:maps){
				if(memberDuration.containsKey(map.get("phoneNum"))){
					map.put("phoneDuration", memberDuration.get(map.get("phoneNum")));
				}else{
					map.put("phoneDuration",0);
				}
			}
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	public String getMonthEnd(String str){
		 Calendar c = Calendar.getInstance();
		  SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
		  try {
			c.setTime(format.parse(str));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  int MaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		  //按你的要求设置时间
		  c.set( c.get(Calendar.YEAR), c.get(Calendar.MONTH), MaxDay, 23, 59, 59);
		  //按格式输出
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		   return sdf.format(c.getTime()); //该月最后一天
	}
}
