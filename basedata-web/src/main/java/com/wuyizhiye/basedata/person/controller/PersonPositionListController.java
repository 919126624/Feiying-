package com.wuyizhiye.basedata.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonPositionService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PersonPositionListController
 * @Description 人员职位列表controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/personPosition/*")
public class PersonPositionListController extends ListController {
	@Autowired
	private PersonPositionService personPositionService;
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		return null;
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		return "com.wuyizhiye.basedata.person.dao.PersonPositionDao.select";
	}

	@Override
	protected BaseService<PersonPosition> getService() {
		return personPositionService;
	}

}
