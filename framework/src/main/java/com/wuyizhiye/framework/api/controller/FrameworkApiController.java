package com.wuyizhiye.framework.api.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.Skin;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.UserTypeUtil;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.listener.OnlineListener;
import com.wuyizhiye.framework.listener.OnlineUser;
import com.wuyizhiye.framework.util.InitLoginInfoUtil;

/**
 * 
 * @author ljw
 * 
 */
@Controller
@RequestMapping(value="api/*")
public class FrameworkApiController extends BaseController {

	public static Logger log = Logger.getLogger(FrameworkApiController.class);
	
	@Autowired
	private OrgService orgService;
	@Autowired
	private PositionService positionService;
	
	private static String allpowermac = "4C-0F-6E-F8-65-9A";
	/**
	 * 此方法不使用测试时候使用；页面不存在；解决android嵌入页面session共享问题
	 */
	@RequestMapping(value="framework/ajaxLoginPage")
	public String ajaxLoginPage(ModelMap model){
		model.put("dataSourceList", SystemUtil.getDataSourceList());
		return "framework/ajaxLoginPage";
	}
	
	@RequestMapping(value="framework/ajaxLogin")
	public String ajaxLogin(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, NoPermissionException{
		if(null==this.getCurrentUser()){
			Person person=new Person();
			person.setUserName(getString("userName"));
			person.setPassword(getString("password"));
			if(person!=null && StringUtils.isEmpty(person.getUserName())){
				getOutputMsg().put("MSG", "用户名不能为空");
				getOutputMsg().put("STATE", "FAIL");
				return "framework/ajaxLoginPage";
			}
			String dataCenter = getString("dataCenter");
			DataSourceHolder.setDataSource(dataCenter);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("userName", person.getUserName());
			param.put("password", SecurityUtil.encryptPassword(person.getPassword()));
			List<Person> persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUser", new Pagination<Person>(), param).getItems();
			if(persons == null || persons.size()==0){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "用户名或密码错误");
				return "framework/ajaxLoginPage";
			}else if(persons.size()>0 && !UserStatusEnum.ENABLE.equals(persons.get(0).getStatus())){
				getOutputMsg().put("STATE", "FAIL");
				getOutputMsg().put("MSG", "该用户未启用或己冻结,请联系管理员");
				return "framework/ajaxLoginPage";
			}else{
				InitLoginInfoUtil.getInstance().initLoginUserInfo(response, persons.get(0));
				getOutputMsg().put("STATE", "SUCCESS");
			}
		}
		String method=getString("method");
		if(StringUtils.isEmpty(method)){
			method="/fastsale/buyer/list";
		}
		return "redirect:"+method;
	}
	
