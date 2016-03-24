package com.wuyizhiye.hr.affair.model;
import java.util.Date;
import java.util.List;

import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.enums.EducationTypeEnum;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.enums.RecruitMethodEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.basedata.person.enums.UserStatusEnum;
import com.wuyizhiye.basedata.person.enums.WedStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.person.model.PersonPosition;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.hr.enums.BillStatusEnum;

/**
 * 人员入职
 * @author Cai.xing
 * @since 2013-04-02
 */
public class EmployeeOrientation extends HrBillBase{

	private static final long serialVersionUID = 1L;
	private String billNumber;
	/**
	 *单据状态
	 */
	private BillStatusEnum billStatu;
	/**
	 * 组织
	 * */
	private Org mainPositionOrg;
	/**
	 * 主要任职信息
	 */
	private Position mainPosition;
	private PersonPosition personPosition;
	/**
	 * 职级
	 * */
	 private JobLevel mainJobLevel;
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
	/**
	 * 单据描述（用于审批流)
	 * */
	private String title;
	
	
	/**
	 * 创建人姓名
	 * */
	private String createName;
	/**
	 * 创建组织名
	 * */
	private String createOrgName;
	/**
	 * 创建人职位
	 * */
	private String createPositionName;
	
	//前三个月的业绩
	private String preFirstMonth;
	private String preTwoMonth;
	private String preThreeMonth;
	private String resultsNote;//业绩备注
	
	private Person referrer;//推荐人
	
	
	public Person getReferrer() {
		return referrer;
	}

	public void setReferrer(Person referrer) {
		this.referrer = referrer;
	}

	public String getResultsNote() {
		return resultsNote;
	}

	public void setResultsNote(String resultsNote) {
		this.resultsNote = resultsNote;
	}

	public String getPreFirstMonth() {
		return preFirstMonth;
	}

	public void setPreFirstMonth(String preFirstMonth) {
		this.preFirstMonth = preFirstMonth;
	}

	public String getPreTwoMonth() {
		return preTwoMonth;
	}

	public void setPreTwoMonth(String preTwoMonth) {
		this.preTwoMonth = preTwoMonth;
	}

	public String getPreThreeMonth() {
		return preThreeMonth;
	}

	public void setPreThreeMonth(String preThreeMonth) {
		this.preThreeMonth = preThreeMonth;
	}

	public String getCreateName() {
		return createName;
	}

	public CardTypeEnum getCardType() {
		return cardType;
	}

	public void setCardType(CardTypeEnum cardType) {
		this.cardType = cardType;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateOrgName() {
		return createOrgName;
	}

	public void setCreateOrgName(String createOrgName) {
		this.createOrgName = createOrgName;
	}

	public String getCreatePositionName() {
		return createPositionName;
	}

	public void setCreatePositionName(String createPositionName) {
		this.createPositionName = createPositionName;
	}

	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public JobLevel getMainJobLevel() {
		return mainJobLevel;
	}
	public void setMainJobLevel(JobLevel mainJobLevel) {
		this.mainJobLevel = mainJobLevel;
	}
	public Org getMainPositionOrg() {
		return mainPositionOrg;
	}
	public void setMainPositionOrg(Org mainPositionOrg) {
		this.mainPositionOrg = mainPositionOrg;
	}
	public Position getMainPosition() {
		return mainPosition;
	}
	public void setMainPosition(Position mainPosition) {
		this.mainPosition = mainPosition;
	}
	public BillStatusEnum getBillStatu() {
		return billStatu;
	}
	public void setBillStatu(BillStatusEnum billStatu) {
		this.billStatu = billStatu;
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
	
	public PersonPosition getPersonPosition() {
		return personPosition;
	}
	public void setPersonPosition(PersonPosition personPosition) {
		this.personPosition = personPosition;
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
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
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
	
	public String getBillStatuName(){
		if(billStatu!=null){
			if(billStatu.getValue().equals("SAVE")){
				return "<font class='modify_font'>未提交</font>";
			}else if(billStatu.getValue().equals("SUBMIT")){
				return "<font class='approve_font'>审批中</font>";
			}else if(billStatu.getValue().equals("APPROVAL")){
				return "<font class='approve_font'>审批中</font>";
			}else if(billStatu.getValue().equals("APPROVED")){
				return "<font class='approve_font01'>已审批</font>";
			}else if(billStatu.getValue().equals("REJECT")){
				return "<font class='delete_font'>已驳回</font>";
			}else if(billStatu.getValue().equals("REVOKE")){
				return "<font class='modify_font'>已撤销</font>";
			}else{
				return "未知类型";
			}
		}else{
			return "";
		}
	}
	public String getOption(){
		String loginId = SystemUtil.getCurrentUser().getId();
		if(billStatu!=null){
			if(billStatu.getValue().equals("SAVE")){
				return "<a class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}else if(billStatu.getValue().equals("SUBMIT")){
				if(loginId.equals(super.getCreator().getId())){
					return "<a class='modify_font' href=\"javascript:changeStatu('"+this.getId()+"','REVOKE' ,'撤销')\">撤销</a>";
				}else{
					return "<a class='approve_font01' href=\"javascript:approval('"+this.getId()+"')\" )\">审批</a> | <a class='delete_font' href=\"javascript:changeStatu('"+this.getId()+"','REJECT','驳回')\">驳回</a>";
					
				}
			}else if(billStatu.getValue().equals("APPROVAL")){
				if(loginId.equals(super.getCreator().getId())){
					return "<a class='modify_font' href=\"javascript:changeStatu('"+this.getId()+"' ,'REVOKE','撤销')\">撤销</a>";
				}else{
					return "<a class='approve_font01' href=\"javascript:approval('"+this.getId()+"')\" )\">审批</a> | <a class='delete_font' href=\"javascript:changeStatu('"+this.getId()+"','REJECT' ,'驳回')\">驳回</a>";
				}
			}else if(billStatu.getValue().equals("APPROVED")){
				return "";
			}else if(billStatu.getValue().equals("REJECT")){
				return "<a class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}else if(billStatu.getValue().equals("REVOKE")){
				return "<a  class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}else{
				return "<a class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}
		}else{
			return "";
		}
	}
}
