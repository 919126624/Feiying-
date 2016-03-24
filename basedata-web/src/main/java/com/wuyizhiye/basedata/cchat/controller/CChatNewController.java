package com.wuyizhiye.basedata.cchat.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.cchat.enums.InfoTypeEnum;
import com.wuyizhiye.basedata.cchat.model.CChat;
import com.wuyizhiye.basedata.cchat.model.NewestCChat;
import com.wuyizhiye.basedata.cchat.service.NewestCChatService;
import com.wuyizhiye.basedata.cchat.service.PrivateLetterService;
import com.wuyizhiye.basedata.info.model.RedisChatInfoSender;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName CChatNewController
 * @Description 查看私信 变动
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping("basedata/cchatNew/*")
public class CChatNewController extends ListController{
	private static Logger logger=Logger.getLogger(CChatNewController.class);
	@Autowired
	private PrivateLetterService privateLetterService;
	
	@Autowired
	private NewestCChatService commonOrderService;
	
	@Override
	protected CoreEntity createNewEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getListView() {
		//聊聊的刷新时间
		Integer refreshtimeChat = ParamUtils.getIntValue("REFRESHTIME_CHAT");
		if(null == refreshtimeChat){
			refreshtimeChat = 8;
		} 
		this.getRequest().setAttribute("REFRESHTIME_CHAT", refreshtimeChat);
		return "basedata/cchat/priLetterList";
	}

	@Override
	protected String getEditView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
    @RequestMapping("priLetterList")
    public String getData(Pagination<NewestCChat> pagination,ModelMap model){
    	Map<String,Object> param=new HashMap<String, Object>();
    	param.put("belongId", SystemUtil.getCurrentUser().getId());
    	param.put("type", InfoTypeEnum.PRIVATE_LETTER);//私信
    	pagination.setPageSize(10);
    	if(StringUtils.isNotNull(getString("currentPage"))){
	    	int currentPage = Integer.parseInt(getString("currentPage"));
	    	pagination.setCurrentPage(currentPage+1);
    	}
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getList", pagination, param);
		/**
		 * 取与每个人发送的最后一条私信内容
		 */
		model.put("dataList", pagination.getItems());
		model.put("curpage", pagination.getCurrentPage());
		model.put("totalpage", pagination.getPageCount()); 
//		model.put("recordCount", pagination.getRecordCount());
		//聊聊的刷新时间
		Integer refreshtimeChat = ParamUtils.getIntValue("REFRESHTIME_CHAT");
		if(null == refreshtimeChat){
			refreshtimeChat = 8;
		} 
		this.getRequest().setAttribute("REFRESHTIME_CHAT", refreshtimeChat);
		return "basedata/cchat/priLetterList";
    }
    
