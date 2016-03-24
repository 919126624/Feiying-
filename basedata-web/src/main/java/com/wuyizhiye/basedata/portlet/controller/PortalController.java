package com.wuyizhiye.basedata.portlet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.portlet.enums.LayoutEnum;
import com.wuyizhiye.basedata.portlet.model.HomeSet;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;
import com.wuyizhiye.basedata.portlet.model.PositionPortlet;
import com.wuyizhiye.basedata.portlet.service.HomeSetService;
import com.wuyizhiye.basedata.portlet.service.PortletLayoutService;
import com.wuyizhiye.basedata.portlet.service.PositionPortletService;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PortalController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/portal/*")
public class PortalController extends ListController {

	@Autowired
	private PortletLayoutService portletLayoutService;
	
	@Autowired
	private HomeSetService homeSetService;
	
	@Autowired
	private PositionPortletService positionPortletService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		Position p = SystemUtil.getCurrentPosition();
		PositionPortlet obj = 
				positionPortletService.getEntityByPositionId(p.getJob().getId());
		Map<String,Object> map = new HashMap<String,Object>();
		HomeSet defhome = null;
		if(null==obj || obj.getLayout()!=null){
			map.put("isdefault", 1);
			List<HomeSet> hlist = 
					this.queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.HomeSetDao.select", map, HomeSet.class);
			if(hlist.size()>0)
				defhome = hlist.get(0);
			else{
				this.getRequest().setAttribute("msg", "请在首页配置页面中至少设置一条默认数据");
				this.getRequest().setAttribute("nodata", true);
				return "interflow/portal/home";
			}
		}else{
			defhome = this.homeSetService.getEntityById(obj.getLayout().getId());
		}
		map = new HashMap<String,Object>();	
		map.put("parentId",defhome.getId());
		map.put("orderby", " order by FLAYX asc,FLAYY asc ");
		List<PortletLayout> list = 
				portletLayoutService.getlist(map);
		List<PortletLayout> leftlist = new ArrayList<PortletLayout>();
		List<PortletLayout> rightlist = new ArrayList<PortletLayout>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getLayoutX()==1) leftlist.add(list.get(i));
			if(list.get(i).getLayoutX()==2) rightlist.add(list.get(i));
		}
		this.getRequest().setAttribute("leftlist", leftlist);
		this.getRequest().setAttribute("rightlist", rightlist);
		if(defhome.getLayout().equals(LayoutEnum.ONECOL)){
			return "interflow/portal/home";
		}else if(defhome.getLayout().equals(LayoutEnum.TWOCOL)){
			return "interflow/portal/newhome";
		}else return "interflow/portal/home";
	}
	/**
	 * 首页预览
	 * @return
	 */
	@RequestMapping(value="preview") 
	public String preview() {
		Map<String,Object> map = new HashMap<String,Object>();
		HomeSet defhome = null;
		map.put("id", getString("id","-1")); 
		List<HomeSet> hlist = this.queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.HomeSetDao.select", map, HomeSet.class);
		if(hlist.size()>0)
			defhome = hlist.get(0);
		else{
			this.getRequest().setAttribute("msg", "请在首页配置页面中至少设置一条默认数据");
			this.getRequest().setAttribute("nodata", true);
			return "interflow/portal/home";
		}
		map = new HashMap<String,Object>();	
		map.put("parentId",defhome.getId());
		map.put("orderby", " order by FLAYX asc,FLAYY asc ");
		List<PortletLayout> list = 	portletLayoutService.getlist(map);
		List<PortletLayout> leftlist = new ArrayList<PortletLayout>();
		List<PortletLayout> rightlist = new ArrayList<PortletLayout>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getLayoutX()==1) leftlist.add(list.get(i));
			if(list.get(i).getLayoutX()==2) rightlist.add(list.get(i));
		}
		this.getRequest().setAttribute("leftlist", leftlist);
		this.getRequest().setAttribute("rightlist", rightlist);
		if(defhome.getLayout().equals(LayoutEnum.ONECOL)){
			return "interflow/portal/home";
		}else if(defhome.getLayout().equals(LayoutEnum.TWOCOL)){
			return "interflow/portal/newhome";
		}else return "interflow/portal/home";
	}
	
	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return null;
	}

	@Override
	protected BaseService getService() {
		return null;
	}

	
	@RequestMapping(value="newhome")
	protected String newhome() {
		Position p = SystemUtil.getCurrentPosition();
		PositionPortlet obj = 
				positionPortletService.getEntityByPositionId(p.getJob().getId());
		Map<String,Object> map = new HashMap<String,Object>();
		HomeSet defhome = null;
		if(null==obj){
			map.put("isdefault", 1);
			List<HomeSet> hlist = 
					this.queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.HomeSetDao.select", map, HomeSet.class);
			if(hlist.size()>0)
				defhome = hlist.get(0);
			else{
				this.getRequest().setAttribute("msg", "请在首页配置页面中至少设置一条默认数据");
				this.getRequest().setAttribute("nodata", true);
				return "interflow/portal/newhome";
			}
		}else{
			defhome = obj.getLayout();
		}
		map = new HashMap<String,Object>();	
		map.put("parentId",defhome.getId());
		map.put("orderby", " order by FLAYX asc,FLAYY asc ");
		List<PortletLayout> list = 
				portletLayoutService.getlist(map);
		List<PortletLayout> leftlist = new ArrayList<PortletLayout>();
		List<PortletLayout> rightlist = new ArrayList<PortletLayout>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getLayoutX()==1) leftlist.add(list.get(i));
			if(list.get(i).getLayoutX()==2) rightlist.add(list.get(i));
		}
		this.getRequest().setAttribute("leftlist", leftlist);
		this.getRequest().setAttribute("rightlist", rightlist);
		return "interflow/portal/newhome";
	}
	
}
