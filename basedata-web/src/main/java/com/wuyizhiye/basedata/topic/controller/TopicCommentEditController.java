package com.wuyizhiye.basedata.topic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.topic.model.TopicComment;
import com.wuyizhiye.basedata.topic.service.TopicCommentService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName TopicCommentEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="basedata/topicComment/*")
public class TopicCommentEditController extends EditController {

	@Autowired
	private TopicCommentService topicCommentService;
	
	@Override
	protected Class getSubmitClass() {
		return TopicComment.class;
	}

	@Override
	protected BaseService getService() {
		return topicCommentService;
	}

}
