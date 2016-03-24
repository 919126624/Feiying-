package com.wuyizhiye.basedata.interflow.online;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.listener.OnlineListener;
import com.wuyizhiye.framework.listener.OnlineUser;

/**
 * @ClassName OnlinePersonController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/online/*")
public class OnlinePersonController extends BaseController {
	private static Logger logger=Logger.getLogger(OnlinePersonController.class);
	@Autowired
	private QueryExecutor queryExecutor;
	
	@Autowired
	private PersonService personService;
	
	@RequestMapping(value="getOnlinePerson_old")
	public void getOnlinePerson_old(HttpServletResponse response){
		 
		Hashtable<String, OnlineUser> tab = OnlineListener.getOnLineUsers();
//		Hashtable<String, OnlineUser> tab = new Hashtable<String, OnlineUser>();
//		for(String str:tab1.keySet()){
//			OnlineUser u = tab1.get(str);
//			if(!u.getIsOnline().equals("no")){
//				//getRequest().getLocale().get
//				//tab.remove(u.getId());
//				//LocalSessionContext.getSession(u.getSessionId()).invalidate();
//				tab.put(u.getId(), u);
//			}
//		}
		
		Hashtable<String, OnlineUser> searchTab = new Hashtable<String, OnlineUser>();
		String key = getString("key");
		String orgName = getString("orgName");
		String positionName = getString("positionName");
		boolean flag = StringUtils.isNotNull(orgName)?false:true;
		boolean flag2 = StringUtils.isNotNull(positionName)?false:true;
		if(StringUtils.isNotNull(key)&&!(key.equals("名称/短号"))){
			if(flag&&flag2){
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if(user.getUserName().indexOf(key)!=-1||user.getNumber().indexOf(key)!=-1){
						searchTab.put(user.getId(), user);
					}
				}
			}else if(flag&&!flag2){
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if((user.getUserName().indexOf(key)!=-1||user.getNumber().indexOf(key)!=-1)&&user.getPositionName().equals(positionName)){
						searchTab.put(user.getId(), user);
					}
				}
			}else if(!flag&&flag2){
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if((user.getUserName().indexOf(key)!=-1||user.getNumber().indexOf(key)!=-1)&&user.getOrgName().equals(orgName)){
						searchTab.put(user.getId(), user);
					}
				}
			}else{
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if((user.getUserName().indexOf(key)!=-1||user.getNumber().indexOf(key)!=-1)&&user.getOrgName().equals(orgName)&&user.getPositionName().equals(positionName)){
						searchTab.put(user.getId(), user);
					}
				}
			}
		}else{
			if(flag&&flag2){
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					searchTab.put(user.getId(), user);
				}
			}else if(flag&&!flag2){
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if(user.getPositionName().equals(positionName)){
						searchTab.put(user.getId(), user);
					}
				}
			}else if(!flag&&flag2){
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if(user.getOrgName().equals(orgName)){
						searchTab.put(user.getId(), user);
					}
				}
			}else{
				for(String s:tab.keySet()){
					OnlineUser user = tab.get(s);
					if(user.getOrgName().equals(orgName)&&user.getPositionName().equals(positionName)){
						searchTab.put(user.getId(), user);
					}
				}
			}
		}
		//Map<String, Map<String, Object>> onlinePersons = (Map<String, Map<String, Object>>) getRequest().getSession().getServletContext().getAttribute("loginInfos");
//		Set<String> set= onlinePersons.keySet();
//		for (String string : set) {
//			Map<String, Object> map = onlinePersons.get(string);
//			persons.add(map);
//		}
//		outPrint(response, JSONArray.fromObject(persons,getDefaultJsonConfig()));
//		for(String key :onlinePersons.keySet()){
//			System.out.println("当前登陆时间==============================="+onlinePersons.get(key).get("loginTime"));
//		}
		outPrint(response, JSONObject.fromObject(searchTab));
	}
	
	@RequestMapping(value="getOnlinePerson")
	public void getOnlinePerson(HttpServletResponse response){ 
		String cuId = SystemUtil.getCurrentControlUnit().getId();//cuid
		Hashtable<String, OnlineUser> usersForPc = OnlineListener.getOnLineUsers();//在线电脑 用户
		/*for(String gas:usersForPc.keySet()){//把不是该cu下的在线人员删除
			OnlineUser user = usersForPc.get(gas);
			if(user.getControlUnit()!=null && !user.getControlUnit().getId().equals(cuId)){
				usersForPc.remove(gas);
			}
		}*/
		Hashtable<String, OnlineUser> searchTab_pc = new Hashtable<String, OnlineUser>();
		Hashtable<String, OnlineUser> searchTab_mobile = new Hashtable<String, OnlineUser>();
		Hashtable<String, Map> dataMap = new Hashtable<String, Map>();
		String key = getString("key"); 
		
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("key_workbench", key);
		param.put("changeTypeNotIn", "DIMISSION");
		//param.put("cuId", cuId);
		Pagination<Person> pagination = new Pagination<Person>();
		pagination.setPageSize(20);
		if(StringUtils.isNotNull(getString("currPage"))){
			pagination.setCurrentPage(Integer.parseInt(getString("currPage"))+1);
		}
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.selectOfOnlineByParam", pagination, param);//所有用户
		List<Person> users = pagination.getItems();
		HashMap<String,Object> pageMap= new HashMap<String, Object>();
		pageMap.put("currPage", pagination.getCurrentPage());
		pageMap.put("pageCount", pagination.getPageCount());
		dataMap.put("page", pageMap);
		Hashtable<String, OnlineUser> usersForMobile  = OnlineListener.getOnLineUsersForMobile() ;//在线手机用户
		/*for(String gas:usersForMobile.keySet()){//把不是该cu下的在线人员删除
			OnlineUser user = usersForMobile.get(gas);
			if(user.getControlUnit()!=null && !user.getControlUnit().getId().equals(cuId)){
				usersForMobile.remove(gas);
			}
		}*/
		
		pageMap.put("count", Integer.parseInt(pagination.getCount())-usersForPc.size()-usersForMobile.size());
		List<Person> userList = users;
		if(users!=null){
			for(String gas:usersForPc.keySet()){
				for(int i=0;i<users.size();i++){
					OnlineUser user = usersForPc.get(gas);
					if(users.get(i).getId().equals(user.getId())){
						userList.remove(users.get(i));
					}
				}
			}
	
			for(String gas:usersForMobile.keySet()){
				for(int i=0;i<users.size();i++){
					OnlineUser user = usersForMobile.get(gas);
					if(users.get(i).getId().equals(user.getId())){
						userList.remove(users.get(i));
					}
				}
			}
		}
		
	
		if(StringUtils.isNotNull(key)&&!(key.equals("名称/短号/组织/岗位"))){
			for(String gas:usersForPc.keySet()){
				 OnlineUser user = usersForPc.get(gas);
				 if((user.getUserName()!=null && user.getUserName().indexOf(key)!=-1) || (user.getNumber() !=null &&user.getNumber().indexOf(key)!=-1)
						 || (user.getOrgName()!=null && user.getOrgName().indexOf(key)!=-1 )
						 || ( user.getPositionName()!=null && user.getPositionName().indexOf(key)!=-1)
						 || ( user.getPositionNum()!=null && user.getPositionNum().indexOf(key)!=-1)
						 ){
					 searchTab_pc.put(user.getId(), user);
				 }
			}
			for(String s:usersForMobile.keySet()){
				 OnlineUser user = usersForMobile.get(s);
				 if((user.getUserName()!=null && user.getUserName().indexOf(key)!=-1) || (user.getNumber() !=null &&user.getNumber().indexOf(key)!=-1)
						 || (user.getOrgName()!=null && user.getOrgName().indexOf(key)!=-1 )
						 || ( user.getPositionName()!=null && user.getPositionName().indexOf(key)!=-1)
						 || ( user.getPositionNum()!=null && user.getPositionNum().indexOf(key)!=-1)
						 ){
					 searchTab_mobile.put(user.getId(), user);
				 }
			}
			dataMap.put("pcOnlin", searchTab_pc);
			dataMap.put("mobileOnlin", searchTab_mobile);
			HashMap<String,Object> temp= new HashMap<String, Object>();
			temp.put("user", userList);
			dataMap.put("users",temp);
		}else{
			dataMap.put("pcOnlin", usersForPc);
			dataMap.put("mobileOnlin", usersForMobile);
			
			HashMap<String,Object> temp= new HashMap<String, Object>();
			temp.put("user", userList);
			dataMap.put("users",temp);
		}
		outPrint(response, JSONObject.fromObject(dataMap));
	}
	
	@RequestMapping(value="toJobList")
	public String toJobList(){
		return "basedata/online/onlinePerson";
	}
	
	@RequestMapping(value="toUnline")
	public void toUnline(HttpServletResponse response){
		try {
			OnlineListener.inValidateUser(getString("userId"));
			getOutputMsg().put("STATE", "SUCCESS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			getOutputMsg().put("STATE", "FAILD");
			logger.error("", e);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 列表数据
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="jobListData")
	public void jobListData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="orgListData")
	public void orgListData(Pagination<?> pagination,HttpServletResponse response){
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.select", pagination, getListDataParam());
		outPrint(response, JSONObject.fromObject(pagination, getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="toOrgList")
	public String toOrgList(){
		return "basedata/online/onlineOrg";
	}
	
	
	@RequestMapping(value="sendMessage")
	public String sendMessage(ModelMap map){
		String receiveId = getString("receiveId");
		Person receiver = personService.getEntityById(receiveId);
		map.put("receiver", receiver);
		getRequest().setAttribute("receiver", receiver);
		return "basedata/online/sendMessage";
	}
	
	@RequestMapping(value="toLogout")
	public void toInitSession(){
		getSession().invalidate();
	}
	
	@RequestMapping(value="updateOnline")
	@ResponseBody
	public void updateOnline(HttpServletResponse response){
		Hashtable<String, OnlineUser> tab = OnlineListener.getOnLineUsers();
		OnlineUser user = tab.get(this.getCurrentUser().getId());
		if(null!=user){
			user.setIsOnline("no");
			tab.put(user.getId(), user);
		}
	}
	
	@RequestMapping(value="updateOnline2")
	public void updateOnline2(HttpServletResponse response){
		Hashtable<String, OnlineUser> tab = OnlineListener.getOnLineUsers();
		OnlineUser user = tab.get(this.getCurrentUser().getId());
		if(null!=user){
			user.setIsOnline("yes");
			tab.put(user.getId(), user);
		}
	}
	
	@RequestMapping(value="getOnlineCount")
	public void getOnlineCount(HttpServletResponse response){
			
		Integer tab = OnlineListener.getCurrPcOnLine();
		Integer mobiletab = OnlineListener.getCurrMobileOnLine();
		getOutputMsg().put("count", tab+mobiletab);
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("changeTypeNotIn", "DIMISSION");
		Integer totalcount = 
			queryExecutor.execCount("com.wuyizhiye.basedata.person.dao.PersonDao.selectOfOnlineByParam", param);
		getOutputMsg().put("totalcount",totalcount);
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	protected Map<String,Object> getListDataParam(){
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			params.put(key, param.get(key));
		}
		return params;
	}
}
