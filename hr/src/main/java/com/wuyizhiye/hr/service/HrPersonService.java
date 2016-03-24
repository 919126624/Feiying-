/**
 * com.wuyizhiye.basedata.person.service.PersonService.java
 */
package com.wuyizhiye.hr.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.Education;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.model.WorkExperience;

/**
 * @author FengMy
 *
 * @since 2012-10-9
 */
public interface HrPersonService extends PersonService {
	void addPerson(Person person,List<PersonPosition> personPosition,List<RewardPunishment> rewardPunishmentList, 
			List<AgentCertificate> agentCertificateList,List<WorkExperience> workExperienceList, List<Education>educationList);
	void updatePerson(Person person,List<PersonPosition> personPosition,List<RewardPunishment> rewardPunishmentList, 
			List<AgentCertificate> agentCertificateList,List<WorkExperience> workExperienceList, List<Education>educationList);
	/**
	 * 删除人员
	 * @param personId
	 */
	void deletePerson(Person person);
	
	/**
	 * 查询人员
	 * @param cond
	 * @return
	 */
	public Map<String, Object> queryPersonByCond(Map<String, Object> cond);
	
	/**
	 * 导出excl
	 * @param cond
	 * @param os
	 * @throws Exception
	 */
	public void exportPersonByCond(Map<String, Object> cond,OutputStream os) throws Exception;
}
