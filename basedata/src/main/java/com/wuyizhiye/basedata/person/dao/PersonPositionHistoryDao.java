package com.wuyizhiye.basedata.person.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;

/**
 * @ClassName PersonPositionHistoryDao
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonPositionHistoryDao extends BaseDao{
	
	public void insertPositionHistory(PersonPositionHistory pstHis);
	
	public void updatePositionHistorySelective(PersonPositionHistory pstHis,PersonPositionHistory pstHisParam);
	
	/**
	 * 根据主键修改任职历史
	 * @param pstHis
	 */
	public void updateByPrimaryKeySelective(PersonPositionHistory pstHis);
	
	public void updateByPrimaryKey(PersonPositionHistory pstHis);
	
	public void deletePositionHistoryById(String personPositionHistoryId);
	
	public void deletePositionHistorySelective(PersonPositionHistory pstHisParam);
	
	/**
	 * 任职历史 通用查询方法
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PersonPositionHistory> getPositionHistory(Map<String,Object> params);
}
