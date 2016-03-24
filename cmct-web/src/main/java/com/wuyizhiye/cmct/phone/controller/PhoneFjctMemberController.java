package com.wuyizhiye.cmct.phone.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.cmct.phone.model.ApiCallBack;
import com.wuyizhiye.cmct.phone.model.PhoneDxCallErrorStatus;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneMemberService;
import com.wuyizhiye.cmct.phone.util.CtPhoneSession;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PhoneFjctMemberController
 * @Description 福建电信phonememeber控制器
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/phoneFjctmember/*")
public class PhoneFjctMemberController extends EditController {

	@Autowired
	private PhoneMemberService phoneMemberService;
	@Override
	protected Class getSubmitClass() {
		// TODO Auto-generated method stub
		return PhoneMember.class;
	}

	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return phoneMemberService;
	}
	
	/**
	 * 拨打电话
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value="dailPhone")
	public void dailPhone(HttpServletResponse response){
		String showNumber=getString("showPhone");
		String userId=getString("userId");
		String passWord=getString("passWord");
		String toPhone=getString("toPhone");
		String appId=getString("appId");
		String httpUrl=getString("httpUrl");
		String spid=getString("spid");
		String passWd=getString("passWd");
		String defaultShowPhone=getString("defaultShowPhone");
		try {
			if(StringUtils.isEmpty(showNumber)){
				throw new Exception("去电号码为空");
			}
			if(StringUtils.isEmpty(userId)){
				throw new Exception("计费号码为空");
			}
			if(StringUtils.isEmpty(toPhone)){
				throw new Exception("被叫号码为空");
			}
			if(toPhone.equals(showNumber)){
				throw new Exception("不能拨打自己的号码");
			}
			if(StringUtils.isEmpty(appId)){
				throw new Exception("appId不存在");
			}
			if(StringUtils.isEmpty(httpUrl)){
				throw new Exception("httpUrl不存在");
			}
			PhoneMember ppm=new PhoneMember();
			ppm.setUserId(userId);
			ppm.setShowPhone(showNumber);
			ppm.setPassword(passWord);
			ppm.setAnswerPhone(toPhone);
			ppm.setOrgInterfaceId(appId);
			ppm.setHttpUrl(httpUrl);
			ppm.setSpid(spid);
			ppm.setPassWd(passWd);
			ppm.setDefaultShowPhone(defaultShowPhone);
			Map<String, Object>result=FjCtCmctMemberUtil.dailPhone(ppm);
			if(null!=result && "SUCCESS".equals(result.get("STATE"))){
				Map respMap=(Map<String, Object>)result.get("respMap");
				this.getOutputMsg().put("STATE", "SUCCESS");
				this.getOutputMsg().put("MSG", respMap.get("Sessionid"));
			}else{
				throw new Exception((String)result.get("MSG"));
			}
		} catch (Exception e) {
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", e.getMessage());	
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 挂断电话
	 */
	@RequestMapping(value="hookPhone")
	public void hookPhone(HttpServletResponse response){
		String sessionId=getString("sessionid");
		String appId=getString("appId");
		String passWd=getString("passWd");
		String spid=getString("spid");
		String httpUrl=getString("httpUrl");
		try {
			if(StringUtils.isEmpty(sessionId)){
				throw new Exception("会话的id不存在");
			}
			if(StringUtils.isEmpty(appId)){
				throw new Exception("appId不存在");
			}
			PhoneMember ppm=new PhoneMember();
			ppm.setSessionId(sessionId);
			ppm.setOrgInterfaceId(appId);
			ppm.setPassWd(passWd);
			ppm.setSpid(spid);
			ppm.setHttpUrl(httpUrl);
			Map<String, Object>result=FjCtCmctMemberUtil.hookPhone(ppm);
			if(null!=result && "SUCCESS".equals(result.get("STATE"))){
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "挂断成功");
			}else{
				throw new Exception((String)result.get(FjCtCmctMemberUtil.TIP_KEY_MSG));
			}
		} catch (Exception e) {
			this.getOutputMsg().put("STATE", "FAIL");
			this.getOutputMsg().put("MSG", e.getMessage());	
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	

	/**
	 * 定时读取通话状态
	 */
	@RequestMapping(value="getCallStatus")
	public void getCallStatus(HttpServletResponse response){
		
		String id=getString("id","");
		
		ApiCallBack apiCallBack = CtPhoneSession.get(id);
		
		PrintWriter pw=null;
		try {
			pw=response.getWriter();
		} catch (IOException e) {
//			System.out.println(e.getMessage());
		}
		if(null==apiCallBack || StringUtils.isEmpty(apiCallBack.getvCallState())){
			pw.write("NOCALLSTATUS,0,0");
		}else{
			pw.write(apiCallBack.getvCallState()+","+apiCallBack.getStatusRingCount()+","+apiCallBack.getStatusAnswCount());
		}		
		pw.flush();
		pw.close();
	}
	
	
	/**
	 *获取归属地 
	 */
	@RequestMapping(value="getHcode")
	public void getHcode(HttpServletResponse response){
		String phone=getString("phone");
		Map<String, Object>result = FjCtCmctMemberUtil.hcodeSearch(phone);
		if(null!=result && "SUCCESS".equals(result.get("STATE"))){
			Map respMap=(Map<String, Object>)result.get("respMap");
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("province", respMap.get("Province"));
			getOutputMsg().put("city", respMap.get("City"));
			getOutputMsg().put("corp", respMap.get("Corp"));
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 测试
	 */
	@RequestMapping(value="ctPhoneSession")
	public void ctPhoneSession(){
	//	CtPhoneSession.test() ;
	}
	
	/**
	 * 记录电话错误日志
	 */
	@RequestMapping(value="recordErrorPhoneStatus")
	public void recordErrorPhoneStatus(HttpServletResponse response){
		String msg=getString("msg");
		String sessionId=getString("sessionId");
		PhoneDxCallErrorStatus errorMsg=new PhoneDxCallErrorStatus();
		errorMsg.setCreateTime(new Date());
		errorMsg.setSessionId(sessionId);
		errorMsg.setErrorMsg(msg);
		errorMsg.setUUID();
		try{			
			queryExecutor.executeInsert(PhoneDxCallErrorStatus.MAPPER+".insert", errorMsg);
			getOutputMsg().put("STATE", "SUCCESS");
		}catch(Exception e){
			e.printStackTrace();
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
