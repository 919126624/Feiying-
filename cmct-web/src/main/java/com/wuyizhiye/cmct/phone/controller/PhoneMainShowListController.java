package com.wuyizhiye.cmct.phone.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.model.PhoneMainShow;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneMainShowService;
import com.wuyizhiye.cmct.phone.util.PhoneMainShowUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneMainShowListController
 * @Description 主显号码控制类
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMainShow/*")
public class PhoneMainShowListController extends ListController {

	@Autowired
	private PhoneMainShowService phoneMainShowService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return new PhoneMainShow();
	}

	@Override
	protected String getListView() {
		return "cmct/phone/phoneMainShowList" ;
	}

	@Override
	protected String getEditView() {
		return "cmct/phone/phoneMainShowEdit" ;
	}

	@Override
	protected String getListMapper() {
		return PhoneMainShow.MAPPER+".select";
	}

	@Override
	protected BaseService getService() {
		return phoneMainShowService;
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
	
	
	@RequestMapping(value="getIdCode")
	public void getIdCode(HttpServletResponse response){
		String displayNbr=getString("displayNbr");
		String customerId=ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID);
		Map<String, Object>resMap=PhoneMainShowUtil.getMainShowCode(customerId, displayNbr);
		outPrint(response, JSONObject.fromObject(resMap, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="yunList")
	public String yunList(){
		this.getRequest().setAttribute("org", SystemUtil.getCurrentOrg());
		return "cmct/phone/phoneMainShowYun" ;
	}
	
	@RequestMapping(value="yunData")
	public void yunData(Pagination<Map> pagination,HttpServletResponse response){
		int currPage=pagination.getCurrentPage();
		int pageSize=pagination.getPageSize();
		String customerNumber=ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID);
		String key=getString("key","");
		String state=getString("state","");
		Map<String, Object>resMap=PhoneMainShowUtil.getDisplayNbr(customerNumber, key, state, currPage, pageSize);
		if("SUCCESS".equals(resMap.get("STATE"))){
			String data=resMap.get("data").toString();
			outPrint(response, JSONObject.fromObject(data));
		}else{			
			outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
		}
	}
	
	@RequestMapping(value="batchMatch")
	public void batchMatch(HttpServletResponse response){
		String ids=getString("ids");
		String orgId=getString("orgId");
		Map<String, Object>resMap=this.phoneMainShowService.matchDisplayNbr(ids,orgId);
		getOutputMsg().putAll(resMap);
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}

	@Override
	protected boolean isAllowDelete(CoreEntity entity) {
		String id=entity.getId();
		PhoneMainShow mainShow=this.phoneMainShowService.getEntityById(id);
		String displayNbr=mainShow.getDisplayNbr();
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("defaultShowPhone", displayNbr);
		List<PhoneMember>members=queryExecutor.execQuery(PhoneMember.MAPPER+".select", param, PhoneMember.class);
		if(null!=members && members.size()>0){
			getOutputMsg().put("MSG", "当前主显线路绑定在主叫线路,删除失败...");
			return false;
		}
		Map<String, Object>resMap=PhoneMainShowUtil.deleteDisplayNbr(mainShow.getMatchId());
		if("SUCCESS".equals(resMap.get("STATE"))){			
			return true;
		}
		getOutputMsg().put("MSG", resMap.get("MSG"));
		return false;
	}
	
	@RequestMapping(value="getAllYunMainShow")
	public void getAllYunMainShow(HttpServletResponse response){
		String customerNumber=ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID);
		Map<String, Object>resMap=PhoneMainShowUtil.getDisplayNbr(customerNumber, "", "", 1, Integer.MAX_VALUE);
		if("SUCCESS".equals(resMap.get("STATE"))){
			String data=resMap.get("data").toString();
			JSONObject jsonObj=JSONObject.fromObject(data);
			List<Map>maps=JSONArray.toList(JSONArray.fromObject(jsonObj.get("items")), Map.class);
			getOutputMsg().put("data", maps);
			getOutputMsg().put("STATE", "SUCCESS");
		}else{			
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
}
