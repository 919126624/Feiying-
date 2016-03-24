package com.wuyizhiye.basedata.workload.service;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.workload.model.Workload;
import com.wuyizhiye.basedata.workload.model.WorkloadSort;

/**
 * @ClassName WorkloadService
 * @Description 工作量
 * @author li.biao
 * @date 2015-4-3
 */
public interface WorkloadService extends BaseService<Workload>{

	void setAccredit(String workloadId, String jobId);

	void delAccredit(String workloadId, String jobId);

	void saveSort(WorkloadSort workloadSort);

	void initSort(WorkloadSort workloadSort);

}
