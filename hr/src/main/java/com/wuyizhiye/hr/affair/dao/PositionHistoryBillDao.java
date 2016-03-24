package com.wuyizhiye.hr.affair.dao;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;

/**
 * 
 * @author ouyangyi
 * @since 2013-4-7 下午02:10:16
 */
public interface PositionHistoryBillDao extends BaseDao{
	
	public List<PositionHistoryBill> selectPositionHistoryBills(Map<String,Object> params);
	
	public void insertPositionHistoryBill(PositionHistoryBill pstHisBill);
	
	public void updatePositionHistoryBillSelective(PositionHistoryBill pstHisBill,PositionHistoryBill pstHisBillParam);
	
	public void deletePositionHistoryBillById(String personPositionHistoryId);
	
	public void deletePositionHistoryBillSelective(PositionHistoryBill pstHisBillParam);
	
	/**
	 * 根据主键修改调职申请对象(只修改不为空的属性)
	 * @param pstHisBill
	 */
	public void updateHistoryBillByPrimaryKeySelective(PositionHistoryBill pstHisBill);
}
