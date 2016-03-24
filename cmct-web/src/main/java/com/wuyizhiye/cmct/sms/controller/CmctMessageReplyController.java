package com.wuyizhiye.cmct.sms.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStrategyTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.CmctContact;
import com.wuyizhiye.cmct.sms.model.MessageReply;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.MessageReplyService;
import com.wuyizhiye.cmct.sms.utils.ShortMessageUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CmctMessageReplyController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/messageReply/*")
public class CmctMessageReplyController extends ListController{
	
	@Autowired
	private MessageReplyService messageReplyService;

	@Override
	protected CoreEntity createNewEntity() {
		return new MessageReply();
	}

	@Override
	protected String getListView() {
		/**
		 * 将接口短信回复的数据读取到系统来
		 */
		try {
			ShortMessageUtil.putReplyMessage(SMSTypeEnum.AD_TYPE);//广告类
			ShortMessageUtil.putReplyMessage(SMSTypeEnum.BUSINESS_TYPE);//应用类
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cmct/sms/messageReply";
	}
	
	
	/**
	 * 读数据
	 */
	@RequestMapping("getData")
	public void getData(Pagination<MessageReply> pagination, HttpServletResponse response) {
		String page = getString("page");
		int currentPage = ""==page || null==page?1:Integer.parseInt(page);
		pagination.setCurrentPage(currentPage);
		pagination.setPageSize(10);
		pagination = queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		List<MessageReply> mlist=pagination.getItems();
		List<MessageReply> rlist=new ArrayList<MessageReply>();
		Map<String,Object> tem=new HashMap<String, Object>();
		for(MessageReply map:mlist){
			MessageReply mr=new MessageReply();
			String phone=map.getMobile();
			mr.setMobile(phone);
			/**
			 * 取最后一次短信回复内容
			 */
			getLastContent(phone,mr);
			
			/**
			 * 判断该号码是否 在当前人的 通讯录中
			 */
			tem.put("mobile", phone);
			List<CmctContact> clist=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.CmctContactDao.select", tem, CmctContact.class);
			if(null != clist && clist.size()>0){
				mr.setContact(clist.get(0));
			}
			
			rlist.add(mr);
		}
		pagination.setItems(rlist);
		if(pagination!=null){
			getRequest().setAttribute("pag", pagination);
			getRequest().setAttribute("totalpage", pagination.getPageCount());
			getRequest().setAttribute("curpage", pagination.getCurrentPage());
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}

	@Override
	protected String getEditView() {
		return "";
	}
	
	/**
	 * 根据一个电话号码  取得其最后一次回复的短信内容
	 * @param phone
	 * @param mr
	 */
	public void getLastContent(String phone,MessageReply mr){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String,Object> param=getListDataParam();
		param.put("phone", phone);
		List<MessageReply> rlist=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.MessageReplyDao.select", param, MessageReply.class);
		if(null != rlist && rlist.size()>0){
			mr.setContent(rlist.get(0).getContent());
			if(null != rlist.get(0).getReceivetime()){
				mr.setSendContent(df.format(rlist.get(0).getReceivetime()));
			}
		}
	}
	
	/**
	 * 详情页面
	 * @param model
	 * @return
	 */
	@RequestMapping("toDetail")
	public String toDetail(ModelMap model){
		String mobile=getString("mobile");//对应电话号码
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("mobile", mobile);
		List<CmctContact> clist=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.CmctContactDao.select", param, CmctContact.class);
		if(null != clist && clist.size()>0){
			model.put("contact", clist.get(0));
		}
		List<MessageReply> rlist=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.MessageReplyDao.getOnePhoneDetail", param, MessageReply.class);
		model.put("showList", rlist);
		model.put("mobile", mobile);
		getLastNum(model);
		return "cmct/sms/messageOnePage";
	}
	
	public void getLastNum(ModelMap model){
		SMSControl smsControl = null;
		Person person=SystemUtil.getCurrentUser();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", person.getId());//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		if(list==null || list.size()<1){
			model.put("lastNum", 0);
			model.put("lastNumName", "余额");
		}else{
			smsControl = list.get(0);
			if(smsControl.getBalanceStrategyFlag()){//是余额控制
				//余额控制
				model.put("lastNum", smsControl.getBalanceAmout());
				model.put("lastNumName", "余额");
			}
			params.clear();
			params.put("senderId", person.getId());//发送人
			if(smsControl.getLimitStrategyFlag()){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date sendTime=new Date();
				if(SMSStrategyTypeEnum.BY_DAY_TYPE.equals(smsControl.getLimitStrategyType())){
					//上限控制类型 为按日
					params.put("sendDate", df.format(sendTime));
				}else{
					Date firstDay = DateUtil.getFirstDay(sendTime);
					Date lastDay = DateUtil.getLastDay(sendTime);
					params.put("queryStartDate", df.format(firstDay));
					params.put("queryEndDate", df.format(lastDay));
				}
				List<ShortMessage> msgList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond", params, ShortMessage.class);
				if(msgList!=null){
					int hasSendedCount = 0 ;
					for(ShortMessage msg:msgList){
						if(null != msg.getSendSmsCount()){
							hasSendedCount += msg.getSendSmsCount();
						}
					}
					model.put("lastNum", (smsControl.getTopLimitAmount()-hasSendedCount));
					model.put("lastNumName", "剩余");
				}
				
			}
		}
	}
	
	@RequestMapping("send")
	@ResponseBody
	public void send(HttpServletResponse response){
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String content=getString("content");//内容
		String mobile=getString("mobile");//接收号码
		String result=send(content,mobile);
		if("发送成功".equals(result)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功");
			getOutputMsg().put("now", df.format(new Date()));
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", result);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 发送
	 * @param content  内容
	 * @param getId  接收人ID
	 * @param getPhone 接收人电话
	 */
	public String send(String content,String getPhone){
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
	    sm.setReceiverName("外界人员");//接收人姓名
	    sm.setReceiverPhoneNum(getPhone);//接收人电话
	    sm.setContent(content);//短信内容
	    sm.setCreateTime(new Date());
	   String result= ShortMessageUtil.sendOneMessage(sm);//发送操作
	   return result;
	}
	
	

	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("sendId", SystemUtil.getCurrentUser().getId());
		return param;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.cmct.sms.dao.MessageReplyDao.selectReplyPhones";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return messageReplyService;
	}
}
