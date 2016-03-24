package com.wuyizhiye.basedata.portlet.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.portlet.enums.PortletStatusEnum;
import com.wuyizhiye.basedata.portlet.model.HomeSet;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.portlet.model.PortletLayout;
import com.wuyizhiye.basedata.portlet.service.HomeSetService;
import com.wuyizhiye.framework.base.controller.EditController;
import com.wuyizhiye.framework.util.BeanUtils;

/**
 * @ClassName HomeSetEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/homeset/*")
public class HomeSetEditController extends EditController {

	@Autowired
	private HomeSetService homeSetService;
	
	@Override
	protected Class getSubmitClass() {
		return HomeSet.class;
	}

	@Override
	protected BaseService getService() {
		return homeSetService;
	}

	@RequestMapping(value="save")
	public void save(HttpServletResponse response){
		HomeSet data = new HomeSet();
		String portletlist = getString("portletlist");
		String homesetid = getString("homesetid");
		List<PortletLayout> portlayList = new ArrayList<PortletLayout>();
		boolean add = false;
		if(StringUtils.isNotNull(homesetid)){
			data = this.homeSetService.getEntityById(homesetid);
		}else{
			data.setUUID();
			data.setStatus(PortletStatusEnum.ENABLE);
			add = true;
		}
		BeanUtils.fillentity(getParamMap(), data, HomeSet.class);
		List<PortletLayout> portletList = new ArrayList<PortletLayout>(); 
		if(!StringUtils.isEmpty(portletlist)){
			JSONArray ary = JSONArray.fromObject(portletlist);
			
			for(int i=0;i<ary.size();i++){
				PortletLayout pl = new PortletLayout();
				JSONObject obj = (JSONObject) ary.get(i);
				String portletid = obj.get("portletid").toString();
				String x = obj.get("x").toString();
				String y = obj.get("y").toString();
				
				pl.setPortlet(new Portlet(portletid));
				pl.setLayoutX(Integer.parseInt(x));
				pl.setLayoutY(Integer.parseInt(y));
				pl.setParent(data);
				portlayList.add(pl);
			}
		}
		this.homeSetService.addHomeSet(data, portlayList,add);
		getOutputMsg().put("id", ((CoreEntity)data).getId());
		getOutputMsg().put("STATE", "SUCCESS");
		getOutputMsg().put("MSG", "保存成功");
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}
	
}
