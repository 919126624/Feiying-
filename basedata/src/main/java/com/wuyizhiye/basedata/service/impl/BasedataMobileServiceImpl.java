package com.wuyizhiye.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.exceptions.BusinessException;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.access.AccessConstant;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.permission.enums.MenuTypeEnum;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.util.SecurityUtil;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.Skin;
import com.wuyizhiye.basedata.service.BasedataMobileService;
import com.wuyizhiye.basedata.util.BaseConfigUtil;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.UserTypeUtil;

/**
 * @ClassName BasedataMobileServiceImpl
 * @Description 基础模块手机接口实现类
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedataMobileService")
@Transactional
public class BasedataMobileServiceImpl extends BaseServiceImpl implements BasedataMobileService {

	@Autowired
	private OrgService orgService;
	
	@Autowired
	private PositionService positionService;
	
	@Override
	protected BaseDao getDao() {
		return null;
	}
	
	/**
	 * 获取request
	 * @return
	 */
	private HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		
	}

	/**
	 * 获取session
	 * @return
	 */
	private HttpSession getSession() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
	}

	@Override
	public Map<String, Object> mobileLogin(String userName, String password,
			String dataCenter,String signature) throws BusinessException, NoPermissionException {
		
		Map<String,Object> res = new HashMap<String,Object>();
		Map<String,Object> qparam = new HashMap<String,Object>();
		
		//mac限制 配置和数据库设置同时判断
		String access = SystemConfig.getParameter("restrictAccess");
		if(StringUtils.isEmpty(access)){
			access = BaseConfigUtil.getCurrControlMode();
		}
		if(AccessConstant.MAC.equals(access)){
			if(StringUtils.isEmpty(signature)){
				res.put("MSG", "未获取到设备唯一标识");
				res.put("FLAG", "-3");
				return res; 
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("mac", signature);
			param.put("enable", 1);
			int count = queryExecutor.execCount("com.wuyizhiye.basedata.access.dao.MacAddressDao.select", param);
			if(count == 0){
				res.put("MSG", "该设备未得到终端许可");
				res.put("FLAG", "-4");
				return res; 
			}
		}
		
		List<Person> persons =  null;
		qparam.clear();
		qparam.put("userName", userName);
		qparam.put("password", SecurityUtil.encryptPassword(password));
		persons = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.loginUser", new Pagination<Person>(), qparam).getItems();
		if(persons == null || persons.size()==0){
			res.put("MSG", "用户名或密码错误");
			res.put("FLAG", "0");
			return res; 
		}
		if(!UserStatusEnum.ENABLE.equals(persons.get(0).getStatus())){
			res.put("MSG", "该用户未启用或己冻结,请联系管理员");
			res.put("FLAG", "-1");
			return res ;
		}
		
		DataSourceHolder.setDataSource(dataCenter);
		
		Person person = persons.get(0) ;
		
		//查找权限集
		Map<String,Boolean> reqPermMap = new HashMap<String,Boolean>(); 
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", person.getId());
		param.put("primary", 1);
		List<PersonPosition> pps = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param, PersonPosition.class);
		if(pps.size()>0){
			Position position = positionService.getEntityById(pps.get(0).getPosition().getId());
			Set<String> personPermission = new HashSet<String>();
			Map<String,String> permissionMap = new HashMap<String, String>();
			Map<String, Object> pmsParam = new HashMap<String, Object>();
			pmsParam.put("person", person.getId());
			pmsParam.put("position", position.getId());
			List<Map> pms = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.PersonPermissionDao.getPersonPermission", pmsParam, Map.class);
			for(Map m : pms){
				if(m.get("permissionItemMethod")!= null && m.get("permissionItemId")!=null){
					permissionMap.put(m.get("permissionItemId").toString(), m.get("permissionItemMethod").toString());
					personPermission.add(m.get("permissionItemMethod").toString());
				}
			}
			reqPermMap.put("newhouse", personPermission!=null && personPermission.contains(permissionMap.get("3f656527-d930-43af-bdf4-e3cea459b191")));
			reqPermMap.put("oldhouse", personPermission!=null && personPermission.contains(permissionMap.get("6a6ac841-9fdb-4c80-a83a-667c9563d6a0")));
			reqPermMap.put("appcall", personPermission!=null && personPermission.contains(permissionMap.get("81bc9841-8db0-4fe7-b392-69f620bf918d")));
		}else{
			//res.put("MSG", "登录成功");
			//res.put("FLAG", "1");
			res.put("MSG", "没有任职信息，不允许登录");
			res.put("FLAG", "-2");
			return res ;
		}
		res.put("PERMISSION", reqPermMap);
		
		/*
		initLoginUserInfo(persons.get(0));
		loadPersonalPermission();
		putPath();
		*/
		
		JSONObject pjson = new JSONObject();
		
		//查找登录人
		Map<String,Object> personParam = new HashMap<String,Object>();
		personParam.put("id", person.getId());
		Person person2 = queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrg", personParam, Person.class);
		String xsdbNumber = ParamUtils.getParamValueByNumber("KXXSDB");//销售代表
		String zygwNumber = ParamUtils.getParamValueByNumber("ZYGW");//置业顾问
		//销售代表和置业顾问按人查询，其他岗位则按组织查询
		String jobNumber = person2.getPostion().getJob().getNumber();
		if(StringUtils.isEmpty(jobNumber)){
			res.put("MSG", "未找到系统人员岗位");
			res.put("FLAG", "-2");
			return res ;
		}
		boolean selectByPerson = jobNumber.equals(xsdbNumber) || jobNumber.equals(zygwNumber) ;
		pjson.put("id", person.getId());
		pjson.put("name", person.getName());
		pjson.put("userName", person.getUserName());
		pjson.put("number", person.getNumber());
		pjson.put("password", person.getPassword());
		pjson.put("photo", !StringUtils.isEmpty(person.getPhoto()) ? (getPath() + "/images/" + person.getPhoto()) : "");
		pjson.put("orgId", person.getOrg()!=null ? person.getOrg().getId() : "");
		pjson.put("orgName", person.getOrg()!=null ? person.getOrg().getName() : "");
		pjson.put("orgLongNumber", person.getOrg()!=null ? person.getOrg().getLongNumber() : "");
		
		boolean result=false;//是否包含盘客业务或者快销业务
		/*
		if(person.getOrg()!=null){
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("id", person.getOrg().getId());
			Org org=queryExecutor.execOneEntity("com.wuyizhiye.basedata.org.dao.OrgDao.getById", param, Org.class);
			String type=org.getBusinessTypes();//组织业务类型
			if(!StringUtils.isEmpty(type)){
				String ids="(";
				for(String str:type.split(";")){
					ids+="'"+str+"',";
				}
				if(!"(".equals(ids)){
					ids=ids.substring(0, ids.length()-1)+")";
				}else{
					ids="('')";
				}
				param.clear();
				param.put("ids", ids);
				List<BusinessType> blist=queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.BusinessTypeDao.select", param, BusinessType.class);
				for(BusinessType bt:blist){
					if(bt.getNumber().equals("F02")){//盘客业务  三级市场类型
						result =true;
					}
					if(bt.getNumber().equals("F08")){//快销业务  对应  快销
						result =true;
					}
				}
			}
		}
		*/
		pjson.put("business", result);
		pjson.put("selectByPerson", selectByPerson);
		res.put("PERSON", pjson);
		
		//记录操作日志
		String qhd = getRequest().getHeader("user-agent");
		String loginStr = "登录";
		if(qhd.indexOf("Android")>-1){
			loginStr = "Android登录";
		}else if(qhd.indexOf("iPhone")>-1 || qhd.indexOf("Apple") > -1){
			loginStr = "iPhone登录";
		}else if(qhd.indexOf("iPad")>-1){
			loginStr = "iPad登录";
		}
		OperationLogUtil.saveOperationLog(getRequest().getRequestURI(), loginStr , null, null);
		
		
		//res.put("PERSON", persons.get(0));
		res.put("MSG", "登录成功");
		res.put("FLAG", "1");
		return res;
	}
	
	private String getPath(){
		int port = getRequest().getServerPort();
		String scheme = getRequest().getScheme();
		String path = getRequest().getScheme()+"://"+getRequest().getServerName() + ((("http".equals(scheme) && port == 80) ||("https".equals(scheme)  && port == 443)) ? "" : ":" + port) + getRequest().getContextPath();
		return path ;
	}
	
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
	 * 得到IP地址
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@Override
	public List<JSONObject> mobileMeum(String personId)
			throws BusinessException {
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		if(!StringUtils.isEmpty(personId)){
			//查找登录人
			Map<String,Object> qparam = new HashMap<String,Object>();
			qparam.put("id", personId);
			Person person = queryExecutor.execOneEntity("com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrg", qparam, Person.class);
			if(person == null){
				throw new BusinessException("未找到系统人员");
			}
			String jobNumber = person.getPostion().getJob().getNumber();
			if(StringUtils.isEmpty(jobNumber)){
				throw new BusinessException("未找到系统人员岗位");
			}
			Map<String,Object> ppParam = new HashMap<String, Object>();
			ppParam.put("person", person.getId());
			ppParam.put("primary", 1);
			List<PersonPosition> pps = queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", ppParam, PersonPosition.class);
			Position position = null ;
			if(pps!=null && pps.size() > 0){
				position =  positionService.getEntityById(pps.get(0).getPosition().getId());
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("type", MenuTypeEnum.MOBILE.getValue());
			param.put("notEnableFlag", "Y");
			param.put("checkPermission", true);//是否检查权限
			param.put("person", person.getId());
			param.put("position", position != null ? position.getId() : person.getPostion().getId());
			param.put("leaf", "1");//查询叶子节点
			//param.put("includeLink", true);	
			List<Menu> menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
			if(menus !=null && menus.size() > 0){
				for(Menu m : menus){
					JSONObject mjson = new JSONObject();
					mjson.put("id", m.getId());
					mjson.put("number", m.getNumber());
					mjson.put("name", m.getName());
					mjson.put("link", StringUtils.isEmpty(m.getLink()) ? "" : getPath() +"/"+ m.getLink());
					mjson.put("miniIcon", StringUtils.isEmpty(m.getMiniIcon()) ? "" : getPath() +"/images/"+ m.getMiniIcon());
					mjson.put("largeIcon", StringUtils.isEmpty(m.getLargeIcon()) ? "" : getPath() +"/images/"+ m.getLargeIcon());
					jsonList.add(mjson);
				}
			}
		}
		return jsonList;
	}
	
	@Override
	public Map<String, Object> mobileHeadInfo(String personId)
			throws BusinessException {
		Map<String,Object> res = new HashMap<String,Object>();
		
		//获取call客级别
		Map<String,Object> qparam = new HashMap<String,Object>();
		qparam.put("personId", personId);
		List<Map> dataList = queryExecutor.execQuery("com.wuyizhiye.cmct.phone.dao.PhoneCallLevelDao.selectSimple", qparam, Map.class);
		Integer THE_SUCC=0;
		if(null!=dataList && dataList.size()>0){
			THE_SUCC = dataList.get(0).get("callSuccCumulative")!=null ? Integer.valueOf(dataList.get(0).get("callSuccCumulative").toString()) : 0;
		}
		String LV_NAME = "状元";
		String LV = "8";
		if(THE_SUCC <= 500){
			LV_NAME = "小混混";
			LV = "1";
		}else if(THE_SUCC <= 1200){
			LV_NAME = "学渣";
			LV = "2";
		}else if(THE_SUCC <= 2000){
			LV_NAME = "学徒";
			LV = "3";
		}else if(THE_SUCC<= 3000){
			LV_NAME = "学霸";
			LV = "4";
		}else if(THE_SUCC <= 5000){
			LV_NAME = "助教";
			LV = "5";
		}else if(THE_SUCC <= 8000){
			LV_NAME = "讲师";
			LV = "6";
		}else if(THE_SUCC <= 12000){
			LV_NAME = "导师";
			LV = "7";
		}else if(THE_SUCC<= 20000){
			LV_NAME = "叫兽";
			LV = "8";
		}else if(THE_SUCC <= 30000){
			LV_NAME = "院长";
			LV = "9";
		}else if(THE_SUCC <= 50000){
			LV_NAME = "砖家";
			LV = "10";
		}else {
			LV_NAME = "唐僧";
			LV = "11";
		}
		//获取签名
		qparam.clear();
		qparam.put("userId", personId);
		List<Map> autograph = queryExecutor.execQuery("com.wuyizhiye.blog.dao.AutographDao.selectMap", qparam, Map.class);
		String content = "" ;
		if(autograph !=null && autograph.size() > 0 ){
			content = autograph.get(0).get("content") == null ? "" : autograph.get(0).get("content").toString();
		}
		res.put("callLevle", LV);
		res.put("callLevleName", LV_NAME);
		res.put("autograph", content);
		return res;
	}

}
