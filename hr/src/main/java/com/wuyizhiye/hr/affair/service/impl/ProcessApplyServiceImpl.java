package com.wuyizhiye.hr.affair.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.hr.affair.service.ProcessApplyService;
/**
 * 消息中心流程操作处理
 * @author hlz
 *
 */
@Component(value="processApplyService")
@Transactional
public class ProcessApplyServiceImpl implements ProcessApplyService {

	@Autowired
	private QueryExecutor queryExecutor;
	@Override
	public void updateProcess(Map<String, Object> param) {
		queryExecutor.executeUpdate("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.update1", param);
		queryExecutor.executeUpdate("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.update2", param);
		queryExecutor.executeUpdate("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.update3", param);
		queryExecutor.executeUpdate("com.wuyizhiye.hr.affair.dao.ProcessApplyEntityDao.update4", param);
	}

}
