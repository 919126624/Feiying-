package com.wuyizhiye.basedata.person.model;

import com.wuyizhiye.basedata.BillEntity;
import com.wuyizhiye.basedata.org.model.JobLevel;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName BdBillBase
 * @Description 人事单据基类
 * @author li.biao
 * @date 2015-4-3
 */
public class BdBillBase extends BillEntity{
	private static final long serialVersionUID = 1L;
	/**
	 * 申请人
	 */
	private Person applyPerson;
	/**
	 * 申请人姓名
	 */
	private String applyPersonName;
	/**
	 * 申请人组织
	 */
	private Org applyOrg;
	/**
	 * 申请变动组织
	 */
	private Org applyChangeOrg;
	/**
	 * 申请人职位
	 */
	private Position applyPosition;
	/**
	 * 申请变动职位
	 */
	private Position applyChangePosition;
	
	/**
	 * 申请人职级
	 */
	private JobLevel applyJoblevel;
	/**
	 * 申请变动职级
	 */
	private JobLevel applyChangeJoblevel;
	
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
	
	public String getCreateName() {
		return createName;
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
	
	public Person getApplyPerson() {
		return applyPerson;
	}
	public void setApplyPerson(Person applyPerson) {
		this.applyPerson = applyPerson;
	}
	public String getApplyPersonName() {
		return applyPersonName;
	}
	public void setApplyPersonName(String applyPersonName) {
		this.applyPersonName = applyPersonName;
	}
	public Org getApplyOrg() {
		return applyOrg;
	}
	public void setApplyOrg(Org applyOrg) {
		this.applyOrg = applyOrg;
	}
	public Org getApplyChangeOrg() {
		return applyChangeOrg;
	}
	public void setApplyChangeOrg(Org applyChangeOrg) {
		this.applyChangeOrg = applyChangeOrg;
	}
	public Position getApplyPosition() {
		return applyPosition;
	}
	public void setApplyPosition(Position applyPosition) {
		this.applyPosition = applyPosition;
	}
	public Position getApplyChangePosition() {
		return applyChangePosition;
	}
	public void setApplyChangePosition(Position applyChangePosition) {
		this.applyChangePosition = applyChangePosition;
	}
	public JobLevel getApplyJoblevel() {
		return applyJoblevel;
	}
	public void setApplyJoblevel(JobLevel applyJoblevel) {
		this.applyJoblevel = applyJoblevel;
	}
	public JobLevel getApplyChangeJoblevel() {
		return applyChangeJoblevel;
	}
	public void setApplyChangeJoblevel(JobLevel applyChangeJoblevel) {
		this.applyChangeJoblevel = applyChangeJoblevel;
	}
	
}
