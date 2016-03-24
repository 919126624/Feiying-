package com.wuyizhiye.cmct.sms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.annotation.Dependence;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.TreeEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.cmct.sms.enums.SMSControlTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSOperationTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSStrategyTypeEnum;
import com.wuyizhiye.cmct.sms.enums.SMSTypeEnum;
import com.wuyizhiye.cmct.sms.model.SMSConfig;
import com.wuyizhiye.cmct.sms.model.SMSControl;
import com.wuyizhiye.cmct.sms.model.SMSControlHistory;
import com.wuyizhiye.cmct.sms.model.ShortMessage;
import com.wuyizhiye.cmct.sms.service.SMSControlHistoryService;
import com.wuyizhiye.cmct.sms.service.SMSControlService;
import com.wuyizhiye.cmct.sms.utils.ShortMessageUtil;
import com.wuyizhiye.framework.base.controller.TreeListController;

/**
 * @ClassName CmctSMSControlListController
 * @Description 职员列表controller
 * @author li.biao
 * @date 2015-5-26
 */
@Controller
@RequestMapping(value="cmct/smsControl/*")
public class CmctSMSControlListController extends TreeListController {
	static final String ALLOW_SMS_CONFIG = "ALLOW_SMS_CONFIG";//是否允许  配置短信服务
	
	@Autowired
	private SMSControlService smsControlService;
	 
	@Autowired
	private OrgService orgService;
	@Autowired
	private SMSControlHistoryService smsControlHistoryService;
	
	@Override
	protected String getTreeDataMapper() {
		return "com.wuyizhiye.basedata.org.dao.OrgDao.getChild";
	}
	
