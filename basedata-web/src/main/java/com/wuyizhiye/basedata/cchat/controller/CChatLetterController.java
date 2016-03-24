package com.wuyizhiye.basedata.cchat.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.cchat.enums.InfoTypeEnum;
import com.wuyizhiye.basedata.cchat.model.CChat;
import com.wuyizhiye.basedata.cchat.model.NewestCChat;
import com.wuyizhiye.basedata.cchat.service.NewestCChatService;
import com.wuyizhiye.basedata.cchat.service.PrivateLetterService;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName CChatLetterController
 * @Description 私信
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/cchat/*")
public class CChatLetterController extends BaseController {

	@Resource(name="queryExecutor")
	protected QueryExecutor queryExecutor;
	
	@Autowired
	private PrivateLetterService privateLetterService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private NewestCChatService commonOrderService;
	
	@RequestMapping(value="save")
	public void addLetter(HttpServletResponse response){
		String content = getString("content");
		String receiveId = getString("receiveId");
		Person receiver = personService.getEntityById(receiveId);
		CChat privateLetter = new CChat();
		privateLetter.setContent(content);
		privateLetter.setCreateTime(new Date());
		privateLetter.setReceiver(receiver);
		privateLetter.setStatus("0");
		privateLetter.setType("0");
		privateLetter.setId(UUID.randomUUID().toString());
		privateLetter.setTopicId(privateLetter.getId());
		Person sender = SystemUtil.getCurrentUser();
		privateLetter.setSender(sender);
		try {
			privateLetterService.addEntity(privateLetter);
			setCommonOrder(privateLetter);
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "发送成功!");
		} catch (Exception e) {
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", e.getMessage());
		}
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	/**
	 * 向用于私信列表排序的 表中添加数据  如果有该联系人 更新数据
	 * @param pl
	 */
	public void setCommonOrder(CChat pl){
		/**
		 * 先 操作 发送人  commonOrder数据
		 */
		setOrderData(pl.getSender(),pl.getReceiver(),pl,"send");
		
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
				co.setIsRemind(0);
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
			NewestCChat nc=new NewestCChat();
			nc.setId(UUID.randomUUID().toString());
			nc.setBelong(sender);
			nc.setPerson(receiver);
			if("send".equals(type)){//相对发送人
				nc.setUnRead(0);
			}else{//相对接收人
				int count=queryExecutor.execOneEntity("com.wuyizhiye.basedata.cchat.dao.CChatDao.getUnReadCount", param, Integer.class);
				nc.setUnRead(count+1);
				nc.setIsRemind(0);
			}
			nc.setContent(pl.getContent());
			nc.setTime(new Date());
			nc.setType(InfoTypeEnum.PRIVATE_LETTER);//私信类别
			commonOrderService.addEntity(nc);
		}
	}
	
	@RequestMapping(value="insertLetter")
	public void insertLetter(HttpServletResponse response){
		String content = getString("content");
		String receiveId = getString("receiveId");
		Person receiver = personService.getEntityById(receiveId);
		CChat privateLetter = new CChat();
		privateLetter.setContent(content);
		privateLetter.setCreateTime(new Date());
		privateLetter.setReceiver(receiver);
		privateLetter.setStatus("0");
		privateLetter.setType("0");
		privateLetter.setId(UUID.randomUUID().toString());
		privateLetter.setTopicId(privateLetter.getId());
		Person sender = SystemUtil.getCurrentUser();
		privateLetter.setSender(sender);
		try {
			privateLetterService.addEntity(privateLetter);
			setCommonOrder(privateLetter);
			//this.outPrint(response, "<script>parent.successMsg();</script>");
			getOutputMsg().put("STATE", "SUCCESS");
		} catch (Exception e) {
			//this.outPrint(response, "<script>parent.faildMsg();</script>");
			getOutputMsg().put("STATE", "FAILD");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="selectNewLetter")
	public String selectNewLetter(){
		Person receiver = SystemUtil.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		String page = getString("page");
		String size = getString("size");
		int currentPage = null==page || "".equals(page)?1:Integer.parseInt(page);
		int pageSize = null==size || "".equals(size)?10:Integer.parseInt(size);
		if(receiver!=null) map.put("currentId", receiver.getId());
		Pagination<CChat> pag = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectNewLetter", new Pagination<CChat>(pageSize,currentPage), map);
		if(pag!=null){
			getRequest().setAttribute("letterPage", pag);
			getRequest().setAttribute("totalpage", pag.getPageCount());
			getRequest().setAttribute("curpage", pag.getCurrentPage());
		}
		
		return "basedata/cchat/newLetter";
	}
	
	
	@RequestMapping(value="selectAllLetter")
	public String selectAllLetter(){
		Person receiver = SystemUtil.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		String page = getString("page");
		String size = getString("size");
		int currentPage = null==page || "".equals(page)?1:Integer.parseInt(page);
		int pageSize = null==size || "".equals(size)?10:Integer.parseInt(size);
		if(receiver!=null) map.put("currentId", receiver.getId());
		Pagination<CChat> pag = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectAllLetter", new Pagination<CChat>(pageSize,currentPage), map);
		if(pag!=null){
			getRequest().setAttribute("letterPage", pag);
			getRequest().setAttribute("totalpage", pag.getPageCount());
			getRequest().setAttribute("curpage", pag.getCurrentPage());
		}
		
		return "basedata/cchat/allLetter";
	}
	
	@RequestMapping(value="selectMyLetter")
	public String selectMyLetter(){
		Person receiver = SystemUtil.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		String page = getString("page");
		String size = getString("size");
		int currentPage = null==page || "".equals(page)?1:Integer.parseInt(page);
		int pageSize = null==size || "".equals(size)?10:Integer.parseInt(size);
		if(receiver!=null) map.put("currentId", receiver.getId());
		Pagination<CChat> pag = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectMyLetter", new Pagination<CChat>(pageSize,currentPage), map);
		if(pag!=null){
			getRequest().setAttribute("letterPage", pag);
			getRequest().setAttribute("totalpage", pag.getPageCount());
			getRequest().setAttribute("curpage", pag.getCurrentPage());
		}
		return "basedata/cchat/myLetter";
	}
	@RequestMapping(value="toReply")
	public void toReply(HttpServletResponse response){
		String content = getString("content");
		String receiveId = getString("receiveId");
		Person receiver = personService.getEntityById(receiveId);
		CChat privateLetter = new CChat();
		privateLetter.setContent(content);
		privateLetter.setCreateTime(new Date());
		privateLetter.setReceiver(receiver);
		privateLetter.setStatus("0");
		privateLetter.setType("1");
		privateLetter.setId(UUID.randomUUID().toString());
		privateLetter.setTopicId(getString("id"));
		Person sender = SystemUtil.getCurrentUser();
		privateLetter.setSender(sender);
		try {
			privateLetterService.addEntity(privateLetter);
			this.outPrint(response, "{msg:'success'}");
		} catch (Exception e) {
			this.outPrint(response, "{msg:'faild'}");
		}
	}
	
	@RequestMapping(value="getReplyList")
	@ResponseBody
	public List<CChat> getReplyList(){
		String topicId = getString("topicId");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("topicId", topicId);
		//updateStatus(topicId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Pagination<CChat> pag = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectLetter", new Pagination<CChat>(), param);
		List<CChat> letterList = pag.getItems();
		for (CChat privateLetter : letterList) {
			if(privateLetter!=null){
				privateLetter.setDateStr(sdf.format(privateLetter.getCreateTime()));
			}
		}
		return pag.getItems();
	}
	@RequestMapping(value="amount")
	@ResponseBody
	public int selectCount(){
		Person sender = SystemUtil.getCurrentUser();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("currentId", sender.getId());
		int count = privateLetterService.selectLetterCount(param);
		return count;
	}
	//@RequestMapping(value="updateStatus")
	@RequestMapping(value="updateStatus")
	@ResponseBody
	public void updateStatus(){
		String id = getString("topicId");
		Person sender = SystemUtil.getCurrentUser();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("receiverId", sender.getId());
		queryExecutor.executeUpdate("com.wuyizhiye.basedata.cchat.dao.CChatDao.updateLetter", param);
	}
	@RequestMapping(value="show")
	public String showAdd(ModelMap map){
		String receiveId = getString("receiveId");
		Person receiver = personService.getEntityById(receiveId);
		map.put("receiver", receiver);
		getRequest().setAttribute("receiver", receiver);
		return "basedata/cchat/letterEdit";
	}
	@RequestMapping(value="queryPrivateLetter")
	public String queryPrivateLetter(){
		return "basedata/cchat/queryPrivateLetter";
	}
	@RequestMapping(value="toChatRecord")
	public String toChatRecord(){
		return "basedata/cchat/chatRecordList";
	}
	@RequestMapping(value="getChatRecord")
	public void getChatRecord(Pagination<Object> pagination,ModelMap model,HttpServletResponse response){
		Map<String,Object> params = getParaMap();
		String senderId = getString("senderId");
		String receiverId = getString("receiverId");
		String key = getString("key");
		String type = getString("type");
		String sortname = getRequest().getParameter("sortname");
		String sortorder = getRequest().getParameter("sortorder");
		String queryStartDate = getString("queryStartDate");
		String queryEndDate = getString("queryEndDate");
		String queryDate = getString("queryDate");
		if(!StringUtils.isEmpty(sortname)){
			if(sortname.equalsIgnoreCase("createtimestr")){
				sortname = "fcreatetime";
			}
			params.put("sortname", "a."+sortname);
			params.put("sortorder", sortorder);
		}
		if (!StringUtils.isEmpty(queryStartDate)) {
			params.put("queryStartDate",
					DateUtil.convertStrToDate(queryStartDate, "yyyy/MM/dd"));
		}
		if (!StringUtils.isEmpty(queryEndDate)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtil.convertStrToDate(queryEndDate,
					"yyyy/MM/dd"));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			params.put("queryEndDate", calendar.getTime());
		}
		if (!StringUtils.isEmpty(queryDate)) {
			params.put("queryDate",
					DateUtil.convertStrToDate(queryDate, "yyyy/MM/dd"));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtil.convertStrToDate(queryDate,
					"yyyy/MM/dd"));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			params.put("queryDateEnd", calendar.getTime());
		}
        if(senderId!=null&&!"".equals(senderId)){
        	params.put("senderId", senderId);
        }
        if(receiverId!=null&&!"".equals(receiverId)){
        	params.put("receiverId", receiverId);
        }
        if(key!=null&&!"".equals(key)){
        	params.put("key", key);
        }
        if(type!=null&&!"".equals(type)){
        	params.put("type", type);
        }
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.cchat.dao.CChatDao.getListByParam", pagination, params);
		outPrint(response, JSONObject.fromObject(pagination,getDefaultJsonConfig()));
	}
}
