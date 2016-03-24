package com.wuyizhiye.basedata.apiCenter.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.apiCenter.dao.APIItemDao;
import com.wuyizhiye.basedata.apiCenter.model.APIItem;


/**
 * @ClassName APIItemDaoImpl
 * @Description 接口管理Dao实现类
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="apiItemDao")
public class APIItemDaoImpl extends BaseDaoImpl implements APIItemDao{
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.apiCenter.dao.APIItemDao";
	}
	@Override
	public List<APIItem> getList(Map<String, Object> param) {
		return this.getSqlSession().selectList(getNameSpace()+".select", param);
	}
}
