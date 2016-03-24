package com.wuyizhiye.hr.attendance.model;

import java.util.Date;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * 考勤信息录入明细表
 * @author ouyangyi
 * @since 2013-5-16 下午02:20:15
 */
public class AttendanceDetail extends CoreEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4578243348382589349L;
	
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
	
	private Attendance parent ;				//月考勤数据
	private Date recordDate ;				//记录日期
	private String personId ;				//人员ID
	private String personNumber ;			//人员工号
	private String personName ;				//人员名称
	private String positionId ;				//职务ID
	private String positionName ;			//职务名册
	private String orgId ;					//组织ID
	private String orgName ;				//组织名称
	private String jobLevelId;			    //职级ID
	private String jobLevelName;	        //职级名称
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
	private String approvalState ;			//审核状态
	private String remark ;					//备注
	
	 
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
	public Attendance getParent() {
		return parent;
	}
	public void setParent(Attendance parent) {
		this.parent = parent;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	
}
