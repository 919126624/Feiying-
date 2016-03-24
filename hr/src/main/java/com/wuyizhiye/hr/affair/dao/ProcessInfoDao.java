package com.wuyizhiye.hr.affair.dao;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.affair.model.ProcessInfo;
/**
 * 单据信息(公司动态)
 * @author hlz
 *
 */
public interface ProcessInfoDao extends BaseDao {
	public void insertProcessInfo(ProcessInfo pph);
}
