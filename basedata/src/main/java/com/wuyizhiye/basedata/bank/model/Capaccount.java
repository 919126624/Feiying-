package com.wuyizhiye.basedata.bank.model;

import java.util.Date;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.bank.enums.AccountTypeEnum;

/**
 * @ClassName Capaccount
 * @Description 资金账户
 * @author li.biao
 * @date 2015-4-2
 */
public class Capaccount extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountTypeEnum inAccountType; // 资金账户类型(内部)
	private Branch branch; // 银行网点
	private String account; // 账户
	private Integer isBranchAccount; // 是否分行账户 1是分行，0是总部
	private Date accountTime; // 开户时间
	private String remark; // 备注
	private Integer isEnable; // 是否启用
	private Integer isPaymentsRec; // 是否对外收付款
	private AccountTypeEnum outAccountType; // 资金账户类型(外部)
	
	/**
	 * 得到合并的名字
	 * @return
	 */
	public String getCombineName(){
		
		if(this.getAccount()!=null){
			return this.getBranch()==null?"":this.getBranch().getName()+"("+this.getName()+" "+this.getAccount()+")";
		}
		return "("+this.getName()+")";
		
	}

	public AccountTypeEnum getInAccountType() {
		return inAccountType;
	}

	public void setInAccountType(AccountTypeEnum inAccountType) {
		this.inAccountType = inAccountType;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getIsBranchAccount() {
		return isBranchAccount;
	}

	public void setIsBranchAccount(Integer isBranchAccount) {
		this.isBranchAccount = isBranchAccount;
	}

	public Date getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(Date accountTime) {
		this.accountTime = accountTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getIsPaymentsRec() {
		return isPaymentsRec;
	}

	public void setIsPaymentsRec(Integer isPaymentsRec) {
		this.isPaymentsRec = isPaymentsRec;
	}

	public AccountTypeEnum getOutAccountType() {
		return outAccountType;
	}

	public void setOutAccountType(AccountTypeEnum outAccountType) {
		this.outAccountType = outAccountType;
	}

}
