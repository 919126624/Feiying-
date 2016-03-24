package com.wuyizhiye.basedata.person.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.BdPositionHistoryBill;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;

/**
 * @ClassName PersonPositionHistoryService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public interface PersonPositionHistoryService extends BaseService<PersonPositionHistory> {
	
	public void insertPositionHistory(PersonPositionHistory pstHis);
	
	public void updatePositionHistorySelective(PersonPositionHistory pstHis,PersonPositionHistory pstHisParam);
	
	public void deletePositionHistoryById(String personPositionHistoryId);
	
	public void deletePositionHistorySelective(PersonPositionHistory pstHisParam);
	
	public void updateByPrimaryKeySelective(PersonPositionHistory pstHis);
	
	public void updateByPrimaryKey(PersonPositionHistory pstHis);
	
	/**
	 * 根据员工ID 查询所有任职历史记录
	 * @param personId
	 * @return
	 */
	public List<PersonPositionHistory> getAllPersonPositionHistory(String personId);
	
	/**
	 * 得到 任职记录
	 * @param personId
	 * @return
	 */
	public List<PersonPositionHistory> getPersonOnDutyRecord(String personId);
	
	/**
	 * 根据员工ID 查询最后一条任职历史记录
	 * @param personId
	 * @param primary
	 * @return
	 */
	public PersonPositionHistory getLastPersonPositionHistory(String personId,boolean primary);
	
	/**
	 * 根据调职申请单 维护任职历史失效日期
	 * @param bill
	 * @return
	 */
	public String updatePositionHistoryByBill(BdPositionHistoryBill bill);
	
	/**
	 * 根据调职申请单 写任职历史记录
	 * @param (PositionHistoryBill) bill
	 * @return
	 */
	public String addPositionHistoryByBill(BdPositionHistoryBill bill);
}
