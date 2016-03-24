package com.wuyizhiye.base.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @ClassName JScriptBuilder
 * @Description 构建脚本
 * @author li.biao
 * @date 2015-4-1
 */
public class JScriptBuilder {
	/**
	 * 根据中文脚本创建function
	 * 适用JScriptRunner.run(String method,Object... params) throws ScriptException, NoSuchMethodException
	 * @param script
	 * @param method
	 * @param variables
	 * @return
	 */
	public static String buildFunction(String script_cn, String method,
			Collection<JSVariable> variables) {
		StringBuilder script = new StringBuilder();
		script.append("function ").append(method).append("(param){\n");
		if (variables != null) {
			for (JSVariable var : variables) {
				script.append("\t").append(getVariableDefined(var, "param")).append("\n");
			}
		}
		script.append(transform(script_cn, variables));
		script.append("\n");
		if (variables != null) {
			for (JSVariable var : variables) {
				script.append("\t").append(putValue(var, "param")).append("\n");
			}
		}
		script.append("}");
		return script.toString();
	}
	
	/**
	 * 根据英文脚本构建空参数的function
	 * 适用JScriptRunner.run(String method,Map<String,Object> variables)
	 * @param script_en
	 * @param method
	 * @return
	 */
	public static String buildFunction(String script_en,String method){
		StringBuilder script = new StringBuilder();
		script.append("function ").append(method).append("(){\n");
		script.append(script_en);
		script.append("\n");
		script.append("}");
		return script.toString();
	}
	
	public static String transform(String script_cn,Collection<JSVariable> variables){
		List<JSVariable> vars = new ArrayList<JSVariable>(variables);
		Collections.sort(vars, new Comparator<JSVariable>() {
			@Override
			public int compare(JSVariable o1, JSVariable o2) {
				return new Integer(o2.getName().length()).compareTo(new Integer(o1.getName().length()));
			}
		});
		for(JSVariable var : vars){
			script_cn = script_cn.replaceAll(var.getName(), var.getNumber());
		}
		return script_cn;
	}
	
	private static String putValue(JSVariable var, String source){
		return source + ".put('" + var.getNumber() + "'," +  var.getNumber() + ");";
	}

	private static String getVariableDefined(JSVariable var, String source) {
		StringBuilder def = new StringBuilder();
		def.append("var ").append(var.getNumber());
		if (var.getType() == JSVariableTypeEnum.BOOLEAN) {
			def.append(" = ")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("')?")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("'):")
				.append("false;");
		}else if (var.getType() == JSVariableTypeEnum.FLOAT || var.getType() == JSVariableTypeEnum.NUMBER) {
			def.append(" = ")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("')?parseFloat(")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("')):")
				.append("0;");
		}else if (var.getType() == JSVariableTypeEnum.INT) {
			def.append(" = ")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("')?parseInt(")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("'),10):")
				.append("0;");
		}else if (var.getType() == JSVariableTypeEnum.STRING) {
			def.append(" = ")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("')?")
				.append(source)
				.append(".get('")
				.append(var.getNumber())
				.append("'):")
				.append("'';");
		}else{
			def.append(" = null;");
		}
		return def.toString();
	}
}
