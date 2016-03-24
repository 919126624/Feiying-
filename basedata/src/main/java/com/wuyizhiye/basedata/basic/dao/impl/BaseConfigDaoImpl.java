package com.wuyizhiye.basedata.basic.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.dao.BaseConfigDao;
import com.wuyizhiye.basedata.basic.model.BaseConfig;

/**
 * @ClassName BaseConfigDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="baseConfigDao")
public class BaseConfigDaoImpl extends BaseDaoImpl implements BaseConfigDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.basic.dao.BaseConfigDao";
	}

	@Override
	public BaseConfig getBaseConfigByNum(String number) {
		return (BaseConfig) getSqlSession().selectOne(getNameSpace()+".getByNumber", number);
	}

	@Override
	public void addOrupdate(List<BaseConfig> cflist) {
		for(int i=0;i<cflist.size();i++){
			BaseConfig cf = cflist.get(i);
			if(StringUtils.isEmpty(cf.getId())){
				cf.setUUID();
				this.addEntity(cf);
			}else{
				this.updateEntity(cf);
			}
		}
	}

}
