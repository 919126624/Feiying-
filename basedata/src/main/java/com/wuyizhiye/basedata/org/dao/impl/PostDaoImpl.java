package com.wuyizhiye.basedata.org.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.PostDao;

/**
 * @ClassName PostDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="postDao")
public class PostDaoImpl extends BaseDaoImpl implements PostDao {

	@Override
	protected String getNameSpace() {
		// TODO Auto-generated method stub
		return "com.wuyizhiye.basedata.org.dao.PostDao";
	}


}
