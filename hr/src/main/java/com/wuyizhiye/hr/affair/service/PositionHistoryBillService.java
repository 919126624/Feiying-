package com.wuyizhiye.hr.affair.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.hr.affair.model.PositionHistoryBill;

/**
 * 
 * @author ouyangyi
 * @since 2013-4-7 下午02:10:16
 */
public interface PositionHistoryBillService  extends BaseService<PositionHistoryBill> {
	
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
	
	/**
	 * 调职单审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approve(String[] billIds ,String approveType);
	
	/**
	 * 调职单(调职、降职、晋升)  单个审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String approveBill(String billId);
	
	/**
	 * 调职单(调职、降职、晋升)  反审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	@Transactional
	public String backApprove(String billId ,String approveType);
	
	/**
	 * 调职单(兼职)审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approvePartTimeJob(String[] billIds ,String approveType);
	
	/**
	 * 兼职 撤职  反审核
	 * @param billId
	 * @param approveType
	 * @return
	 */
	public String backApprovePartTimeJob(String billId ,String approveType);
	
	/**
	 * 跑盘员删除  审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approveDelRunDisk(String[] billIds ,String approveType);
	
	/**
	 * 跑盘员删除  流程审核
	 * @param billIds
	 * @param approveType
	 * @return
	 */
	public String approveDelRunDiskBill(String billId);
	
	public String addPositionHistoryByBill(PositionHistoryBill bill);
	
	/**
	 * 根据调职申请单 维护任职历史失效日期
	 * @param bill
	 * @return
	 */
	public String updatePositionHistoryByBill(PositionHistoryBill bill);
	
	/**
	 * 主要职位调动 定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	public void approveSchedule(String billId)throws Exception;
	
	/**
	 * 兼职   定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	public void increasePartTimeJobSchedule(String billId)throws Exception;
	
	/**
	 * 撤职   定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	public void dismissPartTimeJobSchedule(String billId)throws Exception;
	
	/**
	 * 跑盘员删除 定时器  写任职历史和修改人员职位信息
	 * @param billId
	 */
	@Transactional
	public void approveDelRunDiskSchedule(String billId)throws Exception;
}
