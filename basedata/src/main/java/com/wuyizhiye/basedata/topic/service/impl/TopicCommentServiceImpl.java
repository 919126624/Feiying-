package com.wuyizhiye.basedata.topic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.topic.dao.TopicCommentDao;
import com.wuyizhiye.basedata.topic.model.TopicComment;
import com.wuyizhiye.basedata.topic.service.TopicCommentService;

/**
 * @ClassName TopicCommentServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="topicCommentService")
@Transactional
public class TopicCommentServiceImpl extends DataEntityService<TopicComment> implements TopicCommentService {
	@Autowired
	private TopicCommentDao topicCommentDao;
	@Override
	protected BaseDao getDao() {
		return topicCommentDao;
	}	
}