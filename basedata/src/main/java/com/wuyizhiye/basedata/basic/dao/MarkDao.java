package com.wuyizhiye.basedata.basic.dao;

import com.wuyizhiye.base.dao.BaseDao;

/**
 * @ClassName MarkDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface MarkDao extends BaseDao {
	
	/**
	 * 生成标签编码
	 * @param prefix M：用户级，S：系统级，默认用户级
	 * @return
	 */
	public String getMarkCode(String prefix) ;
}
