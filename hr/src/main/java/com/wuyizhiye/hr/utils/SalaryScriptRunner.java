/**
 * 
 */
package com.wuyizhiye.hr.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

/**
 * 薪酬公式执行工具
 * 
 */
public class SalaryScriptRunner {
	private Logger log = Logger.getLogger(SalaryScriptRunner.class);
	private Invocable inv;
	private static final String FUNCTIONNAME = "cal";
	
	/**
	 * @param scripts,itemNumber:script
	 */
	public SalaryScriptRunner(Map<String,String> scripts){
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		inv = (Invocable) se;
		String script = initScript(scripts);
		try {
			se.eval(script);
		} catch (ScriptException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 初始化执行脚本
	 * @param scripts
	 * @return
	 */
	private String initScript(Map<String,String> scripts) {
		Set<String> items = new HashSet<String>();//所有的计算项
		Map<String,Set<String>> keyItems = new HashMap<String, Set<String>>();//公式中依赖的计算项
		//解析所有计算项
		Set<String> keys = scripts.keySet();
		for(String key : keys){
			String[] tmp = SalaryFormulaUtil.extractItem(scripts.get(key));
			if(!keyItems.containsKey(key)){
				keyItems.put(key, new HashSet<String>());
			}
			for(String t : tmp){
				items.add(t);
				if(!t.equals(key)){
					keyItems.get(key).add(t);
				}
			}
		}
		validateScripts(keys, keyItems);
		List<String> scriptList = new ArrayList<String>(scripts.keySet());
		List<String> resultList = new ArrayList<String>();
		//将无依赖公式置前
		Iterator<String> iterator = scriptList.iterator();
		while(iterator.hasNext()){
			String s = iterator.next();
			Set<String> o2Set = keyItems.get(s);
			boolean flag = true;
			for(String st : o2Set){
				if(keys.contains(st)){
					flag = false;
					break;
				}
			}
			if(flag){
				resultList.add(s);
				iterator.remove();
			}
		}
		//剩余有依赖的进行排序
		for(int i = 0; i < scriptList.size()-1; i++){
			for(int j = i+1; j < scriptList.size();j++){
				if(keyItems.get(scriptList.get(i)).contains(scriptList.get(j))){
					if(keyItems.get(scriptList.get(j)).contains(scriptList.get(i))){
						//这里存在相互依赖
						throw new RuntimeException("存在相互依赖的公式:"+scripts.get(scriptList.get(i))+"和"+scripts.get(scriptList.get(j)));
					}
					for(int k = j;k>i;k--){
						String s = scriptList.get(k);
						scriptList.set(k, scriptList.get(k-1));
						scriptList.set(k-1, s);
					}
					i--;
					break;
				}
			}
		}
		resultList.addAll(scriptList);
		return buildScript(items, scripts, resultList);
	}
	
	/**
	 * 校验脚本集合，主要校验循环依赖的脚本
	 * @param keys
	 * @param keyItems
	 */
	private void validateScripts(Set<String> keys,Map<String,Set<String>> keyItems){
		for(String key : keys){
			List<String> keyList = new ArrayList<String>();
			keyList.add(key);
			checkCircular(key,keyList, keyItems);
		}
	}
	
	/**
	 * 检查公式依赖
	 * @param key
	 * @param keyList
	 * @param keyItems
	 */
	private void checkCircular(String key,List<String> keyList,Map<String,Set<String>> keyItems){
		Set<String> keys = keyItems.keySet();
		Set<String> items = keyItems.get(key);
		for(String item : items){
			if(keys.contains(item)){
				if(keyList.contains(item)){
					throw new RuntimeException("There is a circular dependency.存在着循环依赖公式."+key+"到"+item);
				}
				List<String> list = new ArrayList<String>(keyList);
				list.add(item);
				checkCircular(item, list, keyItems);
			}
		}
	}
	
	/**
	 * 生成可执行js脚本，以function的方式
	 * @param items
	 * @param scripts
	 * @param resultList
	 * @return
	 */
	private String buildScript(Set<String> items,Map<String,String> scripts,List<String> resultList){
		StringBuilder builder = new StringBuilder();
		builder.append("function ").append(FUNCTIONNAME).append("(param){\n");
		for(String item : items){
			builder.append("\tvar ").append(item).append(" = parseFloat(param.get('").append(item).append("')==null?'0.0':param.get('").append(item).append("'));\n");
		}
		for(String sc : resultList){
			builder.append("\t").append(scripts.get(sc)).append("\n");
		}
		for(String item : items){
			builder.append("\t").append("param.put('").append(item).append("',").append(item).append(");\n");
		}
		builder.append("\n}");
		log.debug(builder.toString());
		return builder.toString();
	}
	
	/**
	 * 运行
	 * @param data核算数据
	 * @param script脚本集合，按顺序执行
	 */
	public void run(Map<String, BigDecimal> salaryData){
		try {
			//log.debug(data.getPerson().getName()+":"+data.getSalaryData());
			inv.invokeFunction(FUNCTIONNAME, new Object[]{salaryData});
			//计算完后，数据为Double,需要转换成Bigdecimal
			Map<String,BigDecimal> d =salaryData;
			Set<String> keys = d.keySet();
			for(String key : keys){
				Object obj = d.get(key);
				d.put(key, new BigDecimal(obj.toString()));
			}
			log.debug(d);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		builder.append("function ").append(FUNCTIONNAME).append("(param){\n");
		builder.append("\tvar ").append("p1").append(" = parseFloat(param.get('").append("p1").append("')==null?'0.0':param.get('").append("p1").append("'));\n");
		builder.append("\tvar ").append("p2").append(" = parseFloat(param.get('").append("p2").append("')==null?'0.0':param.get('").append("p2").append("'));\n");
		builder.append("\t").append("param.put('").append("p1").append("',").append("p1 + p2").append(");\n");
		builder.append("\t").append("param.put('").append("p2").append("',").append("p2").append(");\n");
		builder.append("\n}");
		
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		Invocable inv = (Invocable) se;
		String script = builder.toString();
		try {
			se.eval(script);
			Map<String, BigDecimal> param = new HashMap<String, BigDecimal>();
			param.put("p1", new BigDecimal("3"));
			param.put("p2", new BigDecimal("5"));
			inv.invokeFunction(FUNCTIONNAME, new Object[]{param});
			Map<String,BigDecimal> d = param;
			Set<String> keys = d.keySet();
			for(String key : keys){
				Object obj = d.get(key);
				d.put(key, new BigDecimal(obj.toString()));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
