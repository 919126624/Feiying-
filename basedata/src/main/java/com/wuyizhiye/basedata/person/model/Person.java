package com.wuyizhiye.basedata.person.model;

import java.util.Date;
import java.util.List;

import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.enums.EducationTypeEnum;
import com.wuyizhiye.basedata.enums.PersonShowTypeEnum;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.RecruitMethodEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.WedStatusEnum;

/**
 * @ClassName Person
 * @Description TODO
 * @author li.biao
 * @date 2015-4-3
 */
public class Person extends DataEntity{
	private static final long serialVersionUID = -5002130462652639382L;
	public static final String MAPPER="com.wuyizhiye.basedata.person.dao.PersonDao";
	/**
	 * 曾用名
	 */
	private String oldName;
	/**
	 * 证件号码
	 */
	private String idCard;
	/**
	 * 证件类型
	 */
	private CardTypeEnum cardType;
	/**
	 * 出生日期
	 */
	private Date birthday;
	/**
	 * 性别
	 */
	private SexEnum sex;
	/**
	 * 电话(手机)
	 */
	private String phone;
	
	/**
	 * 工作电话(座机)
	 */
	private String workPhone;
	
	/**
	 * 短号
	 */
	private String shortNumber;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 紧急联系人
	 */
	private String crashContract;
	/**
	 * 紧急联系人电话
	 */
	private String contractPhone;
	/**
	 * 照片
	 */
	private String photo;
	/**
	 * 登录用户名
	 */
	private String userName;
	/**
	 * 登录密码
	 */
	private String password;
	
	/**
	 * 用户状态
	 */
	private UserStatusEnum status;
	
	/**
	 * 主要任职信息(冗余字段)
	 */
	private PersonPosition personPosition;
	
	/**
	 * 冗余组织信息(冗余字段)
	 */
	private Org org;
	
	/************** added by taking.wang start ********************/
	
	private String qq;
	/**
	 * 户籍
	 */
	private String household;
	/**
	 * 现居住地
	 */
	private String nowLivePlace;
	/**
	 * 籍贯
	 */
	private BasicData nativePlace;
	/**
	 * 家庭地址
	 */
	private String familyAddress;
	/**
	 * 家庭电话
	 */
	private String familyTel;
	/**
	 * 民族
	 */
	private BasicData folk;
	/**
	 * 婚姻情况
	 */
	private WedStatusEnum wedStatus;
	/**
	 * 工作年限
	 */
	private String workYear;
	/**
	 * 兴趣爱好
	 */
	private String interest;
	/**
	 * 招聘渠道
	 */
	private RecruitMethodEnum recruitmethod;
	/**
	 * 招聘备注
	 */
	private String recruitRemark;
	/**
	 * 介绍人
	 */
	private Person introducer;
	/**
	 * 与本人关系
	 */
	private String relation;
	/**
	 * 员工状态
	 */
	private PersonStatusEnum personStatus;
	
	/**
	 * 试用期 
	 */
	private Integer probationPeriod;
	/**
	 * 岗位状态
	 */
	private BasicData jobStatus;
	
	/**
	 * 最后离职日期
	 */
	private Date leaveDate;
	/**
	 * 最后入职日期
	 */
	private Date innerDate;
	/**
	 * 开户银行
	 */
	private String bank;
	/**
	 * 银行账号
	 */
	private String bankNumber;
	/**
	 * 社保电脑号
	 */
	private String computerNumber;
	/**
	 * 公积金账号
	 */
	private String fundNumber;
	/**
	 * 最高学历
	 */
	private EducationTypeEnum highestEducation;
	
	/**
	 * 个人说明
	 */
	private String personalRemark;
	
	//at人解析成list使用
	private List<Person> list;
	
	/**
	 * 关注数或粉丝数
	 */
	private int fansCount;
	
	//数据库中无此字段，冗余字段
	private String positionName;

