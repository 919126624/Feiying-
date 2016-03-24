package com.wuyizhiye.basedata.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.topic.service.TopicCommentService;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName TopicCommentListController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/topicCommentList/*")//new
public class TopicCommentListController extends ListController {
	@Autowired
	private TopicCommentService topicCommentService;

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
		return "com.wuyizhiye.basedata.topic.dao.TopicCommentDao.select";
	}

	@Override
	protected BaseService getService() {
		return topicCommentService;
	}

}
