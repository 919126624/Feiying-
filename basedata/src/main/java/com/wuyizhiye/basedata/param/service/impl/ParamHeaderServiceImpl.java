package com.wuyizhiye.basedata.param.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.param.dao.ParamHeaderDao;
import com.wuyizhiye.basedata.param.dao.ParamLinesDao;
import com.wuyizhiye.basedata.param.model.ParamHeader;
import com.wuyizhiye.basedata.param.model.ParamLines;
import com.wuyizhiye.basedata.param.service.ParamHeaderService;
import com.wuyizhiye.basedata.param.service.ParamLinesService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName ParamHeaderServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="paramHeaderService")
@Transactional
public class ParamHeaderServiceImpl extends DataEntityService<ParamHeader> implements ParamHeaderService {
	@Autowired
	private ParamHeaderDao paramHeaderDao;
	@Autowired
	private ParamLinesService paramLinesService;
	
	@Autowired
	private ParamLinesDao paramLinesDao;
	
	@Override
	protected BaseDao getDao() {
		return paramHeaderDao;
	}
	
	@Override
	public void addEntity(ParamHeader entity) {
		if(entity!=null){
		entity.setStatus("1");	
		}
		super.addEntity(entity);
		List<ParamLines> paramLines = entity.getParamLines();
		for (ParamLines lines : paramLines) {
			lines.setParamHeader(entity);
			paramLinesService.addEntity(lines);
		}
	}
	
	@Override
	public void updateEntity(ParamHeader entity) {
		List<ParamLines> oldLinesItems = paramLinesDao.getParamLines(entity.getId());
		List<ParamLines> linesItems = entity.getParamLines();
		List<ParamLines> updateLinesItems = new ArrayList<ParamLines>(linesItems);
		List<ParamLines> newLinesItems = new ArrayList<ParamLines>();
		for (ParamLines d : linesItems) {
			d.setParamHeader(entity);
			if (StringUtils.isEmpty(d.getId())) {
				d.setId(UUID.randomUUID().toString());
				newLinesItems.add(d);
				updateLinesItems.remove(d);
			} else {
				for (ParamLines o : oldLinesItems) {
					if (o.getId().equals(d.getId())) {
						oldLinesItems.remove(o);
						break;
					}
				}
			}
		}
		for (ParamLines o : oldLinesItems) {
			paramLinesService.deleteById(o.getId());
		}
		for (ParamLines o : newLinesItems) {
			paramLinesService.addEntity(o);
		}
		for (ParamLines o : updateLinesItems) {
			paramLinesService.updateEntity(o);
		}
		super.updateEntity(entity);
	}
	public void updateStatus(ParamHeader entity) {
		super.updateEntity(entity);
	}
	@Override
	public List<ParamHeader> getParamHeaderList(Map<String, Object> map) {
		
		return this.paramHeaderDao.getParamHeaderList(map);
	}
}