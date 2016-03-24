package com.wuyizhiye.cmct.phone.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.phone.enums.PhonePayStateEnum;
import com.wuyizhiye.cmct.phone.model.PhoneConfig;
import com.wuyizhiye.cmct.phone.model.PhoneCostPay;
import com.wuyizhiye.cmct.phone.service.PhoneCostPayService;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.cmct.phone.util.ProjectMApiRemoteServer;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhoneCostPayListController
 * @Description 话费管理--话费充值
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCostPay/*")
public class PhoneCostPayListController extends ListController {

	@Autowired
	private PhoneCostPayService phoneCostPayService;
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		if(UserTypeEnum.P01.equals(SystemUtil.getCurrentUserType())){
			getRequest().setAttribute("PerType",UserTypeEnum.P01.getValue());//业务员
		}
		else if(UserTypeEnum.F01.equals(SystemUtil.getCurrentUserType())){
			getRequest().setAttribute("PerType",UserTypeEnum.F01.getValue());//销售员
		}
		if(UserTypeEnum.T01.equals(SystemUtil.getCurrentUserType()))
			getRequest().setAttribute("PerType",UserTypeEnum.T01.getValue());//管理员
		/**
		 * 获取核算渠道
		 */
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
					pc.setQueryAccountUrl(jsonObj.getString("queryAccountUrl"));
					pc.setOrgKey(jsonObj.getString("orgKey"));
					pc.setPartners(jsonObj.getString("partners"));
					pcs.add(pc);
				}
			}
			
		}
		this.getRequest().setAttribute("pcs", pcs);
		return "cmct/phone/phoneCostPayList";
	}

	@Override
	protected String getEditView() {
//		this.getRequest().setAttribute("surplusMoney", Double.valueOf(getString("surplusMoney","0")));
		this.getRequest().setAttribute("orgId", getString("orgId"));
		return "cmct/phone/phoneCostPayEdit";
	}

	@Override
	protected String getListMapper() {
		return PhoneCostPay.MAPPER + ".select";
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneCostPayService;
	}

	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = new JsonConfig();
		//日期
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
		//充值状态
		config.registerJsonValueProcessor(PhonePayStateEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhonePayStateEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((PhonePayStateEnum)value).getName());
					json.put("value", ((PhonePayStateEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof PhonePayStateEnum){
					return ((PhonePayStateEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	@RequestMapping(value="confirmPay")
	public void confirmPay(HttpServletResponse response,@RequestParam(required=true,value="id")String id){
		String queryAccount=getString("queryAccount");
		String payAccount=getString("payAccount");
		Double pay1=0.00d;
		Double pay2=0.00d;
		if(!StringUtils.isEmpty(queryAccount)){
			pay1=Double.valueOf(queryAccount);
		}
		if(!StringUtils.isEmpty(payAccount)){
			pay2=Double.valueOf(payAccount);
		}
		PhoneCostPay phoneCostPay=phoneCostPayService.getEntityById(id);
		if(phoneCostPay==null){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "数据不存在");
		}else{
			phoneCostPay.setFlag(PhonePayStateEnum.FINISH);
			phoneCostPay.setPaytime(new Date());
			phoneCostPay.setPaySurplusCost(pay1+pay2);
			phoneCostPayService.updateEntity(phoneCostPay);
			getOutputMsg().put("MSG", "充值成功！");
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}

	@RequestMapping(value="viewDetail")
	public String viewDetail(ModelMap model,@RequestParam(required=true,value="id")String id){
		model.put("data", getService().getEntityById(id));
		return "cmct/phone/phoneCostPayView";
	}
}
