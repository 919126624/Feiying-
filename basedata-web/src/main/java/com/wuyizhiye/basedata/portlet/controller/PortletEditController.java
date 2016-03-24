package com.wuyizhiye.basedata.portlet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.portlet.enums.PortletStatusEnum;
import com.wuyizhiye.basedata.portlet.model.Portlet;
import com.wuyizhiye.basedata.portlet.service.PortletService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName PortletEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="interflow/portlet/*")
public class PortletEditController extends EditController {

	@Autowired
	private PortletService portletService;
	
	
	@Override
	protected Class getSubmitClass() {
		return Portlet.class;
	}

	@Override
	protected BaseService getService() {
		return portletService;
	}
	
	@Override
	public boolean validate(Object data) {
		Portlet p = (Portlet)data;
		if(StringUtils.isEmpty(p.getId())){
		p.setStatus(PortletStatusEnum.ENABLE);
		}
		return true;
	}

}
