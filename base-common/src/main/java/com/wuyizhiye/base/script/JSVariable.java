package com.wuyizhiye.base.script;

/**
 * @ClassName JSVariable
 * @Description JS变量
 * @author li.biao
 * @date 2015-4-1
 */
public interface JSVariable {

	/**
	 * 变量类型
	 * @return
	 */
	public JSVariableTypeEnum getType();

	/**
	 * 变量名称(中文)
	 * @return
	 */
	public String getName();

	/**
	 * 变量名称(英文)
	 * @return
	 */
	public String getNumber();

}
