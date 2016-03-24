/**
 * com.wuyizhiye.basedata.person.service.impl.PersonServiceImpl.java
 */
package com.wuyizhiye.hr.service.impl;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.service.impl.BaseServiceImpl;
import com.wuyizhiye.base.util.DateUtil;
import com.wuyizhiye.base.util.HttpClientUtil;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.WeixinOrg;
import com.wuyizhiye.basedata.person.dao.PersonDao;
import com.wuyizhiye.basedata.person.dao.PersonPositionDao;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.person.model.PersonPositionHistory;
import com.wuyizhiye.basedata.person.service.PersonService;
import com.wuyizhiye.basedata.person.service.impl.PersonServiceImpl;
import com.wuyizhiye.basedata.util.GenerateKey;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.hr.attendance.model.Attendance;
import com.wuyizhiye.hr.dao.AgentCertificateDao;
import com.wuyizhiye.hr.dao.EducationDao;
import com.wuyizhiye.hr.dao.RewardPunishmentDao;
import com.wuyizhiye.hr.dao.WorkExperienceDao;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;
import com.wuyizhiye.hr.model.AgentCertificate;
import com.wuyizhiye.hr.model.Education;
import com.wuyizhiye.hr.model.RewardPunishment;
import com.wuyizhiye.hr.model.WorkExperience;
import com.wuyizhiye.hr.service.HrPersonService;
import com.wuyizhiye.hr.utils.CommonExcelExportUtils;

/**
 * 职员service
 * 
 * @author FengMy
 * 
 * @since 2012-10-9
 */
