package com.wuyizhiye.basedata.permission.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.basedata.permission.dao.JobPermissionDao;
import com.wuyizhiye.basedata.permission.model.JobPermission;
import com.wuyizhiye.basedata.permission.service.JobPermissionService;

/**
 * @ClassName JobPermissionServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="jobPermissionService")
@Transactional
public class JobPermissionServiceImpl extends BaseServiceImpl<JobPermission>
		implements JobPermissionService {
	@Autowired
	private JobPermissionDao jobPermissionDao;
	@Override
	public void saveJobPermission(List<JobPermission> add,
			List<String> deleteIds) {
		if(deleteIds!=null){
			jobPermissionDao.deleteBatch(deleteIds);
		}
		if(add!=null){
			Map<String, Object> param = new HashMap<String, Object>();
			JobPermission jp;
			Iterator<JobPermission> it = add.iterator();
			while(it.hasNext()){
				jp = it.next();
				param.put("job", jp.getJob().getId());
				param.put("permissionItem", jp.getPermissionItem().getId());
				List<JobPermission> jps = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.JobPermissionDao.select", param, JobPermission.class);
				if(jps.size()>0){
					it.remove();
				}else{
					jp.setId(UUID.randomUUID().toString());
				}
			}
			jobPermissionDao.addBatch(add);
		}
	}

	@Override
	protected BaseDao getDao() {
		return jobPermissionDao;
	}

}
