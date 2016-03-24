package com.wuyizhiye.cmct.phone.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.cmct.phone.model.PhonemMobileMember;
import com.wuyizhiye.cmct.phone.service.PhonemMobileMemberService;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMobileUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhonemMobileMemberEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/mobileMember/*")
public class PhonemMobileMemberEditController extends EditController {

	@Autowired
	private PhonemMobileMemberService phonemMobileMemberService;
	
	@Override
	protected Class getSubmitClass() {
		return PhonemMobileMember.class;
	}

	@Override
	protected BaseService getService() {
		return phonemMobileMemberService;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		Object data = getSubmitEntity();
		PhoneMobileUtil.putMobileMember((PhonemMobileMember)data);
		super.save(response);
	}
	/*
	 * 手机版公用拨打电话ajax方法
	 * */
	@RequestMapping("ajaxPhoneCall")
	public void ajaxPhoneCall(HttpServletResponse response,ModelMap model){
		String telType=getString("telType");//值为mobile 或则 wyzy(51置业)
		String phoneNumber = getString("phoneNumber");//如果为mobile,则phoneNumber为被叫号码,主叫号码为当前登录人的号码,如果为wyzy,phoneNumber为主叫号码,被叫号码通过系统参数设置
		String personName = getString("personName","");
		
		//调用手机版打电话功能
		//测试号码 
		//phoneNumber = "15817452854";
		Map<String, Object> map = FjCtCmctMemberUtil.dailMobilePhone(phoneNumber,telType,personName);
		//System.out.println(map);
		
		outPrint(response, JSONObject.fromObject(map));
	}
}
