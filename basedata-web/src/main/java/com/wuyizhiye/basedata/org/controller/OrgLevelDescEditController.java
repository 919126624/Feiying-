package com.wuyizhiye.basedata.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.OrgLevelDesc;
import com.wuyizhiye.basedata.org.service.OrgLevelDescService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName OrgLevelDescEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/orgleveldesc/*")
public class OrgLevelDescEditController extends EditController {

	@Autowired
	private OrgLevelDescService orgLevelDescService;
	
	@Override
	protected Class getSubmitClass() {
		return OrgLevelDesc.class;
	}

	@Override
	protected BaseService getService() {
		return orgLevelDescService;
	}

}
