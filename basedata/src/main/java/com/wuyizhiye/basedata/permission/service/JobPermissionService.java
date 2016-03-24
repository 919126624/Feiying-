package com.wuyizhiye.basedata.permission.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.permission.model.JobPermission;

/**
 * @ClassName JobPermissionService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface JobPermissionService extends BaseService<JobPermission> {
	void saveJobPermission(List<JobPermission> add,List<String> deleteIds);
}