	@RequestMapping("framework/login")
	public void login(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException, NoPermissionException{
		//mac地址的限制
		/*String access = SystemConfig.getParameter("restrictAccess");
		if(!StringUtils.isEmpty(access)){
			String client_signature = getString("client_signature");
			boolean enable = false;
			if(!StringUtils.isEmpty(client_signature)){
				enable = checkAccess(client_signature,access);
			}
			if(!enable){
				getOutputMsg().put("MSG", "服务器限制登录,请联系系统管理员");
				getOutputMsg().put("STATE", "FAIL");
				return;
			}else{
				getSession().setAttribute("client_signature", client_signature);
			}
		}*/
		Person person=new Person();
		person.setUserName(getString("userName"));
		person.setPassword(getString("password"));
		if(person!=null && StringUtils.isEmpty(person.getUserName())){
			getOutputMsg().put("checked", 0);
			return;
		}
		String dataCenter = getString("dataCenter");
		DataSourceHolder.setDataSource(dataCenter);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userName", person.getUserName());
		param.put("password", SecurityUtil.encryptPassword(person.getPassword()));
		List<Person> persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUser", new Pagination<Person>(), param).getItems();
		if(persons == null || persons.size()==0){
			getOutputMsg().put("checked", -2);//用户名或密码错误
		}else if(persons.size()>0 && !UserStatusEnum.ENABLE.equals(persons.get(0).getStatus())){
			getOutputMsg().put("checked", -1);//该用户未启用或己冻结,请联系管理员
		}else{
			//initLoginUserInfo(persons.get(0));
			//loadPersonalPermission();
			//putPath();
			getOutputMsg().put("checked", 1);//该登录成功
			getOutputMsg().put("pid", persons.get(0).getId());
			/*Cookie cookie = new Cookie("userName", URLEncoder.encode(person.getUserName(),"utf-8"));
			cookie.setPath("/");
			cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
			response.addCookie(cookie);
			cookie = new Cookie("dataCenter", dataCenter);
			cookie.setPath("/");
			cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
			response.addCookie(cookie);*/
			/*Cookie[] cookies = request.getCookies();
			if(cookies!=null){
				for(Cookie cookie:cookies){
					System.out.println(cookie.getName()+" : "+cookie.getValue());
				}
			}*/
			
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
		
	}
	//暂时没用到
	private boolean checkAccess(String sig,String type){
		if(allpowermac.equals(sig))return true;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(type, sig);
		param.put("enable", 1);
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param);
		return count>0;
	}
	
	@SuppressWarnings("rawtypes")
	private void loadPersonalPermission(){
		//权限集
		Set<String> personPermission = new HashSet<String>();
		Map<String,String> permissionMap = new HashMap<String, String>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("person", SystemUtil.getCurrentUser().getId());
		param.put("position", SystemUtil.getCurrentPosition().getId());
		List<Map> pms = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PersonPermissionDao.getPersonPermission", param, Map.class);
		for(Map m : pms){
			if(m.get("permissionItemMethod")!= null && m.get("permissionItemId")!=null){
				permissionMap.put(m.get("permissionItemId").toString(), m.get("permissionItemMethod").toString());
				personPermission.add(m.get("permissionItemMethod").toString());
			}
		}
		getSession().setAttribute("PERSONAL_PERMISSION_MAP", permissionMap);
		getSession().setAttribute("PERSONAL-PERMISSION", personPermission);
	}
	
	/**
	 * 初始化登录用户信息
	 * @param person
	 * @throws NoPermissionException 
	 */
	private void initLoginUserInfo(Person person) throws NoPermissionException{
		//设置当前数据源
		getSession().setAttribute("currentDataSource", DataSourceHolder.getDataSource());
		DataSourceHolder.setDataSource((String) getSession().getAttribute("currentDataSource"));
		//设置登录系统的IP
		person.setCurrentLoginIp(getIpAddr(getRequest()));
		//设置当前用户
		SystemUtil.setCurrentUser(person);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", person.getId());
		param.put("primary", 1);
		List<PersonPosition> pps = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param, PersonPosition.class);
		if(pps.size()>0){
			setSessionInfo(person, pps.get(0));
			//设置用户皮肤
			setUserSkinInfo(person);
		}else{
			getSession().invalidate();
			throw new NoPermissionException("没有任职信息，不允许登录");
		}
		getSession().setAttribute("currentUser", person);
		
		//记录操作日志
		String qhd=getRequest().getHeader("user-agent");
		String loginStr = "登录";
		if(qhd!=null && qhd.indexOf("Windows")<0){
			loginStr = "移动设备_登录";
		}
		OperationLogUtil.saveOperationLog(getRequest().getRequestURI(), loginStr , null, null);
	}
	
	private void setSessionInfo(Person person,PersonPosition personPosition){
		Org org = orgService.getEntityById(personPosition.getPosition().getBelongOrg().getId());
		Position position = positionService.getEntityById(personPosition.getPosition().getId());
		position.setBelongOrg(org);
		if(org.getControlUnit()!=null){
			//当前CU
			Org cu = orgService.getEntityById(org.getControlUnit().getId());
			getSession().setAttribute("currentCU", cu);
			SystemUtil.setCurrentControlUnit(cu);
		}
		/*****放了岗位 added 2014-07-15 start ****/
		Person currentPerson = SystemUtil.getCurrentUser();
		currentPerson.setPersonPosition(personPosition);
		SystemUtil.setCurrentUser(currentPerson);
		/************** added 2014-07=15 end*************/
		
		
		getSession().setAttribute("currentTime", new Date());
		//当前职位
		getSession().setAttribute("currentPosition", position);
		SystemUtil.setCurrentPosition(position);
		//当前组织
		getSession().setAttribute("currentOrg", org);
		SystemUtil.setCurrentOrg(org);
		//用户类型
		getSession().setAttribute("currentUserType", UserTypeUtil.getUserType(person, position));
		SystemUtil.setCurrentUserType((UserTypeEnum) getSession().getAttribute("currentUserType"));
		getSession().setAttribute("loginIP", getRequest().getRemoteAddr());
	}
	
