package com.wuyizhiye.basedata.logoconfig.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.spring.DataSourceHolder;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.attachment.service.AttachmentService;
import com.wuyizhiye.basedata.enums.LoginPageModelEnum;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.images.service.PhotoService;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.service.LogoConfigService;
import com.wuyizhiye.basedata.shortcut.enums.ShortcutTypeEnum;
import com.wuyizhiye.basedata.shortcut.model.OpenTypeEnum;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.framework.redis.DataSourceLevelConstants;
import com.wuyizhiye.framework.util.BeanUtils;

/**
 * @ClassName LogoConfigController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/logoConfig/*")
public class LogoConfigController extends BaseController {
	private static Logger logger=Logger.getLogger(LogoConfigController.class);
	@Autowired
	private LogoConfigService logoConfigService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private PhotoService photoService;

	@RequestMapping(value="toLogoConfig")
	public String toLogoConfig(ModelMap model){
		 Map<String,Object> param=new HashMap<String, Object>();
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", param, LogoConfig.class);
		LogoConfig logoConfig = null ;
		List<Photo> plist =new ArrayList<Photo>();
		
		if(cfglist!=null && cfglist.size()>0){
			logoConfig = cfglist.get(0);
			param.clear();
			param.put("belong", logoConfig.getId());
			plist = this.photoService.selectByCondition(param); 
			for(Photo p :plist){
				p.setPath(p.getPath().replace("size", "origin"));
			}
		}else{
			logoConfig = new LogoConfig();
		}
		
		model.put("data", logoConfig);
		model.put("plist", plist);
		model.put("modelList", LoginPageModelEnum.toList()); 
		return "interflow/logoConfig/logoConfig";
	}
	
	@RequestMapping(value="toFrameworkConfig")
	public String toFrameworkConfig(ModelMap model){
		 Map<String,Object> param=new HashMap<String, Object>();
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", param, LogoConfig.class);
		LogoConfig logoConfig = null ;
		List<Photo> plist =new ArrayList<Photo>();
		
		if(cfglist!=null && cfglist.size()>0){
			logoConfig = cfglist.get(0);
			param.clear();
			param.put("belong", logoConfig.getId());
			plist = this.photoService.selectByCondition(param); 
			for(Photo p :plist){
				p.setPath(p.getPath().replace("size", "origin"));
			}
		}else{
			logoConfig = new LogoConfig();
		}
		
		model.put("data", logoConfig);
		model.put("plist", plist);
		model.put("modelList", LoginPageModelEnum.toList());
		model.put("openTypeEnum", OpenTypeEnum.toList());
		
		List<ShortcutTypeEnum> shortcutTypeList=new ArrayList<ShortcutTypeEnum>();
		shortcutTypeList.add(ShortcutTypeEnum.INTERNAL);
		shortcutTypeList.add(ShortcutTypeEnum.EXTERNAL);
		model.put("shortcutTypeList", shortcutTypeList);
		 
		param.clear();
		param.put("isFrameworkShortcut", 1);
		List<Shortcut> shortcutlist=queryExecutor.execQuery("com.wuyizhiye.basedata.shortcut.dao.ShortcutDao.selectAll", param, Shortcut.class);//查询框架快捷方式
		model.put("shortcutlist", shortcutlist); 
		
		return "interflow/logoConfig/frameworkConfig";
	}
	
	/**
	 * 首页 获取 配置对象
	 * @param response
	 */
	@RequestMapping("getLogoConfig")
	@ResponseBody
	public void getLogoConfig(HttpServletResponse response){
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", null, LogoConfig.class);
		if(null != cfglist && cfglist.size()>0){
			getOutputMsg().put("config", cfglist.get(0));
		}
		outPrint(response,JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
 
	@RequestMapping(value="editLogoConfig")
	public String showAddLogoConfig(ModelMap model,@RequestParam(required=true,value="id")String id){
		
		model.put("flag", "EDIT");
		LogoConfig logoConfig = logoConfigService.getEntityById(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("belong", id);
		List<Photo> plist = this.photoService.selectByCondition(map);  
		model.put("data", logoConfig);
		model.put("plist", plist);
		return "interflow/logoConfig/addLogoConfig";
	}
	
	/**
	 * 新增公告,有分录 图片 附件
	 * @param response
	 */
	@RequestMapping(value="insertLogoConfig")
	public void insertLogoConfig(HttpServletResponse response){
		try {
			String id = getString("logoConfigId");
			LogoConfig logoConfig = null;
			if(!StringUtils.isEmpty(id)){
				logoConfig = logoConfigService.getEntityById(id);
			}else{
				logoConfig = new LogoConfig();
			}
			
			logoConfig = BeanUtils.fillentity(getParamMap(),logoConfig, LogoConfig.class);
			
			List<Photo> photoList = new ArrayList<Photo>();
			String imgstr = getString("logoConfigphotoids");
			if(!StringUtils.isEmpty(imgstr) && !"[]".equals(imgstr)){  
			    JSONArray arr=JSONArray.fromObject(imgstr);
				//String[] sary = imgstr.split(",");
				
				for(int i=0;i<arr.size();i++){				
					Photo p = new Photo();
					JSONObject jo=(JSONObject)arr.get(i);
					p.setId(jo.getString("id"));
					p.setDescription(jo.getString("description"));
					p.setIdx(i);
					photoList.add(p);
				}
			}
			
			List<Shortcut> shortcutList = new ArrayList<Shortcut>();
			String shortcutids = getString("shortcutids"); //设置快捷方式的顺序号
			if(!StringUtils.isEmpty(shortcutids)){ 
				String[] sary = shortcutids.split(",");
				
				for(int i=0;i<sary.length;i++){				
					Shortcut p = new Shortcut();
					p.setId(sary[i]);
					p.setIdx(i);
					shortcutList.add(p);
				}
			}
			 
			String logoPath = "";
			String logoPath4Login = "";
			if(StringUtils.isNotNull(logoConfig.getLogoUrl())){
				logoPath = logoConfig.getLogoUrl().replace("size", "origin");
			}
			if(StringUtils.isNotNull(logoConfig.getLogoUrl4Login())){
				logoPath4Login = logoConfig.getLogoUrl4Login().replace("size", "origin");
			}
			getSession().setAttribute("logoPath", logoPath);
			getSession().setAttribute("logoPath4Login", logoPath4Login);
			logoConfigService.addLogoConfig(logoConfig, photoList,shortcutList);
			getOutputMsg().put("STATE", "SUCCESS");
			
			String logoKey = 
				DataSourceLevelConstants.HEADER.LOGO+SystemUtil.getCustomerNumber()+DataSourceHolder.getDataSource();		
			this.getRedisClient().expire(logoKey, 0);
			
		} catch (Exception e) {
			 
			getOutputMsg().put("STATE", "FAILD");
			logger.error("", e);
		}
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	
	@RequestMapping(value="deteleById")
	public void deteleById(@RequestParam(required=true,value="id")String id,HttpServletResponse response){			
		logoConfigService.deleteLogoConfig(id);	
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "删除成功");
		response.setContentType("text/html");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
	@RequestMapping(value="getLogoConfigEntity")
	@ResponseBody
	public LogoConfig getLogoConfigEntity(){
		String id = getString("id");
		LogoConfig logoConfig = logoConfigService.getEntityById(id);
		return logoConfig;
	}
	
	 
}
