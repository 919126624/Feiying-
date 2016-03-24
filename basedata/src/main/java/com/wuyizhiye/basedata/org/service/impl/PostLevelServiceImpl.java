package com.wuyizhiye.basedata.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.org.dao.PostLevelDao;
import com.wuyizhiye.basedata.org.model.PostLevel;
import com.wuyizhiye.basedata.org.service.PostLevelService;

/**
 * @ClassName PostLevelServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="postLevelService")
@Transactional
public class PostLevelServiceImpl extends BaseServiceImpl<PostLevel> implements
		PostLevelService {
	@Autowired
	private PostLevelDao postLevelDao;
	@Override
	protected BaseDao getDao() {
		return postLevelDao;
	}
	@Override
	public void deleteByPost(String job) {
		postLevelDao.deleteByPost(job);
	}

}
