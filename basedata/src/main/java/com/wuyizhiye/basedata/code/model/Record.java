package com.wuyizhiye.basedata.code.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.org.model.Org;

/**
 * @ClassName Record
 * @Description 编码生成记录实体 
 * @author li.biao
 * @date 2015-4-2
 */
public class Record extends DataEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code; //编码号
	private Org codeOrg; //编码所属组织
	private String status; //编码状态
	private String sequences; //格式化的序列号
	private BillType type; //单据类型
	private Rules rules; //编码规则
	private int seqNumber; //序列号,递增
	private String reBuild; //重新生成编码(年,月,日)
	
	
	
	public String getReBuild() {
		return reBuild;
	}
	public void setReBuild(String reBuild) {
		this.reBuild = reBuild;
	}
	
	public String getSequences() {
		return sequences;
	}
	public int getSeqNumber() {
		return seqNumber;
	}
	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}
	public void setSequences(String sequences) {
		this.sequences = sequences;
	}
	public BillType getType() {
		return type;
	}
	public void setType(BillType type) {
		this.type = type;
	}
	public Rules getRules() {
		return rules;
	}
	public void setRules(Rules rules) {
		this.rules = rules;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Org getCodeOrg() {
		return codeOrg;
	}
	public void setCodeOrg(Org codeOrg) {
		this.codeOrg = codeOrg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
