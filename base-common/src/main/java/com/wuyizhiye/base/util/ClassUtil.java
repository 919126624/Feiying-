package com.wuyizhiye.base.util;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

/**
 * @ClassName ClassUtil
 * @Description 类操作工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class ClassUtil {
	private static Logger logger=Logger.getLogger(ClassUtil.class);
	/**
	 * @Description 得到class的一个字段
	 * @param cls class
	 * @param fieldName 字段名
	 * @return
	 */
	public static Field getField(Class cls, String fieldName){
		if (cls == Object.class)
			return null;
		Field f = null;
		try {
			f = cls.getDeclaredField(fieldName);
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("", e);
		}
		if (null == f) {
			return getField(cls.getSuperclass(), fieldName);
		} else {
			f.setAccessible(true);
			return f;
		}
	}
	
	/**
	 * @Description 获取字段fieldName的值
	 * @param obj 对象
	 * @param fieldName 字段名
	 * @return
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Field f = getField(obj.getClass(), fieldName);
		if (null == f) {
			return null;
		}
		try {
			return f.get(obj);
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			logger.error("", e);
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			logger.error("", e);
		}
		return null ;
	}
	
	/**
	 * @Description 设置fieldName的值
	 * @param obj 对象
	 * @param fieldName 字段
	 * @param value 值
	 */
	public static void setFieldValue(Object obj,String fieldName,Object value){
		Field f = getField(obj.getClass(),  fieldName);
		if(null == f){
			return ;
		}
		try {
			f.set(obj, value);
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			logger.error("", e);
		} catch (IllegalAccessException e) {
//			e.printStackTrace();
			logger.error("", e);
		}
	}
	
	/**
	 * @Description 加载一个类的实例
	 * @param clsName 类
	 * @return
	 */
	public static Object newInstance(String clsName) {
		try {
			Class cls = Class.forName(clsName);
			return cls.newInstance();
		} catch (Exception e) {
//			e.printStackTrace();
			logger.error("", e);
		} 
		return null;
	}
	
	/**
	 * @Description 获取类路径
	 * @return
	 */
	public static String getClassPath(){
		return ClassUtil.class.getClassLoader().getResource("/").getPath();
	}
	
}
