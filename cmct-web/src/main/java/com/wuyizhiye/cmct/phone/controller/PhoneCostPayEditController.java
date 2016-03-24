package com.wuyizhiye.cmct.phone.controller;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.enums.PhonePayStateEnum;
import com.wuyizhiye.cmct.phone.model.PhoneCostPay;
import com.wuyizhiye.cmct.phone.service.PhoneCostPayService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneCostPayEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneCostPay/*")
public class PhoneCostPayEditController extends EditController {

	@Autowired
	private PhoneCostPayService phoneCostPayService;
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return PhoneCostPay.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneCostPayService;
	}

	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		PhoneCostPay data = (PhoneCostPay) getSubmitEntity();
		if(validate(data)){
			if(data instanceof CoreEntity){
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
//					data.setPaySurplusCost(data.getPaycost()+data.getPaySurplusCost());
					data.setOrg(getCurrentUser().getOrg());
					data.setPerson(getCurrentUser());
					data.setPaytime(new Date());
					data.setFlag(PhonePayStateEnum.APPLY);
					getService().addEntity((CoreEntity)data);
				}else{
					getService().updateEntity((CoreEntity)data);
				}
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

}
