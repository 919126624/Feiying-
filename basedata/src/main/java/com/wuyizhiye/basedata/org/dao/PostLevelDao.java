package com.wuyizhiye.basedata.org.dao;

import java.util.List;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.org.model.PostLevel;

/**
 * @ClassName PostLevelDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PostLevelDao extends BaseDao {
	void deleteByPost(String post);
	List<PostLevel> getByPost(String post);
}
