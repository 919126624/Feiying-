package com.wuyizhiye.basedata.org.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.PostLevel;

/**
 * @ClassName PostLevelService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PostLevelService extends BaseService<PostLevel> {
	void deleteByPost(String post);
}
