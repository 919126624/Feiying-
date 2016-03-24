package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.hr.enums.AttendanceStatusEnum;

/**
 * 考勤信息录入表 
 * @author ouyangyi
 * @since 2013-5-16 下午02:20:27
 */
public class Attendance extends CoreEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7084152330577000532L;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 创建用户
	 */
	private Person creator;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 最后更新用户
	 */
	private Person updator;
	
	private String period ;				    //考勤期间(年-月份)
	private Date   monthFirstDay ;			//考勤月 第一天
	private Date   monthLastDay ;			//考勤月 最后一天
	private String personId ;				//人员ID
	private String personNumber ;			//人员工号
	private String personName ;				//人员名称
	private String positionId ;				//职务ID
	private String positionName ;			//职务名册
	private String orgId ;					//组织ID
	private String orgName ;				//组织名称
	private String jobLevelId;			    //职级ID
	private String jobLevelName;	        //职级名称
	private double attendanceDays ;			//应出勤天数
	private double actualDay ;				//实际出勤天数
	private int late ;						//迟到次数
	private int leaveEarly ;				//早退次数
	private int skipWork ;					//旷工次数
	private double compassionateLeave ;		//事假（天）
	private double sickLeave ;				//病假（天）
	private double restDays ;				//休息（天）
	private double annualLeave ;			//年假（天）
	private double marriageLeave ;			//婚假（天）
	private double bereavementLeave ;		//丧假（天）
	private double IPPFLeave ;				//计生假（天）
	private double skipWorkLeave ;			//旷工（天）
	private double noEntry ;				//未入职天数
	private double otherHolidays ;			//其它假期(天)
	private double otherHolidays1 ;			//其它假期(天) --假期类型备用字段
	private double otherHolidays2 ;			//其它假期(天) --假期类型备用字段
	private double otherHolidays3 ;			//其它假期(天) --假期类型备用字段
	private double otherHolidays4 ;			//其它假期(天) --假期类型备用字段
	private double otherHolidays5 ;			//其它假期(天) --假期类型备用字段
	private String approvalState ;			//月审核状态
	private String approvalStateName ;		//月审核状态 显示名称
	private String approvalPersonId  ;		//审核人
	private String approvalPersonName ;		//审核人名称
	private Date approvalDate ;				//审核日期
	private String approvalRemark ;			//审核意见
	private String remark ;					//备注
	private Date lastEnterDate ;			//FLASTENTERDATE  录入的最大日期
	 
	private int ruleType;//规则类型（五天制、六天制、七天制)
	
	/**
	 * 数据所属CU
	 */
	private Org controlUnit;
	
	public Org getControlUnit() {
		return controlUnit;
	}
	public void setControlUnit(Org controlUnit) {
		this.controlUnit = controlUnit;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Person getCreator() {
		return creator;
	}
	public void setCreator(Person creator) {
		this.creator = creator;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Person getUpdator() {
		return updator;
	}
	public void setUpdator(Person updator) {
		this.updator = updator;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getJobLevelId() {
		return jobLevelId;
	}
	public void setJobLevelId(String jobLevelId) {
		this.jobLevelId = jobLevelId;
	}
	public String getJobLevelName() {
		return jobLevelName;
	}
	public void setJobLevelName(String jobLevelName) {
		this.jobLevelName = jobLevelName;
	}
	public double getAttendanceDays() {
		return attendanceDays;
	}
	public void setAttendanceDays(double attendanceDays) {
		this.attendanceDays = attendanceDays;
	}
	public double getActualDay() {
		return actualDay;
	}
	public void setActualDay(double actualDay) {
		this.actualDay = actualDay;
	}
	public int getLate() {
		return late;
	}
	public void setLate(int late) {
		this.late = late;
	}
	public int getLeaveEarly() {
		return leaveEarly;
	}
	public void setLeaveEarly(int leaveEarly) {
		this.leaveEarly = leaveEarly;
	}
	public int getSkipWork() {
		return skipWork;
	}
	public void setSkipWork(int skipWork) {
		this.skipWork = skipWork;
	}
	public double getCompassionateLeave() {
		return compassionateLeave;
	}
	public void setCompassionateLeave(double compassionateLeave) {
		this.compassionateLeave = compassionateLeave;
	}
	public double getSickLeave() {
		return sickLeave;
	}
	public void setSickLeave(double sickLeave) {
		this.sickLeave = sickLeave;
	}
	public double getRestDays() {
		return restDays;
	}
	public void setRestDays(double restDays) {
		this.restDays = restDays;
	}
	public double getAnnualLeave() {
		return annualLeave;
	}
	public void setAnnualLeave(double annualLeave) {
		this.annualLeave = annualLeave;
	}
	public double getMarriageLeave() {
		return marriageLeave;
	}
	public void setMarriageLeave(double marriageLeave) {
		this.marriageLeave = marriageLeave;
	}
	public double getBereavementLeave() {
		return bereavementLeave;
	}
	public void setBereavementLeave(double bereavementLeave) {
		this.bereavementLeave = bereavementLeave;
	}
	public double getIPPFLeave() {
		return IPPFLeave;
	}
	public void setIPPFLeave(double iPPFLeave) {
		IPPFLeave = iPPFLeave;
	}
	public double getSkipWorkLeave() {
		return skipWorkLeave;
	}
	public void setSkipWorkLeave(double skipWorkLeave) {
		this.skipWorkLeave = skipWorkLeave;
	}
	public double getNoEntry() {
		return noEntry;
	}
	public void setNoEntry(double noEntry) {
		this.noEntry = noEntry;
	}
	public double getOtherHolidays() {
		return otherHolidays;
	}
	public void setOtherHolidays(double otherHolidays) {
		this.otherHolidays = otherHolidays;
	}
	public String getApprovalState() {
		return approvalState;
	}
	public void setApprovalState(String approvalState) {
		this.approvalState = approvalState;
	}
	public String getApprovalStateName() {
		if(!StringUtils.isEmpty(approvalState)){
			return AttendanceStatusEnum.getEnumByValue(approvalState).getLabel();
		}
		return approvalStateName;
	}
	public void setApprovalStateName(String approvalStateName) {
		this.approvalStateName = approvalStateName;
	}
	public String getApprovalPersonId() {
		return approvalPersonId;
	}
	public void setApprovalPersonId(String approvalPersonId) {
		this.approvalPersonId = approvalPersonId;
	}
	public String getApprovalPersonName() {
		return approvalPersonName;
	}
	public void setApprovalPersonName(String approvalPersonName) {
		this.approvalPersonName = approvalPersonName;
	}
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	public String getApprovalRemark() {
		return approvalRemark;
	}
	public void setApprovalRemark(String approvalRemark) {
		this.approvalRemark = approvalRemark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getLastEnterDate() {
		return lastEnterDate;
	}
	public void setLastEnterDate(Date lastEnterDate) {
		this.lastEnterDate = lastEnterDate;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public Date getMonthFirstDay() {
		return monthFirstDay;
	}
	public void setMonthFirstDay(Date monthFirstDay) {
		this.monthFirstDay = monthFirstDay;
	}
	public Date getMonthLastDay() {
		return monthLastDay;
	}
	public void setMonthLastDay(Date monthLastDay) {
		this.monthLastDay = monthLastDay;
	}
	public double getOtherHolidays1() {
		return otherHolidays1;
	}
	public void setOtherHolidays1(double otherHolidays1) {
		this.otherHolidays1 = otherHolidays1;
	}
	public double getOtherHolidays2() {
		return otherHolidays2;
	}
	public void setOtherHolidays2(double otherHolidays2) {
		this.otherHolidays2 = otherHolidays2;
	}
	public double getOtherHolidays3() {
		return otherHolidays3;
	}
	public void setOtherHolidays3(double otherHolidays3) {
		this.otherHolidays3 = otherHolidays3;
	}
	public double getOtherHolidays4() {
		return otherHolidays4;
	}
	public void setOtherHolidays4(double otherHolidays4) {
		this.otherHolidays4 = otherHolidays4;
	}
	public double getOtherHolidays5() {
		return otherHolidays5;
	}
	public void setOtherHolidays5(double otherHolidays5) {
		this.otherHolidays5 = otherHolidays5;
	}
	public int getRuleType() {
		return ruleType;
	}
	public void setRuleType(int ruleType) {
		this.ruleType = ruleType;
	}
	
}
