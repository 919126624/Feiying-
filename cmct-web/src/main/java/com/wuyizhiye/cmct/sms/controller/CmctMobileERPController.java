package com.wuyizhiye.cmct.sms.controller;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.utils.ShortMessageUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CmctMobileERPController
 * @Description 移动ERP
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/mobileERP/*")
public class CmctMobileERPController extends ListController{

	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		this.getRequest().setAttribute("mobileUrl", ParamUtils.getParamValue("MOBILEURL"));//移动客户端下载 链接标示
		return "cmct/sms/mobileERP";
	}
	
	/**
	 * 发送
	 * @param response
	 */
	@RequestMapping("sendMes")
	@ResponseBody
	public void sendMes(HttpServletResponse response){
		String phone=getString("phone");
		Person current=SystemUtil.getCurrentUser();
		Org org=SystemUtil.getCurrentOrg();
		ShortMessage sm=new ShortMessage();
        sm.setId(UUID.randomUUID().toString());
	    sm.setCreator(current);//创建人
	    sm.setOrg(org);//组织
	    sm.setControlType(SMSControlTypeEnum.PERSONAL_TYPE);//控制类型个人
	    sm.setType(SMSTypeEnum.BUSINESS_TYPE);//短信类型 业务类
	    sm.setSenderId(current.getId());//发送人ID
	    sm.setSenderNumber(current.getNumber());///发送人 工号
	    sm.setSenderName(current.getName());//发送人姓名
	    sm.setSenderPhoneNum(current.getPhone());//发送人电话
	    sm.setSendTime(null);//发送时间
	    sm.setReceiverName(current.getName());//接收人姓名
	    sm.setReceiverPhoneNum(phone);//接收人电话
	    sm.setCreateTime(new Date());
	    sm.setContent("请点击:http://www.dingjiansoft.com/download/"+ParamUtils.getParamValue("MOBILEURL")+".apk 下载鼎尖ERP客户端!");//短信内容
	    String result= ShortMessageUtil.sendOneMessage(sm);//发送操作
	    if("发送成功".equals(result)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功!");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", result);
		}
		outPrint(response,JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}

}
