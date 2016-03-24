package com.wuyizhiye.cmct.sms.utils;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import Decoder.BASE64Encoder;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.ObjectCopyUtils;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSSendTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStatusEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStrategyTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.MessageReply;
import com.wuyizhiye.cmct.sms.model.SMSConfig;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.ShortMessage;

/**
 * @ClassName ShortMessageUtil
 * @Description 短信控制发送 工具类
 * @author li.biao
 * @date 2015-5-26
 */
public class ShortMessageUtil {
	
	//短信长度单位，超过70个字多发一条
	private static final int msg_unit = 70 ;
	
	//是否开启短信服务
	private static final String SMS_SERVICE_OPEN = "SMS_SERVICE_OPEN" ;
	
	private static final String SEND_SUCC = "发送成功" ;
	
	private static QueryExecutor queryExecutor ;
	
	
	/**
	 * 发送短信 (单个发送：不支持群发,短信内容不能超过70个字)
	 * @param shortMessage 
	 * controlType 控制类型  个人、部门、公司    (必填)
	 * type 短信类型  业务、广告   (必填)
	 * senderId 发送人   (必填)
	 * senderPhoneNum 发送手机号码    
	 * receiverName 接收人名称     (只能一个名称)    (必填)
	 * receiverPhoneNum 接收人手机号码   (只能一个接收号码)   (必填)
	 * content 短信内容:  每条短信不能超过70个字  (必填)
	 * sendTime 发送时间    (为空时立即发送，不为空时 定时发送)
	 * 
	 * @return resultMsg  成功提示或异常提示
	 */
	public static String sendOneMessage(ShortMessage sms){
		String resultMsg = "";
		resultMsg = commonOneValidate(sms);
		if("SUCC".equals(resultMsg)){
			resultMsg = "";
		}else{
			//单条短信 验证
			//单个发送：不支持群发,短信内容不能超过70个字
			return resultMsg;
		}
		try {
			//发送短信事务控制
			resultMsg = sendMessageControl(sms);
		} catch (Exception e) {
			resultMsg = e.getMessage();
			if(StringUtils.isEmpty(resultMsg)){
				if(e.getStackTrace()!=null && e.getStackTrace().length>0){
					StackTraceElement stack = e.getStackTrace()[0];
					resultMsg = "className:"+stack.getClassName()+" --methodName:"+stack.getMethodName()+" -- lineNum:"+stack.getLineNumber();
				}
				
			}
		}
		if(StringUtils.isEmpty(resultMsg)){
			resultMsg = "";
		}
		 return resultMsg;
	}
	
	/**
	 * 发送短信
	 * @param shortMessage 
	 * controlType 控制类型  个人、部门、公司    (必填)
	 * type 短信类型  业务、广告   (必填)
	 * senderId 发送人   (必填)
	 * senderPhoneNum 发送手机号码    
	 * receiverName 接收人名称     (多个名称之间用半角逗号隔开)    (必填)
	 * receiverPhoneNum 接收人手机号码   (多个号码之间用半角逗号隔开)   (必填)
	 * content 短信内容:每超过70个字多发一条   (必填)
	 * sendTime 发送时间    (为空时立即发送，不为空时 定时发送)
	 * 
	 * @return resultMsg  成功提示或异常提示
	 */
	public static String sendMessage(ShortMessage sms){
		String resultMsg = "";
		
		try {
			//发送短信事务控制
			resultMsg = sendMessageControl(sms);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = e.getMessage();
			if(StringUtils.isEmpty(resultMsg)){
				if(e.getStackTrace()!=null && e.getStackTrace().length>0){
					StackTraceElement stack = e.getStackTrace()[0];
					resultMsg = "className:"+stack.getClassName()+" --methodName:"+stack.getMethodName()+" -- lineNum:"+stack.getLineNumber();
				}
				
			}
		}
		
		 return resultMsg;
	}
	
