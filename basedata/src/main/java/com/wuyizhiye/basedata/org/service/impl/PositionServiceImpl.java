package com.wuyizhiye.basedata.org.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.dao.QueryExecutor;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.OrgDao;
import com.wuyizhiye.basedata.org.dao.PositionDao;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.org.service.PositionService;

/**
 * @ClassName PositionServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="positionService")
@Transactional
public class PositionServiceImpl extends BaseServiceImpl<Position> implements
		PositionService {
	@Autowired
	private PositionDao positionDao;
	@Autowired
	private OrgDao orgDao;
	@Autowired
	private QueryExecutor queryExecutor;
	
	@Override
	public List<Position> getByOrg(String org) {
		return positionDao.getByOrg(org);
	}

	@Override
	protected BaseDao getDao() {
		return positionDao;
	}

	@Override
	public void savePositions(Org org,List<Position> positions) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("belongOrg", org.getId());
		List<Position> old = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.PositionDao.select", param , Position.class);
		Iterator<Position> pos = positions.iterator();
		Position position;
		while(pos.hasNext()){
			position = pos.next();
			if(StringUtils.isEmpty(position.getId())){
				super.addEntity(position);
			}else{
				Iterator<Position> ops = old.iterator();
				while(ops.hasNext()){
					Position o = ops.next();
					if(o.getId().equals(position.getId())){
						o.setJob(position.getJob());
						o.setName(position.getName());
						o.setLeading(position.getLeading());
						o.setReport(position.getReport());
						super.updateEntity(o);
						ops.remove();
					}
				}
			}
		}
		if(old.size()>0){
			for(Position p : old){
				super.deleteEntity(p);
			}
		}
		old = queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.PositionDao.select", param , Position.class);
		StringBuilder jobs = new StringBuilder();
		for(Position p : old){
			jobs.append(p.getName()).append(";");
		}
		org = orgDao.getEntityById(org.getId());
		org.setJobs(jobs.toString());
		orgDao.updateEntity(org);
	}
}
