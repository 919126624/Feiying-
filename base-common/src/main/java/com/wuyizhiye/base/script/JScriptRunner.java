package com.wuyizhiye.base.script;

import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName JScriptRunner
 * @Description 描述:  脚本执行
 * @author li.biao
 * @date 2015-4-1
 */
public class JScriptRunner {
	private static final ScriptEngineManager SEM = new ScriptEngineManager();
	private Invocable invocable;
	private ScriptEngine se;
	public JScriptRunner(String script) throws ScriptException{
		if(StringUtils.isEmpty(script)){
			throw new RuntimeException("script is null");
		}
		this.se = SEM.getEngineByName("javascript");
		se.eval(script);
		invocable = (Invocable)se;
	}
	
	public Object run(String method,Object... params) throws ScriptException, NoSuchMethodException{
		return invocable.invokeFunction(method, params);
	}
	
	public Object run(String method,Map<String,Object> variables) throws ScriptException, NoSuchMethodException{
		Bindings bind = se.getBindings(ScriptContext.ENGINE_SCOPE);
		bind.clear();
		Set<String> keySet = variables.keySet();
		for(String key : keySet){
			bind.put(key, variables.get(key));
		}
		return invocable.invokeFunction(method, new Object[]{});
	}
}
