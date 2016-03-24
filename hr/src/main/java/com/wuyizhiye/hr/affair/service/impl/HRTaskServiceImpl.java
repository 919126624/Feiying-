/**
 * com.wuyizhiye.hr.service.impl.EmployeeOrientationServiceImpl.java
 */
package com.wuyizhiye.hr.affair.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.timing.TaskLastStatusEnum;
import com.wuyizhiye.base.timing.TaskStatusEnum;
import com.wuyizhiye.base.timing.model.Task;
import com.wuyizhiye.base.timing.model.TaskLog;
import com.wuyizhiye.base.timing.service.TaskLogService;
import com.wuyizhiye.base.timing.service.TaskService;
import com.wuyizhiye.base.timing.util.TimingTaskUtil;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.basedata.service.impl.DataEntityService;
import com.wuyizhiye.workflow.enums.BillStatusEnum;
import com.wuyizhiye.hr.affair.model.HrBillBase;
import com.wuyizhiye.hr.affair.service.HRTaskService;

/**
 * 人事流程定时任务管理
 * @author 孙海涛
 * @since 2014-11-18
 */
@Component(value="hrTaskService")
@Transactional
public class HRTaskServiceImpl extends DataEntityService<HrBillBase> implements HRTaskService {
	private Logger log = Logger.getLogger(HRTaskServiceImpl.class);
 
	@Autowired
	private TaskLogService taskLogService;
	@Autowired
	private TaskService taskService;
	
	public void execute(String taskId){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("innerDate", DateUtil.convertDateToStr(new Date()));
		param.put("isEffective", 0);
		param.put("billSta", BillStatusEnum.APPROVED.toString()); 
	    Task task=taskService.getEntityById(taskId);
		long start = System.currentTimeMillis();
		Map<String,String> hrItemMap=new HashMap<String, String>();
		
		/*T_HR_EMPLOYEERUNDISK_BILL  新增跑盘
		T_HR_POSITIONHISTORY_BILL  调职  兼职  撤职  删除跑盘  降职  晋升
		t_hr_Staffturnover         复职、离职、转正*/
		
		
		hrItemMap.put("com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.select", "employeeOrientationService.approveSchedule(billId)"); //查询未生效的且已审批通过的入职单据
		hrItemMap.put("com.wuyizhiye.hr.affair.dao.PositiveDao.select", "positiveService.approveSchedule(billId)"); //转正单据
		hrItemMap.put("com.wuyizhiye.hr.affair.dao.LeaveOfficeDao.select", "leaveOfficeService.approveSchedule(billId)"); //离职单据
		hrItemMap.put("com.wuyizhiye.hr.affair.dao.ReinstatementDao.select", "reinstatementService.approveSchedule(billId)"); //复职单据
		
		hrItemMap.put("com.wuyizhiye.hr.affair.dao.EmployeeRunDiskDao.getEmployeeRunDiskBills", "employeeRunDiskService.approveSchedule(billId)"); //新增跑盘
	 
		param.put("changeType", "'TRANSFER','DEMOTION','PROMOTION'");
		hrItemMap.put("com.wuyizhiye.hr.dao.PositionHistoryBillDao.seletToTask", "positionHistoryBillService.approveSchedule(billId)"); //调职、降职、晋升
		
		param.put("changeType", "'INCREASE_PARTTIMEJOB'");
		hrItemMap.put("com.wuyizhiye.hr.dao.PositionHistoryBillDao.seletToTask", "positionHistoryBillService.approveDelRunDiskSchedule(billId)"); //兼职
		
		param.put("changeType", "'DISMISS_PARTTIMEJOB'");
		hrItemMap.put("com.wuyizhiye.hr.dao.PositionHistoryBillDao.seletToTask", "positionHistoryBillService.dismissPartTimeJobSchedule(billId)"); //撤职
		
		param.put("changeType", "'DELRUNDISK'");
		hrItemMap.put("com.wuyizhiye.hr.dao.PositionHistoryBillDao.seletToTask", "positionHistoryBillService.approveDelRunDiskSchedule(billId)"); //删除跑盘
		
		
		for(String q:hrItemMap.keySet()){
			try{
				//查询未生效的且已审批通过的入职单据
				List<HrBillBase> billList=this.queryExecutor.execQuery(q, param, HrBillBase.class);
				if(billList!=null && billList.size()>0){
					for(HrBillBase h:billList){
						task.setTarget(hrItemMap.get(q)); 
						task.setParams(h.getId());
						this.runTask(task,h.getTitle());  
					}
				}  
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		/*try{
			//查询未生效的且已审批通过的入职单据
			List<HrBillBase> billList=this.queryExecutor.execQuery("com.wuyizhiye.hr.affair.dao.EmployeeOrientationDao.select", param, HrBillBase.class);
			if(billList!=null && billList.size()>0){
				for(HrBillBase h:billList){
					task.setTarget("employeeOrientationService.approveSchedule(billId)");
					task.setParams(h.getId());
					this.runTask(task,h.getTitle());  
				}
			}  
		}catch(Exception e){
			e.printStackTrace();
		}*/
		 
		
		task.setTarget("hrTaskService.execute(taskId)");
		task.setParams(taskId);
		task.setLastRunTime(new Date());
		task.setTimeConsuming(System.currentTimeMillis() - start);
		task.setTimes(task.getTimes()+1);
		task.setLastRunMsg(TaskLastStatusEnum.RUNED.getLabel());
		taskService.updateEntity(task);
	}
	
	@Transactional
	public void runTask(Task task,String msg){
		TaskLog taskLog = new TaskLog();
		taskLog.setCreateTime(new Date());
		taskLog.setTask(task);
		try {
			TimingTaskUtil.runTask(task); 
			taskLog.setInfo(msg+TaskStatusEnum.RUNED.getLabel());
		} catch (Exception e) {
			if("".equals(e.getMessage())){
				taskLog.setInfo(msg+"执行失败，无详细");
			}else{
				taskLog.setInfo(msg+"执行失败，详细："+e.getMessage());
			}
			log.error(e);
		}finally{
			taskLog.setUUID();
			taskLogService.addEntity(taskLog);
		}
	}
	
	 

	@Override
	protected BaseDao getDao() {
		// TODO Auto-generated method stub
		return null;
	}
}
