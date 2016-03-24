package com.wuyizhiye.basedata.topic.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.topic.dao.TopicCommentDao;

/**
 * @ClassName TopicCommentDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="topicCommentDao")
public class TopicCommentDaoImpl extends BaseDaoImpl implements TopicCommentDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.topic.dao.TopicCommentDao";
	}
}
