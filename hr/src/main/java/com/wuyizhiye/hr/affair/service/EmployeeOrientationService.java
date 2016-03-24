/**
 * com.wuyizhiye.hr.service.EmployeeOrientationService.java
 */
package com.wuyizhiye.hr.affair.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.hr.affair.model.Education;
import com.wuyizhiye.hr.affair.model.EmployeeOrientation;
import com.wuyizhiye.hr.affair.model.WorkExperience;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.RewardPunishment;
/**
 * @author Cai.xing
 * 
 * @since 2013-04-02
 */
public interface EmployeeOrientationService extends BaseService<EmployeeOrientation> {
	void addEmployeeOrientation(EmployeeOrientation employeeOrientation,List<PersonPosition> personPosition,List<RewardPunishment> rewardPunishmentList, 
			List<AgentCertificate> agentCertificateList,List<WorkExperience> workExperienceList, List<Education>educationList);
	void updateEmployeeOrientation(EmployeeOrientation employeeOrientation,List<PersonPosition> personPosition,List<RewardPunishment> rewardPunishmentList, 
			List<AgentCertificate> agentCertificateList,List<WorkExperience> workExperienceList, List<Education>educationList);
	/**
	 * 删除记录
	 * @param personId
	 */
	void deleteEmployeeOrientation(EmployeeOrientation employeeOrientation);
	
	String personHasBillAppvol(String billId,String personId,String appStatu);
	
	/**
	 * 审核通过
	 * @param billId
	 * @return
	 */
	public Map<String,Object> approvalBill(String billId) throws Exception;
	
	/**
	 * 人员入职 定时器  写任职历史和修改人员信息
	 * @param billId
	 */
	@Transactional
	public void approveSchedule(String billId);
}
