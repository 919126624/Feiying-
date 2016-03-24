package com.wuyizhiye.basedata.topic.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.basedata.topic.dao.TopicDao;
import com.wuyizhiye.basedata.topic.model.DealHistory;
import com.wuyizhiye.basedata.topic.model.Topic;
import com.wuyizhiye.basedata.topic.service.DealHistoryService;
import com.wuyizhiye.basedata.topic.service.TopicService;
import com.wuyizhiye.basedata.util.SystemUtil;

/**
 * @ClassName TopicServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="topicService")
public class TopicServiceImpl extends DataEntityService<Topic> implements
		TopicService {
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private DealHistoryService dealHistoryService;
	@Override
	protected BaseDao getDao() {
		return topicDao;
	}
	
	@Override
	public void addEntity(Topic entity) {
		entity.setSubmitTime(new Date());
		super.addEntity(entity);
		DealHistory h = new DealHistory();
		h.setId(UUID.randomUUID().toString());
		h.setDealPerson(SystemUtil.getCurrentUser());
		h.setDealTime(new Date());
		h.setRecord("创建问题");
		h.setTopic(entity);
		dealHistoryService.addEntity(h);
	}
}
