package com.wuyizhiye.cmct.sms.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSOperationTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSSendTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.SMSConfig;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.SMSControlHistory;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.SMSConfigService;
import com.wuyizhiye.cmct.sms.service.SMSControlHistoryService;
import com.wuyizhiye.cmct.sms.service.SMSControlService;
import com.wuyizhiye.cmct.sms.utils.ShortMessageUtil;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;

/**
 * @ClassName CmctSMSControlEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/smsControl/*")
public class CmctSMSControlEditController extends EditController {
	@Autowired
	private SMSControlService smsControlService;
	 
	@Autowired
	private SMSControlHistoryService smsControlHistoryService;
	
	@Autowired
	private SMSConfigService smsConfigService;
	
	@Override
	protected Class<SMSControl> getSubmitClass() {
		return SMSControl.class;
	}

	@Override
	protected BaseService<SMSControl> getService() {
		return smsControlService;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected SMSControl getSubmitEntity(){
		 
		String id = getString("id");
		SMSControl entity = null;
		if(!StringUtils.isEmpty(id)){
			entity = getService().getEntityById(id);
		}
		entity = BeanUtils.fillentity(getParamMap(),entity, SMSControl.class);
		entity.setUpdator(SystemUtil.getCurrentUser());
		Date now = new Date();
		entity.setLastUpdateTime(now);
		entity.setOrg(SystemUtil.getCurrentOrg());
		if(entity.getCreator()==null){
			entity.setCreator(SystemUtil.getCurrentUser());
			entity.setCreateTime(now);
		}
		return entity;
	}
	
	@Override
	protected boolean validate(Object data) {
		SMSControl smsControl = ((SMSControl)data);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("controlType", SMSControlTypeEnum.PERSONAL_TYPE);//控制类型 个人
		params.put("objectId", smsControl.getObjectId());//控制类型 个人
		if(smsControl!=null && StringUtils.isNotNull(smsControl.getId())){
		  params.put("idNotEq", smsControl.getId());
		}
		List<SMSControl> list = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControlByCond", params, SMSControl.class);
		if(list!=null && list.size()>0){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", smsControl.getObjectName()+"已经存在短信控制，新增失败！");
			return false;
		}
		
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="save")
	@Transactional
	public void save(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		SMSControl data = getSubmitEntity();
		if(validate(data)){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id",data.getObjectId());
			Person  p =  (Person)queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getById",param, Person.class);
			if(p!=null){
				data.setObjectName(p.getName());
				data.setObjectNumber(p.getNumber());
			}
			//写短信 设置历史
			SMSControlHistory controlHis = new SMSControlHistory();
				if(StringUtils.isEmpty(((CoreEntity)data).getId())){
					getService().addEntity(data);
					//开通
					controlHis.setOperationtype(SMSOperationTypeEnum.OPEN_SET_TYPE);
				}else{
					getService().updateEntity(data);
					//设置
					controlHis.setOperationtype(SMSOperationTypeEnum.MODIFY_SET_TYPE);
				}
				Date now = new Date();
				controlHis.setOrg(p==null?SystemUtil.getCurrentOrg():p.getOrg());
				controlHis.setCreator(SystemUtil.getCurrentUser());
				controlHis.setCreateTime(now);
				controlHis.setControlId(data.getId());
				
				controlHis.setObjectId(data.getObjectId());
				controlHis.setObjectName(data.getObjectName());
				controlHis.setObjectNumber(data.getObjectNumber());
				controlHis.setBalanceAmout(data.getBalanceAmout());
				controlHis.setTopLimitAmount(data.getTopLimitAmount());
				controlHis.setUUID();
				smsControlHistoryService.addEntity(controlHis);
				getOutputMsg().put("id", ((CoreEntity)data).getId());
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	protected SMSControlHistory getSMSControlHistory(){
		 
		SMSControlHistory entity = null;
		 
		entity = BeanUtils.fillentity(getParamMap(),entity, SMSControlHistory.class);
		Date now = new Date();
		entity.setOrg(SystemUtil.getCurrentOrg());
		entity.setCreator(SystemUtil.getCurrentUser());
		entity.setCreateTime(now);
		
		return entity;
	}
	
	@RequestMapping(value="changeBalance")
	@Transactional
	public void changeBalance(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		SMSControlHistory controlHis = getSMSControlHistory();
		BeanUtils.fillentity(getParamMap(),controlHis, SMSControlHistory.class);
		SMSControl control = getService().getEntityById(controlHis.getControlId());
		control.setUpdator(SystemUtil.getCurrentUser());
		control.setLastUpdateTime(new Date());
		if(controlHis.getOperationtype()!=null && "DEDUCTION_TYPE".equals(controlHis.getOperationtype().toString())){
			if(controlHis.getBalanceAmout()>control.getBalanceAmout()){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "扣减条数不能大于余额");
				outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
				return ;
		     }
			//扣减
			control.setBalanceAmout(control.getBalanceAmout()-controlHis.getBalanceAmout());
			
		}else{
		   //充值
			control.setBalanceAmout(control.getBalanceAmout()+controlHis.getBalanceAmout());
		}
		//充值、扣减
		getService().updateEntity(control);
		
		//写短信 设置历史
		Date now = new Date();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id",control.getObjectId());
		Person  p =  (Person)queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getById",param, Person.class);
		controlHis.setOrg(p==null?SystemUtil.getCurrentOrg():p.getOrg());
		controlHis.setCreator(SystemUtil.getCurrentUser());
		controlHis.setCreateTime(now);
		controlHis.setUUID();
		smsControlHistoryService.addEntity(controlHis);
		
		getOutputMsg().put("id", controlHis.getId());
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="sendMessage")
	public void sendMessage(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String senderId = getString("senderId");
		ShortMessage sms = new ShortMessage();
		sms.setControlType(SMSControlTypeEnum.PERSONAL_TYPE);
		sms.setType(SMSTypeEnum.AD_TYPE);
		sms.setSenderId(senderId);
		sms.setSenderPhoneNum("18688717887");
		sms.setReceiverPhoneNum("15219481921");
		sms.setContent("测试短信  配置服务");
		sms.setReceiverName("n1");
		sms.setSendType(SMSSendTypeEnum.STANDARD_SMS);//标准短信
		sms.setCreateTime(new Date());
		Calendar sendTime = Calendar.getInstance();
		sendTime.set(Calendar.HOUR_OF_DAY, 13);
		//sms.setSendTime(sendTime.getTime());
		String msg = ShortMessageUtil.sendMessage(sms);
		/*ShortMessage smsCy = new ShortMessage();
		smsCy.setControlType(SMSControlTypeEnum.PERSONAL_TYPE);
		smsCy.setType(SMSTypeEnum.AD_TYPE);
		smsCy.setSenderId(senderId);
		smsCy.setSenderPhoneNum("18688717887");
		smsCy.setReceiverPhoneNum("15219481921");
		smsCy.setContent("测试短信  掌赢a");
		smsCy.setReceiverName("n1");
		//sms.setSendTime(sendTime.getTime());
		 msg += ShortMessageUtil.sendMessage(smsCy);*/
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", msg);
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="saveSmsConfig")
	public void saveSmsConfig(HttpServletResponse response) throws InstantiationException, IllegalAccessException{
		String smsConfigJson = getString("smsConfigJson");
		List<SMSConfig> configList = null;
		if(!StringUtils.isEmpty(smsConfigJson)){
			configList = new ArrayList<SMSConfig>(JSONArray.toCollection(JSONArray.fromObject(smsConfigJson), SMSConfig.class));
		}
		//先删除 后新增
		queryExecutor.executeDelete("com.wuyizhiye.cmct.sms.dao.SMSConfigDao.deleteAll",null);
		if(configList!=null && configList.size()>0){
		  smsConfigService.addBatch(configList);
		}
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
}
