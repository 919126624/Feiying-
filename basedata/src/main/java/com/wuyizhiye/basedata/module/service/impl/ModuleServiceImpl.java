package com.wuyizhiye.basedata.module.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.module.dao.ModuleDao;
import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.module.model.ModuleLic;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.module.service.ModuleService;
import com.wuyizhiye.basedata.util.Validate;

/**
 * @ClassName ModuleServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="moduleService")
@Transactional
public class ModuleServiceImpl extends BaseServiceImpl<Module> implements ModuleService {
	@Autowired
	private ModuleDao moduleDao;
	@Override
	protected BaseDao getDao() {
		return moduleDao;
	}
	
	@Override
	public Module getModuleByType(ModuleEnum module) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("modules", "'" + module + "'");
		return queryExecutor.execOneEntity("com.wuyizhiye.base.module.dao.ModuleDao.select", param, Module.class);
	}	
	
	@Override
	@CacheEvict(value="default",key="#entity.type")
	public void updateEntity(Module entity) {
		super.updateEntity(entity);
	}
	
	@Override
	public String enable(Module module, boolean enable) {
		if(!enable){
			//随时可以禁用
			module.setEnable(enable);
			updateEntity(module);
			return null;
		}
		Map<ModuleEnum,ModuleLic> params = Validate.getCurrPerms();
		Set<ModuleEnum> keys = params.keySet();
		for(ModuleEnum key : keys){
			if(key ==module.getType()){
				ModuleLic lic = params.get(key);
				Date now = new Date();
				if(now.compareTo(lic.getStart()) >= 0 && now.compareTo(lic.getEnd()) <= 0 ){
					module.setEnable(enable);
					updateEntity(module);
					return null;
				}else{
					return "不在许可期限内,不允许启用";
				}
			}
		}
		return "未找到许可信息,不允许启用";
	}
	
	@Override
	public void refreshLicense() {
		Map<ModuleEnum,ModuleLic> params = Validate.getCurrPerms();
		List<Module> modules = queryExecutor.execQuery("com.wuyizhiye.base.module.dao.ModuleDao.select", new HashMap<String,Object>(), Module.class);
		Set<ModuleEnum> keys = params.keySet();
		for(Module m : modules){
			m.setEnable(false);
			m.setStart(null);
			m.setEnd(null);
			for(ModuleEnum key : keys){
				if(key == m.getType()){
					m.setEnable(true);
					m.setStart(params.get(key).getStart());
					m.setEnd(params.get(key).getEnd());
					break;
				}
			}
		}
		super.updateBatch(modules);
	}

	@Override
	public List<Module> getModuleList(Map<String, Object> map) {
		
		return this.moduleDao.getModuleList(map);
	}

	@Override
	public void updatePartBatch(List<Module> mlist) {
		this.moduleDao.updatePartBatch(mlist);
		
	}	
}