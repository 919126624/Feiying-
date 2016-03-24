package com.wuyizhiye.hr.salary.model;
import com.wuyizhiye.base.CoreEntity;
/**
 *  薪酬标准 项目 明细
 * @author hly
 *
 */
public class WagesItem extends CoreEntity{
	
	private WagesTemplate wagesTemplate;//对应工资条模板
	
	private SalaryItem salaryItem;//对应 薪酬项目
    private int sortNumber;//排序号
	public WagesTemplate getWagesTemplate() {
		return wagesTemplate;
	}

	public void setWagesTemplate(WagesTemplate wagesTemplate) {
		this.wagesTemplate = wagesTemplate;
	}

	public SalaryItem getSalaryItem() {
		return salaryItem;
	}

	public void setSalaryItem(SalaryItem salaryItem) {
		this.salaryItem = salaryItem;
	}

	public int getSortNumber() {
		return sortNumber;
	}

	public void setSortNumber(int sortNumber) {
		this.sortNumber = sortNumber;
	}

	
}