	//职位，结算中查询人员时用到，数据库里无此字段
	private Position postion;
	//职位 因为上面的代码写错了，少带了个i，但是又怕之前的有有影响，所以不该改，就重新加了个冗余字段
	private Position position;

	//数据库中无此字段，冗余字段
	private String currentLoginIp;
	
	/**************** end *****************/
	private String billNumber;
	
	private String calendarType;//生日类型
	private String birthdayCn;//阳历 生日
	private String birthdayEn;//阴历 生日
	private PersonShowTypeEnum   showType; //控制人员信息再通讯录里面的显示类型
	
	private String oldOrgName;
	
	private String oldPosiName;
	
	
	//该字段不存在数据中,只是用于数据导出
	private String ppposorgname;//组织
	
	private String pposname;//职位
	
	private String ppjobname;//职级
	
	private String innerDate2;
	
	private String online; 
	
	 
	public PersonShowTypeEnum getShowType() {
		return showType;
	}

	public void setShowType(PersonShowTypeEnum showType) {
		this.showType = showType;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public String getInnerDate2() {
		return innerDate2;
	}

	public void setInnerDate2(String innerDate2) {
		this.innerDate2 = innerDate2;
	}

	public String getPpposorgname() {
		return ppposorgname;
	}

	public void setPpposorgname(String ppposorgname) {
		this.ppposorgname = ppposorgname;
	}

	public String getPposname() {
		return pposname;
	}

	public void setPposname(String pposname) {
		this.pposname = pposname;
	}

	public String getPpjobname() {
		return ppjobname;
	}

	public void setPpjobname(String ppjobname) {
		this.ppjobname = ppjobname;
	}

	public Person(){
		
	}

//	public String getJobStatusName() {
//		if(jobStatus!=null){
//			jobStatusName =  jobStatus.getLabel();
//		}
//		return jobStatusName;
//	}

//	public void setJobStatusName(String jobStatusName) {
//		this.jobStatusName = jobStatusName;
//	}

	public Person(String id){
		super.setId(id);
	}
	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getOldName() {
		return oldName;
	}
	public List<Person> getList() {
		return list;
	}
	public void setList(List<Person> list) {
		this.list = list;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	public CardTypeEnum getCardType() {
		return cardType;
	}

	public void setCardType(CardTypeEnum cardType) {
		this.cardType = cardType;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public SexEnum getSex() {
		return sex;
	}
	public void setSex(SexEnum sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getShortNumber() {
		return shortNumber;
	}
	public void setShortNumber(String shortNumber) {
		this.shortNumber = shortNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCrashContract() {
		return crashContract;
	}
	public void setCrashContract(String crashContract) {
		this.crashContract = crashContract;
	}
	public String getContractPhone() {
		return contractPhone;
	}
	public void setContractPhone(String contractPhone) {
		this.contractPhone = contractPhone;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setPersonPosition(PersonPosition personPosition) {
		this.personPosition = personPosition;
	}
	public PersonPosition getPersonPosition() {
		return personPosition;
	}
	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}
	public UserStatusEnum getStatus() {
		return status;
	}
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	
	public String getNowLivePlace() {
		return nowLivePlace;
	}
	public void setNowLivePlace(String nowLivePlace) {
		this.nowLivePlace = nowLivePlace;
	}
	
	public String getFamilyAddress() {
		return familyAddress;
	}
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
	public String getFamilyTel() {
		return familyTel;
	}
	public void setFamilyTel(String familyTel) {
		this.familyTel = familyTel;
	}
	
	public WedStatusEnum getWedStatus() {
		return wedStatus;
	}
	public void setWedStatus(WedStatusEnum wedStatus) {
		this.wedStatus = wedStatus;
	}
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public RecruitMethodEnum getRecruitmethod() {
		return recruitmethod;
	}
	public void setRecruitmethod(RecruitMethodEnum recruitmethod) {
		this.recruitmethod = recruitmethod;
	}
	public String getRecruitRemark() {
		return recruitRemark;
	}
	public void setRecruitRemark(String recruitRemark) {
		this.recruitRemark = recruitRemark;
	}
	public Person getIntroducer() {
		return introducer;
	}
	public void setIntroducer(Person introducer) {
		this.introducer = introducer;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public PersonStatusEnum getPersonStatus() {
		return personStatus;
	}
	public void setPersonStatus(PersonStatusEnum personStatus) {
		this.personStatus = personStatus;
	}
	public Integer getProbationPeriod() {
		return probationPeriod;
	}

	public void setProbationPeriod(Integer probationPeriod) {
		this.probationPeriod = probationPeriod;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public Date getInnerDate() {
		return innerDate;
	}
	public void setInnerDate(Date innerDate) {
		this.innerDate = innerDate;
	}
	public String getBankNumber() {
		return bankNumber;
	}
	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}
	public String getComputerNumber() {
		return computerNumber;
	}
	public void setComputerNumber(String computerNumber) {
		this.computerNumber = computerNumber;
	}
	public String getFundNumber() {
		return fundNumber;
	}
	public void setFundNumber(String fundNumber) {
		this.fundNumber = fundNumber;
	}
	public EducationTypeEnum getHighestEducation() {
		return highestEducation;
	}
	public void setHighestEducation(EducationTypeEnum highestEducation) {
		this.highestEducation = highestEducation;
	}
	public String getPersonalRemark() {
		return personalRemark;
	}
	public void setPersonalRemark(String personalRemark) {
		this.personalRemark = personalRemark;
	}

	public int getFansCount() {
		return fansCount;
	}
	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}
	public String getHousehold() {
		return household;
	}
	public void setHousehold(String household) {
		this.household = household;
	}
	
	
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public Position getPostion() {
		return postion;
	}
	public void setPostion(Position postion) {
		this.postion = postion;
	}
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	public String getPersonStatusName(){
		if(personStatus!=null){
			return personStatus.getLabel();
		}
		return "";
	}

	public BasicData getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(BasicData nativePlace) {
		this.nativePlace = nativePlace;
	}

	public BasicData getFolk() {
		return folk;
	}

	public void setFolk(BasicData folk) {
		this.folk = folk;
	}

	public BasicData getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(BasicData jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCurrentLoginIp() {
		return currentLoginIp;
	}

	public void setCurrentLoginIp(String currentLoginIp) {
		this.currentLoginIp = currentLoginIp;
	}

	public String getCalendarType() {
		if(StringUtils.isEmpty(calendarType)){
			return "solar";//默认阳历
		}
		return calendarType;
	}

	public void setCalendarType(String calendarType) {
		this.calendarType = calendarType;
	}

	public String getBirthdayCn() {
		return birthdayCn;
	}

	public void setBirthdayCn(String birthdayCn) {
		this.birthdayCn = birthdayCn;
	}

	public String getBirthdayEn() {
		return birthdayEn;
	}

	public void setBirthdayEn(String birthdayEn) {
		this.birthdayEn = birthdayEn;
	}

	public String getOldOrgName() {
		return oldOrgName;
	}

	public void setOldOrgName(String oldOrgName) {
		this.oldOrgName = oldOrgName;
	}

	public String getOldPosiName() {
		return oldPosiName;
	}

	public void setOldPosiName(String oldPosiName) {
		this.oldPosiName = oldPosiName;
	}
	
	public String getFolkName(){
		return null==folk?"":folk.getName();
	}
	public String getSexDesc(){
		return null==sex?"":sex.getName();
	}
	public String getNativePlaceName(){
		return null==nativePlace?"":nativePlace.getName();
	}
	public String getHighestEducationName(){
		return null==highestEducation?"":highestEducation.getLabel();
	}
	public String getWedStatusDesc(){
		return null==wedStatus?"":wedStatus.getLabel();
	}
	public String getRecruitmethodDesc(){
		return null==recruitmethod?"":recruitmethod.getLabel();
	}
	public String getJobStatusName(){
		return null==jobStatus?"":jobStatus.getName();
	}
}
