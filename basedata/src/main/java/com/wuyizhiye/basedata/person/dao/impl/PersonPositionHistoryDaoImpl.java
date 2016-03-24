package com.wuyizhiye.basedata.person.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;

/**
 * @ClassName PersonPositionHistoryDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
@Component(value="personPositionHistoryDao")
public class PersonPositionHistoryDaoImpl extends BaseDaoImpl implements PersonPositionHistoryDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.person.dao.PersonPositionHistoryDao";
	}
	
	public void insertPositionHistory(PersonPositionHistory pstHis){
		this.getSqlSession().insert(getNameSpace()+".insert", pstHis);
	}
	
	@SuppressWarnings("unchecked")
	public List<PersonPositionHistory> getPositionHistory(Map<String,Object> params){
		return (List<PersonPositionHistory>)this.getSqlSession().selectList(getNameSpace()+".selectByExample", params);
	}
	
	public void updatePositionHistorySelective(PersonPositionHistory pstHis,PersonPositionHistory pstHisParam){
		Map<String, PersonPositionHistory> params = new HashMap<String, PersonPositionHistory>();
		params.put("record", pstHis);
		params.put("paramObj", pstHisParam);
		this.getSqlSession().insert(getNameSpace()+".updateByPositionHisSelective", params);
	}
	
	public void updateByPrimaryKeySelective(PersonPositionHistory pstHis){
		this.getSqlSession().insert(getNameSpace()+".updateByPrimaryKeySelective", pstHis);
	}
	
	public void updateByPrimaryKey(PersonPositionHistory pstHis){
		this.getSqlSession().insert(getNameSpace()+".updateByPrimaryKey", pstHis);
	}
	
	public void deletePositionHistoryById(String personPositionHistoryId){
		 
		this.getSqlSession().delete(getNameSpace()+".deleteByPrimaryKey", personPositionHistoryId);
	}
	
	public void deletePositionHistorySelective(PersonPositionHistory pstHisParam){
		Map<String, PersonPositionHistory> params = new HashMap<String, PersonPositionHistory>();
		params.put("paramObj", pstHisParam);
		this.getSqlSession().delete(getNameSpace()+".deleteByExample", params);
	}

}
