package com.wuyizhiye.basedata.workload.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.workload.dao.WorkloadDao;
import com.wuyizhiye.basedata.workload.model.Workload;
import com.wuyizhiye.basedata.workload.model.WorkloadAssociate;
import com.wuyizhiye.basedata.workload.model.WorkloadSort;
import com.wuyizhiye.basedata.workload.service.WorkloadService;

/**
 * @ClassName WorkloadServiceImpl
 * @Description 工作量
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="workloadService")
@Transactional
public class WorkloadServiceImpl extends BaseServiceImpl<Workload> implements WorkloadService{

	@Autowired
	private WorkloadDao workloadDao;
	
	@Override
	protected BaseDao getDao() {
		return workloadDao;
	}

	@Override
	public void setAccredit(String workloadId, String jobId) {
		WorkloadAssociate workloadAssociate = new WorkloadAssociate();
		workloadAssociate.setId(StringUtils.getUUID());
		workloadAssociate.setJobId(jobId);
		workloadAssociate.setShoutcutId(workloadId);
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("workloadId", workloadId);
		param.put("jobId", jobId);
		queryExecutor.executeInsert("com.wuyizhiye.basedata.workload.dao.WorkloadDao.setAccredit", workloadAssociate);
	}

	@Override
	public void delAccredit(String workloadId, String jobId) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("workloadId", workloadId);
		param.put("jobId", jobId);
		queryExecutor.executeDelete("com.wuyizhiye.basedata.workload.dao.WorkloadDao.delAccredit", param);
	}
	
	@Override
	public void initSort(WorkloadSort workloadSort){
		if(!StringUtils.isNotNull(workloadSort.getId())){
			workloadSort.setUUID();
		}
		queryExecutor.executeDelete("com.wuyizhiye.basedata.workload.dao.WorkloadDao.initSort", workloadSort);
	}
	
	@Override
	public void saveSort(WorkloadSort workloadSort){
		if(!StringUtils.isNotNull(workloadSort.getId())){
			workloadSort.setUUID();
		}
		queryExecutor.executeDelete("com.wuyizhiye.basedata.workload.dao.WorkloadDao.insertSort", workloadSort);
	}

}
