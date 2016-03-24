package com.wuyizhiye.basedata.org.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.PostLevelDao;
import com.wuyizhiye.basedata.org.model.PostLevel;

/**
 * @ClassName PostLevelDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="postLevelDao")
public class PostLevelDaoImpl extends BaseDaoImpl implements PostLevelDao {

	@Override
	public void deleteByPost(String post) {
		getSqlSession().delete(getNameSpace() + ".deleteByPost", post);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PostLevel> getByPost(String post) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("post", post);
		return getSqlSession().selectList(getNameSpace() + ".select", param);
	}

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.PostLevelDao";
	}


}
