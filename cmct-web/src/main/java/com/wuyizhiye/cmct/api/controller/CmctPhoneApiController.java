package com.wuyizhiye.cmct.api.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.enums.CommonFlagEnum;
import com.wuyizhiye.cmct.phone.constant.PhoneConstant;
import com.wuyizhiye.cmct.phone.model.ApiCallBack;
import com.wuyizhiye.cmct.phone.model.PhoneCallRecorDing;
import com.wuyizhiye.cmct.phone.model.PhoneMarket;
import com.wuyizhiye.cmct.phone.model.PhoneMarketBack;
import com.wuyizhiye.cmct.phone.model.PhoneMember;
import com.wuyizhiye.cmct.phone.service.PhoneCallRecorDingService;
import com.wuyizhiye.cmct.phone.service.PhoneMarketBackService;
import com.wuyizhiye.cmct.phone.util.CtPhoneSession;
import com.wuyizhiye.cmct.phone.util.FjCtCmctMemberUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName CmctPhoneApiController
 * @Description 通讯平台 -- 外部开放接口控制器
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="api/*")
public class CmctPhoneApiController extends BaseController {
	
	public static Logger log =Logger.getLogger(CmctPhoneApiController.class); 
		
	@Autowired
	private PhoneCallRecorDingService phoneCallRecorDingService;
	
	@Autowired
	private PhoneMarketBackService phoneMarketBackService;
	
	@RequestMapping(value="/cmct/callStatus")
	public void callStatus(HttpServletResponse response){
		log.error("FJCT-CALL-STATUS-"+DateUtil.convertDateToStr(new Date(), DateUtil.GENERAL_FORMHMS)+":sessionId:"+getString("vSessionsId")+"---callState:"+getString("vCallState"));
		
		/*PhoneDxCallLog dxCallLog=new PhoneDxCallLog();
		dxCallLog.setSessionId(getString("vSessionsId"));
		dxCallLog.setStatus(PhoneDxLogEnum.CALLSTATUS);
		dxCallLog.setLogDetail(getString("vCallState"));*/
		
		//回调报告类型 1、 呼叫状态
		String vType = getString("vType","");						
		
		//能力类型 Dial:点击拨号  Talks:多方通话  TalksV2:智能多方通话 VoiceNotice:语音通知 VoiceNotice2：问卷统计Marketing：营销接口等
		String vServiceType = getString("vServiceType","");
		
		//通话标志
		String vSessionsId = getString("vSessionsId","");
		
		//主叫号码
		String vCallerNbr = getString("vCallerNbr","");
		
		//原被叫
		String vCalleeNbr = getString("vCalleeNbr","");
		
		//IMS状态 NEW:发起呼叫 RINGING:振铃 ANSWERING:SDP协商中（振铃后可能会有多次SDP协商）  ANSWERED:接听 DISCONNECTED:挂机 FAILED:呼叫失败（可能是挂机、无应答或拒绝）CANCEL:主叫侧取消呼叫

		String vCallState = getString("vCallState","");
		
		//触发类型： 0:向上能力  1:被叫触发
		String vIsincomingcall = getString("vIsincomingcall","");
		
		//IMS状态产生时间 格式：yyyyMMddHHmmss
		String vStateTime = getString("vStateTime","");
		
		if(PhoneConstant.CallVType.callStatus.equals(vType)|| "1".equals(vType)){
			ApiCallBack callB = new ApiCallBack() ;
			callB.setvType(vType);
			callB.setvServiceType(vServiceType);
			callB.setvSessionsId(vSessionsId);
			callB.setvCallerNbr(vCallerNbr);
			callB.setvCalleeNbr(vCalleeNbr);
			callB.setvCallState(vCallState);
			callB.setvIsinconmingcall(vIsincomingcall);
			callB.setvStateTime(vStateTime);
			CtPhoneSession.add(callB);
		}
		try {
			response.getWriter().write("success");			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			getSession().invalidate();
			//this.phoneDxCallLogService.addEntity(dxCallLog);//保存日志
		}
	}
	
