package com.wuyizhiye.framework.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wuyizhiye.base.util.StringUtils;

/**
 * @ClassName BeanUtils
 * @Description Bean工具类,属性注入
 * @author li.biao
 * @date 2015-4-7
 */
public class BeanUtils {
	
	/**
	 * Map填充到Entity,根据C新建entity
	 * @param <T>
	 * @param param
	 * @param c
	 * @return
	 */
	public static <T> T fillentity(Map<String,String> param,T entity,Class<T> c){
		if(param==null || c==null){
			return null;
		}
		try {
			if(entity==null){
				entity = c.newInstance();
			}
			Map<String,Method> setter = getSetters(c);
			Map<String,Method> getter = getGetters(c);
			Set<String> keys = param.keySet();
			for(String key : keys){
				if(key.indexOf(".")>0){
					String[] ks = key.split("\\.");
					Map<String,Method> setterTmp = setter;
					Map<String,Method> getterTmp = getter;
					Class cTmp = c;
					Object entityTmp = entity;
					String kTmp;
					for(int i = 0; i < ks.length; i++){
						kTmp = ks[i];
						if(setterTmp.containsKey(kTmp)){
							if(i != ks.length - 1){
								if(getterTmp.get(kTmp).invoke(entityTmp, new Object[]{})==null){
									setValueToEntity(setterTmp.get(kTmp),getterTmp.get(kTmp),entityTmp,param.get(key));
								}
								entityTmp = getterTmp.get(kTmp).invoke(entityTmp, new Object[]{});
								getterTmp = getGetters(setterTmp.get(kTmp).getParameterTypes()[0]);
								setterTmp = getSetters(setterTmp.get(kTmp).getParameterTypes()[0]);
							}else{
								setValueToEntity(setterTmp.get(kTmp),getterTmp.get(kTmp),entityTmp,param.get(key));
							}
						}else{
							break;
						}
					}
				}else{
					if(setter.containsKey(key)){
						String value = param.get(key);
						setValueToEntity(setter.get(key),getter.get(key),entity,param.get(key));
					}
				}
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return entity;
	}
	
	private static void setValueToEntity(Method setter,Method getter,Object entity,String value) throws Exception{
		if(setter==null || getter==null || entity==null){
			return;
		}
		Class[] types = setter.getParameterTypes();
		String type = types[0].getName();
		if (value != null && !value.equals("") && !"[object Object]".equals(value)) {
	        //method.setAccessible(true);
	        if (type.equals("java.lang.String")) {
	        	setter.invoke(entity, new Object[] { value });
	        } else if(type.equals("byte") || type.equals("java.lang.Byte")){
	        	setter.invoke(entity, new Object[] { new Byte(value) });
	        } else if (type.equals("int") || type.equals("java.lang.Integer")) {
	        	setter.invoke(entity, new Object[] { new Integer(value) });
	        } else if (type.equals("long") || type.equals("java.lang.Long")) {
	        	setter.invoke(entity, new Object[] { new Long(value) });
	        } else if (type.equals("short") || type.equals("java.lang.Short")) {
	        	setter.invoke(entity, new Object[] { new Short(value) });
	        } else if (type.equals("float") || type.equals("java.lang.Float")) {
	        	setter.invoke(entity, new Object[] { new Float(value) });
	        } else if (type.equals("double") || type.equals("java.lang.Double")) {
	        	setter.invoke(entity, new Object[] { new Double(value) });
	        } else if(type.equals("java.math.BigDecimal")){
	        	setter.invoke(entity, new Object[] { new BigDecimal(value) });
	        } else if (type.equals("boolean") || type.equals("java.lang.Boolean")) {
	        	setter.invoke(entity, new Object[] { Boolean.valueOf(value) });
	        } else if (type.equals("java.util.Date")) {
		        setter.invoke(entity, new Object[] { parseDate(value) });
	        } else if(type.equals("java.sql.Date")){
	        	java.sql.Date date = new java.sql.Date(parseDate(value).getTime());
		        setter.invoke(entity, new Object[] { date });
	        } else if(types[0].isEnum()){
	        	setter.invoke(entity, new Object[] {Enum.valueOf(types[0], value)});
	        } else{
	        	//为自定义类型,为空时则实例化一个
	        	if(getter.invoke(entity, new Object[]{})==null){
		        	Object obj = types[0].newInstance();
		        	setter.invoke(entity, new Object[]{obj});
	        	}
	        }
		}else{
			setter.invoke(entity, new Object[]{null});
		}
	}
	
	private static Date parseDate(String value) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isEmpty(value)){
			return null;
		}
		if(value.contains(":")){
			return timeFormat.parse(value);
		}else{
			return dateFormat.parse(value);
		}
	}
	
	private static Map<String,Method> getSetters(Class c){
		Map<String,Method> setters = new HashMap<String, Method>();
		Method[] methods = c.getMethods();
		String methodName;
		for(Method m : methods){
			if(m.getName().startsWith("set") && m.getParameterTypes().length==1){
				methodName = m.getName().substring(3,4).toLowerCase()+m.getName().substring(4);
				setters.put(methodName, m);
			}
		}
		return setters;
	}
	
	private static Map<String,Method> getGetters(Class c){
		Map<String,Method> getters = new HashMap<String, Method>();
		Method[] methods = c.getMethods();
		String methodName;
		for(Method m : methods){
			if(m.getName().startsWith("get") && m.getParameterTypes().length==0){
				methodName = m.getName().substring(3,4).toLowerCase()+m.getName().substring(4);
				getters.put(methodName, m);
			}
		}
		return getters;
	}
}