package com.wuyizhiye.basedata.portlet.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.portlet.enums.LayoutEnum;
import com.wuyizhiye.basedata.portlet.enums.PortletStatusEnum;
import com.wuyizhiye.basedata.portlet.model.HomeSet;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;
import com.wuyizhiye.basedata.portlet.service.HomeSetService;
import com.wuyizhiye.basedata.portlet.service.PortletLayoutService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName HomeSetListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/homeset/*")
public class HomeSetListController extends ListController {

	@Autowired
	private HomeSetService homeSetService;
	
	@Autowired
	private PortletLayoutService portletLayoutService;
	
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return "interflow/portlet/HomeGroupList";
	}

	@Override
	protected String getEditView() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", PortletStatusEnum.ENABLE);
		List<Portlet> portlist = 
				this.queryExecutor.execQuery("com.wuyizhiye.basedata.portlet.dao.PortletDao.select", map, Portlet.class);
		
		String editId = this.getString("id");
		if(StringUtils.isNotNull(editId)){
		map = new HashMap<String,Object>();
		map.put("parentId", editId);
		map.put("orderby", " order by FLAYX asc,FLAYY asc ");
		List<PortletLayout> list = 
				portletLayoutService.getlist(map);
		String pstr = "[";
		for(int i=0;i<list.size();i++){
			String dataStr = "";
			PortletLayout t = list.get(i);
			if(i>0){
				dataStr+=",";
			}
			dataStr += "{"; 
			dataStr += "'portletid':'"+t.getPortlet().getId()+"',";
			dataStr += "'portletname':'"+t.getPortlet().getName()+"("+t.getPortlet().getFormatSizeStr()+")"+"',";
			dataStr += "'x':'"+t.getLayoutX()+"',";
			dataStr += "'y':'"+t.getLayoutY()+"'";
			dataStr += "}";
			pstr += dataStr;
		}
		pstr += "]";
		this.getRequest().setAttribute("portlaystr", pstr);
		}
		this.getRequest().setAttribute("portlist", portlist);
		this.getRequest().setAttribute("formatlist", LayoutEnum.values());
		return "interflow/portlet/HomeSetEdit";
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.portlet.dao.HomeSetDao.select";
	}

	@Override
	protected BaseService getService() {
		return homeSetService;
	}
	
	@RequestMapping(value="updateStatus")
	public void updateStatus(HttpServletResponse response,HomeSet portlet) {
		this.homeSetService.updateStatus(portlet);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));

	}
	
	@RequestMapping(value="updateDefault")
	public void updateDefault(HttpServletResponse response,HomeSet portlet) {
		this.homeSetService.updateDefault(portlet);
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));

	}

	@RequestMapping(value="showlist")
	public String showlist(ModelMap map){
		return "interflow/portlet/HomeSetList";
	}
}