	/**
	 * 读取通话状态
	 */
	@RequestMapping(value="/cmct/getCallStatus")
	public void getCallStatus(HttpServletResponse response){
		
		response.addHeader("Access-Control-Allow-Origin", "*");//允许所有的外部请求读走该数据
		
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
	
	//录音时查看
	@RequestMapping(value="/cmct/callRecord")
	public void callRecord(HttpServletResponse response){
		//log.error("FJCT-CALL-RECORD-1:"+getString("vRecordUrl"));
		
		/*PhoneDxCallLog dxCallLog=new PhoneDxCallLog();
		dxCallLog.setSessionId(getString("vSessionId"));
		dxCallLog.setStatus(PhoneDxLogEnum.RECORDSTATUS);
		dxCallLog.setLogDetail(getString("vRecordUrl"));*/
		
		//主叫号码
		String callerNumber=getString("vCaller","");
		
		//被叫号码
		String calleeNumber=getString("vCallee","");
		
		//sessionId  vSessionId
		String sessionId=getString("vSessionId","");
		
		//slowRdUrl  录音临时地址
		String slowRdUrl=getString("vRecordUrl","");//http://202.109.211.100:8088/voice/Dial/common_djbh/Dial_20140421114506_013510628424_018674635215_21906832598445251706579284579195.wav
		
		if(StringUtils.isEmpty(sessionId)){
			sessionId=getSessionId(slowRdUrl);
		}
		
		//回调报告类型 14、 录音状态
		String vType = getString("vType","");	
		
		//录音时间
		
		if(PhoneConstant.CallVType.CallRecorDing.equals(vType)){
			
			/**
			 * 如果设置了数据中心.设置当前的数据中心,
			 */
			String currDataSource=getString("dataCenter");
			if(!StringUtils.isEmpty(currDataSource)){
				DataSourceHolder.setDataSource(currDataSource);
			}
			
			PhoneCallRecorDing rd=new PhoneCallRecorDing();
			
			/**
			 * 去除重复
			 */
			boolean isExist=false;
			if(!StringUtils.isEmpty(sessionId)){
				Map<String, Object>param=new HashMap<String, Object>();
				param.put("sessionsId", sessionId);
				param.put("slowRdUrl", slowRdUrl);
				List<PhoneCallRecorDing>pcrds=queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneCallRecorDingDao.select", param, PhoneCallRecorDing.class);
				if(null!=pcrds && pcrds.size()>0){
					isExist=true;
					rd=pcrds.get(0);
				}
			}
			
			/**
			 * 设置计费号码
			 */
			if(!StringUtils.isEmpty(callerNumber)){
				String likeShowPhone=callerNumber.substring(2, callerNumber.length());//截掉前面两位,因为固话和手机接口推送过来的主显号码和数据库的showPhone不一样,
				Map<String,Object> condMap = new HashMap<String,Object>();
				condMap.put("phoneType", "HW");
				condMap.put("likeShowPhone", likeShowPhone);
				List<PhoneMember>showPhones=queryExecutor.execQuery(PhoneMember.MAPPER+".select", condMap, PhoneMember.class);
				if(null!=showPhones && showPhones.size()>0){
					PhoneMember member=showPhones.get(0);
					if(null!=member.getIsAllot() && "YES".equals(member.getIsAllot().getValue())){
						rd.setCostNumber(member.getShowPhone());
					}else{
						rd.setCostNumber(member.getNewPhone());
					}
					
				}
			}
			rd.setCallerNumber(callerNumber);
			rd.setCalleeNumber(calleeNumber);
			rd.setSessionId(sessionId);
			rd.setSlowRdUrl(slowRdUrl);
			rd.setDownStatus(CommonFlagEnum.NO.getValue());//未下载
			rd.setCallRdDate(new Date());
			if(isExist){
				phoneCallRecorDingService.updateEntity(rd);
			}else{
				phoneCallRecorDingService.addEntity(rd);
			}
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				//this.phoneDxCallLogService.addEntity(dxCallLog);//保存日志
			}
		}
		
	}
	
