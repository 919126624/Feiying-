package com.wuyizhiye.basedata.topic.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.topic.dao.TopicDao;

/**
 * @ClassName TopicDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="topicDao")
public class TopicDaoImpl extends BaseDaoImpl implements TopicDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.topic.dao.TopicDao";
	}
}
