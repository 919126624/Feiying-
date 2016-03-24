package com.wuyizhiye.basedata.interflow.online;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.listener.LocalSessionContext;
import com.wuyizhiye.framework.listener.LoginTypeEnum;
import com.wuyizhiye.framework.listener.OnlineListener;
import com.wuyizhiye.framework.listener.OnlineUser;
import com.wuyizhiye.framework.redis.LoginInfoUtil;

/**
 * @ClassName OnlinePersonRedisController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/onlineredis/*")
public class OnlinePersonRedisController extends BaseController {

	@Autowired
	private QueryExecutor queryExecutor;
	
	@Autowired
	private PersonService personService;
	
	@RequestMapping(value="toJobList")
	public String toJobList(){
		return "basedata/online/onlinePerson";
	}
	
	@RequestMapping(value="toUnline")
	public void toUnline(HttpServletResponse response){
		String userId = getString("userId");
		List<OnlineUser> onPclist =
			this.getRedisClient().getListData(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.PC+userId, OnlineUser.class);	
		List<OnlineUser> onMobilelist =
			this.getRedisClient().getListData(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.MOBILE+userId, OnlineUser.class);
		for(OnlineUser user : onPclist){
			String rediskey = user.getSessionId();
			this.getRedisClient().expireByte(rediskey, 0);
			this.getRedisClient().expire(SystemUtil.getCustomerOnlySign()+"|"+rediskey.substring(rediskey.lastIndexOf("|")+1), 0);
		}
		for(OnlineUser user : onMobilelist){
			String rediskey = user.getSessionId();
			this.getRedisClient().expireByte(rediskey, 0);
			this.getRedisClient().expire(SystemUtil.getCustomerOnlySign()+"|"+rediskey.substring(rediskey.lastIndexOf("|")+1), 0);
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
		this.getRedisClient().expireByte(SystemUtil.getCurrentRedisKey(), 0);
		this.getRedisClient().expire(SystemUtil.getCustomerOnlySign()+"|"+SystemUtil.getCurrentRedisKey().substring(SystemUtil.getCurrentRedisKey().lastIndexOf("|")+1), 0);
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
		Set<String> onlinelist =
			this.getRedisClient().getListKey(SystemUtil.getCustomerOnlySign()+"*");
		getOutputMsg().put("count",onlinelist.size());
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
	
	@RequestMapping(value="getOnlinePerson")
	public void getOnlinePerson(HttpServletResponse response){
		List<OnlineUser> onPclist =
			this.getRedisClient().getListData(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.PC+"*", OnlineUser.class);
		
		List<OnlineUser> onMobilelist =
			this.getRedisClient().getListData(SystemUtil.getCustomerOnlySign()+"|"+LoginTypeEnum.MOBILE+"*", OnlineUser.class);
		
		String cuId = SystemUtil.getCurrentControlUnit().getId();//cuid
		Hashtable<String, OnlineUser> usersForPc = new Hashtable<String, OnlineUser>();//在线电脑 用户
		for(OnlineUser onlineUser : onPclist){
			usersForPc.put(onlineUser.getId(), onlineUser);
		}
		for(String gas:usersForPc.keySet()){//把不是该cu下的在线人员删除
			OnlineUser user = usersForPc.get(gas);
			if(user.getControlUnit()!=null && !user.getControlUnit().getId().equals(cuId)){
				usersForPc.remove(gas);
			}
		}
		Hashtable<String, OnlineUser> searchTab_pc = new Hashtable<String, OnlineUser>();
		Hashtable<String, OnlineUser> searchTab_mobile = new Hashtable<String, OnlineUser>();
		Hashtable<String, Map> dataMap = new Hashtable<String, Map>();
		String key = getString("key"); 
		
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		param.put("key_workbench", key);
		param.put("changeTypeNotIn", "DIMISSION");
		param.put("cuId", cuId);
		Pagination<Person> pagination = new Pagination<Person>();
		pagination.setPageSize(20);
		if(StringUtils.isNotNull(getString("currPage"))){
			pagination.setCurrentPage(Integer.parseInt(getString("currPage"))+1);
		}
		pagination = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.selectOfOnline", pagination, param);//所有用户
		List<Person> users = pagination.getItems();
		HashMap<String,Object> pageMap= new HashMap<String, Object>();
		pageMap.put("currPage", pagination.getCurrentPage());
		pageMap.put("pageCount", pagination.getPageCount());
		dataMap.put("page", pageMap);
		Hashtable<String, OnlineUser> usersForMobile  = new Hashtable<String, OnlineUser>() ;//在线手机用户
		for(OnlineUser onlineUser : onMobilelist){
			usersForPc.put(onlineUser.getId(), onlineUser);
		}
		for(String gas:usersForMobile.keySet()){//把不是该cu下的在线人员删除
			OnlineUser user = usersForMobile.get(gas);
			if(user.getControlUnit()!=null && !user.getControlUnit().getId().equals(cuId)){
				usersForMobile.remove(gas);
			}
		}
		
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
}