	/**
	 * 定时器调用  获取短信发送状态
	 */
	public static void fixedMesStatus(){
		try {
			ShortMessageUtil.getMessageStatus(SMSTypeEnum.AD_TYPE);
			ShortMessageUtil.getMessageStatus(SMSTypeEnum.BUSINESS_TYPE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取短信发送状态
	 * @throws Exception
	 */
	public static void getMessageStatus(SMSTypeEnum smsType)throws Exception{
		if(queryExecutor==null){
			queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
		}
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		//根据 控制类型 和 短信类型 取短信接口配置信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type",smsType);
		param.put("enableFlag",1);
		SMSConfig  smsConfig = queryExecutor.execOneEntity("com.wuyizhiye.cmct.sms.dao.SMSConfigDao.selectConfigOne",param, SMSConfig.class);
		String groupId = smsConfig.getGroupId();
		String accountName = smsConfig.getAccountName();
		String password = smsConfig.getPassword(); 
		String beanId = smsConfig.getBeanId(); 
		String methodName = "getMessageStatus";//接口方法 固定
		String url = smsConfig.getStatusUrl();//读取短信发送状态url
		
		//根据配置读 短信接口类和方法
		Method smsMethod = null;
		Object smsService = null;
		
		smsService = ApplicationContextAware.getApplicationContext().getBean(beanId);
		Method[] methods = smsService.getClass().getMethods();
		for(Method m:methods){
			if(methodName.equals(m.getName())){
				smsMethod = m;
				break;
			}
		}
		try {
			Object methodMsg = smsMethod.invoke(smsService, groupId,accountName,password, url);
			result= (List<Map<String, Object>>) (methodMsg==null?"":methodMsg) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(null != result && result.size()>0){//有回复的短信  将信息插入到数据库
			for(Map<String,Object> map:result){
				ShortMessage sm=new ShortMessage();
				sm.setReceiverPhoneNum(map.get("mobile").toString());
				sm.setTaskId(map.get("taskid").toString());
				String status=map.get("status").toString();//回复状态 10发送成功 20 发送失败
				if("10".equals(status)){
					sm.setStatus(SMSStatusEnum.SEND_SUCCESS);
				}else if("20".equals(status)){//发送失败
					/**
					 * 发送失败  返还短信余额
					 */
					Map<String,Object> kael=new HashMap<String, Object>();
					kael.put("mobile", map.get("mobile"));
					kael.put("taskid", map.get("taskid"));
					ShortMessage sg=queryExecutor.execOneEntity("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond", kael, ShortMessage.class);
					SMSControl sender=getPersonSMS(sg.getSenderId());//发送人  短信控制信息
					sender.setBalanceAmout(sender.getBalanceAmout()+sg.getSendSmsCount());//余额
					queryExecutor.executeUpdate("com.wuyizhiye.cmct.sms.dao.SMSControlDao.update", sender);
					sm.setStatus(SMSStatusEnum.SEND_FAIL);
				}
				queryExecutor.executeUpdate("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.updateMessageStatus", sm);//更新状态
			}
		}
	}
	
	/**
	 * 取对应人员的  短信控制信息
	 * @param id
	 * @return
	 */
	public static SMSControl getPersonSMS(String id){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", id);//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 查询短信回复数据
	 * @return
	 * @throws Exception
	 */
	public static void putReplyMessage(SMSTypeEnum smsType) throws Exception{
		if(queryExecutor==null){
			queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
		}
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		//根据 控制类型 和 短信类型 取短信接口配置信息
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type",smsType);
		param.put("enableFlag",1);
		SMSConfig  smsConfig = queryExecutor.execOneEntity("com.wuyizhiye.cmct.sms.dao.SMSConfigDao.selectConfigOne",param, SMSConfig.class);
		String groupId = smsConfig.getGroupId();
		String accountName = smsConfig.getAccountName();
		String password = smsConfig.getPassword(); 
		String beanId = smsConfig.getBeanId(); 
		String methodName = "getReplyData";//接口方法 固定
		String url = smsConfig.getReplyUrl();
		
		//根据配置读 短信接口类和方法
		Method smsMethod = null;
		Object smsService = null;
		
		smsService = ApplicationContextAware.getApplicationContext().getBean(beanId);
		Method[] methods = smsService.getClass().getMethods();
		for(Method m:methods){
			if(methodName.equals(m.getName())){
				smsMethod = m;
				break;
			}
		}
		try {
			Object methodMsg = smsMethod.invoke(smsService, groupId,accountName,password, url);
			result= (List<Map<String, Object>>) (methodMsg==null?"":methodMsg) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if(null != result && result.size()>0){//有回复的短信  将信息插入到数据库
			for(Map<String,Object> map:result){
				MessageReply mr=new MessageReply();
				mr.setId(UUID.randomUUID().toString());
				String mobile=map.get("mobile").toString();
				String taskid=map.get("taskid").toString();
				String content=map.get("content").toString();
				String receivetime=map.get("receivetime").toString();//回复时间
				mr.setMobile(mobile);
				mr.setType(smsType);
				mr.setTaskId(taskid);
				mr.setContent(content);
				mr.setReceivetime(df.parse(receivetime));
				queryExecutor.executeInsert("com.wuyizhiye.cmct.sms.dao.MessageReplyDao.insert", mr);
			}
		}
	}
	
	/**
	 * 发送短信
	 * @param shortMessage 
	 * controlType 控制类型  个人、部门、公司    (必填)
	 * type 短信类型  业务、广告   (必填)
	 * senderId 发送人   (必填)
	 * senderPhoneNum 发送手机号码    
	 * receiverName 接收人名称     (多个名称之间用半角逗号隔开)    (必填)
	 * receiverPhoneNum 接收人手机号码   (多个号码之间用半角逗号隔开)   (必填)
	 * content 短信内容:每超过70个字多发一条   (必填)
	 * sendTime 发送时间    (为空时立即发送，不为空时 定时发送)
	 * 
	 * @return resultMsg  成功提示或异常提示
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class) 
	private static String sendMessageControl(ShortMessage sms) throws Exception{
		String smsServiceOpen = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), SMS_SERVICE_OPEN);
		if(!"Y".equals(smsServiceOpen)){
			return "公司未开通短信服务";
		}
		
		if(queryExecutor==null){
			queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
		}
		String resultMsg = commonValidate(sms);
		if("SUCC".equals(resultMsg)){
			resultMsg = "";
		}else{
			//短信 基本信息不完整
			return resultMsg;
		}
		
		/*if(sms.getSendTime()==null){
			//如果发送时间 为空  则取当前时间  立即发送
			Calendar  cal  = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -5);
			sms.setSendTime(cal.getTime());
		}*/
		
		//设置发送人 编码和名称
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id",sms.getSenderId());
		Person  sender =  (Person)queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getById",param, Person.class);
		if(sender!=null){
			sms.setSenderNumber(sender.getNumber());
			sms.setSenderName(sender.getName());
			sms.setOrg(sender.getOrg());
		}else{
			resultMsg = "发送人不存在";
			return resultMsg ;
		}
		SMSControl smsControl = null;
		//短信 控制校验
		if(SMSControlTypeEnum.PERSONAL_TYPE.equals(sms.getControlType())){
			//如果 控制类型为个人  必有配置 短信控制
			Map<String,Object> resultMap = smsControl4Personal(sms);
			resultMsg = resultMap.get("state").toString();
			if("SUCC".equals(resultMsg)){
				resultMsg = "";
				smsControl = (SMSControl)resultMap.get("smsControl");
			}else{
				//短信 控制校验 不通过
				return resultMsg;
			}
		}
		//根据 控制类型 和 短信类型 取短信接口配置信息
		SMSConfig smsConfig = getSMSConfig(sms.getControlType(),sms.getType());
		String groupId = smsConfig.getGroupId();
		String accountName = smsConfig.getAccountName();
		String password = smsConfig.getPassword(); 
		String beanId = smsConfig.getBeanId(); 
		String methodName = smsConfig.getMethodName();
		String url = smsConfig.getUrl();
		if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(accountName) || StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(beanId) || StringUtils.isEmpty(methodName) || !smsConfig.getEnableFlag()){
			resultMsg = " "+sms.getType().getName()+ "短信服务配置不完整 或者未启用";
			throw new Exception(resultMsg);
		}
		
		//根据配置读 短信接口类和方法
		Method smsMethod = null;
		Object smsService = null;
		
		smsService = ApplicationContextAware.getApplicationContext().getBean(beanId);
		if(smsService==null){
			resultMsg = sms.getControlType().getName() + " "+sms.getType().getName()+ " 短信服务配置错误,beanId:"+beanId+"不存在";
			throw new Exception(resultMsg);
		}
		Method[] methods = smsService.getClass().getMethods();
		for(Method m:methods){
			if(methodName.equals(m.getName())){
				smsMethod = m;
				break;
			}
		}
		if(smsMethod==null){
			resultMsg = sms.getControlType().getName() + " "+sms.getType().getName()+ " 短信服务配置错误,methodName:"+methodName+"不存在";
			throw new Exception(resultMsg);
		}
		Map<String,Object> sendMap=new HashMap<String, Object>();
		//发送短信 
		//String groupId,String accountName,String password,String from,String to ,String content ,Date sendTime
		
		sms.setDescription(sms.getControlType().getName() + "--"+sms.getType().getName()+ ",beanId:"+beanId+"--methodName:"+ methodName+"--groupId:"+ groupId+"--accountName:"+ accountName);
		if(null==sms.getCreateTime()){
			sms.setCreateTime(new Date());
		}
		//判断短信的批次通道.同样的短信内容判断当前的短信的条数加上数据库里待发送的条数和短信配置的批次最低条数去比
		if(null!=smsConfig.getChannelType() && "BATCH_CHANNEL".equals(smsConfig.getChannelType().getValue())){
			sms.setSendType(SMSSendTypeEnum.BATCH_SMS);
			Map<String, Object>smsParam=new HashMap<String, Object>();
			smsParam.put("newDate", new Date());//创建时间小于当前时间的
			smsParam.put("content", sms.getContent());
			List<ShortMessage>dataBaseSmsList=queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.checkBatchSmsNum", smsParam, ShortMessage.class);
			StringBuffer sb=new StringBuffer();
			Integer databaseSmsLength=0;//同样短信类型数据库存储的条数
			if(null!=dataBaseSmsList){
				databaseSmsLength=dataBaseSmsList.size();
				for(ShortMessage sm:dataBaseSmsList){
					sb.append(sm.getReceiverPhoneNum()).append(",");
				}
			}
			
			Integer currentSmsLength=1;//当前短信发送的条数
			if(sms.getReceiverPhoneNum().indexOf(",")>0){
				String []rpSms= sms.getReceiverPhoneNum().split(",");
				currentSmsLength=rpSms.length;
			}
			
			Integer totalSmsLength=databaseSmsLength+currentSmsLength;//当前发送的短信条数和数据库待发送短信条数之和
			Integer configMinNum=smsConfig.getBatchMinNum();
			if(null==configMinNum){
				configMinNum=0;
			}
			if(totalSmsLength>=configMinNum){//当前短信的总条数大于或则等于批次最低条数
				sms.setSendTime(new Date());
				String rps=sms.getReceiverPhoneNum();
				if(sb.length()>0){
					sms.setReceiverPhoneNum(sb.toString()+sms.getReceiverPhoneNum());
				}
				//发送
				try {
					Object methodMsg = smsMethod.invoke(smsService, groupId,accountName,password, url,sms.getSenderPhoneNum(),sms.getReceiverPhoneNum(),sms.getContent(),sms.getSendTime());
					sendMap = (Map<String, Object>) (methodMsg==null?"":methodMsg);
					resultMsg=sendMap.get("state").toString();
					if("ok".equals(resultMsg)){
						resultMsg="发送成功";
						for(ShortMessage sm:dataBaseSmsList){//给数据库里面存储的数据做修改
							sm.setSendTime(new Date());
							sm.setTaskId(sendMap.get("taskId").toString());//任务ID
							queryExecutor.executeUpdate("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.update", sm);
						}
						sms.setReceiverPhoneNum(rps);
						sms.setSendTime(new Date());
						writeMsgDataBase(sms,sendMap.get("taskId").toString());
					}else{
						resultMsg=sendMap.get("MSG").toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
					resultMsg = "调用短信接口异常--beanId:"+beanId+"--methodName:"+ methodName;
					String errMsg = e.getStackTrace().toString();
					if(errMsg !=null && errMsg.length()>3000){
						try {
							//
							errMsg = errMsg.substring(0, 3000);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					OperationLogUtil.saveOperationLog(beanId+"."+methodName, "调用短信接口异常", errMsg, "失败");
					throw new Exception(resultMsg);
				}
			}else{//只做数据库不保存,不发送
				sms.setSendTime(null);
				writeMsgDataBase(sms,null);
				resultMsg="发送成功";
			}
			
		}else{
			//如果当前的接收人的电话号码的数量超过通道批次的最低数量,或则通道类型为标准通道的
			sms.setSendTime(new Date());
			if(null==sms.getSendType()){
				sms.setSendType(SMSSendTypeEnum.STANDARD_SMS);
			}
			try {
				Object methodMsg = smsMethod.invoke(smsService, groupId,accountName,password, url,sms.getSenderPhoneNum(),sms.getReceiverPhoneNum(),sms.getContent(),sms.getSendTime());
				sendMap = (Map<String, Object>) (methodMsg==null?"":methodMsg);
				resultMsg=sendMap.get("state").toString();
				if("ok".equals(resultMsg)){
					resultMsg="发送成功";
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMsg = "调用短信接口异常--beanId:"+beanId+"--methodName:"+ methodName;
				String errMsg = e.getStackTrace().toString();
				if(errMsg !=null && errMsg.length()>3000){
					//
					errMsg = errMsg.substring(0, 3000);
				}
				OperationLogUtil.saveOperationLog(beanId+"."+methodName, "调用短信接口异常", errMsg, "失败");
				throw new Exception(resultMsg);
			}
			if(!SEND_SUCC.equals(resultMsg)){
				throw new Exception(resultMsg);
			}
			//写入数据库
			writeMsgDataBase(sms,sendMap.get("taskId").toString());
		}
		if(smsControl!=null){
		  queryExecutor.executeUpdate("com.wuyizhiye.cmct.sms.dao.SMSControlDao.update", smsControl);
		}
		return resultMsg;
	}
	
	
	/**
	 * 根据 控制类型 和 短信类型 取短信接口配置信息
	 * @return
	 */
	private static SMSConfig getSMSConfig(SMSControlTypeEnum controlType,SMSTypeEnum smsType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type",smsType.toString());
		param.put("enableFlag",1);
		SMSConfig  config = queryExecutor.execOneEntity("com.wuyizhiye.cmct.sms.dao.SMSConfigDao.getSMSConfigByCon",param, SMSConfig.class);
		if(config==null){
			config = new SMSConfig();
		}	
		return config;
	}
	
	/**
	 * 短信 控制校验
	 * @param shortMessage
	 * @return
	 */
	private static Map<String,Object> smsControl4Personal(ShortMessage sms){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String resultMsg = "SUCC";
		resultMap.put("state", resultMsg);
		SMSControl smsControl = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", sms.getControlType());//控制类型 个人
		params.put("objectId", sms.getSenderId());//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		if(list==null || list.size()<1){
			
			resultMsg = sms.getSenderName()+"未开通短信控制";
			resultMap.put("state", resultMsg);
			return resultMap;
		}
		smsControl = list.get(0);
		String[] receiverPhoneNums = sms.getReceiverPhoneNum().split(",");
		//本次发送短信总条数(短信内容字数第超过70多发一条) = (单个号码短信条数)*接收电话号码数
		int totalAmount = (sms.getContent().length()/msg_unit) ;
		if(sms.getContent().length()<=msg_unit){
			totalAmount =1;
		}else if(sms.getContent().length()>msg_unit && sms.getContent().length()%msg_unit>0){
			totalAmount += 1;
		}
		totalAmount = totalAmount*receiverPhoneNums.length;
		//OperationLogUtil.saveOperationLog(totalAmount+'.'+sms.getContent().length()+"."+receiverPhoneNums.length, "短信发送条数", "总共"+totalAmount+"条短信", "失败");
		if(smsControl.getBalanceStrategyFlag()){
			//余额控制
			if(smsControl.getBalanceAmout()<1){
				resultMsg =  sms.getSenderName()+"的短信余额不足";
				resultMap.put("state", resultMsg);
				return resultMap;
			}
			if(totalAmount> smsControl.getBalanceAmout()){
				resultMsg =  "每超过70个字多发一条, " + sms.getSenderName()+"的短信余额不足";
				resultMap.put("state", resultMsg);
				return resultMap;
			}
		}
		if(smsControl.getLimitStrategyFlag()){
			//上限控制
			Date sendTime = sms.getSendTime()==null?(new Date()):sms.getSendTime();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try {
				sendTime = df.parse(df.format(sendTime));
			} catch (ParseException e) {
			}
			params.clear();
			params.put("senderId", sms.getSenderId());//发送人
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
				if((hasSendedCount+totalAmount)>=smsControl.getTopLimitAmount()){
					resultMsg = "本次发送"+totalAmount+"条短信,"+sms.getSenderName()+"超出"+ smsControl.getLimitStrategyType().getName()+"上限"+smsControl.getTopLimitAmount()+"条短信";
					resultMap.put("state", resultMsg);
					return resultMap;
				}
			}
			
		}
		
		if(smsControl.getBalanceStrategyFlag()){
			//启用余额控制时  发短信后  扣减余额
			smsControl.setBalanceAmout(smsControl.getBalanceAmout()- totalAmount);
			//queryExecutor.executeUpdate("com.wuyizhiye.cmct.sms.dao.SMSControlDao.update", smsControl);
			resultMap.put("smsControl", smsControl);
		}
		
		resultMap.put("state", resultMsg);
		return resultMap;
	}
	
	/**
	 * 基本信息验证
	 * @param sms
	 * @return
	 */
	private static String commonValidate(ShortMessage sms){
		String resultMsg = "SUCC";
		if(sms==null){
			resultMsg = "短信信息不完整";
		}
		if(sms.getControlType()==null){
			resultMsg += " 必须指定短信控制类型";
		}
		if(sms.getType()==null){
			resultMsg += " 必须指定短信类型";
		}
		if(StringUtils.isEmpty(sms.getSenderId())){
			resultMsg += " 必须指定发送人";
		}
		/*if(StringUtils.isEmpty(sms.getSenderNumber())){
			resultMsg += " 必须指定发送人编码";
		}
		if(StringUtils.isEmpty(sms.getSenderName())){
			resultMsg += " 必须指定发送人名称";
		}*/
		if(StringUtils.isEmpty(sms.getReceiverName())){
			resultMsg += " 必须指定接收人名称";
		}
		if(StringUtils.isEmpty(sms.getReceiverPhoneNum())){
			resultMsg += " 必须指定接收人号码";
		}
		if(StringUtils.isEmpty(sms.getContent())){
			resultMsg += " 必须填写短信内容";
		}
		if(!"SUCC".equals(resultMsg)){
			return resultMsg;
		}
		String[] receiverPhoneNums = sms.getReceiverPhoneNum().split(",");
		String[] receiverNames = sms.getReceiverName().split(",");
		if(receiverPhoneNums.length!= receiverNames.length){
			resultMsg += " 接收人号码与接收人名称不对应";
		}
		
		return resultMsg;
	}
	
	/**
	 * 单条发送  基本信息验证
	 * @param sms
	 * @return
	 */
	private static String commonOneValidate(ShortMessage sms){
		String resultMsg = "SUCC";
		if(sms==null){
			resultMsg = "短信信息不完整";
		}
		if(StringUtils.isEmpty(sms.getReceiverName())){
			resultMsg += " 必须指定接收人名称";
		}
		if(StringUtils.isEmpty(sms.getReceiverPhoneNum())){
			resultMsg += " 必须指定接收人号码";
		}
		
		if(StringUtils.isEmpty(sms.getContent())){
			resultMsg += " 必须填写短信内容";
		}
		
		if(!"SUCC".equals(resultMsg)){
			return resultMsg;
		}
		
		String[] receiverPhoneNums = sms.getReceiverPhoneNum().split(",");
		String[] receiverNames = sms.getReceiverName().split(",");
		if(receiverPhoneNums.length>1 || receiverNames.length>1){
			resultMsg += " 一次只能指定一个接收人号码与接收人名称";
		}
		
		/*if(sms.getContent().length()>msg_unit){
			resultMsg += " 每条短信内容不能超过70个汉字";
		}*/
		 
		return resultMsg;
	}
	
	public static String getSmsBalance(SMSTypeEnum smsType){
		String resultMsg = "";
		try{
			String smsServiceOpen = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), SMS_SERVICE_OPEN);
			if(!"Y".equals(smsServiceOpen)){
				return "公司未开通短信服务";
			}
			
			if(queryExecutor==null){
				queryExecutor = (QueryExecutor)ApplicationContextAware.getApplicationContext().getBean("queryExecutor");
			}
			
			//根据 控制类型 和 短信类型 取短信接口配置信息
			SMSConfig smsConfig = getSMSConfig(null,smsType);
			String groupId = smsConfig.getGroupId();
			String accountName = smsConfig.getAccountName();
			String password = smsConfig.getPassword(); 
			String beanId = smsConfig.getBeanId(); 
			String url = smsConfig.getUrl();
			if(StringUtils.isEmpty(groupId) || StringUtils.isEmpty(accountName) || StringUtils.isEmpty(password)
					|| StringUtils.isEmpty(beanId) || StringUtils.isEmpty(smsConfig.getMethodName()) 
					|| StringUtils.isEmpty(url)|| !smsConfig.getEnableFlag()){
				resultMsg = " "+smsType.getName()+ "短信服务配置不完整 或者未启用";
				throw new Exception(resultMsg);
			}
			
			//根据配置读 短信接口类和方法
			Method smsMethod = null;
			Object smsService = null;
			
			 smsService = ApplicationContextAware.getApplicationContext().getBean(beanId);
			if(smsService==null){
				resultMsg = " "+smsType.getName()+ " 短信服务配置错误,beanId:"+beanId+"不存在";
				throw new Exception(resultMsg);
			}
			String methodName = "getSmsBalance";//查短信账户余额
			Method[] methods = smsService.getClass().getMethods();
			for(Method m:methods){
				if(methodName.equals(m.getName())){
					smsMethod = m;
					break;
				}
			}
			if(smsMethod==null){
				resultMsg =  " "+smsType.getName()+ " 短信服务配置错误,methodName:"+methodName+"不存在";
				throw new Exception(resultMsg);
			}
			
			//发送短信 
			//String groupId,String accountName,String password,String from,String to ,String content ,Date sendTime
			try {
				Object methodMsg = smsMethod.invoke(smsService, groupId,accountName,password,url);
				resultMsg = methodMsg==null?"":methodMsg.toString() ;
			} catch (Exception e) {
				e.printStackTrace();
				resultMsg = "调用查询短信账号余额接口异常--beanId:"+beanId+"--methodName:"+ methodName;
				String errMsg = e.getStackTrace().toString();
				if(errMsg !=null && errMsg.length()>3000){
					//
					errMsg = errMsg.substring(0, 3000);
				}
				OperationLogUtil.saveOperationLog(beanId+"."+methodName, "调用短信接口异常", errMsg, "失败");
				throw new Exception(resultMsg);
			}
		}catch(Exception e){
			OperationLogUtil.saveOperationLog("", "调用查询短信账号余额接口异常", resultMsg, "失败");
		}
		 
		return resultMsg;
	}
	
	public static void writeMsgDataBase(ShortMessage sms,String taskId){
		//发送成功  写短信记录
		ShortMessage shortMsg = new ShortMessage();
		ObjectCopyUtils.copy(sms, shortMsg);
		shortMsg.setCreator(SystemUtil.getCurrentUser());
		String[] receiverPhoneNums = sms.getReceiverPhoneNum().split(",");
		String[] receiverNames = sms.getReceiverName().split(",");
		int sendSmsCount = (sms.getContent().length()/msg_unit) ;
		if(sms.getContent().length()<=msg_unit){
			sendSmsCount =1;
		}else if(sms.getContent().length()>msg_unit && sms.getContent().length()%msg_unit>0){
			sendSmsCount += 1;
		}
		for(int i=0;i<receiverPhoneNums.length;i++){
			String receiverPhoneNum = receiverPhoneNums[i];
			String receiverName = receiverNames[i];
			shortMsg.setReceiverPhoneNum(receiverPhoneNum);
			shortMsg.setReceiverName(receiverName);
			shortMsg.setSendSmsCount(sendSmsCount);
			shortMsg.setTaskId(taskId);//任务ID
			shortMsg.setStatus(SMSStatusEnum.SEND_ING);//发送中
			shortMsg.setUUID();//主键
			queryExecutor.executeInsert("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.insert", shortMsg);
		}
	}
	
	public static void main(String[] args) {
		String str="http://120.25.236.193:9980/wuyiyun/ivrCall.jsp";
		BASE64Encoder encoder = new BASE64Encoder();
	    String result = encoder.encode(str.getBytes());
//	   System.out.println(result);
	}
}