@Component(value = "hrPersonService")
@Transactional
public class HrPersonServiceImpl extends PersonServiceImpl implements
		HrPersonService {
	@Autowired
	private PersonDao personDao;
	@Autowired
	private PersonPositionDao personPositionDao;
	@Autowired
	private RewardPunishmentDao rewardPunishmentDao;
	@Autowired
	private AgentCertificateDao agentCertificateDao;
	@Autowired
	private WorkExperienceDao workExperienceDao;
	@Autowired
	private EducationDao educationDao;
	@Autowired
	private PersonService personService;
	
	@Override
	protected BaseDao getDao() {
		return personDao;
	}

	@Override
	public void addPerson(Person person, List<PersonPosition> personPosition,
			List<RewardPunishment> rewardPunishmentList,
			List<AgentCertificate> agentCertificateList,
			List<WorkExperience> workExperienceList,
			List<Education> educationList) {
		super.addPerson(person, personPosition);
		this.addRewardPunishment(person, rewardPunishmentList);
		this.addAgentCertificate(person, agentCertificateList);
		this.addWorkExperience(person, workExperienceList);
		this.addEducation(person, educationList);
		
//		personService.synWeiXinPerson(person, personPosition, "add"); //微信同步
		
	}

	/**
	 * 增加奖惩记录 added by taking.wang
	 * 
	 * @param rewardPunishmentList
	 */
	private void addRewardPunishment(Person person,
			List<RewardPunishment> rewardPunishmentList) {
		if (rewardPunishmentList != null && rewardPunishmentList.size() > 0) {
			for (RewardPunishment rewardPunishment : rewardPunishmentList) {
				rewardPunishment.setPerson(person);
			}
			rewardPunishmentDao.addBatch(rewardPunishmentList);
		}
	}

	/**
	 * 增加经纪人证 added by taking.wang
	 * 
	 * @param rewardPunishmentList
	 */
	private void addAgentCertificate(Person person,
			List<AgentCertificate> agentCertificateList) {
		if (agentCertificateList != null && agentCertificateList.size() > 0) {
			for (AgentCertificate agentCertificate : agentCertificateList) {
				agentCertificate.setPerson(person);
			}
			agentCertificateDao.addBatch(agentCertificateList);
		}
	}

	/**
	 * 增加工作经历 added by taking.wang
	 * 
	 * @param rewardPunishmentList
	 */
	private void addWorkExperience(Person person,
			List<WorkExperience> workExperienceList) {
		if (workExperienceList != null && workExperienceList.size() > 0) {
			for (WorkExperience workExperience : workExperienceList) {
				workExperience.setPerson(person);
			}
			workExperienceDao.addBatch(workExperienceList);
		}
	}

	/**
	 * 增加教育经历 add by taking.wang
	 * 
	 * @param educationList
	 */
	private void addEducation(Person person, List<Education> educationList) {
		if (educationList != null && educationList.size() > 0) {
			for (Education education : educationList) {
				education.setPerson(person);
			}
			educationDao.addBatch(educationList);
		}
	}

	@Override
	public void updatePerson(Person person,
			List<PersonPosition> personPosition,
			List<RewardPunishment> rewardPunishmentList,
			List<AgentCertificate> agentCertificateList,
			List<WorkExperience> workExperienceList,
			List<Education> educationList) {

		super.updatePerson(person, personPosition);

		// 先删除职位，工作经历，教育经历，奖惩记录，经纪人证，那些，然后再插入值
		this.workExperienceDao.deleteByPersonId(person.getId());
		this.educationDao.deleteByPersonId(person.getId());
		this.rewardPunishmentDao.deleteByPersonId(person.getId());
		this.agentCertificateDao.deleteByPersonId(person.getId());

		this.addRewardPunishment(person, rewardPunishmentList);
		this.addAgentCertificate(person, agentCertificateList);
		this.addWorkExperience(person, workExperienceList);
		this.addEducation(person, educationList);
		
		 
//	    personService.synWeiXinPerson(person, personPosition, "update"); //微信同步

	}

	@Override
	public void deletePerson(Person person) {
		// TODO Auto-generated method stub

		// 先删除职位，工作经历，教育经历，奖惩记录，经纪人证，那些，然后再插入值
		this.workExperienceDao.deleteByPersonId(person.getId());
		this.educationDao.deleteByPersonId(person.getId());
		this.rewardPunishmentDao.deleteByPersonId(person.getId());
		this.agentCertificateDao.deleteByPersonId(person.getId());

		// 保存员工编号
		GenerateKey.updateKeyCode(person.getNumber(), null, "YGBH");

		// 删人员
		super.deleteEntity(person);
		
		/**
		 * 将用户同步到微信企业号 begin
		 */
	    try{
	    	
	    	String strJson = "{";
	        strJson += "\"userid\":\""+person.getNumber()+"\"";
	        strJson += "}";
		    HttpClientUtil.callHttpUrl(SystemUtil.getBase()+"/weixinapi/weixinperson?t=del", URLEncoder.encode(strJson,"utf-8"));
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
	    /**
		 * 将用户同步到微信企业号 end
		 */	
	}

	@Override
	public Map<String, Object> queryPersonByCond(Map<String, Object> cond) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Person> personList = queryExecutor
				.execQuery(
						"com.wuyizhiye.basedata.person.dao.PersonDao.getPersonInfoByOrgLongNumber",
						cond, Person.class);
		result.put("personList", personList);
		result.put("MSG", "SUCC");
		return result;
	}

	@Override
	public void exportPersonByCond(Map<String, Object> cond, OutputStream os)
			throws Exception {
		String[] excelHeader = new String[] { "工号","名称","组织","职位","职级","手机号","入职日期","身份证"};
		String[] excelField = new String[] { "number","name","ppposorgname","pposname","ppjobname","phone","innerDate2","idCard"};
		Map<String, Object> resultMap = queryPersonByCond(cond);
		List<Person> personList = new ArrayList<Person>();
		if ("SUCC".equals(resultMap.get("MSG"))) {
			personList = (List<Person>)resultMap.get("personList");
		} else {
			os.write("没有可导出的人员信息".getBytes());
		}
		CommonExcelExportUtils.exportExcelByDataList(personList,
				excelHeader, excelField, os, "导出人员", true);

	}
}
