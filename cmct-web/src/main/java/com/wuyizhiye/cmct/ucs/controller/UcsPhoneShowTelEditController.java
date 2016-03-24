package com.wuyizhiye.cmct.ucs.controller;

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
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneMember;
import com.wuyizhiye.cmct.ucs.model.UcsPhoneShowTel;
import com.wuyizhiye.cmct.ucs.service.UcsPhoneShowTelService;
import com.wuyizhiye.cmct.ucs.util.UcsPhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName UcsPhoneShowTelEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/ucsPhoneShowTel/*")
public class UcsPhoneShowTelEditController extends EditController {

	@Autowired
	private UcsPhoneShowTelService ucsPhoneShowTelService;
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return UcsPhoneShowTel.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return ucsPhoneShowTelService;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		Object data = getSubmitEntity();
		if(validate(data)){
			UcsPhoneShowTel showTel = (UcsPhoneShowTel) data ;
			getShowTelNo(showTel);
 			UcsPhoneMember member=new UcsPhoneMember();
			Map<String,Object> result = new HashMap<String,Object>();
			//新增数据
			if(StringUtils.isEmpty(showTel.getId())){
				
				String key="";
				try {
					key = SecurityUtil.encryptPassword((UcsPhoneMemberUtil.getPhoneConfigParam(UcsPhoneMemberUtil.UCSPHONE_KEY)+showTel.getAgentId()).trim()).toLowerCase();
				} catch (Exception e) {
					e.printStackTrace();
				}
				member.setKey(key);
				member.setShowTel(showTel);
				result=UcsPhoneMemberUtil.ucsPhoneUrl(member, UcsPhoneMemberUtil.UCSUPLOADSHOWNO_URL);
			}else{
				
			}
			if("SUCCESS".equals(result.get("STATE").toString())){
				if(StringUtils.isEmpty(showTel.getId())){
					getService().addEntity(showTel);
				}else{
					
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", UcsPhoneMemberUtil.getMsgByErrorKey(UcsPhoneMemberUtil.UCSUPLOADSHOWNO_URL,((String)result.get("MSG")).trim()));
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "验证数据实体出现错误");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));

	}
	public void getShowTelNo(UcsPhoneShowTel showTel){
		String contactJson=showTel.getContactJson();
		StringBuilder showTelNoMore=new StringBuilder();
		if(!StringUtils.isEmpty(contactJson)){
			JSONArray arry = JSONArray.fromObject(contactJson);
			List<UcsPhoneShowTel> typeList = (List<UcsPhoneShowTel>) JSONArray.toCollection(arry, UcsPhoneShowTel.class);
			for(UcsPhoneShowTel tel:typeList){
				showTelNoMore.append(tel.getShowTelNo()).append(",");
			}
			if(!StringUtils.isEmpty(showTelNoMore.toString())){
				showTel.setShowTelNoMore(showTelNoMore.substring(0, showTelNoMore.length()-1).toString());
			}
		}
	}
}
