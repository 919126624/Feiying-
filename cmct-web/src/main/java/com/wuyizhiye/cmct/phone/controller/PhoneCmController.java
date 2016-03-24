package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneDialDetail;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneCmController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCm/*")
public class PhoneCmController extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		put("showMonth",df.format(new Date()).substring(0, 7));
		put("showDay",df.format(new Date()));
		put("startDay",(df.format(new Date()).substring(0, 7)+"-01").replaceAll("-", "/"));
		put("endDay",df.format(new Date()).replaceAll("-", "/"));
		put("disPlayCallBill", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_DISPLAY_DCB));
		Map<String, String>param=new HashMap<String, String>();
		List<PhoneConfig>pcs=new ArrayList<PhoneConfig>();
		param.put("customerId", ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID));
		Map<String, String> result=ProjectMApiRemoteServer.getCustomerByOrgId(param);//获取核算渠道,一个客户下可能有多个核算渠道
		if(result!=null && "SUCCESS".equals(result.get("STATE"))){
			String strConfig=result.get("pcs");
			JSONArray jsonArr=JSONArray.fromObject(strConfig);
			if(null!=jsonArr && jsonArr.size()>0){
				for(int i=0;i<jsonArr.size();i++){
					JSONObject jsonObj=jsonArr.getJSONObject(i);
					PhoneConfig pc=new PhoneConfig();
					pc.setConfigName(jsonObj.getString("configName"));
					pc.setOrgId(jsonObj.getString("orgId"));
					pc.setPartners(jsonObj.getString("partners"));
					pcs.add(pc);
				}
			}
			
		}
		put("pcs", pcs);
		return "cmct/phone/phoneCm";
	}

	/**
	 * 获取明细数据
	 * @param key
	 * @param o
	 */
	@RequestMapping(value="queryCmdData")
	public void queryCmdData(Pagination<?> pagination,HttpServletResponse response){
		Map<String, String>map=getParamMap();
		String cmrId=map.get("cmrId");
		if(!StringUtils.isEmpty(cmrId)){
			map.put("start", String.valueOf(pagination.getCurrentPage()));
			map.put("rows", String.valueOf(pagination.getPageSize()));
			Map<String, String>result=ProjectMApiRemoteServer.getAllPhoneCmd(map);
			if(result!=null && "SUCCESS".equals(result.get("STATE"))){
				String cmdStr=result.get("phoneCmd");
				outPrint(response,cmdStr);
			}else{
				outPrint(response, JSONObject.fromObject("{}"));
			}
		}else{
			outPrint(response, JSONObject.fromObject("{}"));
		}
	}
	
	/**
	 * 获取组织数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="queryCmrData")
	public void queryCmrData(Pagination<?> pagination,HttpServletResponse response){
		Map<String, String>map=getParamMap();
		Map<String, String>result=ProjectMApiRemoteServer.getAllPhoneCmr(map);
		if(result!=null && "SUCCESS".equals(result.get("STATE"))){
			String cmrStr=result.get("phoneCmr");
			JSONArray jsonArr=JSONArray.fromObject(cmrStr);
			outPrint(response, JSONObject.fromObject(jsonArr.get(0)));
		}else{
			outPrint(response, JSONObject.fromObject("{}"));
		}
	}
	
	public void put(String key,Object o){
		this.getRequest().setAttribute(key, o);
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
	
	/**
	 * 读取当前的核算渠道及当前时间所对应的电话时长,使用模式.电话量
	 */
	@RequestMapping(value="loadData")
	public void loadData(Pagination<PhoneDialDetail> pagination,HttpServletResponse response){
		Map<String, Object>param=getListDataParam();
		String year=(String) param.get("year");
		String month=(String) param.get("month");
		String period=year+"-"+month;
		param.clear();
		param.put("period", period);
		
		//查找出开通的电话，过滤出使用模式和使用组织
		List<PhoneMember> callUserList = queryExecutor.execQuery(PhoneMember.MAPPER+".select", null, PhoneMember.class);
		List<PhoneDialDetail>pdds=queryExecutor.execQuery(PhoneDialDetail.MAPPER+".selectLoadData", param, PhoneDialDetail.class);
		for(PhoneDialDetail pdd:pdds){

			PhoneMember cs = this.getUseTypeByShowPhone(callUserList, pdd.getInfoNumber()) ;
			if(cs != null){
				pdd.setUseType(cs.getUseType() == null ? "" : cs.getUseType().getName());
				pdd.setOrgName(cs.getOrg() == null ? "" : cs.getOrg().getName());
			}
		}
		
		pagination.setItems(pdds);
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	
		
	}
	
	private PhoneMember getUseTypeByShowPhone(List<PhoneMember> callUserList,String showPhone){
		PhoneMember callSet = null ;
		for(PhoneMember cs : callUserList){
			if(cs.getShowPhone()!=null && cs.getShowPhone().contains(showPhone)){
				callSet = cs ;
				break ;
			}
		}
		return callSet;
	}
}
