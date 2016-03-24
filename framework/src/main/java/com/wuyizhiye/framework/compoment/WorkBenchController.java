package com.wuyizhiye.framework.compoment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.module.ModuleUtils;
import com.wuyizhiye.basedata.org.enums.WorkBenchTypeEnum;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.permission.enums.MenuTypeEnum;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;
import com.wuyizhiye.basedata.util.ParamUtils;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.basedata.util.Validate;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName WorkBenchController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
public class WorkBenchController extends BaseController {
	
	@RequestMapping(value="workbench")
	public String workBench(ModelMap model){
		Person person =SystemUtil.getCurrentUser();
		Position position = SystemUtil.getCurrentPosition();
		Org org = SystemUtil.getCurrentOrg();
		model.put("person", person);
		model.put("position", position);
		model.put("org", org);	
		model.put("dataSourceList", SystemUtil.getDataSourceList());
		String workbench = getString("workbench");
		WorkBenchTypeEnum workBenchType = WorkBenchTypeEnum.SIMPLE;
		if(StringUtils.isEmpty(workbench)){
			workbench = "simple";
			if(position!=null){
				Job job = position.getJob();
				//Map<String, Object> param = new HashMap<String, Object>();
				//param.put("id", job.getId());
				//job = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", param , Job.class).get(0);
				if(job.getWorkBenchType()!=null){
					workbench = job.getWorkBenchType().getValue();
					workBenchType = job.getWorkBenchType();
				}		
				
			}
		}
		if(this.isMobileDevice()){
			if(position!=null){
				model.put("menus", getMobileMenu(person, position,workBenchType));
			}
			return "framework/mobileIndex";
		}
		if(position!=null){
			model.put("menus", getMenu(person, position,workBenchType));
		}
		/*List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", new HashMap<String, Object>(), LogoConfig.class);
		LogoConfig logoConfig = null ;
		if(cfglist!=null && cfglist.size()>0){
			logoConfig = cfglist.get(0);
		}else{
			logoConfig = new LogoConfig();
		}
		String logoPath = "";
		if(StringUtils.isNotNull(logoConfig.getLogoUrl())){
			logoPath = logoConfig.getLogoUrl().replace("size", "origin");
		}
		model.put("logoPath", logoPath);*/
		model.put("workbench", workbench);
		model.put("isEnableHR", true);
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("person", person.getId());
		model.put("personPositions", queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonPositionDao.select", param, PersonPosition.class));
		model.put("topic", ParamUtils.getParamValue("WTFK"));  //htf add 通过该参数,设置调整的问题反馈页面
		
		boolean lineSendPer = this.hasPermission("07ab3e88-ea42-4fb5-a92f-9c5c766e97d3");
		model.put("lineSendPer", lineSendPer);
		
		List<LogoConfig> cfglist=queryExecutor.execQuery("com.wuyizhiye.basedata.dao.LogoConfigDao.getLogoConfigByCon", new HashMap<String, Object>(), LogoConfig.class);
		LogoConfig logoConfig = null ;
		if(cfglist!=null && cfglist.size()>0){
			logoConfig = cfglist.get(0);
			model.put("jiangHuUrl",logoConfig.getJiangHuUrl());
			model.put("logoPath",logoConfig.getLogoUrl());
			
		}
		
		//判断是否有启用消息中心，或者流程，或者鼎尖聊聊，如果都没有启用的话，则前台就不定时的去读取数据了
		if(null != logoConfig &&  StringUtils.isNotNull(logoConfig.getToolCheck())
				&& (logoConfig.getToolCheck().indexOf("XXZX")!=-1 || logoConfig.getToolCheck().indexOf("QYLC")!=-1
						|| logoConfig.getToolCheck().indexOf("DJLL")!=-1
					)
			){
			model.put("needIntervalRead", 1);		//需要定时读取数据
		} else {
			model.put("needIntervalRead", 0);		//不需要定时读取数据
		}
		
		//获取快捷菜单
		Map<String,Object> quickMap = new HashMap<String,Object>();
		quickMap.put("jobId", SystemUtil.getCurrentPosition().getJob().getId());
		/*List<Map> quickList = queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao.selectMenuByJob", quickMap, Map.class);
		if(quickList == null || quickList.size() == 0){
			//没有找到对应岗位的，找系统默认的
			quickMap.put("jobId", null);
			quickMap.put("defaultFlag", CommonFlagEnum.YES.getValue());
			quickList = queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.QuickMenuItemDao.selectDefaultMenu", quickMap, Map.class);
		}*/
		quickMap.put("isFrameworkShortcut", 1);//查询框架快捷方式
		List<Shortcut> quickList = queryExecutor.execQuery("com.wuyizhiye.basedata.shortcut.dao.ShortcutDao.selectFrameworkShortcut", quickMap, Shortcut.class);
		
		model.put("quickList", quickList);
		
		
		//得到系统设置的系统参数
		model.put("SCREEN_NOTICE", ParamUtils.getParamValue("SCREEN_NOTICE"));
		model.put("SCREEN_INSTITUTION", ParamUtils.getParamValue("SCREEN_INSTITUTION"));
		model.put("SCREEN_BILL", ParamUtils.getParamValue("SCREEN_BILL"));
		model.put("CMCT_DIAPPHONEISHTTP", ParamUtils.getParamValue("CMCT_DIAPPHONEISHTTP"));
		
		//所有消息的刷新时间
		Integer refreshtimeAllmsg = ParamUtils.getIntValue("REFRESHTIME_ALLMSG");
		if(null == refreshtimeAllmsg){
			refreshtimeAllmsg = 8;
		} 
		model.put("REFRESHTIME_ALLMSG", refreshtimeAllmsg);
		
		return "framework/workbench_simple";//先统一用同一个首页
	}
	
	@RequestMapping(value="mobile_home")
	public String mobile_home(ModelMap model){
		Person person = (Person) getSession().getAttribute("currentUser");
		Position position = (Position) getSession().getAttribute("currentPosition");
		Org org = (Org) getSession().getAttribute("currentOrg");
		model.put("person", person);
		model.put("position", position);
		model.put("org", org);
		
		
		
		model.put("dataSourceList", SystemUtil.getDataSourceList());
		String workbench = getString("workbench");
		WorkBenchTypeEnum workBenchType = WorkBenchTypeEnum.SIMPLE;
		if(StringUtils.isEmpty(workbench)){
			workbench = "simple";
			if(position!=null){
				Job job = position.getJob();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("id", job.getId());
				job = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.JobDao.select", param , Job.class).get(0);
				if(job.getWorkBenchType()!=null){
					workbench = job.getWorkBenchType().getValue();
					workBenchType = job.getWorkBenchType();
				}
			}
		}
		if(position!=null){
			model.put("menus", getMobileMenu(person, position,workBenchType));
		}
		return "framework/mobileIndex";
	}
	
	@RequestMapping(value="workbench_left")
	public String workbench_left(ModelMap model){
		workBench(model);
		return "framework/workbench-left-"+model.get("workbench");
	}
	
	@RequestMapping(value="workbench_home")
	public String workbench_home(ModelMap model){
		return "framework/workbench_home";
	}
	
	/***
	 * 取电脑菜单方法
	 * @param person
	 * @param position
	 * @param workBenchType
	 * @return
	 */
	private List<Menu> getMenu(Person person,Position position,WorkBenchTypeEnum workBenchType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", MenuTypeEnum.PC.getValue());
		param.put("notEnableFlag", "Y");
		List<Menu> menus = null;
		if(WorkBenchTypeEnum.ICON.equals(workBenchType)){
			param.put("checkPermission", true);//是否检查权限
			param.put("person", person.getId());
			param.put("position", position.getId());
			param.put("includeLink", true);

			menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
		}else{
			menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param , Menu.class);
//			根据权限过滤菜单
			Iterator<Menu> iterator = menus.iterator();
			param.clear();
			param.put("notEnableFlag", "Y");
			param.put("checkPermission", true);//是否检查权限
			param.put("person", person.getId());
			param.put("position", position.getId());
			while(iterator.hasNext()){
				Menu m = iterator.next();
				param.put("longNumber", m.getLongNumber());
				param.put("includeLink", true);
				List<Menu> children = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.getChild", param, Menu.class);
				if(children==null || children.size() == 0){
					iterator.remove();
				}else{
					m.setChildren(children);
				}
			}
		}
		
//		根据license过滤菜单
//		if(menus!=null && menus.size()>0){
//			 Map<ModuleEnum,ModuleLic> perms=Validate.getCurrPerms();
//			if(perms!=null && perms.size()>0){
//				Iterator<Menu> iterator = menus.iterator();
//				while(iterator.hasNext()){
//					Menu m = iterator.next();
//					if(perms.get(m.getModuleType())==null){
//						iterator.remove();
//					}
//				}
//			}else{
//				return null;
//			} 
//		}
		return menus;
	}
	
	/**
	 * 取手机菜单方法
	 * @param person
	 * @param position
	 * @param workBenchType
	 * @return
	 */
	private List<Menu> getMobileMenu(Person person,Position position,WorkBenchTypeEnum workBenchType){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", MenuTypeEnum.MOBILE.getValue());
		param.put("notEnableFlag", "Y");
		List<Menu> menus = null;
		param.put("checkPermission", true);//是否检查权限
		param.put("person", person.getId());
		param.put("position", position.getId());
		param.put("leaf", "1");//查询叶子节点
		//param.put("includeLink", true);	
		menus = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.MenuDao.select", param, Menu.class);
		return menus;
	}
}
