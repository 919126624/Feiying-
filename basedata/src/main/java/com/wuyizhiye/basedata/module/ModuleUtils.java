package com.wuyizhiye.basedata.module;

import com.wuyizhiye.base.module.enums.ModuleEnum;
import com.wuyizhiye.base.module.model.Module;
import com.wuyizhiye.base.spring.ApplicationContextAware;
import com.wuyizhiye.basedata.module.service.ModuleService;

/**
 * @ClassName ModuleUtils
 * @Description 模块工具类
 * @author li.biao
 * @date 2015-4-2
 */
public class ModuleUtils {
	
	/**
	 * 判断模块是否启用
	 * @param module
	 * @return
	 */
	public static boolean isEnable(ModuleEnum module){
		ModuleService service = ApplicationContextAware.getApplicationContext().getBean(ModuleService.class);
		Module m = service.getModuleByType(module);
		if(m==null){
			throw new RuntimeException("模块不存在[" + module.getName() + "]");
		}
		return m.isEnable();
	}
}
