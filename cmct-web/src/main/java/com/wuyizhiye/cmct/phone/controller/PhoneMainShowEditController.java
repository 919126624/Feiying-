package com.wuyizhiye.cmct.phone.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.cmct.phone.model.PhoneMainShow;
import com.wuyizhiye.cmct.phone.service.PhoneMainShowService;
import com.wuyizhiye.cmct.phone.util.PhoneMainShowUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMemberUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneMainShowEditController
 * @Description 主显号码控制类
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMainShow/*")
public class PhoneMainShowEditController extends EditController {

	@Autowired
	private PhoneMainShowService phoneMainShowService;
	
	@Override
	protected Class getSubmitClass() {
		return PhoneMainShow.class;
	}

	@Override
	protected BaseService getService() {
		return phoneMainShowService;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		PhoneMainShow mainShow = (PhoneMainShow) getSubmitEntity();
		String customerNumber=ParamUtils.getParamValue(PhoneMemberUtil.CMCT_CUSOMETID);
		Map<String, Object>resMap=PhoneMainShowUtil.registDisplayNbr(customerNumber, mainShow.getDisplayNbr(), mainShow.getCode(),StringUtils.isEmpty(mainShow.getDescription())?"":mainShow.getDescription());
		getOutputMsg().putAll(resMap);
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
