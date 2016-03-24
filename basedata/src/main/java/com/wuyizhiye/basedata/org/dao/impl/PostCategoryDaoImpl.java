package com.wuyizhiye.basedata.org.dao.impl;


import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.org.dao.PostCategoryDao;

/**
 * @ClassName PostCategoryDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="postCategoryDao")
public class PostCategoryDaoImpl extends BaseDaoImpl implements PostCategoryDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.org.dao.PostCategoryDao";
	}


}
