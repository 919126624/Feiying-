package com.wuyizhiye.base.module.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.module.model.Module;

/**
 * @ClassName ModuleDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
public interface ModuleDao extends BaseDao {
	
	List<Module> getModuleList(Map<String,Object> map);
	
	/**
	 * 更新模块部分属性 编码 备注 类型
	 * @param mlist
	 */
	void updatePartBatch(List<Module> mlist);
}
