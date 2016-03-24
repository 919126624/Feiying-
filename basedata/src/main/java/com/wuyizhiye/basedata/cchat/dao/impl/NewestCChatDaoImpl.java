package com.wuyizhiye.basedata.cchat.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.cchat.dao.NewestCChatDao;

/**
 * @ClassName NewestCChatDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedatacommonOrderDao")
public class NewestCChatDaoImpl extends BaseDaoImpl implements NewestCChatDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.cchat.dao.NewestCChatDao";
	}
}
