package com.wuyizhiye.cmct.sms.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSSendTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStrategyTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.ContactPerson;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.SMSTemplate;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.SMSTemplateService;
import com.wuyizhiye.cmct.sms.utils.ShortMessageUtil;
import com.wuyizhiye.cmct.utils.ExcelReader;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.util.Constant;

/**
 * @ClassName CmctNoteManagerController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping("cmct/note/*")
public class CmctNoteManagerController extends BaseController{
	
	@Autowired
	private SMSTemplateService sMSTemplateService;
	/**
	 * 号码导入
	 * @param model
	 * @return
	 */
	@RequestMapping("excelOther")
	public String excelOther(ModelMap model){
		return "cmct/sms/excelOther";
	}
	
	/**
	 * 导入方法
	 */
	@RequestMapping("saveExcelOther")
	@ResponseBody
	public void saveExcelOther(@RequestParam(value="filePath")MultipartFile file,
			HttpServletRequest request,HttpServletResponse response){
		List<String> result=new ArrayList<String>();
		if(!file.isEmpty()){
			int rowNum = 1 ;
			try{
				String originalFileName  = file.getOriginalFilename();

				if(!originalFileName.endsWith(".xls") && !originalFileName.endsWith(".xlsx")){
					getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
					getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入文件格式不对，请使用.xls或者.xlsx模板文件");
				}else{
					//读取Excel数据
					List<String[]> excelData = ExcelReader.getExcelData(file.getInputStream(), 1,originalFileName.endsWith(".xls"));
					if(excelData!=null && excelData.size()>0){
						List<String> exceptionMsg = new ArrayList<String>();
						String nowDateStr = DateUtil.dateConvertStr(DateUtil.getCurDate(),"yy-MM-dd HH:mm");
						nowDateStr = nowDateStr.replaceAll("[^0-9]", "");
						for(int i = 0 ; i < excelData.size() ; i ++ ){
							String[] data =  excelData.get(i);
							if(judgeData(data)){
								if(data[0] == null || "".equals(data[0])){
									exceptionMsg.add("第"+(i+1)+"行电话号码空！");
								}else{
									String cp = data[0] ;
									if(cp.contains("E")){//处理科学计数法
										BigDecimal bd = new BigDecimal(cp); 
										cp = bd.toPlainString();
									}
									result.add(cp);
								}
								
							}else{
								break;
							}
						}
						
						if(exceptionMsg!=null && exceptionMsg.size() == 0){
							
							getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_SUCCESS);
							getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入数据成功");
						}else{
							getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
							getOutputMsg().put("EXCEPTION_MSG", exceptionMsg);
						}
					}else{
						getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
						getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入数据为空");
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
				getOutputMsg().put(Constant.RESULT_MSG_KEY, "导入数据出现异常，rowNum = "+rowNum+" 信息："+e.getMessage());
			}
		}else{
			getOutputMsg().put(Constant.RESULT_STATUS_KEY, Constant.RESULT_STATUS_FAIl);
			getOutputMsg().put(Constant.RESULT_MSG_KEY, "文件路径不存在");
		}
		response.setContentType("text/html");
		getOutputMsg().put("result", result);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	public boolean judgeData(String[] data){
		boolean flag=true;
		if(data[0] == null){
			flag=false;
		}
		return flag;
	}
	
	/**
	 * 发送短信  总页面
	 */
	
	@RequestMapping("totalPage")
	public String totalPage(ModelMap model){
		return "cmct/sms/messageTotal";
	}
	
	/**
	 * 短信赠送
	 */
	@RequestMapping("giveMessage")
	public String giveMessage(ModelMap model){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", SystemUtil.getCurrentUser().getId());//控制类型 个人
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		boolean flag=false;//只有开通短信控制 以及 类型为  余额控制的 才可以
		if(null != list && list.size()>0){//接受人 有 开通短信控制
			SMSControl sc=list.get(0);//可以给该人赠送短信
			if(sc.getBalanceStrategyFlag()){
				flag=true;
			}
		}
		if(flag){
			model.put("flag", "yes");
		}else{
			model.put("flag", "no");
		}
		
		return "cmct/sms/giveMessage";
	}
	
	/**
	 * 查询个人 发送短信详情
	 */
	@RequestMapping("searchMessage")
	public String searchMessage(ModelMap model){
		return "cmct/sms/personMessageShow";
	}
	
	
     
	/**
	 * 此处是 各个分页面 点击 发送短信  区分从 首页工具栏中点击进入 短信详情页面
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("topicMessage")
	public String topicMessage(ModelMap model){
		model.put("closeFlag", "oneStep");//关闭 一级父窗口
		getLastNum(model);
		String personId=getString("personId");//收信人
		String roomListingId=getString("roomListingId");//楼盘ID
		String customerId=getString("customerId");//客户Id
		String transferId=getString("transferId");//过户单据ID
		String resourceId=getString("resourceId");//资源客ID
		String intentionId=getString("intentionId");//意向客ID
		model.put("companySign",  ParamUtils.getParamValue("COMPANYSIGN"));//公司签名
		
		/**
		 * 如果楼盘ID 不为空 即要根据 楼盘ID 查询所有业主
		 */
		Map<String,Object> param=new HashMap<String, Object>();
		if(null != resourceId && !"".equals(resourceId)){//盘源查询页面 发送短信给 业主
			param.put("id", resourceId);
			Map one=queryExecutor.execOneEntity("com.wuyizhiye.fastsale.dao.ResourceCustomerDao.getResourceCustomerOne", param, Map.class);
			model.put("defaultOther", one.get("PHONE"));
			/**
			 * 默认 选择第三个(外界)页签
			 */
			model.put("defaultTabs", "out");
		}
		
		/**
		 * 如果楼盘ID 不为空 即要根据 楼盘ID 查询所有业主
		 */
		if(null != intentionId && !"".equals(intentionId)){//盘源查询页面 发送短信给 业主
			param.put("id", intentionId);
			Map one=queryExecutor.execOneEntity("com.wuyizhiye.fastsale.dao.IntentionCustomerDao.getIntentionOne", param, Map.class);
			model.put("defaultOther", one.get("PHONE"));
			/**
			 * 默认 选择第三个(外界)页签
			 */
			model.put("defaultTabs", "out");
		}
		
		/**
		 * 如果楼盘ID 不为空 即要根据 楼盘ID 查询所有业主
		 */
		if(null != roomListingId && !"".equals(roomListingId)){//盘源查询页面 发送短信给 业主
			param.put("roomListingId", roomListingId);
			List<Map> olist=queryExecutor.execQuery("com.wuyizhiye.broker.dao.OwnerDao.getOwnerMap", param, Map.class);
			model.put("ownerList", olist);
			/**
			 * 默认 选择第二个(同事)页签
			 */
			model.put("ownerTag", "yes");
			model.put("defaultTabs", "owner");
		}
		/**
	     * 过户 页面 发送短信
	     */
		if(null != transferId && !"".equals(transferId)){
			/**
			 * 过户 发送短信 需调一个回调函数
			 */
			model.put("transferId", transferId);
			model.put("tranferType", getString("type"));
			param.put("transferId", transferId);
			param.put("type", getString("type"));//区分 业主Or客户
			List<Map> clist=queryExecutor.execQuery("com.wuyizhiye.transfer.dao.TransferMenberDao.selectByCondMessage", param, Map.class);
			model.put("tranList", clist);
			if("CUSTOMER".equals(getString("type"))){
				model.put("customerTag", "yes");
				model.put("defaultTabs", "customer");
			}else{
				model.put("ownerTag", "yes");
				model.put("defaultTabs", "owner");
			}
		}
		if(null != customerId && !"".equals(customerId)){//客户查看页面  给联系人发短信
			param.put("customer", customerId);
		    List<ContactPerson> clist=queryExecutor.execQuery("com.wuyizhiye.broker.dao.ContactPersonDao.select", param, ContactPerson.class);
		    model.put("concatList", clist);
		    /**
			 * 默认 选择第二个(同事)页签
			 */
		    model.put("customerTag", "yes");
			model.put("defaultTabs", "customer");
		}
		if(null != personId && !"".equals(personId)){
			param.put("id", personId);
			Person accept=queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.select", param, Person.class);
			model.put("defaultMate", accept);
			/**
			 * 默认 选择第二个(同事)页签
			 */
			model.put("defaultTabs", "mate");
		}
		return "cmct/sms/messageDetail";
	}
	
	@RequestMapping(value="topicMessageNew")
	public String topicMessageNew(ModelMap model){
		String resourceId=getString("resourceId");//资源客ID
		String intentionId=getString("intentionId");//意向客ID
		Map<String,Object> param=new HashMap<String, Object>();
		if(null != resourceId && !"".equals(resourceId)){
			param.put("id", resourceId);
			Map one=queryExecutor.execOneEntity("com.wuyizhiye.fastsale.dao.ResourceCustomerDao.getResourceCustomerOne", param, Map.class);
			model.put("defaultOther", one.get("PHONE"));
		}
		if(null != intentionId && !"".equals(intentionId)){
			param.put("id", intentionId);
			Map one=queryExecutor.execOneEntity("com.wuyizhiye.fastsale.dao.IntentionCustomerDao.getIntentionOne", param, Map.class);
			model.put("defaultOther", one.get("PHONE"));
		}
		
		List<SMSTemplate>sts=queryExecutor.execQuery(SMSTemplate.MAPPER+".select", null, SMSTemplate.class);
		if(null!=sts && sts.size()>0){
			SMSTemplate st=getSmsTemValue(sts.get(0));
			model.put("smsTem", st);
		}
		
		model.put("sts", sts);
		return "cmct/sms/messageDetailNew";
	}
	
	/**
	 * 匹配短信模板上的内容
	 * @param s
	 * @return
	 */
	public SMSTemplate getSmsTemValue(SMSTemplate s){
		SMSTemplate sts=new SMSTemplate();
		if(null==s){
			return sts;
		}
		String contnet=s.getSmsContent();
		if(StringUtils.isEmpty(contnet)){
			return s;
		}
		if(contnet.indexOf("#personNumber#")>0){
			contnet=contnet.replace("#personNumber#",getCurrentUser().getNumber());
		}
		if(contnet.indexOf("#personName#")>0){
			contnet=contnet.replace("#personName#",getCurrentUser().getName());
		}
		if(contnet.indexOf("#personPhone#")>0){
			contnet=contnet.replace("#personPhone#",getCurrentUser().getPhone());
		}
		if(contnet.indexOf("#companySign#")>0){
			contnet=contnet.replace("#companySign#",ParamUtils.getParamValue("COMPANYSIGN"));
		}
		sts.setSmsContent(contnet);
		return sts;
	}
	
	@RequestMapping(value="getSmsContent")
	public void getSmsContent(HttpServletResponse response){
		String id=getString("id");
		if(!StringUtils.isEmpty(id)){
			SMSTemplate sm=sMSTemplateService.getEntityById(id);
			SMSTemplate smt=getSmsTemValue(sm);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("sms", smt.getSmsContent());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 此处用于普通 art谈车  发送短信页面
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("message")
	public String sendThree(ModelMap model){
		String personId=getString("personId");//收信人
		String roomListingId=getString("roomListingId");//楼盘ID
		String customerId=getString("customerId");//客户Id
		String transferId=getString("transferId");//过户单据ID
		model.put("companySign",  ParamUtils.getParamValue("COMPANYSIGN"));//公司签名
		/**
		 * 如果楼盘ID 不为空 即要根据 楼盘ID 查询所有业主
		 */
		Map<String,Object> param=new HashMap<String, Object>();
		if(null != roomListingId && !"".equals(roomListingId)){//盘源查询页面 发送短信给 业主
			param.put("roomListingId", roomListingId);
			List<Map> olist=queryExecutor.execQuery("com.wuyizhiye.broker.dao.OwnerDao.getOwnerMap", param, Map.class);
			model.put("ownerList", olist);
			if(null != olist && olist.size() == 1){//只有一条数据时  不显示 复选框
				model.put("onlyOne", olist.get(0));
				model.put("onlyOneVal", olist.get(0).get("PHONENUMBER"));
			}
		}
		if(null != customerId && !"".equals(customerId)){//客户查看页面  给联系人发短信
			param.put("customer", customerId);
		    List<ContactPerson> clist=queryExecutor.execQuery("com.wuyizhiye.broker.dao.ContactPersonDao.select", param, ContactPerson.class);
		    model.put("concatList", clist);
		    if(null != clist && clist.size() == 1){//只有一条数据时  不显示 复选框
				model.put("onlyOne", clist.get(0));
				model.put("onlyOneVal", clist.get(0).getMobile());
			}
		}
	    /**
	     * 过户 页面 发送短信
	     */
		if(null != transferId && !"".equals(transferId)){
			param.put("transferId", transferId);
			param.put("type", getString("type"));//区分 业主Or客户
			List<Map> clist=queryExecutor.execQuery("com.wuyizhiye.transfer.dao.TransferMenberDao.selectByCondMessage", param, Map.class);
			model.put("tranList", clist);
			if(null != clist && clist.size() == 1){//只有一条数据时  不显示 复选框
				model.put("onlyOne", clist.get(0));
				model.put("onlyOneVal", clist.get(0).get("MOBILE"));
			}
		}
		if(null != personId && !"".equals(personId)){//针对个人 发送短信
			param.put("id", personId);
			Person accept=queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.select", param, Person.class);
			model.put("accept", accept);
		}
		return "cmct/sms/messagePage";
	}
	
	/**
	 * 发送短信 详细页面 (可 发送 自己/同事/外界)  (首页 工具 点击进入 )
	 * @param model
	 * @return
	 */
	@RequestMapping("detailPage")
	public String detailPage(ModelMap model){
		getLastNum(model);
		model.put("closeFlag", "twoStep");//关闭二级父窗口
		model.put("companySign",  ParamUtils.getParamValue("COMPANYSIGN"));//公司签名
		return "cmct/sms/messageDetail";
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
	
	/**
	 * 发送 操作(详细 可发三种 对象 )
	 */
	@RequestMapping("send")
	@ResponseBody
	public void send(HttpServletResponse response){
		String selectType=getString("selectType");//选择的类型 (自己self/同事mate/外界out)
		String content=getString("content");//短信内容
		String dataType=getString("dataType");//即时短信(now)or定时短信(time)
		String sendTime=getString("sendTime");// 发送时间
		String selfPhone=getString("selfPhone");//自己的电话
		String mateVal=getString("mateVal");//同事  电话(格式:同事ID+","+电话号码+";"+同事ID+","+电话号码)
		String otherVal=getString("otherVal");//外界 电话号码 逗号隔开
		String addSign=getString("addSign");//是否附加签名
		String signVal=getString("signVal");//附加签名内容
		String cusOwnerVal=getString("cusOwnerVal");//客户 业主 接收电话
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result="";
		boolean flag=false;
		Date send=null;
		if("time".equals(dataType)){//选择 定时短信
			try {
				flag=true;
				send=df.parse(sendTime);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		}
		/**
		 * 发送
		 */
			if("mate".equals(selectType)){//发送给同事
				String[] addr=mateVal.split(";");
				String abc=ParamUtils.getParamValue("COMPANYSIGN");
				if(null == abc){
					abc="";
				}
				if(null != addSign && "on".equals(addSign)){//选中 添加  附加签名
					content=content+signVal;
				}
				content=content+abc;
				/**
				 * 增加判断 如果发送号码超过3个 就调用群发接口
				 */
				if(addr.length<3){//单发接口
					for(String str:addr){
						String[] tem=str.split(",");
						String name=tem[0];//取人员ID
						String phone=tem[1];//电话
						
						 String one=send(content,name,phone,send,selectType,flag);//发送
						 if(!result.contains(one)){
							 result+=one+",";
						 }
					}
				}else{//群发接口
					String phones="";//拼接接收号码
					String names="";//拼接接收姓名
					for(String str:addr){
						String[] tem=str.split(",");
						String name=tem[0];//取人员ID
						String phone=tem[1];//电话
						phones+=phone+",";
						names+=name+",";
					}
					if(!"".equals(phones)){
						phones=phones.substring(0, phones.length()-1);
						names=names.substring(0, names.length()-1);
					}
					result=sendGroup(content, names, phones, send);
				}
			} else if("out".equals(selectType)){//发送给外界
				String[] addr=otherVal.split(",");
				String abc=ParamUtils.getParamValue("COMPANYSIGN");
				if(null == abc){
					abc="";
				}
				if(null != addSign && "on".equals(addSign)){//选中 添加  附加签名
					content=content+signVal;
				}
				content=content+abc;
				/**
				 * 增加判断 如果发送号码超过3个 就调用群发接口
				 */
				if(addr.length<3){//单发接口
					for(String str:addr){
						String one=send(content,"外界人员",str,send,selectType,flag);//发送
						if(!result.contains(one)){
							 result+=one+",";
						 }
					}
				}else{//群发接口
					String phones="";//拼接接收号码
					String names="";//拼接接收姓名
					for(String str:addr){
						phones+=str+",";
						names+="外界人员,";
					}
					if(!"".equals(phones)){
						phones=phones.substring(0, phones.length()-1);
						names=names.substring(0, names.length()-1);
					}
					result=sendGroup(content, names, phones, send);
				}
				
				
			}else if("self".equals(selectType)){//发送给自己
				String abc=ParamUtils.getParamValue("COMPANYSIGN");
				if(null == abc){
					abc="";
				}
				content=content+abc;
				Person current=SystemUtil.getCurrentUser();
				String one=send(content,current.getName(),selfPhone,send,selectType,flag);//发送
				if(!result.contains(one)){
					 result+=one+",";
				 }
			}else if("customer".equals(selectType) || "owner".equals(selectType)){//客户 / 业主
				String[] addr=cusOwnerVal.split(";");
				String abc=ParamUtils.getParamValue("COMPANYSIGN");
				if(null == abc){
					abc="";
				}
				if(null != addSign && "on".equals(addSign)){//选中 添加  附加签名
					content=content+signVal;
				}
				content=content+abc;
				/**
				 * 增加判断 如果发送号码超过3个 就调用群发接口
				 */
				if(addr.length<3){//单发接口
					for(String str:addr){
						String getName=str.split(",")[0];
						String getPhone=str.split(",")[1];
						String one=cusOwnerSend(content,getName,getPhone,send,flag);//发送
						if(!result.contains(one)){
							 result+=one+",";
						 }
					}
				}else{//群发接口
					String phones="";//拼接接收号码
					String names="";//拼接接收姓名
					for(String str:addr){
						String[] tem=str.split(",");
						String name=tem[0];//取人员ID
						String phone=tem[1];//电话
						phones+=phone+",";
						names+=name+",";
					}
					if(!"".equals(phones)){
						phones=phones.substring(0, phones.length()-1);
						names=names.substring(0, names.length()-1);
					}
					result=sendGroup(content, names, phones, send);
				}
			}
			if(!"".equals(result)){
				result=result.substring(0, result.length()-1);
			}
			if("发送成功".equals(result)){
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "发送成功!");
			}else{
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", result);
			}
			outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 群发短信
	 * @return
	 */
	public String sendGroup(String content,String reName,String rePhone,Date date){
		/**
		 * 群发操作 start
		 */
		Person current=SystemUtil.getCurrentUser();
		Org org=SystemUtil.getCurrentOrg();
		ShortMessage all=new ShortMessage();
		all.setId(UUID.randomUUID().toString());
		all.setCreator(current);//创建人
		all.setOrg(org);//组织
		all.setControlType(SMSControlTypeEnum.PERSONAL_TYPE);//控制类型个人
		all.setType(SMSTypeEnum.AD_TYPE);//群发 广告类
	   
		all.setSenderId(current.getId());//发送人ID
		all.setSenderNumber(current.getNumber());///发送人 工号
		all.setSenderName(current.getName());//发送人姓名
		all.setSenderPhoneNum(current.getPhone());//发送人电话
		all.setSendTime(null);//发送时间
		all.setContent(content);//短信内容
		all.setReceiverName(reName);
		all.setReceiverPhoneNum(rePhone);
		all.setCreateTime(date);
		String result= ShortMessageUtil.sendMessage(all);//发送操作
		return result;
	}
	
	/**
	 * 客户 业主 发送操作
	 * @return
	 */
	public String cusOwnerSend(String content,String getName,String getPhone,Date sendTime,boolean flag){
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
	    sm.setCreateTime(sendTime);
	    sm.setReceiverName(getName);//接收人姓名
	    sm.setReceiverPhoneNum(getPhone);//接收人电话
	    sm.setContent(content);//短信内容
	    if(flag){
	    	sm.setSendType(SMSSendTypeEnum.TIMING_SMS);
	    }
	   String result= ShortMessageUtil.sendOneMessage(sm);//发送操作
	   return result;
	}
	
	/**
	 * 发送
	 * @param content  内容
	 * @param getId  接收人ID
	 * @param getPhone 接收人电话
	 */
	public String send(String content,String getName,String getPhone,Date sendTime,String type,boolean flag){
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
	    sm.setCreateTime(sendTime);
	    sm.setReceiverName(getName);//接收人姓名
	    sm.setReceiverPhoneNum(getPhone);//接收人电话
	    sm.setContent(content);//短信内容
	    if(flag){
	    	 sm.setSendType(SMSSendTypeEnum.TIMING_SMS);//定时短信
	    }
	   String result= ShortMessageUtil.sendOneMessage(sm);//发送操作
	   return result;
	}
	
	/**
	 * 发送 (首页 在线人员 出 发送操作)
	 */
	@RequestMapping("sendTopic")
	@ResponseBody
	public void sendTopic(HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		String acceptPhone=getString("acceptPhone");//接收人电话
		String acceptId=getString("acceptId");//接收人ID
		String dataType=getString("dataType");//短信 类型 (即时now/定时time)
		String sendTime=getString("sendTime");//发送时间
		String content=getString("content");//短信内容
		String addSign=getString("addSign");//是否附加签名
		String signVal=getString("signVal");//附加签名内容
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date send=null;
		boolean flag=false;
		if("time".equals(dataType)){//选择 定时短信
			flag=true;
			try {
				send=df.parse(sendTime);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		}
		
		if(null != addSign && "on".equals(addSign)){//选中 添加  附加签名
			String sign=ParamUtils.getParamValue("COMPANYSIGN");
			if(null == sign){
				sign="";
			}
			content=content+signVal+sign;
		}
		String result=send(content,acceptId,acceptPhone,send,"mate",flag);
		if("发送成功".equals(result)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功!");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", result);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 洗客 发短信
	 * @param response
	 */
	@RequestMapping("saveBatchMessage")
	@ResponseBody
	public void saveBatchMessage(HttpServletResponse response){
		String phones=getString("phones");//接收电话
		String content=getString("content");//短信内容
		String result="";
		for(String str:phones.split(",")){
			String one=send(content,null,str,null,"out",false);
			if(!result.contains(one)){
				result+=one+",";
			}
		}
		if(!"".equals(result)){
			result=result.substring(0, result.length()-1);
		}
		if("发送成功".equals(result)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功!");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", result);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 普通 页面 发送操作
	 */
	@RequestMapping("sendCommon")
	@ResponseBody
	public void sendCommon(HttpServletResponse response){
		String acceptPhone=getString("acceptPhone");//接收人电话
		String acceptId=getString("acceptId");//接收人ID
		String dataType=getString("dataType");//短信 类型 (即时now/定时time)
		String sendTime=getString("sendTime");//发送时间
		String content=getString("content");//短信内容
		String addSign=getString("addSign");//是否附加签名
		String signVal=getString("signVal");//附加签名内容
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date send=null;
		if("time".equals(dataType)){//选择 定时短信
			try {
				send=df.parse(sendTime);
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		}
		
		if(null != addSign && "on".equals(addSign)){//选中 添加  附加签名
			String sign=ParamUtils.getParamValue("COMPANYSIGN");
			if(null == sign){
				sign="";
			}
			content=content+signVal+sign;
		}
		String result=send(content,acceptId,acceptPhone,send,"mate",false);
		if("SUCC".equals(result)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功!");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", result);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 普通 页面 发送操作
	 */
	@RequestMapping("sendNew")
	@ResponseBody
	public void sendNew(HttpServletResponse response){
		String acceptPhone=getString("acceptPhone");//接收人电话
		String content=getString("content");//短信内容
		String result=send(content,"外界",acceptPhone,new Date(),"mate",false);
		if("发送成功".equals(result)){
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功!");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", result);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 选 人员
	 */
	@RequestMapping("getPersons")
	@ResponseBody
	public void getPersons(HttpServletResponse response){
		response.setContentType("text/html");
		String key  = getString("term");
		int maxRows  = getInt("maxRows");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("key", key);
		Pagination<Person> pagination=queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.select",new Pagination<Person>(maxRows, 0),map);
		for(Person p:pagination.getItems()){
			String name=p.getName();
			String phone=p.getPhone();
			p.setName(name+"("+phone+")");
			p.setId(name+","+phone);
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
}
