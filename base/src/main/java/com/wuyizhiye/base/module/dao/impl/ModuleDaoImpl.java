package com.wuyizhiye.base.module.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.module.dao.ModuleDao;
import com.wuyizhiye.base.module.model.Module;

/**
 * @ClassName ModuleDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="moduleDao")
public class ModuleDaoImpl extends BaseDaoImpl implements ModuleDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.base.module.dao.ModuleDao";
	}

	@Override
	public List<Module> getModuleList(Map<String, Object> map) {
		
		return this.getSqlSession().selectList(getNameSpace()+".select", map);
	}

	@Override
	public void updatePartBatch(List<Module> mlist) {
		for(Module m:mlist){
			this.getSqlSession().update(getNameSpace()+".updateModule", m);
		}
	}
}
