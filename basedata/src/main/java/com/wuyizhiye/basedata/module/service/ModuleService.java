package com.wuyizhiye.basedata.module.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;

import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.service.BaseService;

/**
 * @ClassName ModuleService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface ModuleService extends BaseService<Module> {
	void refreshLicense();
	/**
	 * 根据模块枚举获取模块信息
	 * @param module
	 * @return
	 */
	@Cacheable(value="default",key="#module")
	Module getModuleByType(ModuleEnum module);
	
	/**
	 * 启用/禁用
	 * @param module
	 * @param enable
	 * @return
	 */
	String enable(Module module,boolean enable);
	
	List<Module> getModuleList(Map<String,Object> map);
	
	/**
	 * 更新模块部分属性 编码 备注 类型
	 * @param mlist
	 */
	void updatePartBatch(List<Module> mlist);
}
