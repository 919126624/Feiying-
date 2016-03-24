package com.wuyizhiye.basedata.cchat.dao.impl;



import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.cchat.dao.PrivateLetterDao;

/**
 * @ClassName PrivateLetterDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="basedataprivateLetterDao")
public class PrivateLetterDaoImpl extends BaseDaoImpl implements
		PrivateLetterDao {
	
	@Override
	protected String getNameSpace() {
		
		return "com.wuyizhiye.basedata.cchat.dao.CChatDao";
	}

	@Override
	public int selectLetterCount(Map<String, Object> map) {
		int count = (Integer) getSqlSession().selectOne("com.wuyizhiye.basedata.cchat.dao.CChatDao.selectCount",map);
		return count;
	}
	


}