	/**
	 * 批量营销状态推送
	 * @throws Exception 
	 */
	@RequestMapping(value="/cmct/callMarket")
	public void callMarket(HttpServletResponse response) throws Exception{
		//log.error("FJCT-CALL-MARKET-"+DateUtil.convertDateToStr(new Date(), DateUtil.GENERAL_FORMHMS)+":vWorkID:"+getString("vWorkID")+"---callState:"+getString("vCallState"));
		String vType=getString("vType");
		String vWorkId=getString("vWorkID");
		String vCallState=getString("vCallState");
		String vSendCount=getString("vSendCount");
		String vStateTime=getString("vStateTime");
		PhoneMarketBack pmb=new PhoneMarketBack();
		pmb.setUUID();
		pmb.setType(vType);
		pmb.setWorkID(vWorkId);
		pmb.setStateTime(getDateFormatStr(vStateTime));
		pmb.setSendCount(vSendCount);
		pmb.setCreateTime(new Date());
		pmb.setCallState(vCallState);
		if(!StringUtils.isEmpty(vWorkId)){
			Map<String, Object>param=new HashMap<String, Object>();
			param.put("workId", vWorkId);
			List<PhoneMarket>pms=queryExecutor.execQuery(PhoneMarket.MAPPER+".select", param, PhoneMarket.class);
			if(null!=pms && pms.size()>0){
				pmb.setPhoneMarket(pms.get(0));
			}
		}
		this.phoneMarketBackService.addEntity(pmb);
		try {
			response.getWriter().write("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getSessionId(String url){
		if(!StringUtils.isEmpty(url)){
			String dailUrl=url.substring(url.lastIndexOf("Dial_"), url.length());
			String [] urlArr=dailUrl.split("_");
			for(String str:urlArr){
				if(str.contains(".wav")){
					String sessionId=str.replace(".wav", "");
					return sessionId;
				}
			}
		}
		return null;
	}
	
	@RequestMapping(value="/cmct/ivrCall")
	public void ivrCall(HttpServletResponse response){
		
		//主叫号码
		String vCallerNbr=getString("vCallerNbr","");
		
		//被叫号码
		String vCalleeNbr=getString("vCalleeNbr","");
		
		//sessionId  vSessionId
		String vSessionId=getString("vSessionId","");
		
		/**
		 * 根据主叫号码,找到关联的转接号码,事先要将主叫号码和转接号码在系统绑定
		 */
		String showPhone=vCallerNbr;
		if(!StringUtils.isEmpty(showPhone)&&showPhone.startsWith("86")){
			showPhone="0"+vCallerNbr.substring(2, vCallerNbr.length());
		}
		Map<String, Object>param=new HashMap<String, Object>();
		param.put("showPhone", showPhone);
		List<PhoneMember>pms=queryExecutor.execQuery(PhoneMember.MAPPER+".select", param, PhoneMember.class);
		try {
			if(null!=pms && pms.size()>0){
				response.getWriter().write("0;"+pms.get(0).getShowPhone());//返回转接的号码
			}else{
				response.getWriter().write("-1");//错误返回值
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Date getDateFormatStr(String strDate) throws ParseException{
		if(StringUtils.isEmpty(strDate)){
			return null;
		}
		SimpleDateFormat simp=new SimpleDateFormat("yyyyMMddhhmmss");
		return simp.parse(strDate);
	}
	
	//用于51置业拨打电话
	@RequestMapping(value="/cmct/wyzy/dailPhone")
	public void dailPhone(HttpServletResponse response,ModelMap model){
		String phoneNumber = getString("phoneNumber");
		Map<String, Object> map = FjCtCmctMemberUtil.dailMobilePhone(phoneNumber,"wyzy","");
		outPrint(response, JSONObject.fromObject(map));
	}
	
	//用于鼎尖官网拨打电话
	@RequestMapping(value="/webSite/dialPhone")
	public void webSiteDialPhone(HttpServletResponse response){
		DataSourceHolder.setDataSource("dataSource_YSHJ");
		String phoneNumber = getString("phoneNumber");
		Map<String, Object> map = FjCtCmctMemberUtil.dailMobilePhone(phoneNumber,"wyzy","");
		outPrint(response, JSONObject.fromObject(map));
	}
}