    @RequestMapping("priLetterMsg")
    public void priLetterMsg(HttpServletResponse response,ModelMap model){
//    	Map<String,Object> param=new HashMap<String, Object>();
//    	param.put("belongId", SystemUtil.getCurrentUser().getId());
//    	param.put("type", CommonOrderEnum.PRIVATE_LETTER);//私信
//    	param.put("unRead", "true");//私信
//		List<CommonOrder> coList = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getList", param, CommonOrder.class);
		
		Person per = SystemUtil.getCurrentUser();
		HashMap<String,Object> data = 
				this.getRedisClient().getByteObject(SystemUtil.getCustomerOnlySign()+per.getId(), HashMap.class);
		String unReadPrivateLetterContent = (String) data.get("CCHATCONTENT");
		List<RedisChatInfoSender> senderList = (List<RedisChatInfoSender>) JSONArray.toCollection(JSONArray.fromObject(unReadPrivateLetterContent),RedisChatInfoSender.class);
		
		 List<NewestCChat> ncList = new ArrayList<NewestCChat>();
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
					 ncList.add(tmpCommonOrder);
				 }
			 }
		 }
		 
		/**
		 * 取与每个人发送的最后一条私信内容
		 */
		NewestCChat commonOrder = null;
		if(ncList.size()>0){
			commonOrder = ncList.get(0);
		}
		model.put("commonOrder", commonOrder);
		model.put("count", ncList.size());
		model.put("coList", ncList);
		outPrint(response, JSONObject.fromObject(model));
    }
    
    @RequestMapping("getPriLetterCount")
    public void getPriLetterCount(HttpServletResponse response,ModelMap model){
    	Map<String,Object> param=new HashMap<String, Object>();
    	param.put("currId", SystemUtil.getCurrentUser().getId());
    	param.put("type", InfoTypeEnum.PRIVATE_LETTER);//私信
		int count = queryExecutor.execOneEntity("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getPriLetterCount", param, Integer.class);
		model.put("count", count);
		outPrint(response, JSONObject.fromObject(model));
    }
    
    public Integer getCount(){
    	Map<String,Object> param=new HashMap<String, Object>();
    	param.put("currId", SystemUtil.getCurrentUser().getId());
    	param.put("type", InfoTypeEnum.PRIVATE_LETTER);//私信
    	int count = 0;
		count = queryExecutor.execOneEntity("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getPriLetterCount", param, Integer.class);
		return count;
    }
    
    /**
     * 点击全部已读之后改变私信状态
     * @param response
     * @param model
     */
    @RequestMapping("refreshMessage")
    public void refreshMessage(HttpServletResponse response,ModelMap model){//用privateletterdao里找语句
    	Map<String,Object> param=new HashMap<String, Object>();
    	param.put("currentId", SystemUtil.getCurrentUser().getId());//发送人
    	param.put("senderId", getString("senderId"));//接收人
    	List<CChat> pList = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectNewLetter", param, CChat.class);
    	for(CChat privateLetter:pList){
    		queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.CChatDao.updateStatus", privateLetter);
    	}
    	model.put("pList", pList);
    	outPrint(response, JSONObject.fromObject(model));
    }
    
    /**
     * 取得最新未读消息
     * @param response
     * @param model
     */
    @RequestMapping("updateStatu")
    public void updateStatu(HttpServletResponse response,ModelMap model){//用privateletterdao里找语句
    	
    	Map<String,Object> param= new HashMap<String, Object>();
    	String receiverId=getString("id");
    	String isAll=getString("isAll");
    	param.put("personId", receiverId);
    	if("false".equals(isAll)){
    		queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.updateStatus", param);
    	}else{
    		queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.updateStatusOfPerson", param);
    	}
    }
    
    
    /**
     * 取得最新有更新的会话页签的信息
     * @param response
     * @param model
     */
    @RequestMapping("refreshPriLetterList")
    public void refreshPriLetterList(HttpServletResponse response,ModelMap model){
    	try{    		
    		Map<String,Object> param=new HashMap<String, Object>();
    		param.put("belongId", SystemUtil.getCurrentUser().getId());
    		param.put("type", InfoTypeEnum.PRIVATE_LETTER);//私信
    		param.put("unRead", "true");
    		List<NewestCChat> cList = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getListByMessage", param, NewestCChat.class);
    		
    		/**
    		 * 取得有最新消息的人的最后一条私信内容
    		 */
    		model.put("cList", setcList(cList));
    		
    		String detailFlag=getString("detailFlag","");
    		if("yes".equals(detailFlag)){//窗口打开
    			String senderId=getString("senderId");
    			param.clear();
    			param.put("currentId", SystemUtil.getCurrentUser().getId());//发送人
    			param.put("senderId", senderId);//接收人
    			List<CChat> pList = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectNewLetter", param, CChat.class);
    			for(CChat privateLetter:pList){
    				queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.CChatDao.updateStatus", privateLetter);
    			}
    			param.clear();
    			param.put("belongId", SystemUtil.getCurrentUser().getId());
    			param.put("personId", senderId);
    	    	queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.updateUnReadAndRemined", param);
    			model.put("pList", setpList(pList));
    		}
    		model.put("STATE", "SUCCESS");
    	}catch(Exception e){
    		model.put("STATE", "FAIL");
    		model.put("MSG", e.getMessage());
    	}
		outPrint(response, JSONObject.fromObject(model));
    }
    
    private List<Map>setcList(List<NewestCChat> cList){
    	List<Map>maps=new ArrayList<Map>();
    	if(null!=cList && cList.size()>0){
    		for(NewestCChat chat:cList){
    			Map map=new HashMap();
    			map.put("PERSONID", chat.getPerson().getId());
    			map.put("CONTENT", chat.getContent());
    			map.put("PERSONPHOTO", chat.getPerson().getPhoto());
    			map.put("PERSONNAME", chat.getPerson().getName());
    			map.put("PERSONNUMBER", chat.getPerson().getNumber());
    			map.put("UNREAD", chat.getUnRead());
    			map.put("TIME", DateUtil.convertDateToStr(chat.getTime(), "yy-MM-dd hh:mm:ss"));
    			maps.add(map);
    		}
    	}
    	return maps;
    }
    
    
    private List<Map>setpList(List<CChat> pList){
    	List<Map>maps=new ArrayList<Map>();
    	if(null!=pList && pList.size()>0){
    		for(CChat chat:pList){
    			Map map=new HashMap();
    			map.put("SENDERPHOTO", chat.getSender().getPhoto());
    			map.put("CONTENT", chat.getContent());
    			map.put("CREATETIMESTR", chat.getCreateTimeStr());
    			maps.add(map);
    		}
    	}
    	return maps;
    }
    
    protected JsonConfig getDataDefaultJsonConfig(){
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
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
    
    @RequestMapping("priLetterListJson")
    public void getDataForJson(Pagination<NewestCChat> pagination,HttpServletResponse response){
    	Map<String,Object> param=new HashMap<String, Object>();
    	param.put("belongId", SystemUtil.getCurrentUser().getId());
    	param.put("type", InfoTypeEnum.PRIVATE_LETTER);//私信
		pagination.setPageSize(10);
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.getList", pagination, param);
		/**
		 * 取与每个人发送的最后一条私信内容
		 */
		getOutputMsg().put("dataList", pagination.getItems());
		getOutputMsg().put("curpage", pagination.getCurrentPage());
		getOutputMsg().put("totalpage", pagination.getPageCount()); 
		getOutputMsg().put("recordCount", pagination.getRecordCount());
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
    }
    
//    /**
//     * 最后一次私信内容
//     */
//    public void getLastContent(PrivateLetter pl){
//    	Map<String,Object> param=this.getListDataParam();
//    	param.put("receiverId", pl.getReceiver().getId());
//    	List<PrivateLetter> plist=queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.getLastContent", param, PrivateLetter.class);
//    	if(null != plist && plist.size()>0){
//    		pl.setContent(plist.get(plist.size()-1).getContent());
//    		pl.setCreateTime(plist.get(plist.size()-1).getCreateTime());
//    	}
//    }
    
    @RequestMapping("toDetail")
    public String toDetail(ModelMap model){
    	String receiverId=getString("id");//对应人员ID
    	Map<String,Object> param=this.getListDataParam();
    	if(!StringUtils.isNotNull(receiverId)){
    		receiverId = param.get("receiveId").toString();
    	}
    	model.put("receiverId", receiverId);
    	param.put("receiverId", receiverId);
    	Person person=queryExecutor.execOneEntity("com.wuyizhiye.basedata.cchat.dao.CChatDao.getPersonData", param, Person.class);
    	model.put("person", person);
    	
    	
    	
    	Pagination<CChat> pagination = new Pagination<CChat>(6,1);
    	pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.getLastContent", pagination, param);
    	List<CChat> plist=pagination.getItems();
	    Collections.reverse(plist);
    	model.put("showList", plist);
    	model.put("currentPage", pagination.getCurrentPage());
    	model.put("pageCount", pagination.getPageCount());
    	
    	
    	/**
    	 * 更新点击查看的  接收人是自己的 所有私信状态
    	 */
    	List<CChat> ulist=new ArrayList<CChat>();
    	String cid=SystemUtil.getCurrentUser().getId();
    	for(CChat pl:plist){
    		if(pl.getReceiver().getId().equals(cid)){
    			ulist.add(pl);
    		}
    	}
    	for(CChat pe:ulist){
    		queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.CChatDao.updateStatus", pe);
    	}
    	//更新排序表的数据
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("belongId", SystemUtil.getCurrentUser().getId());
    	map.put("personId", receiverId);
    	queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.updateStatus", map);
    	return "basedata/cchat/priLetterDetail";
    }	
    
    
    @RequestMapping("more")
    public void more(HttpServletResponse response){
    	Map<String,Object> param=this.getListDataParam();
    	String receiverId = param.get("receiveId").toString();
    	param.put("personId", getCurrentUser().getId());
    	param.put("receiverId", receiverId);
    	Pagination<CChat> pagination = new Pagination<CChat>();
    	pagination.setPageSize(6);
    	String currentPage = getString("currentPage");
    	if(StringUtils.isNotNull(currentPage)){
    		pagination.setCurrentPage(Integer.parseInt(currentPage));
    	}else{
    		pagination.setCurrentPage(1);
    	}
    	pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.getLastContent", pagination, param);
    	getOutputMsg().put("showList", pagination);
    	
    	
    	
    	List<CChat> plist=pagination.getItems();
    	/**
    	 * 更新点击查看的  接收人是自己的 所有私信状态
    	 */
    	List<CChat> ulist=new ArrayList<CChat>();
    	String cid=SystemUtil.getCurrentUser().getId();
    	for(CChat pl:plist){
    		if(pl.getReceiver().getId().equals(cid)){
    			ulist.add(pl);
    		}
    	}
    	for(CChat pe:ulist){
    		queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.CChatDao.updateStatus", pe);
    	}
    	//更新排序表的数据
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("belongId", SystemUtil.getCurrentUser().getId());
    	map.put("personId", receiverId);
    	queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.updateStatus", map);
    	
    	outPrint(response, JSONObject.fromObject(getOutputMsg()));
    }	
    
    /**
     * 保存私信
     * @param response
     */
    @RequestMapping(value="saveLetter")
    @ResponseBody
	public void saveLetter(HttpServletResponse response){
    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String content = getString("content");
		String receiverId = getString("receiverId");
		
		//是否发送微信
    	String isWx = getString("isWx");
    	if("true".equals(isWx)){
    		try {
				HttpClientUtil.callHttpsUrl(SystemUtil.getBase()+"/weixinapi/sendMessage?m_teyp=text&m_opiden="+getCurrentUser().getNumber()+"&m_module=zhushou&m_contten="+URLEncoder.encode(
						"私信消息："+content,"utf-8"), "");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
    	}
		
		Person receiver = new Person();
		receiver.setId(receiverId);
		CChat privateLetter = new CChat();
		privateLetter.setContent(content);
		privateLetter.setCreateTime(new Date());
		privateLetter.setReceiver(receiver);
		privateLetter.setStatus("0");
		privateLetter.setType("0");
		privateLetter.setId(UUID.randomUUID().toString());
		privateLetter.setTopicId(privateLetter.getId());
		Person sender = SystemUtil.getCurrentUser();
		privateLetter.setOrg(SystemUtil.getCurrentOrg());
		privateLetter.setSender(sender);
		try {
			privateLetterService.addEntity(privateLetter);
			 
			setCommonOrder(privateLetter);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("now", df.format(new Date()));
			getOutputMsg().put("pic", getCurrentUser().getPhoto());
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			getOutputMsg().put("STATE", "FAILD");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
    /**
	 * 向用于私信列表排序的 表中添加数据  如果有该联系人 更新数据
	 * @param pl
	 */
	public void setCommonOrder(CChat pl){
		/**
		 * 先 操作 发送人  commonOrder数据
		 */
	//	setOrderData(pl.getSender(),pl.getReceiver(),pl,"send");
		
		/**
		 * 接收人
		 */
		setOrderData(pl.getReceiver(),pl.getSender(),pl,"receive");
	}
	
	public void setOrderData(Person sender,Person receiver,CChat pl,String type){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("belongId", sender.getId());//发送人
		param.put("personId", receiver.getId());//接收人
		
		List<NewestCChat> clist=queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.NewestCChatDao.select", param, NewestCChat.class);
		if(null != clist && clist.size()>0){
			if(clist.size() == 1){//有一条数据 更新这条数据的信息
				NewestCChat co=clist.get(0);
				co.setContent(pl.getContent());
				co.setTime(new Date());
				int count=queryExecutor.execOneEntity("com.wuyizhiye.basedata.cchat.dao.CChatDao.getUnReadCount", param, Integer.class);
				co.setIsRemind(0);
				co.setUnRead(count);
				commonOrderService.updateEntity(co);
			}else{//如果有多余的重复数据 自动删除
				String ids="(";
				for(int i=1;i<clist.size();i++){
					NewestCChat cd=clist.get(i);
					ids+="'"+cd.getId()+"',";
				}
				if(!"(".equals(ids)){
					ids=ids.substring(0, ids.length()-1)+")";
				}else{
					ids="('')";
				}
				//根据ID删除多余的数据
				commonOrderService.deleteById(ids);
			}
		}else{//新增条数据
			NewestCChat co=new NewestCChat();
			co.setId(UUID.randomUUID().toString());
			co.setBelong(sender);
			co.setPerson(receiver);
			co.setIsRemind(0);
			 
			int count=queryExecutor.execOneEntity("com.wuyizhiye.basedata.cchat.dao.CChatDao.getUnReadCount", param, Integer.class);
			co.setUnRead(count+1);
			 
			co.setContent(pl.getContent());
			co.setTime(new Date());
			co.setType(InfoTypeEnum.PRIVATE_LETTER);//私信类别
			commonOrderService.addEntity(co);
		}
	}
    
	@Override
	protected Map<String, Object> getListDataParam() {
		Map<String,Object> param=super.getListDataParam();
		param.put("personId", SystemUtil.getCurrentUser().getId());
		return param;
	}

	@Override
	protected String getListMapper() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.cchat.dao.CChatDao.getAllSingel";
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
