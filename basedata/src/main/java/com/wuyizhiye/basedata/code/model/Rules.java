package com.wuyizhiye.basedata.code.model;

import java.util.List;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.basic.model.BasicData;
import com.wuyizhiye.basedata.code.enums.PriorityEnum;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName Rules
 * @Description 自定义编码规则实体 T_CODE_RULES
 * @author li.biao
 * @date 2015-4-2
 */
public class Rules extends DataEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BasicData type; //单据类型
	private String example; //编号示例
	private String isInterrupt; //生成编号是否连接
	private PriorityEnum priority;  //优先级别
	private Org codeOrg; //编码所属组织
	private String isDisable; //是否禁用
	private String module; //类型 FKMODULEID	
	private List<RuleItems> ruleItems;  //规则明细
	private String reBuild; //重新生成编码(年,月,日)
	
	
	public String getReBuild() {
		return reBuild;
	}
	public void setReBuild(String reBuild) {
		this.reBuild = reBuild;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public List<RuleItems> getRuleItems() {
		return ruleItems;
	}
	public void setRuleItems(List<RuleItems> ruleItems) {
		this.ruleItems = ruleItems;
	}
	public BasicData getType() {
		return type;
	}
	public void setType(BasicData type) {
		this.type = type;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	public String getIsInterrupt() {
		return isInterrupt;
	}
	public void setIsInterrupt(String isInterrupt) {
		this.isInterrupt = isInterrupt;
	}
	public PriorityEnum getPriority() {
		return priority;
	}
	public void setPriority(PriorityEnum priority) {
		this.priority = priority;
	}
	public Org getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(Org codeOrg) {
		this.codeOrg = codeOrg;
	}
	public String getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(String isDisable) {
		this.isDisable = isDisable;
	}
}
