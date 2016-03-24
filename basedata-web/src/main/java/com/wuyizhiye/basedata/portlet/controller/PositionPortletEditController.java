package com.wuyizhiye.basedata.portlet.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.portlet.model.HomeSet;
import com.wuyizhiye.basedata.portlet.model.PositionPortlet;
import com.wuyizhiye.basedata.portlet.service.PositionPortletService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PositionPortletEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/positionPortlet/*")
public class PositionPortletEditController extends EditController {

	@Autowired
	private PositionPortletService positionPortletService;
	
	@Override
	protected Class getSubmitClass() {
		return PositionPortlet.class;
	}

	@Override
	protected BaseService getService() {
		return positionPortletService;
	}
	
	@RequestMapping(value="saveAndUpdate")
	public void saveAndUpdate(HttpServletResponse response){
		
		String layoutid = this.getString("layoutid");
		String posid = this.getString("posid");
		List<PositionPortlet> pplist = new ArrayList<PositionPortlet>();
		String[] posary = posid.split(",");
		for(int i=0;i<posary.length;i++){
			PositionPortlet ptemp = new PositionPortlet();
			Job job = new Job();
			job.setId(posary[i]);
			ptemp.setPosition(job);
			
			HomeSet hs = new HomeSet();
			hs.setId(layoutid);
			ptemp.setLayout(hs);
			pplist.add(ptemp);
		}
		this.positionPortletService.saveAndUpdate(pplist);
		this.getOutputMsg().put("STATE", "SUCCESS");
		this.getOutputMsg().put("MSG", "保存成功");	
		
		outPrint(response, JSONObject.fromObject(getOutputMsg(), getDefaultJsonConfig()));
	}

}
