package com.wuyizhiye.hr.affair.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.affair.dao.PositionHistoryBillDao;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;

@Component(value="positionHistoryBillDao")
public class PositionHistoryBillDaoImpl extends BaseDaoImpl implements PositionHistoryBillDao{

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.dao.PositionHistoryBillDao";
	}
	
	public List<PositionHistoryBill> selectPositionHistoryBills(Map<String,Object> params){
		return this.getSqlSession().selectList(getNameSpace()+".selectByExample", params);
	}
	
	public void insertPositionHistoryBill(PositionHistoryBill pstHisBill){
		this.getSqlSession().insert(getNameSpace()+".insert", pstHisBill);
	}
	
	public void updatePositionHistoryBillSelective(PositionHistoryBill pstHisBill,PositionHistoryBill pstHisBillParam){
		Map<String, PositionHistoryBill> params = new HashMap<String, PositionHistoryBill>();
		params.put("record", pstHisBill);
		params.put("paramObj", pstHisBillParam);
		this.getSqlSession().insert(getNameSpace()+".updateByExampleSelective", params);
	}
	
	public void updateHistoryBillByPrimaryKeySelective(PositionHistoryBill pstHisBill){
		this.getSqlSession().insert(getNameSpace()+".updateByPrimaryKeySelective", pstHisBill);
	}
	
	public void deletePositionHistoryBillById(String personPositionHistoryId){
		 
		this.getSqlSession().delete(getNameSpace()+".deleteByPrimaryKey", personPositionHistoryId);
	}
	
	public void deletePositionHistoryBillSelective(PositionHistoryBill pstHisBillParam){
		 
		this.getSqlSession().delete(getNameSpace()+".deleteByExample", pstHisBillParam);
	}

}
