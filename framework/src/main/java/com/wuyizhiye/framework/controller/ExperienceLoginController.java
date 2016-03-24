package com.wuyizhiye.framework.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.org.service.PositionService;
import com.wuyizhiye.basedata.person.enums.UserTypeEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.Skin;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.util.OperationLogUtil;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.UserTypeUtil;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName ExperienceLoginController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="/exper/*")
public class ExperienceLoginController extends BaseController {
	
	//准备3个体验的角色
	private static final String ADMIN_ID = "000000000000000000000000000000A";
	private static final String SELLER_ID = "426f5371-8ea6-4f18-a8fd-d898d2102a0a"; 
	private static final String BOSS_ID= "9daf1b3d-dba0-4eef-b5d8-c8b5e24fa361";
	//新房
	public static class NEWHOUSE{
		public static final String ADMIN_ID = "000000000000000000000000000000A";
		public static final String SELLER_ID = "426f5371-8ea6-4f18-a8fd-d898d2102a0a"; 
		public static final String BOSS_ID= "9daf1b3d-dba0-4eef-b5d8-c8b5e24fa361";
	}
	//二手房
	public static class SECONDHOUSE{
		public static final String ADMIN_ID = "000000000000000000000000000000A";
		public static final String SELLER_ID = "f2253676-fc40-4bcf-9b56-fab6cc5beaf8"; 
		public static final String BOSS_ID= "a66f6d5a-c4c2-4de3-abd1-fd0bce4a333f";
		public static final String SECRETARY_ID= "3aa6d876-a777-449b-bb67-6fc658eb3285";//秘书ID
	}
	
	
	//特殊字段 发送短信的人的ID
	private static final String SENDER_ID="000000000000000000000000000000A";
	@Autowired
	PersonService personService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private PositionService positionService;
	
	@RequestMapping(value="view")
	public String view (){
		this.putCommonPath();
		String type= getString("type");
		getSession().setAttribute("type", type);
		return "framework/view";
	}
	/**
	 * 登陆
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="ajaxLogin")
	public void ajaxLogin(ModelMap model,HttpServletResponse response) throws Exception{
		
		//手机验证码是否正确
		String myPhoneCode = getString("myPhoneCode");
		String phoneCode= (String) getSession().getAttribute("phoneCode");
		
		if(!phoneCode.equals(myPhoneCode)){
			model.put("STATE", "FAIL");
			model.put("MSG", "验证码错误");
			outPrint(response, JSONObject.fromObject(model));
			return;
		}
		//
		DataSourceHolder.setDataSource("dataSource_dingjian");
		//想体验的角色  依据type
		String type= (String) getSession().getAttribute("type");
		String role = getString("role");
		String phonenumber = getString("phonenumber");
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("id", StringUtils.getUUID());
		param.put("phone", phonenumber);
		param.put("loginTime", new Date());
		param.put("loginRole", role);
		this.queryExecutor.executeInsert("com.wuyizhiye.basedata.orepationlog.dao.OperationLogDao.addIntentCompany", param);
		
		DataSourceHolder.setDataSource("dataSource_YSHJ");
		
		if(!"first".equals(type) && !"second".equals(type)){
			type="first";
		}
		if("first".equals(type)){
			if (role.equals("BOSS_ID")) {
				role=NEWHOUSE.BOSS_ID;
			}else if(role.equals("SELLER_ID")){
				role=NEWHOUSE.SELLER_ID;
			}else if(role.equals("ADMIN_ID")){
				role=NEWHOUSE.ADMIN_ID;
			}
		}else if("second".equals(type)){
			if (role.equals("BOSS_ID")) {
				role=SECONDHOUSE.BOSS_ID;
			}else if(role.equals("SELLER_ID")){
				role=SECONDHOUSE.SELLER_ID;
			}else if(role.equals("ADMIN_ID")){
				role=SECONDHOUSE.ADMIN_ID;
			}else if(role.equals("SECRETARY_ID")){
				role=SECONDHOUSE.SECRETARY_ID;
			}
		}
		Person person = personService.getEntityById(role);
		model.put("STATE", null==person?"FAIL":"SUCCESS");
		if(null!=person){
			initLoginUserInfo(person);
			loadPersonalPermission();
		}
		
		outPrint(response, JSONObject.fromObject(model));
	}
	
	/**
	 * 验证验证码是否输入正确
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="ajaxCheckCode")
	public void ajaxCheckCode(ModelMap model,HttpServletResponse response)throws Exception{
		String code = (String) getSession().getAttribute("checkCode");
		String uCode =getString("myCode") ;
		
		if(code.equalsIgnoreCase((uCode))){
			getOutputMsg().put("MSG", "验证码正确");
			getOutputMsg().put("STATE", "SUCCESS");
		}else{
			getOutputMsg().put("MSG", "验证码有误");
			getOutputMsg().put("STATE", "FAIL");
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	/**
	 * 点击替换验证码
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="ajaxChangeCode")
	public void ajaxChangeCode(ModelMap model,HttpServletResponse response)throws Exception{
		//4位随机码
		String checkCode = getCheckCode();
		getSession().setAttribute("checkCode", checkCode);
		//生成图片
		getCheckCodeImg(100,100,checkCode);
		
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}
	
	/**
	 * 生成验证码图片
	 * @param width
	 * @param height
	 * @param checkCode
	 * @return
	 */
	private BufferedImage image;
	private void getCheckCodeImg(int width, int height,String checkCode)throws Exception {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics(); 
		Font font = new Font("微软雅黑",Font.BOLD,30);
		g.fillRect(0, 0, width, height);
		g.setFont(font);
		g.setColor(Color.red);
		
		char[] chars= checkCode.toCharArray();
		for (int i = 0; i < 4; i++) {
			g.drawString(String.valueOf(chars[i]), 20*i+10, 60);
		}
		g.dispose();
	}

	/**
	 * 初始化session信息
	 * @param person
	 * @param personPosition
	 */
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
		
		getSession().setAttribute("logouttourl", "exper/view"+((null==getSession().getAttribute("type"))?"":("?type="+getSession().getAttribute("type"))));
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
	/**
	 * 给当前用户权限
	 */
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
	 * 生成4位随机验证码
	 */
	private static final Random random= new Random(); 
	private static String[] numberStr= {"1","2","3","4","5","6","7","8","9","0"};
	private static String[] wordStr = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	private static String[] wordUpStr={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	public static String getCheckCode(){
		StringBuffer checkCode =new StringBuffer();
		int count=4;
		while(count>0){
			count--;
			switch (random.nextInt(3)) {
			case 0:checkCode.append(numberStr[random.nextInt(10)]);break;
			case 1:checkCode.append(wordStr[random.nextInt(26)]);  break;
			case 2:checkCode.append(wordUpStr[random.nextInt(26)]);break;
			}
		}
		return checkCode.toString();
	}
	/**
	 * 生成6位随机数字
	 * @return
	 */
	private static String getRandomSixNum(){
		String number="";
		for (int i = 0; i < 6; i++) {
			number+=random.nextInt(10);
		}
		return number;
	}
	/**
	 * 测试
	 */
	public static void main(String[] args) {
		String code =getCheckCode();
		System.out.println(code);
		
		String sixNum= getRandomSixNum();
		System.out.println(sixNum);
	}
}
