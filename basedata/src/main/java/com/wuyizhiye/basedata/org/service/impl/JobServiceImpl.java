package com.wuyizhiye.basedata.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.JobDao;
import com.wuyizhiye.basedata.org.dao.JobLevelDao;
import com.wuyizhiye.basedata.org.model.Job;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.service.JobLevelService;
import com.wuyizhiye.basedata.org.service.JobService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName JobServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="jobService")
@Transactional
public class JobServiceImpl extends DataEntityService<Job> implements JobService {
	@Autowired
	private JobDao jobDao;
	@Autowired
	private JobLevelService jobLevelService;
	@Autowired
	private JobLevelDao jobLevelDao;
	@Override
	protected BaseDao getDao() {
		return jobDao;
	}
	
	@Override
	public void addEntity(Job entity) {
		super.addEntity(entity);
		List<JobLevel> jobLevels = entity.getJobLevel();
		for(JobLevel jl : jobLevels){
			jl.setJob(entity);
			jobLevelService.addEntity(jl);
		}
	}
	
	@Override
	public void updateEntity(Job entity) {
		List<JobLevel> oldJobLevel = jobLevelDao.getByJob(entity.getId());
		List<JobLevel> jobLevels = entity.getJobLevel();
		List<JobLevel> updateJobLevel = new ArrayList<JobLevel>(jobLevels);
		List<JobLevel> newJobLevel = new ArrayList<JobLevel>();
		for(JobLevel d : jobLevels){
			d.setJob(entity);
			if(StringUtils.isEmpty(d.getId())){
				d.setId(UUID.randomUUID().toString());
				newJobLevel.add(d);
				updateJobLevel.remove(d);
			}else{
				for(JobLevel o : oldJobLevel){
					if(o.getId().equals(d.getId())){
						oldJobLevel.remove(o);
						break;
					}
				}
			}
		}
		for(JobLevel o : oldJobLevel){
			jobLevelService.deleteById(o.getId());
		}
		for(JobLevel o : newJobLevel){
			jobLevelService.addEntity(o);
		}
		for(JobLevel o : updateJobLevel){
			jobLevelService.updateEntity(o);
		}
		super.updateEntity(entity);
	}
	
	@Override
	public void deleteEntity(Job entity) {
		jobLevelService.deleteByJob(entity.getId());
		super.deleteEntity(entity);
	}
	
	@Override
	public void deleteById(String id) {
		jobLevelService.deleteByJob(id);
		super.deleteById(id);
	}
}