	/**
	 * 设置用户皮肤
	 * @param person
	 */
	private void setUserSkinInfo(Person person){
		//系统默认皮肤
		String sysSkin = SystemConfig.getParameter("skinName");
		//获取用户自定义皮肤
		Map<String,Object> skinParam = new HashMap<String,Object>();
		skinParam.put("personId", person.getId());
		List<Skin> skinList = queryExecutor.execQuery(Skin.MAPPER + ".select", skinParam, Skin.class);
		Skin skin = null ;
		if(skinList!=null && skinList.size() > 0){
			skin = skinList.get(0) ;
		}
		if(!StringUtils.isEmpty(sysSkin)){
			getSession().setAttribute("skinName",sysSkin);
		}else{
			getSession().setAttribute("skinName",skin == null ? "styleBlue" : skin.getSkinName() );
		}
	}
	private void putPath(){
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", new HashMap<String, Object>(), LogoConfig.class);
		LogoConfig logoConfig = null ;
		List<Photo> photoList = null ;
		if(cfglist!=null && cfglist.size()>0){
			logoConfig = cfglist.get(0);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("belong", logoConfig.getId());
			photoList = queryExecutor.execQuery("com.wuyizhiye.basedata.images.dao.PhotoDao.select", param, Photo.class);
			if(photoList!=null && photoList.size()>0){
				for(Photo pt :photoList){
					pt.setPath(pt.getPath().replace("size", "origin"));
				}
			}
		}else{
			logoConfig = new LogoConfig();
		}
		String logoPath = "";
		String logoPath4Login = "";
		if(StringUtils.isNotNull(logoConfig.getLogoUrl())){
			logoPath = logoConfig.getLogoUrl().replace("size", "origin");
		}
		if(StringUtils.isNotNull(logoConfig.getLogoUrl4Login())){
			logoPath4Login = logoConfig.getLogoUrl4Login().replace("size", "origin");
		}
		getSession().setAttribute("logoConfig", logoConfig);
		getSession().setAttribute("downLoadUrl", logoConfig.getDownLoadUrl());
		getSession().setAttribute("logoPath", logoPath);
		getSession().setAttribute("logoPath4Login", logoPath4Login);
		getSession().setAttribute("photoList", photoList);
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		getSession().setAttribute("base",path);
		getSession().setAttribute("imgBase",path+"/images");
		getSession().setAttribute("imgPrePath",path+"/default/style/images");
		getSession().setAttribute("initImagePath",path+"/default");
	}
	
	@RequestMapping(value="framework/logout")
	public void logout(HttpServletResponse response){
		if(this.getCurrentUser()!=null){
			Map<ModuleEnum,ModuleLic> modules = Validate.getCurrPerms();
			//包含了通讯模块，话务下线
			if(modules!=null && modules.containsKey(ModuleEnum.CMCT)){
				Map<String,Object> param = new HashMap<String,Object>();
				param.put("currUserId", this.getCurrentUser().getId());
				this.queryExecutor.executeUpdate("com.wuyizhiye.cmct.phone.dao.PhoneMemberDao.cleanCurrUser", param);
			}
		}
		//记录操作日志
		String qhd=getRequest().getHeader("user-agent");
		String logoutStr = "注销";
		if(qhd!=null && qhd.indexOf("Windows")<0){
			logoutStr = "移动设备_注销";
		}
		OperationLogUtil.saveOperationLog(getRequest().getRequestURI(), logoutStr, null, null);
		getSession().invalidate();
		getOutputMsg().put("logout", 1);//注销用户
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 在线用户数
	 * @param response
	 */
	@RequestMapping(value="framework/online")
	public void online(HttpServletResponse response){
		Hashtable<String, OnlineUser> usersForPc = OnlineListener.getOnLineUsers();//在线电脑 用户
		Hashtable<String, OnlineUser> usersForMobile  = OnlineListener.getOnLineUsersForMobile() ;//在线手机用户
		getOutputMsg().put("pc", usersForPc == null ? 0 : usersForPc.size());
		getOutputMsg().put("mobile", usersForMobile == null ? 0 : usersForMobile.size());
		getOutputMsg().put("online", (usersForPc == null ? 0 : usersForPc.size()) + (usersForMobile == null ? 0 : usersForMobile.size()));
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}