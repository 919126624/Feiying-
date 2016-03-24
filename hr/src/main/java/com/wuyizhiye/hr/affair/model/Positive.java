package com.wuyizhiye.hr.affair.model;

import java.util.Date;

import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.hr.enums.BillStatusEnum;
import com.wuyizhiye.hr.enums.PositionChangeTypeEnum;

/**
 * 人员转正
 * @author Cai.xing
 * @since 2013-04-08
 */
public class Positive extends HrBillBase{
	private static final long serialVersionUID = 1L;
	/**
	 * 单据状态
	 * */
	private BillStatusEnum billStatus;
	/**
	 * 异动类型
	 * */
	private PositionChangeTypeEnum changeType;
	/**
	 * 生效日期
	 * */
	private Date validateTime;
	/**
	 * 备注
	 * */
	private String remark;
	/**
	 * 单据描述（用于审批流)
	 * */
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

	private String title;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getValidateTime() {
		return validateTime;
	}
	public void setValidateTime(Date validateTime) {
		this.validateTime = validateTime;
	}
	public BillStatusEnum getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(BillStatusEnum billStatus) {
		this.billStatus = billStatus;
	}
	public PositionChangeTypeEnum getChangeType() {
		return changeType;
	}
	public void setChangeType(PositionChangeTypeEnum changeType) {
		this.changeType = changeType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBillStatuName(){
		if(billStatus!=null){
			if(billStatus.getValue().equals("SAVE")){
				return "<font class='modify_font'>未提交</font>";
			}else if(billStatus.getValue().equals("SUBMIT")){
				return "<font class='approve_font'>审批中</font>";
			}else if(billStatus.getValue().equals("APPROVAL")){
				return "<font class='approve_font'>审批中</font>";
			}else if(billStatus.getValue().equals("APPROVED")){
				return "<font class='approve_font01'>已审批</font>";
			}else if(billStatus.getValue().equals("REJECT")){
				return "<font class='delete_font'>已驳回</font>";
			}else if(billStatus.getValue().equals("REVOKE")){
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
		if(billStatus!=null){
			if(billStatus.getValue().equals("SAVE")){
				return "<a class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}else if(billStatus.getValue().equals("SUBMIT")){
				if(loginId.equals(super.getCreator().getId())){
					return "<a class='modify_font' href=\"javascript:changeStatu('"+this.getId()+"','REVOKE' ,'撤销')\">撤销</a>";
				}else{
					return "<a class='approve_font01' href=\"javascript:approval('"+this.getId()+"')\" )\">审批</a> | <a class='delete_font' href=\"javascript:changeStatu('"+this.getId()+"','REJECT','驳回')\">驳回</a>";
					
				}
			}else if(billStatus.getValue().equals("APPROVAL")){
				if(loginId.equals(super.getCreator().getId())){
					return "<a class='modify_font' href=\"javascript:changeStatu('"+this.getId()+"' ,'REVOKE','撤销')\">撤销</a>";
				}else{
					return "<a class='approve_font01' href=\"javascript:approval('"+this.getId()+"')\" )\">审批</a> | <a class='delete_font' href=\"javascript:changeStatu('"+this.getId()+"','REJECT' ,'驳回')\">驳回</a>";
				}
			}else if(billStatus.getValue().equals("APPROVED")){
				return "";
			}else if(billStatus.getValue().equals("REJECT")){
				return "<a class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}else if(billStatus.getValue().equals("REVOKE")){
				return "<a  class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}else{
				return "<a class='modify_font' href=\"javascript:editRow({id:'"+this.getId()+"'});\">编辑</a> | <a class='delete_font' href=\"javascript:deleteRow({id:'"+this.getId()+"'});\">删除</a>";
			}
		}else{
			return "";
		}
	}
}
