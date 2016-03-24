package com.wuyizhiye.hr.affair.model;
import java.util.Date;

import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.enums.CardTypeEnum;
import com.wuyizhiye.basedata.person.enums.SexEnum;
import com.wuyizhiye.hr.enums.BillStatusEnum;

/**
 * 人员入职
 * @author Cai.xing
 * @since 2013-04-02
 */
public class EmployeeRunDiskBill extends HrBillBase{

	private static final long serialVersionUID = 1L;
	
	private String billNumber;//单据编号
	
	private String changeType;//异动类型
	
	/**
	 *单据状态
	 */
	private BillStatusEnum billStatus;
	/**
	 * 组织
	 * */
	private Org mainPositionOrg;
	/**
	 * 主要任职信息
	 */
	private Position mainPosition;
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
	
	private Date effectdate;//生效日期
	    
	private String title;//单据描述（用于审批流)
	
	
	
	
	public CardTypeEnum getCardType() {
		return cardType;
	}
	public void setCardType(CardTypeEnum cardType) {
		this.cardType = cardType;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
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
	public BillStatusEnum getBillStatus() {
		return billStatus;
	}
	public String getBillStatuName(){
		if(billStatus!=null){
			if(billStatus.getValue().equals("SAVE")){
				return "已保存";
			}else if(billStatus.getValue().equals("SUBMIT")){
				return "审批中";
			}else if(billStatus.getValue().equals("APPROVAL")){
				return "审批中";
			}else if(billStatus.getValue().equals("APPROVED")){
				return "审批通过";
			}else if(billStatus.getValue().equals("REJECT")){
				return "已驳回";
			}else if(billStatus.getValue().equals("REVOKE")){
				return "已撤销";
			}else{
				return "未知类型";
			}
		}else{
			return "";
		}
	}
	public String getOldName() {
		return oldName;
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
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
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
	public Date getEffectdate() {
		return effectdate;
	}
	public void setEffectdate(Date effectdate) {
		this.effectdate = effectdate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setBillStatus(BillStatusEnum billStatus) {
		this.billStatus = billStatus;
	}
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
}
