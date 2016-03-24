package com.wuyizhiye.hr.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wuyizhiye.hr.salary.model.SalaryItem;


/**
 * 薪酬公式工具类
 * @author hlz
 * @since 2014-02-14
 */
public class SalaryFormulaUtil {
	public static String formula2script(String formula,List<SalaryItem> items){
		//首先将公式项排序，按公式项名称长度排序，由长至短,这样可以避免长项被短项先替换掉部份
		Collections.sort(items, new Comparator<SalaryItem>() {
			@Override
			public int compare(SalaryItem o1, SalaryItem o2) {
				if(o1.getName().length()>o2.getName().length()){
					return -1;
				}else if(o1.getName().length()<o2.getName().length()){
					return 1;
				}
				return 0;
			}
		});
		String result = formula;
		//循环公式项，将名称替换为编码
		for(SalaryItem item : items){
			result = result.replaceAll(item.getName(), item.getNumber());
		}
		return result;
	}
	
	/**
	 * 从脚本中抽取项目编码
	 * @param script
	 * @return
	 */
	public static String[] extractItem(String script){
		//标准公式项必须以FT+数字组合,数据源公式项必须以DS+数字组合
		Pattern pattern = Pattern.compile("(SI[0-9]+)|(DS[0-9]+)");
		Matcher matcher = pattern.matcher(script);
		List<String> itemNumbers = new ArrayList<String>();
		while(matcher.find()){
			String tmp = matcher.group();
			if(!itemNumbers.contains(tmp)){
				itemNumbers.add(tmp);
			}
		}
		return itemNumbers.toArray(new String[itemNumbers.size()]);
	}
	
	/**
	 * 根据标准公式生成js方法
	 * (FT01*0.2+FT02*10)/2  methodName
	 * ->
	 * function methodName(param){
	 *     var FT01 = parseFloat(param.get('FT01'));
	 *     var FT02 = parseFloat(param.get('FT02'));
	 *     return (FT01*0.2+FT02*10)/2;
	 * }
	 * @param formula
	 * @param functionName
	 * @return
	 */
	public static String script2function(String script,String functionName){
		String[] formulaItems = extractItem(script);
		StringBuilder function = new StringBuilder();
		function.append("function ").append(functionName).append("(param){\n");
		if(formulaItems!=null && formulaItems.length > 0){
			for(String item : formulaItems){
				function.append("\tvar ").append(item).append(" = parseFloat(param.get('").append(item).append("')==null?'0.0':param.get('").append(item).append("'));\n");
			}
		}
		function.append(script).append(";\n");
		if(formulaItems!=null && formulaItems.length > 0){
			for(String item : formulaItems){
				function.append("\t").append("param.put('").append(item).append("',").append(item).append(");\n");
			}
		}
		function.append("\n\treturn param;\n}");
		return function.toString();
	}
}
