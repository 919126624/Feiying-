package com.wuyizhiye.cmct.ucs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneNumber;
import com.wuyizhiye.cmct.ucs.util.UcsPhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName UcsPhoneCenterNewController
 * @Description 话务中心控制器 后期2.0版本
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhonenew/*")
public class UcsPhoneCenterNewController extends BaseController {
	
	/**
	 * 获取渠道商固话列表
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="getCallRecordList")
	public void getCallRecordList(Pagination<UcsPhoneNumber> pagination,HttpServletResponse response){
		try{
			UcsPhoneMember member=new UcsPhoneMember();
			member.setTelNo("13510628424");
			member.setKey("aba1aad751428206d9e004660d68ce0b");
			Map<String,Object> result = UcsPhoneMemberUtil.getCallRecordList(member);
			if("SUCCESS".equals(result.get("STATE").toString())){
				JSONArray numberList =  JSONArray.fromObject(result.get("callRecordList"));
				List<UcsPhoneNumber> queryList = new ArrayList<UcsPhoneNumber>();
				for(int i = 0 ; i < numberList.size() ; i ++){
					UcsPhoneNumber cn = new UcsPhoneNumber();
					JSONObject jsonObj = numberList.getJSONObject(i);
					cn.setIp(jsonObj.getString("IP"));
					cn.setCallUnit(jsonObj.getString("callUnit"));
					cn.setCaller(jsonObj.getString("caller"));
					cn.setCardNo(jsonObj.getString("cardNo"));
					cn.setConnectTime(jsonObj.getString("connectTime"));
					cn.setDuration(jsonObj.getString("duration"));
					cn.setSvcalloutuseroffhoktime(StringUtils.isEmpty(jsonObj.getString("m_svcalloutuseroffhoktime"))? null : jsonObj.getString("m_svcalloutuseroffhoktime"));
					cn.setMsvcalloutusertalkmoney(jsonObj.getString("m_svcalloutusertalkmoney"));
					cn.setSvcalloutcusconame(jsonObj.getString("svcalloutcusconame"));
					cn.setSvcalloutdispcaller(jsonObj.getString("svcalloutdispcaller"));
					queryList.add(cn);
				}
				pagination.setItems(queryList);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			pagination = new Pagination<UcsPhoneNumber>();
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 获取登录人呼叫信息
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="getUserInfo")
	public void getUserInfo(HttpServletResponse response) throws Exception{
		if(SystemUtil.getCurrentUser() == null || StringUtils.isEmpty(SystemUtil.getCurrentUser().getId())){
			getOutputMsg().put("STATE", "EXCEPTION");
			getOutputMsg().put("NOLOGIN", "请先登录系统");
		}else{
			//查找登录人是否之前有选择的路线,即查找当前使用人的线路
			UcsPhoneMember callSet = null ;
			Map<String,Object> condMap = new HashMap<String,Object>();
			condMap.put("currUserId", SystemUtil.getCurrentUser().getId());
			condMap.put("enable", CommonFlagEnum.YES);
			List<UcsPhoneMember> callSetList = queryExecutor.execQuery("com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao.select", condMap, UcsPhoneMember.class);
			if(callSetList!=null && callSetList.size()>0){
				callSet = callSetList.get(0);
			}else{
				//如果没有当前使用的，则查找当前人的绑定的线路
				condMap.clear();
				condMap.put("onlyUserId", SystemUtil.getCurrentUser().getId());
				condMap.put("enable", CommonFlagEnum.YES);
				callSetList = queryExecutor.execQuery("com.wuyizhiye.cmct.ucs.dao.UcsPhoneMemberDao.select", condMap, UcsPhoneMember.class);
				if(callSetList!=null && callSetList.size()>0){
					callSet = callSetList.get(0);
				}else{
				}
			}
			if(callSet!=null && !StringUtils.isEmpty(callSet.getId())){
				getOutputMsg().put("STATE", "SUCCESS");
				//callSet.setOrgInterfaceId(CallCenterUtil.getSystemParam(CallCenterUtil.CALL_ORGID));
				getOutputMsg().put("callSet", callSet);
			}else{
				//未找到线路，用户自己去选择
				getOutputMsg().put("STATE", "FAIL");
			}
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
