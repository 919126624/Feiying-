package com.wuyizhiye.basedata.info.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.cchat.controller.CChatNewController;
import com.wuyizhiye.basedata.cchat.model.NewestCChat;
import com.wuyizhiye.basedata.info.model.InfoCenterUnread;
import com.wuyizhiye.basedata.info.model.InfoTypeEnum;
import com.wuyizhiye.basedata.info.model.Infocenter;
import com.wuyizhiye.basedata.info.model.RedisChatInfoSender;
import com.wuyizhiye.basedata.info.model.Remind;
import com.wuyizhiye.basedata.info.model.RemindTypeEnum;
import com.wuyizhiye.basedata.info.service.InfoCenterUnreadService;
import com.wuyizhiye.basedata.info.service.InfocenterService;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.InfomationUtils;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.RedisHolder;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;
import com.wuyizhiye.framework.qqmial.controller.QQMailController;

/**
 * @ClassName InfocenterListController
 * @Description 消息中心 控制器
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value = "basedata/info/*")
public class InfocenterListController extends ListController {
	private static Logger logger=Logger.getLogger(InfocenterListController.class);
	@Autowired
	private InfocenterService infocenterService;
	
	@Autowired
	private PersonService personService;
	@Autowired
	private InfoCenterUnreadService infoCenterUnreadService;
	
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		return "basedata/info/infocenterList";
	}

	@RequestMapping(value = "listAll")
	public String listAll() {
		getRequest().setAttribute("infoType",InfoTypeEnum.values());// 消息类型
		return "basedata/info/infocenterList";
	}
	//消息中心、聊聊后台合并
	@RequestMapping(value = "interactionView")
	public String interactionView(){
		return "basedata/info/interactionView";
	}
	//转到批处理页
	@RequestMapping(value = "batchReadedlist")
	public String batchReadedlist(){
		//获取未读数量
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("isRead", "0");
		int count= queryExecutor.execOneEntity("com.wuyizhiye.basedata.info.dao.InfocenterDao.selectCount", map,Integer.class);
		getRequest().setAttribute("notReadCount",count);
		return "basedata/info/batchReadedlist";
	}
	//进行批处理后转到消息list页
	@RequestMapping(value = "batchHandleRead")
	public void batchHandleRead(HttpServletResponse response){
		//进行isRead更新处理
		Map<String,Object> map=new HashMap<String, Object>();
		String queryStartDate = getRequest().getParameter("queryStartDate");
		String queryEndDate = getRequest().getParameter("queryEndDate");
		map.put("queryStartDate", queryStartDate);
		map.put("queryEndDate", queryEndDate);
		map.put("isRead", "0");
		String mapper="com.wuyizhiye.basedata.info.dao.InfocenterDao.updateIsRead";
		queryExecutor.executeUpdate(mapper, map);
		
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	//获取未读数量
	
	@RequestMapping(value = "notReadCount")
	public void notReadCount(HttpServletResponse response){
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("isRead", "0");
		String queryStartDate = getRequest().getParameter("queryStartDate");
		String queryEndDate = getRequest().getParameter("queryEndDate");
		map.put("queryStartDate", queryStartDate);
		map.put("queryEndDate", queryEndDate);

		int count= queryExecutor.execOneEntity("com.wuyizhiye.basedata.info.dao.InfocenterDao.selectCount", map,Integer.class);
		getOutputMsg().put("COUNT", count);
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	/**
	 * 已读/未读
	 * @param org 
	 * @param response
	 */
	@RequestMapping(value="isRead")
	public void isRead(Infocenter infocenter,HttpServletResponse response){
		Infocenter old = infocenterService.getEntityById(infocenter.getId());
		old.setIsRead(infocenter.getIsRead());
		infocenterService.updateEntity(old);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	

	
	/**
	 * json转换config
	 * @return
	 */
	@Override
	protected JsonConfig getDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
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

	@RequestMapping(value = "updateStatus")
	public void updateStatus(HttpServletResponse response) {
		InfomationUtils.updateInfoStatus(getString("id"), getString("status"));
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}
	
	@RequestMapping(value = "updateNoWarnInfoStatus")
	public void updateNoWarnInfoStatus(HttpServletResponse response) {
		InfomationUtils.updateNoWarnInfoStatus();
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "操作成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg()).toString());
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListMapper() {
		return Infocenter.NAME_SPACE + ".select";
	}

	@Override
	protected BaseService getService() {
		return infocenterService;
	}

	@RequestMapping(value="saveSysMessage")
	public void saveSysMessage(HttpServletResponse response){
		this.infocenterService.sendInfo(getParaMap());
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 信息中心count
	 * @return
	 */
	@RequestMapping(value="getMsgCount")
	@ResponseBody
	public void getAllMsgCount(HttpServletResponse response){		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("currentId", SystemUtil.getCurrentUser().getId());
		if(StringUtils.isNotNull(getString("type"))){
			param.put("type", getString("type"));
		}
		
		Map<String,String> countMap = queryExecutor.execOneEntity("com.wuyizhiye.basedata.info.dao.InfocenterDao.getMsg", param, Map.class);
		
		getOutputMsg().put("countMap", countMap);
		
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	public Map<String,Integer> getAllMsgCount(){		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("currentId", SystemUtil.getCurrentUser().getId());
		if(StringUtils.isNotNull(getString("type"))){
			param.put("type", getString("type"));
		}
		
		Map<String,Integer> countMap = queryExecutor.execOneEntity("com.wuyizhiye.basedata.info.dao.InfocenterDao.getMsg", param, Map.class);
		
		return countMap;
	}
	
	@RequestMapping(value="show")
	public String toInfoCenter(ModelMap map){
		boolean lineSendPer = this.hasPermission("07ab3e88-ea42-4fb5-a92f-9c5c766e97d3");
		map.put("lineSendPer", lineSendPer);
		return "basedata/info/infocenter";
	}
	
	/**
	 * 读取系统消息
	 * @param response
	 */
	@RequestMapping(value="otherInfoData")
	@ResponseBody
	public Pagination<Infocenter> otherInfoData(){
		String page = getString("page");
		String size = getString("size");
		Map<String, Object> param = new HashMap<String, Object>();
		
		Map<String,String> params = getParamMap();
		Set<String> keys = params.keySet();
		for(String key : keys){
			param.put(key, params.get(key));
		}
		
		String queryStartDate = (String)param.get("queryStartDate");
		String queryEndDate = (String)param.get("queryEndDate");
		if(!StringUtils.isEmpty(queryStartDate)){
			param.put("queryStartDate", DateUtil.convertDateToStr(DateUtil.convertStrToDate(queryStartDate,"yyyy/MM/dd"),"yyyy-MM-dd"));
		}
		if(!StringUtils.isEmpty(queryEndDate)){
			param.put("queryEndDate", DateUtil.convertDateToStr(DateUtil.getNextDay(DateUtil.convertStrToDate(queryEndDate,"yyyy/MM/dd")),"yyyy-MM-dd"));
		}
		int currentPage = null==page||"".equals(page)?1:Integer.parseInt(page);
		int pageSize = null == size ||"".equals(size)?10:Integer.parseInt(size);
		
		param.put("currentId", SystemUtil.getCurrentUser().getId());
		String type = getString("type");
		if(StringUtils.isNotNull(type)){
			param.put("type", type);
		}
		Pagination<Infocenter> pagination = new Pagination<Infocenter>(pageSize, currentPage);
		pagination = queryExecutor.execQuery(Infocenter.NAME_SPACE+".select", pagination, param);
		queryExecutor.getExecuteSql(Infocenter.NAME_SPACE+".select", param);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Infocenter> msgList = pagination.getItems();
		for (Infocenter info : msgList) {
			info.setCreateDateStr(sdf.format(info.getCreateDate()));
		}
		
		return pagination;
	}
	
	@RequestMapping(value="read")
	public void read(HttpServletResponse response){
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		param.put("personId", getCurrentUser().getId());
		param.put("id", getString("infoId"));
		queryExecutor.executeUpdate(Infocenter.NAME_SPACE+".updateIsReadByPerson", param);
		
		InfoCenterUnread data=new InfoCenterUnread();
		data.setId(getCurrentUser().getId());
		infoCenterUnreadService.saveNotRead(data); //修改未读消息汇总表
		
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="getMsg")
	public void getMsg(HttpServletResponse response){
		String TIMINGREADMSGMETHOD = ParamUtils.getParamValue("TIMINGREADMSGMETHOD");
//		HashMap<String,Object> param = new HashMap<String, Object>();
//		param.put("currentId", getCurrentUser().getId());
//		param.put("remindType", "FLOAT_BOX");
//		Map<String,String> countMap = queryExecutor.execOneEntity(Infocenter.NAME_SPACE+".getMsg", param, Map.class);
//		HashMap<String,Object> param1 = new HashMap<String, Object>();
//		param1.put("currentId", getCurrentUser().getId());
//		Map<String,String> countMapForUnread = queryExecutor.execOneEntity(Infocenter.NAME_SPACE+".getMsg", param1, Map.class);
//		Set<String> keys = countMap.keySet();
//		Iterator<String> it = keys.iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			if(!BigDecimal.ZERO.equals(countMap.get(key))){
//				param.put("isRead", "0");
//				if(key.equals("I_COUNT")){
//					param.put("type", "INTERACTIVE");
//					Infocenter infocenter = queryExecutor.execOneEntity(Infocenter.NAME_SPACE+".select", param, Infocenter.class);
//					getOutputMsg().put("I_Infocenter", infocenter);
//					if(infocenter!=null){
//						infocenter.setIsRead(0);
//						infocenter.setRemindType(RemindTypeEnum.FLOAT_BOX_ED);
//						infocenterService.updateEntity(infocenter);
//					}
//				}else if(key.equals("S_COUNT")){
//					param.put("type", "SYSTEM");
//					Infocenter infocenter = queryExecutor.execOneEntity(Infocenter.NAME_SPACE+".select", param, Infocenter.class);
//					getOutputMsg().put("S_Infocenter", infocenter);
//					if(infocenter!=null){
//						infocenter.setIsRead(0);
//						infocenter.setRemindType(RemindTypeEnum.FLOAT_BOX_ED);
//						infocenterService.updateEntity(infocenter);
//					}
//				}else if(key.equals("B_COUNT")){
//					param.put("type", "BUSINESS");
//					Infocenter infocenter = queryExecutor.execOneEntity(Infocenter.NAME_SPACE+".select", param, Infocenter.class);
//					getOutputMsg().put("B_Infocenter", infocenter);
//					if(infocenter!=null){
//						infocenter.setIsRead(0);
//						infocenter.setRemindType(RemindTypeEnum.FLOAT_BOX_ED);
//						infocenterService.updateEntity(infocenter);
//					}
//				}else if(key.equals("P_COUNT")){
//					param.put("type", "PROCESS");
//					Infocenter infocenter = queryExecutor.execOneEntity(Infocenter.NAME_SPACE+".select", param, Infocenter.class);
//					getOutputMsg().put("P_Infocenter", infocenter);
//					if(infocenter!=null){
//						infocenter.setIsRead(0);
//						infocenter.setRemindType(RemindTypeEnum.FLOAT_BOX_ED);
//						infocenterService.updateEntity(infocenter);
//					}
//				}
//			}
//		}
//		
//		getOutputMsg().put("countMap", countMap);
//		getOutputMsg().put("countMapForUnread", countMapForUnread);
		 if(TIMINGREADMSGMETHOD!=null && ("redis".equals(TIMINGREADMSGMETHOD) || "tempTable".equals(TIMINGREADMSGMETHOD)))	{	
			Person per = SystemUtil.getCurrentUser();
			HashMap<String,Object> data = 
					this.getRedisClient().getByteObject(SystemUtil.getCustomerOnlySign()+per.getId(), HashMap.class);
			Boolean msgNeedFloatDlg = (Boolean) data.get("MSGNEEDFLOATDLG");
			
			if(null != msgNeedFloatDlg && msgNeedFloatDlg){
				Integer unreadCount =(Integer)data.get("UNREADCOUNT");
				String content = (String) data.get("UNREADCONTENT");
				String title = (String) data.get("UNREADTITLE");
				this.getOutputMsg().put("FLAG", "HASDATA");
				this.getOutputMsg().put("unreadCount", unreadCount);
				this.getOutputMsg().put("content", content);
				this.getOutputMsg().put("title", title);
				
				//将消息设置成已经弹窗
				this.queryExecutor.executeUpdate("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.updateMsgRemindType", per.getId());
			} else {
				this.getOutputMsg().put("FLAG", "NODATA");
			};
		 }
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 提醒页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addRemind")
	public String addRemind(ModelMap model) {
		model.put("title", getString("title"));
		model.put("url", getString("url"));
		model.put("objId", getString("objId"));
		model.put("from", getString("from"));
		model.put("currDate", new Date());
		model.put("currPerson", SystemUtil.getCurrentUser());
		return "basedata/info/customerRemind";
	}
	
	/**
	 * @Description: 保存提醒
	 * @author wsw
	 */
	@RequestMapping(value = "saveRemind")
	public void saveRemind(HttpServletResponse response) {
		String remindPersonIds = getString("remindjson");//提醒人
		String remindTimeStr = getString("remindTimeStr");//时间
		String content = getString("remindDesc");//提醒内容
		String title = getString("title");//提醒标题
		String system = getString("system");//系统
		String sms = getString("sms");//短信
		String weixin = getString("weixin");//微信
		String url = getString("url");
		String objId=getString("objId");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		boolean validate = true;
		if(!StringUtils.isNotNull(sms) && !StringUtils.isNotNull(weixin) && !StringUtils.isNotNull(system)){
			getOutputMsg().put("MSG", "保存失败，请选择提醒渠道！");
			validate = false;
		}else if(!StringUtils.isNotNull(content)){
			getOutputMsg().put("MSG", "保存失败，请填写提醒内容！");
			validate = false;
		}else if(!StringUtils.isNotNull(remindPersonIds)){
			getOutputMsg().put("MSG", "保存失败，必须选择提醒人！");
			validate = false;
		}else if(!StringUtils.isNotNull(remindTimeStr)){
			getOutputMsg().put("MSG", "保存失败，必须选择提醒时间！");
			validate = false;
		}
		if(validate){
			try {
				Date time = sdf.parse(remindTimeStr);
				Remind remind = new Remind();
				remind.setUUID();
				remind.setPersonIds(remindPersonIds);
				remind.setTime(time);
				remind.setContent(content);
				remind.setTitle(title);
				remind.setIsRead(0);
				remind.setUrl(url);
				remind.setCreator(getCurrentUser());
				remind.setObjId(objId);
				if(sms==null){
					remind.setSms(0);
				}else{
					remind.setSms(Integer.parseInt(sms));
				}
				if(system==null){
					remind.setSystem(0);
				}else{
					remind.setSystem(Integer.parseInt(system));
				}
				if(weixin==null){
					remind.setWeixin(0);
				}else{
					remind.setWeixin(Integer.parseInt(weixin));
				}
				remind.setCreateTime(new Date());
				infocenterService.saveRemind(remind);
				
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "保存成功！");
			} catch (ParseException e) {
				logger.error("", e);
			}
		}
		outPrint(response,
				JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="remindPage")
	public String remindPage(){
		this.getRequest().setAttribute("from", "sy");
		return "basedata/info/remindPage";
	}
	
	@RequestMapping(value="deleteRemind")
	public void deleteRemind(@RequestParam(required=true,value="id")String id,HttpServletResponse response){
		int isSuccess = infocenterService.deleteRemind(id);
		if(isSuccess>0){
				getOutputMsg().put("STATE", "SUCCESS");
				getOutputMsg().put("MSG", "删除成功");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "记录不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="remindData")
	public void remindData(Pagination<Remind> pagination,HttpServletResponse response){
		pagination = new Pagination<Remind>();
		HashMap<String, Object> param = new HashMap<String,Object>();
		param.put("isRead", "false");
		param.put("personId", SystemUtil.getCurrentUser().getId());
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.info.dao.InfocenterDao.selectRemind", pagination, param);
		List<Remind> remindList = pagination.getItems();
		for(Remind remind : remindList){
			String personIds = remind.getPersonIds();
			if(personIds.indexOf(",")>0){
				String [] personIdArr = personIds.split(",");
				int j=0;
				if(personIdArr.length>3){
					j=3;
				}else{
					j=personIdArr.length;
				}
				String personStr = "";
				for(int i=0;i<j;i++){
					Person person = personService.getEntityById(personIdArr[i]);
					if(person!=null){
						if(i==j-1&&j<personIdArr.length){
							personStr += person.getName()+"……等";
						}else if(i==j-1){
							personStr += person.getName();
						}else{
							personStr += person.getName()+",";
						}
					}
				}
				remind.setPersonIds(personStr);
			}else{
				Person person = personService.getEntityById(remind.getPersonIds());
				remind.setPersonIds(person.getName());
			}
			
		}
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	/**
	 * 整合首页递归count
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="allCount")
	public void allCount(HttpServletResponse response){
		Map<String,Object> data = new HashMap<String, Object>();
		try {
			Person per = SystemUtil.getCurrentUser();
			Boolean getDataFromRedis = false;
			String TIMINGREADMSGMETHOD = ParamUtils.getParamValue("TIMINGREADMSGMETHOD");
			int isQueryDJLL=getInt("isQueryDJLL",1); //是否需要查询鼎尖聊聊数据 
			if(!StringUtils.isEmpty(TIMINGREADMSGMETHOD) && "redis".equalsIgnoreCase(TIMINGREADMSGMETHOD)){
				getDataFromRedis = true;
			}
			
			if("redis".equals(TIMINGREADMSGMETHOD)){ //从redis缓存中取数据
				data = this.getRedisClient().getByteObject(SystemUtil.getCustomerOnlySign()+per.getId(), HashMap.class);
			} else if("tempTable".equals(TIMINGREADMSGMETHOD)) {  //从数据库临时表中取数据
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("id", per.getId());
				List<Map> mapList= (List<Map>) this.queryExecutor.execQuery("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.selectNotReadMsgInfo", param, Map.class);
				if(null != mapList && mapList.size() > 0){
					data = mapList.get(0);
				}
			}else{
				    LogoConfig logoConfig = null;
					List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", null, LogoConfig.class);
					if(null != cfglist && cfglist.size()>0){
						logoConfig = cfglist.get(0);
					}
					data = new HashMap<String, Object>();
					Map<String,Integer> map = new HashMap<String,Integer>();
					HashMap<String,Object> param = new HashMap<String, Object>();
					if(logoConfig!=null && logoConfig.getToolCheck().indexOf("XXZX")!=-1){ //消息中心
						param.put("id", getCurrentUser().getId());
						InfoCenterUnread countMap = queryExecutor.execOneEntity(InfoCenterUnread.NAME_SPACE+".getById", param, InfoCenterUnread.class);
						if(countMap!=null){
							data.put("unreadCount", countMap.getUnreadCount());
							data.put("lastContent", countMap.getLastContent());
							data.put("lastTitle", countMap.getLastTitle());
							data.put("needOpenDialog", countMap.getNeedOpenDialog());
							
							countMap.setNeedOpenDialog(0); //修改成不需要弹窗
							infoCenterUnreadService.updateEntity(countMap);
						}else{
							data.put("unreadCount", 0);
							data.put("lastContent", "");
							data.put("lastTitle", "");
							data.put("needOpenDialog", 0);
						}
					}
					if(logoConfig!=null && logoConfig.getToolCheck().indexOf("QYYX")!=-1){
						QQMailController qqMail = (QQMailController) ApplicationContextAware.getApplicationContext().getBean(QQMailController.class);//邮件提醒数量
						int qqMailCount = qqMail.getNewCount();
						//map.put("QQMAIL", new Integer(qqMailCount));
						data.put("QQMAIL", new Integer(qqMailCount));
					}
					
					if(logoConfig!=null && logoConfig.getToolCheck().indexOf("QYLC")!=-1){
						param.put("personId", getCurrentUser().getId());
						param.put("positionId", (null==SystemUtil.getCurrentPosition()?"":SystemUtil.getCurrentPosition().getId()));
						Map<String,Object> workflowMap =  queryExecutor.execOneEntity("com.wuyizhiye.workflow.dao.ProcessViewDao.getPersonCount", param,Map.class);
						//map.put("WORKFLOWCOUNT", new Integer(workflowMap.get("PERSONSUBMITCOUNT").toString()));
						data.put("WORKFLOWCOUNT", new Integer(workflowMap.get("PERSONSUBMITCOUNT").toString()));
					}
					if(isQueryDJLL==1 && logoConfig!=null && logoConfig.getToolCheck().indexOf("DJLL")!=-1){
						CChatNewController cchat = (CChatNewController) ApplicationContextAware.getApplicationContext().getBean(CChatNewController.class);//聊聊提醒数量
						int msgCount = cchat.getCount();
						//map.put("CCHATCOUNT", new Integer(msgCount));
						data.put("CCHATCOUNT", new Integer(msgCount));
						
				    	Map<String,Object> djllParam =new HashMap<String, Object>();
				    	djllParam.put("belongId", SystemUtil.getCurrentUser().getId());
				    	djllParam.put("type", "PRIVATE_LETTER");//私信
				    	djllParam.put("isRemind", 0); //查询未弹窗的
				    	
				    	Map<String,Object> chatDataMap =new HashMap<String, Object>();
						List<NewestCChat> coList = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getList", djllParam, NewestCChat.class);
						if(coList!=null && coList.size()>0){ 
							chatDataMap.put("commonOrder", coList.get(0)); 
							chatDataMap.put("count", coList.size());
							chatDataMap.put("coList", coList);
							
							djllParam.clear();
							djllParam.put("belongId", SystemUtil.getCurrentUser().getId());
							djllParam.put("isRemind", 1);
							queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.updateIsRemindOfPerson", djllParam); //修改弹窗状态
							
						}else{
							chatDataMap.put("count", 0);
						}
						data.put("chatDataMap", chatDataMap);
					} 
				
			}
			if("redis".equals(TIMINGREADMSGMETHOD) || "tempTable".equals(TIMINGREADMSGMETHOD)){
				if(null == data){
					data = new HashMap<String, Object>();
				} else {
					if("redis".equals(TIMINGREADMSGMETHOD) || "tempTable".equals(TIMINGREADMSGMETHOD)){
						String DataBaseType = ParamUtils.getParamValue("DataBaseType");
						/*****************存放消息中心的数据 start*********************/
						Integer msgNeedFloatDlg = 0; 
						if("MYSQL".equalsIgnoreCase(DataBaseType)){
							msgNeedFloatDlg = (Integer) data.get("MSGNEEDFLOATDLG");
						} else {
							msgNeedFloatDlg = ((BigDecimal) data.get("MSGNEEDFLOATDLG")) == null ? 0 : ((BigDecimal) data.get("MSGNEEDFLOATDLG")).intValue();
						}
						Map<String,Object> msgDataMap = new HashMap<String, Object>();			//消息的容器
						if(null != msgNeedFloatDlg && msgNeedFloatDlg == 1){	//如果需要弹窗的话
							Integer unreadCount = 0;
							if("MYSQL".equalsIgnoreCase(DataBaseType)){
								unreadCount = (Integer) data.get("UNREADCOUNT");
							} else {
								unreadCount = ((BigDecimal)data.get("UNREADCOUNT")) == null ? 0 : ((BigDecimal)data.get("UNREADCOUNT")).intValue();
							}
									
							String content = (String) data.get("UNREADCONTENT");
							String title = (String) data.get("UNREADTITLE");
							msgDataMap.put("FLAG", "HASDATA");
							msgDataMap.put("unreadCount", unreadCount);
							msgDataMap.put("content", content);
							msgDataMap.put("title", title);
							
							//将消息设置成已经弹窗
							this.queryExecutor.executeUpdate("com.wuyizhiye.basedata.info.dao.TimingGetNotReadInfoDao.updateMsgRemindType", per.getId());
						} else {
							msgDataMap.put("FLAG", "NODATA");
						}
						data.put("msgDataMap", msgDataMap);
						/*****************存放消息中心的数据 end*********************/
						
						/*****************存放聊聊的数据 start********************/
						if(isQueryDJLL==1){
							Map<String,Object> chatDataMap = new HashMap<String, Object>();
							
							String unReadPrivateLetterContent = (String) data.get("CCHATCONTENT");
							List<RedisChatInfoSender> senderList = (List<RedisChatInfoSender>) JSONArray.toCollection(JSONArray.fromObject(unReadPrivateLetterContent),RedisChatInfoSender.class);
							
							 List<NewestCChat> coList = new ArrayList<NewestCChat>();
							 if(null != senderList && senderList.size() >0){
								 for(RedisChatInfoSender tmpVo : senderList){
									 if(null != tmpVo){
										 NewestCChat tmpCommonOrder = new NewestCChat();
										 Person p = new Person(tmpVo.getSendPersonId());
										 p.setName(tmpVo.getSendPersonName());
										 p.setPhoto(tmpVo.getSendPersonPhotoUrl());
										 tmpCommonOrder.setPerson(p);
										 tmpCommonOrder.setUnRead(tmpVo.getNotReadCount());
										 tmpCommonOrder.setContent(tmpVo.getLastMsgContent());
										 coList.add(tmpCommonOrder);
									 }
								 }
							 }
							 
							/**
							 * 取与每个人发送的最后一条私信内容
							 */
							 NewestCChat commonOrder = null;
							if(coList.size()>0){
								commonOrder = coList.get(0);
							}
							chatDataMap.put("commonOrder", commonOrder);
							chatDataMap.put("count", coList.size());
							chatDataMap.put("coList", coList);
							data.put("chatDataMap", chatDataMap);
							/*****************存放聊聊的数据 end*********************/
						}
					}
					if(getDataFromRedis){
						//将redis里面的数据注销
						RedisHolder.getRedisClient().expire(SystemUtil.getCustomerOnlySign()+per.getId(), 0);
					}
				}
			}
			
			data.put("STATE", "SUCCESS");
		} catch (Exception e){
			logger.error("", e);
			data.put("MSG", e.getMessage());
			data.put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(data));
	}
	
	private Map<String,Object> getCurrUerMsg(String personId){
		HashMap<String,Object> param = new HashMap<String, Object>();
		HashMap<String,Object> restMap = new HashMap<String, Object>();
		param.put("currentId",personId);
		param.put("remindType", "FLOAT_BOX");
		param.put("isRead", "0");
		List<Infocenter> list = queryExecutor.execQuery(Infocenter.NAME_SPACE+".select", param, Infocenter.class); 
		restMap.put("FLAG", "noData");
		if(list!=null && list.size()>0){
			restMap.put("FLAG", "HASDATA");
			restMap.put("unreadCount", list.size());
			restMap.put("content", list.get(list.size()-1).getContent());
			restMap.put("title", list.get(list.size()-1).getTitle());
			param.clear();
			param.put("personId",personId);
			param.put("emindType",RemindTypeEnum.POPUP_ED.toString());
			this.queryExecutor.executeUpdate(Infocenter.NAME_SPACE+".updateInfocenterRemindType", param);
		} 
		return restMap;
	}
}