	@Override
	public void treeData(HttpServletResponse response) {
		// TODO Auto-generated method stub
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();
		Org org = orgService.getEntityById(SystemUtil.getCurrentOrg().getId());
		if(getString("longNumber")!=null){
			param.put("longNumber", getString("longNumber"));
			param.put("parent","807daadd-5011-41c0-ae1d-2976035225db");
		}
		if(org.getParent() != null && parent == null){
			parent = org.getParent().getId();
			param.put("longNumber", org.getLongNumber());
			
		}
		CoreEntity pare = null;
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
		}
		if(pare!=null){
			if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
				param.put("includeChild", includeChild);
				param.put("longNumber", ((TreeEntity)pare).getLongNumber());
			}else{
				param.put("parent", pare.getId());
			}
		}
		Pagination<Object> page = new Pagination<Object>(Integer.MAX_VALUE,0);
		page = queryExecutor.execQuery(getTreeDataMapper(), page, param);
		String result = JSONArray.fromObject(page.getItems(), getDefaultJsonConfig()).toString();
		outPrint(response, result);
	}
	
	
	@RequestMapping(value="treeDataAll")
	public void treeDataAll(HttpServletResponse response) {
		String parent = getString("parent");
		String includeChild = getString("includeChild");
		Map<String,Object> param = getTreeFilterParam();

		CoreEntity pare = null;
		if(!StringUtils.isEmpty(parent)){
			pare = getTreeService().getEntityById(parent);
		}
		if(pare!=null){
			if(!StringUtils.isEmpty(includeChild) && Boolean.getBoolean(includeChild)==true){
				param.put("includeChild", includeChild);
				param.put("longNumber", ((TreeEntity)pare).getLongNumber());
			}else{
				param.put("parent", pare.getId());
			}
		}
		Pagination<Object> page = new Pagination<Object>(Integer.MAX_VALUE,0);
		page = queryExecutor.execQuery(getTreeDataMapper(), page, param);
		String result = JSONArray.fromObject(page.getItems(), getDefaultJsonConfig()).toString();
		outPrint(response, result);
	}
	

	@Override
	protected CoreEntity createNewEntity() {
		return new SMSControl();
	}

	@Override
	protected String getListView() {
		
		//是否允许  配置短信服务
		String allowSMSConfigFlag = ParamUtils.getParamValue(SystemUtil.getCurrentOrg().getId(), ALLOW_SMS_CONFIG);
		if(StringUtils.isEmpty(allowSMSConfigFlag)){
			allowSMSConfigFlag ="N";//默认不允许 配置
		}
		String bussinessBalance = ShortMessageUtil.getSmsBalance(SMSTypeEnum.BUSINESS_TYPE);
		String adBalance = ShortMessageUtil.getSmsBalance(SMSTypeEnum.AD_TYPE);
		this.getRequest().setAttribute("allowSMSConfigFlag", allowSMSConfigFlag);
		this.getRequest().setAttribute("bussinessBalance", bussinessBalance);
		this.getRequest().setAttribute("adBalance", adBalance);
		/************** end *****************/
		return "cmct/sms/smsControlList";
	}
	
	@Override
	protected String getEditView() {
		
		getRequest().setAttribute("limitStrategyTypes", SMSStrategyTypeEnum.toList());
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "cmct/sms/smsControlEdit";
	}
	
	@RequestMapping(value="editPersonalControl")
	@Dependence(method="list")
	public String edit(ModelMap model){
		String id = getString("id");
		String objectId = getString("objectId");
		String objectNumber = getString("objectNumber");
		String objectName = getString("objectName");
		if(StringUtils.isNotNull(id)){
		 model.put("data", getService().getEntityById(id));
		}else{
			SMSControl smsControl = new SMSControl();
			smsControl.setObjectId(objectId);
			smsControl.setObjectName(objectName);
			smsControl.setObjectNumber(objectNumber);
			model.put("data", smsControl);
		}
		//将当前状态放入到界面 addded by taking.wang 2013-01-24
		model.put("edit_viewStatus", this.getString("VIEWSTATE"));
		return getEditView();
	}
	
	@RequestMapping(value="toChangeBalance")
	public String toChangeBalance(){
		String controlId = getString("controlId");
		String operationtype = getString("operationtype","");
		SMSControl control = smsControlService.getEntityById(controlId);
		SMSControlHistory controlHis = new SMSControlHistory();
		controlHis.setControlId(controlId);
		controlHis.setObjectId(control.getObjectId());
		controlHis.setObjectName(control.getObjectName());
		controlHis.setObjectNumber(control.getObjectNumber());
		this.getRequest().setAttribute("data", controlHis);
		this.getRequest().setAttribute("operationtype", operationtype.trim());
		return "cmct/sms/smsChangeBalanceEdit";
	}
	
	/**
	 * 公司或集团下 或者 组织类型为 人力资源(T02)下的人 不需要根据登录人组织过滤
	 * @return
	 */
	private String isFilterByOrg(){
		Org org = SystemUtil.getCurrentOrg();
		if(org ==null || StringUtils.isEmpty(org.getBusinessTypes())){
			return  "N";//公司 和集团
		}
		//业务类型为 人力资源
		if(OrgUtils.isType(org, "T02")){
			return "N";
		}
		return "Y";
	}

	
	@Override
	protected BaseService<SMSControl> getService() {
		return smsControlService;
	}
	
	@Override
	protected BaseService<Org> getTreeService() {
		return orgService;
	}

	@Override
	protected String getSimpleTreeDataMapper() {
		return null;
	}
	
	@Override
	protected JsonConfig getDefaultJsonConfig() {
		JsonConfig config = super.getDefaultJsonConfig();
		config.registerJsonValueProcessor(SMSControlTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSControlTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SMSControlTypeEnum)value).getName());
					json.put("value", ((SMSControlTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSControlTypeEnum){
					return ((SMSControlTypeEnum)value).getName();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(SMSStrategyTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSStrategyTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SMSStrategyTypeEnum)value).getName());
					json.put("value", ((SMSStrategyTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSStrategyTypeEnum){
					return ((SMSStrategyTypeEnum)value).getName();
				}
				return null;
			}
		});
		return config;
	}
	
	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.cmct.sms.dao.SMSControlDao.getSMSControl4List";
	}
	
	/**
	 * 数据列表
	 */
	@Override
	public void listData(Pagination<?> pagination,HttpServletResponse response){
		pagination = this.queryExecutor.execQuery(getListMapper(), pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="delete")
	@Dependence(method="list")
	public void delete(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		SMSControl entity = getService().getEntityById(id);
		if(entity!=null){
			if(isAllowDelete(entity)){
				getService().deleteEntity(entity);
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "短信控制已关闭");
				//写短信控制 历史
				SMSControlHistory controlHis = new SMSControlHistory();
				Date now = new Date();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("id",entity.getObjectId());
				Person  p =  (Person)queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getById",param, Person.class);
				 
				controlHis.setOrg(p==null?SystemUtil.getCurrentOrg():p.getOrg());
				controlHis.setCreator(SystemUtil.getCurrentUser());
				controlHis.setCreateTime(now);
				controlHis.setControlId(entity.getId());
				controlHis.setOperationtype(SMSOperationTypeEnum.CLOSE_SET_TYPE);
				controlHis.setObjectId(entity.getObjectId());
				controlHis.setObjectName(entity.getObjectName());
				controlHis.setObjectNumber(entity.getObjectNumber());
				controlHis.setBalanceAmout(entity.getBalanceAmout());
				controlHis.setTopLimitAmount(entity.getTopLimitAmount());
				controlHis.setUUID();
				smsControlHistoryService.addEntity(controlHis);
			}else{
				getOutputMsg().put("STATE", "FAIl");
			}
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="toControlHistory")
	public String toControlHistory(){
		String objectId = getString("objectId");
		this.getRequest().setAttribute("objectId", objectId);
		return "cmct/sms/smsControlHistoryList";
	}
	
	protected JsonConfig getControlHistoryJsonConfig() {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(SMSOperationTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSOperationTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SMSOperationTypeEnum)value).getName());
					json.put("value", ((SMSOperationTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSOperationTypeEnum){
					return ((SMSOperationTypeEnum)value).getName();
				}
				return null;
			}
		});
		
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
	
	protected String getControlHistoryListMapper() {
		return "com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao.getControlHisByCond";
	}
	
	/**
	 * 数据列表
	 */
	@RequestMapping(value="controlHistoryListData")
	public void controlHistoryListData(Pagination<SMSControlHistory> pagination,HttpServletResponse response){
		pagination = this.queryExecutor.execQuery(getControlHistoryListMapper(), pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getControlHistoryJsonConfig()));
	}
	
	
	protected JsonConfig getShortMessageJsonConfig() {
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(SMSControlTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSControlTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SMSControlTypeEnum)value).getName());
					json.put("value", ((SMSControlTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSControlTypeEnum){
					return ((SMSControlTypeEnum)value).getName();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(SMSTypeEnum.class, new JsonValueProcessor() {
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSTypeEnum){
					JSONObject json = new JSONObject();
					json.put("name", ((SMSTypeEnum)value).getName());
					json.put("value", ((SMSTypeEnum)value).getValue());
					return json;
				}
				return null;
			}
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof SMSTypeEnum){
					return ((SMSTypeEnum)value).getName();
				}
				return null;
			}
		});
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			public Object processObjectValue(String key, Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
			
			public Object processArrayValue(Object value, JsonConfig cfg) {
				if(value!=null && value instanceof Date){
					return sdf.format(value);
				}
				return null;
			}
		});
		return config;
	}
	
	@RequestMapping(value="toSmsIndex")
	public String toSmsIndex(){
		return "cmct/sms/smsIndex";
	}
	
	@RequestMapping(value="toShortMessageList")
	public String toShortMessageList(){
		getRequest().setAttribute("isFilterByOrg", isFilterByOrg());
		return "cmct/sms/shortMessageList";
	}
	
	protected String getShortMessageListMapper() {
		return "com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond";
	}
	
	/**
	 * 短信列表
	 */
	@RequestMapping(value="listShortMessageData")
	public void listShortMessageData(Pagination<SMSControlHistory> pagination,HttpServletResponse response){
		/**
		 * 每次查询短信  请求一次短信状态接口
		 */
		ShortMessageUtil.fixedMesStatus();
		pagination = this.queryExecutor.execQuery(getShortMessageListMapper(), pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getShortMessageJsonConfig()));
	}
	
	/**
	 * 短信服务配置
	 * @return
	 */
   @RequestMapping(value="toSmsConfig")
   protected String toSmsConfig() {
	    if(StringUtils.isNotNull(getString("dc"))){
	    	DataSourceHolder.setDataSource(getString("dc"));
	    }
	    List<SMSConfig> configList = this.queryExecutor.execQuery(getSMSConfigListMapper(), getListDataParam(),SMSConfig.class);
		getRequest().setAttribute("controlTypes", SMSControlTypeEnum.toList());
		getRequest().setAttribute("smsTypes", SMSTypeEnum.toList());
		getRequest().setAttribute("configList", configList);
		return "cmct/sms/smsConfigList";
	}
   
   protected String getSMSConfigListMapper() {
		return "com.wuyizhiye.cmct.sms.dao.SMSConfigDao.getSMSConfigByCon";
	}
   
   /**
	 * 短信列表
	 */
	@RequestMapping(value="listSmsConfigData")
	public void listSmsConfigData(Pagination<SMSControlHistory> pagination,HttpServletResponse response){
		pagination = this.queryExecutor.execQuery(getSMSConfigListMapper(), pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getShortMessageJsonConfig()));
	}
   
   
   /**
    * 短信统计
    * @return
    */
   @RequestMapping(value="toShortMessageReport")
   protected String toShortMessageReport() {
	   
	   return "cmct/sms/shortMessageReport";
   }
   
   /**
    * 初始化 短信统计
    * @param response
 * @throws ParseException 
    */
   @RequestMapping(value="initSmsReport")
   public void initSmsReport(HttpServletResponse response) throws ParseException{
	   List<Object> smsReportList = new ArrayList<Object>();
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
	   Map<String, Object> item = new HashMap<String, Object>();
	   //默认统计登录人组织及下级所在组织  当前月 的短信发送情况
	   Org org = SystemUtil.getCurrentOrg();
	   item.put("objId", org.getId());
	   item.put("objName", org.getName());
	   item.put("objNumber", org.getNumber());
	   item.put("objLongNumber", org.getLongNumber());
	   item.put("objLevel", org.getLevel());
	   
	 	//当月 总短信发送条数
	   int totalSendAmount = 0;
	   int businessAmount = 0;
	   int adAmount = 0;
	   Map<String, Object> params =  new HashMap<String, Object>();
	   Calendar now = Calendar.getInstance();
	   String queryStartDate = getString("queryStartDate",""); 
	   String queryEndDate = getString("queryEndDate",""); 
	   Date begDate = null;
	   Date endDate = null;
	   if(StringUtils.isEmpty(queryStartDate)){
		   //发送最小时间为空时 取最小年1910
		   now.set(Calendar.YEAR, 1910);
		   begDate = DateUtil.getFirstDay(now.getTime());
	   }else{
		   begDate = df2.parse(queryStartDate);
	   }
	   if(StringUtils.isEmpty(queryEndDate)){
		   //发送最大时间  为空时 取最大年2099
		   now.set(Calendar.YEAR, 2099);
		   endDate = df.parse(df.format(now.getTime()));
	   }else{
		   endDate = df2.parse(queryEndDate);
	   }
	   
	   params.put("id", org.getId());
	   params.put("begDate", begDate);
	   params.put("endDate",endDate);
	   	//查询当前组织的下级历史组织
	   List<Org> orgList = OrgUtils.getHistoryOrgList(params,true);
	   String orgIds = "";
	   for(Org o:orgList){
		   if("".equals(orgIds)){
			   orgIds = "'"+o.getId()+"'";
		   }else{
			   orgIds += ",'"+o.getId()+"'";
		   }
	   }
	   params.clear();
	   params.put("orgIds", orgIds);
	   params.put("queryStartDate", df.format(begDate));
	   params.put("queryEndDate", df.format(endDate));
	   List<ShortMessage> msgList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond", params, ShortMessage.class);
	   if(msgList!=null && msgList.size()>0){
		   //totalSendAmount = msgList.size();//短信总数
		   for(ShortMessage msg :msgList){
			   totalSendAmount += msg.getSendSmsCount();
			   if(SMSTypeEnum.BUSINESS_TYPE.equals(msg.getType())){
				   businessAmount += msg.getSendSmsCount();;//业务类短信
			   }else if(SMSTypeEnum.AD_TYPE.equals(msg.getType())){
				   adAmount += msg.getSendSmsCount();;//广告类短信
			   }
		   }
	   }
	   item.put("totalSendAmount",totalSendAmount);//短信总数
	   item.put("businessAmount",businessAmount);//业务类短信
	   item.put("adAmount",adAmount);//广告类短信
	   /**
	    * 取充值条数
	    */
	   //默认 当月充值
	   int totalChargeAmount = 0;
	   params.clear();
	   params.put("orgIds", orgIds);
	   params.put("queryStartDate", df.format(begDate));
	   params.put("queryEndDate", df.format(endDate));
	   List<SMSControlHistory> msgControlList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao.getControlHisByCond", params, SMSControlHistory.class);
	   if(msgControlList!=null && msgControlList.size()>0){
		   for(SMSControlHistory control:msgControlList){
			   if(SMSOperationTypeEnum.CHARGE_TYPE.equals(control.getOperationtype())){
				   totalChargeAmount += control.getBalanceAmout();//充值
			   }else if(SMSOperationTypeEnum.DEDUCTION_TYPE.equals(control.getOperationtype())){
				   //totalChargeAmount -= control.getBalanceAmout();//扣减
			   }
		   }
	   }
	   item.put("totalChargeAmount",totalChargeAmount);
	   smsReportList.add(item);
	   getOutputMsg().put("smsReportList", smsReportList);
	   outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
   }
   
   @RequestMapping(value="getSmsReport")
	public void getSmsReport(HttpServletResponse response) throws Exception{
	   List<Object> smsReportList = new ArrayList<Object>();	 
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   SimpleDateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
	   Map<String, Object> params =  new HashMap<String, Object>();
	   String objId = getString("objId"); 
	   String queryStartDate = getString("queryStartDate",""); 
	   String queryEndDate = getString("queryEndDate",""); 
	   Date begDate = null;
	   Date endDate = null;
	   //Date now = new Date();
	   Calendar now = Calendar.getInstance();
	   if(StringUtils.isEmpty(queryStartDate)){
		   //发送最小时间为空时 取最小年1910
		   now.set(Calendar.YEAR, 1910);
		   begDate = DateUtil.getFirstDay(now.getTime());
	   }else{
		   begDate = df2.parse(queryStartDate);
	   }
	   if(StringUtils.isEmpty(queryEndDate)){
		   //发送最大时间  为空时 取最大年2099
		   now.set(Calendar.YEAR, 2099);
		   endDate = df.parse(df.format(now.getTime()));
	   }else{
		   endDate = df2.parse(queryEndDate);
	   }
	   
	   params.put("parentid", objId);
	   params.put("begDate", begDate);
	   params.put("endDate",endDate);
	   List<Org> objList = OrgUtils.getHistoryOrgList(params,true);
	   
	   if(objList!=null && objList.size()>0){
		   //根据组织 和时间范围统计人员短信
		   List<Object> smsPersonList =  getSmsReport4Person(objId,begDate,endDate);
		   smsReportList.addAll(smsPersonList);
		   
	      for(Org org :objList){
		   Map<String, Object> item = new HashMap<String, Object>();
		   item.put("objId", org.getId());
		   item.put("objName", org.getName());
		   item.put("objNumber", org.getNumber());
		   item.put("objLongNumber", org.getLongNumber());
		   item.put("objLevel", org.getLevel());
		   
		 	//当月 总短信发送条数
		   int totalSendAmount = 0;
		   int businessAmount = 0;
		   int adAmount = 0;
		   params.clear();
		   params.put("id", org.getId());
		   params.put("begDate", begDate);
		   params.put("endDate",endDate);
		   	//查询当前组织的下级历史组织
		   List<Org> orgList = OrgUtils.getHistoryOrgList(params,true);
		   String orgIds = "";
		   for(Org o:orgList){
			   if("".equals(orgIds)){
				   orgIds = "'"+o.getId()+"'";
			   }else{
				   orgIds += ",'"+o.getId()+"'";
			   }
		   }
		   params.clear();
		   params.put("orgIds", orgIds);
		   params.put("queryStartDate", df.format(begDate));
		   params.put("queryEndDate", df.format(endDate));
		   List<ShortMessage> msgList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond", params, ShortMessage.class);
		   if(msgList!=null && msgList.size()>0){
			   //totalSendAmount = msgList.size();//短信总数
			   for(ShortMessage msg :msgList){
				   totalSendAmount += msg.getSendSmsCount();
				   if(SMSTypeEnum.BUSINESS_TYPE.equals(msg.getType())){
					   businessAmount += msg.getSendSmsCount();;//业务类短信
				   }else if(SMSTypeEnum.AD_TYPE.equals(msg.getType())){
					   adAmount += msg.getSendSmsCount();;//广告类短信
				   }
			   }
		   }
		   item.put("totalSendAmount",totalSendAmount);//短信总数
		   item.put("businessAmount",businessAmount);//业务类短信
		   item.put("adAmount",adAmount);//广告类短信
		   //默认 当月充值
		   int totalChargeAmount = 0;
		   params.clear();
		   params.put("orgIds", orgIds);
		   params.put("queryStartDate", df.format(begDate));
		   params.put("queryEndDate", df.format(endDate));
		   List<SMSControlHistory> msgControlList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao.getControlHisByCond", params, SMSControlHistory.class);
		   if(msgControlList!=null && msgControlList.size()>0){
			   for(SMSControlHistory control:msgControlList){
				   if(SMSOperationTypeEnum.CHARGE_TYPE.equals(control.getOperationtype())){
					   totalChargeAmount += control.getBalanceAmout();//充值
				   }else if(SMSOperationTypeEnum.DEDUCTION_TYPE.equals(control.getOperationtype())){
					   //totalChargeAmount -= control.getBalanceAmout();//扣减
				   }
			   }
		   }
		   item.put("totalChargeAmount",totalChargeAmount);
		   smsReportList.add(item);
	    }
	   }else{
		   //没有下级组织时  统计人的短信
		   //根据组织 和时间范围统计人员短信
		   List<Object> smsPersonList =  getSmsReport4Person(objId,begDate,endDate);
		   smsReportList.addAll(smsPersonList);
	   }
	   getOutputMsg().put("smsReportList", smsReportList);
	   getOutputMsg().put("STATE", "SUCCESS");
		
	   outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
   
   /**
    * 根据组织 和时间范围统计人员短信
    * @param orgId
    * @param begDate
    * @param endDate
    * @return
    */
   private List<Object> getSmsReport4Person(String orgId,Date begDate,Date endDate){
	   List<Object> smsReportList = new ArrayList<Object>();	 
	   Map<String, Object> params =  new HashMap<String, Object>();
	   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	   Org org = orgService.getEntityById(orgId);
	   params.clear();
	   params.put("orgId", orgId);
	   params.put("endDate", endDate);
	   List<Person> personList = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonByOrgHis", params, Person.class);
	   if(personList!=null && personList.size()>0){
		   for(Person p:personList){
			   Map<String, Object> item = new HashMap<String, Object>();
			   item.put("objId", p.getId());
			   item.put("objName", p.getName());
			   item.put("objNumber", p.getNumber());
			   item.put("objLongNumber", org.getLongNumber()+"_person");
			   item.put("objLevel", org.getLevel()+1);
			 //当月 总短信发送条数
			   int totalSendAmount = 0;
			   int businessAmount = 0;
			   int adAmount = 0;
			    
			   params.clear();
			   params.put("senderId", p.getId());//发送人
			   params.put("orgId", orgId);//发送人组织
			   params.put("queryStartDate", df.format(begDate));
			   params.put("queryEndDate", df.format(endDate));
			   List<ShortMessage> msgList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.ShortMessageDao.getShortMessageByCond", params, ShortMessage.class);
			   if(msgList!=null && msgList.size()>0){
				   //totalSendAmount = msgList.size();//短信总数
				   for(ShortMessage msg :msgList){
					   totalSendAmount += msg.getSendSmsCount();
					   if(SMSTypeEnum.BUSINESS_TYPE.equals(msg.getType())){
						   businessAmount += msg.getSendSmsCount();;//业务类短信
					   }else if(SMSTypeEnum.AD_TYPE.equals(msg.getType())){
						   adAmount += msg.getSendSmsCount();;//广告类短信
					   }
				   }
			   }
			   item.put("totalSendAmount",totalSendAmount);//短信总数
			   item.put("businessAmount",businessAmount);//业务类短信
			   item.put("adAmount",adAmount);//广告类短信
			   //默认 当月充值
			   int totalChargeAmount = 0;
			   params.clear();
			   params.put("objectId", p.getId());
			   params.put("queryStartDate", df.format(begDate));
			   params.put("queryEndDate", df.format(endDate));
			   List<SMSControlHistory> msgControlList = queryExecutor.execQuery("com.wuyizhiye.cmct.sms.dao.SMSControlHistoryDao.getControlHisByCond", params, SMSControlHistory.class);
			   if(msgControlList!=null && msgControlList.size()>0){
				   for(SMSControlHistory control:msgControlList){
					   if(SMSOperationTypeEnum.CHARGE_TYPE.equals(control.getOperationtype())){
						   totalChargeAmount += control.getBalanceAmout();//充值
					   }else if(SMSOperationTypeEnum.DEDUCTION_TYPE.equals(control.getOperationtype())){
						   totalChargeAmount -= control.getBalanceAmout();//扣减
					   }
				   }
			   }
			   item.put("totalChargeAmount",totalChargeAmount);
			   smsReportList.add(item);
		   }
	   }
	   return smsReportList;
   }
}
