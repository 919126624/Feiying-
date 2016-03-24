package com.wuyizhiye.cmct.phone.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketBindPerson;
import com.wuyizhiye.cmct.phone.service.PhoneMarketService;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.cmct.phone.util.PhoneMarketUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneMarketEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneMarket/*")
public class PhoneMarketEditController extends EditController {

	@Autowired
	private PhoneMarketService phoneMarketService;
	
	@Override
	protected Class getSubmitClass() {
		return PhoneMarket.class;
	}

	@Override
	protected BaseService getService() {
		return phoneMarketService;
	}

	/**
	 * 调用接口,新增营销接口数据
	 */
	@Override
	public void save(HttpServletResponse response)
			throws InstantiationException, IllegalAccessException {
		PhoneMarket phoneMarket=(PhoneMarket) getSubmitEntity();
		setPhoneMarketTime(phoneMarket);
		if(!StringUtils.isEmpty(getCurrentUser().getId())){
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("personId", getCurrentUser().getId());
			List<PhoneMarketBindPerson> pms=queryExecutor.execQuery(PhoneMarketBindPerson.MAPPER+".select", param, PhoneMarketBindPerson.class);
			if(null!=pms && pms.size()>0){
				phoneMarket.setPhoneMbp(pms.get(0));
				Map<String, Object>res=FjCtCmctMemberUtil.commonMarketMethond(phoneMarket, "Marketing1");
				if(null!=res && "SUCCESS".equals(res.get("STATE"))){
					Map<String, Object>resMap=(Map<String, Object>) res.get("respMap");
					phoneMarket.setModeID(resMap.get("WorkID").toString());
					phoneMarket.setStatus("1");//默认为暂停的
					phoneMarket.setSendCount(Integer.parseInt(resMap.get("SendCount").toString()));
					phoneMarketService.addEntity(phoneMarket);
					getOutputMsg().put("STATE", "SUCCESS");
					getOutputMsg().put("MSG", "保存成功,20秒之后点击启动操作..");
				}else{
					getOutputMsg().put("STATE", "FAIL");
					getOutputMsg().put("MSG", PhoneMarketUtil.getMsgByErrorKey(res.get("MSG").toString()));
				}
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "没有找到对应的计费号码");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "当前登录人为空");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	public void setPhoneMarketTime(PhoneMarket phoneMarket){
		//phoneMarket.setAmBeginTime(phoneMarket.getAmBeginTime()+":00");
		//phoneMarket.setAmEndTime(phoneMarket.getAmEndTime()+":00");
		//phoneMarket.setPmBeginTime(phoneMarket.getPmBeginTime()+":00");
		//phoneMarket.setPmEndTime(phoneMarket.getPmEndTime()+":00");
		if(!StringUtils.isEmpty(phoneMarket.getEffectDate())){		
			phoneMarket.setEffectDate(phoneMarket.getEffectDate()+":00");
		}
	}
}
