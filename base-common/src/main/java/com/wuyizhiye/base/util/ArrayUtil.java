package com.wuyizhiye.base.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @ClassName ArrayUtil
 * @Description 数组操作工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class ArrayUtil {
	
	/**
	 * @Description 判断数组是否为空
	 * @param objs
	 * @return
	 */
	public static boolean isEmpty(Object[] objs) {
		if (null == objs || objs.length < 1)
			return true;
		return false;
	}

	/**
	 * @Description 判断集合是否为空
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Collection c) {
		if (null == c || c.size() < 1)
			return true;
		return false;
	}

	/**
	 * @Description 移除 集合中字段名为name并且字段的值为value一个元素
	 * @param c 集合对象
	 * @param fieldName 字段name
	 * @param fieldValue 值
	 * @return
	 */
	public static Object remove(Collection c, String fieldName,
			String fieldValue) {
		if (isEmpty(c)) {
			return null;
		}
		Iterator iter = c.iterator();
		Object itemObj = null;
		while (iter.hasNext()) {
			itemObj = iter.next();
			if (fieldValue.equals(ClassUtil.getFieldValue(itemObj, fieldName))) {
				iter.remove();
				return itemObj;
			}
		}
		return null;
	}

	/**
	 * @Description 
	 * @param list
	 * @param removeList
	 * @param fieldName
	 * @return
	 */
	public static Collection removeAll(Collection list, Collection removeList,
			String fieldName) {
		if (isEmpty(list) || isEmpty(removeList)) {
			return list;
		}

		Iterator iter = removeList.iterator();
		Object itemObj = null;
		Object value = null;
		while (iter.hasNext()) {
			itemObj = iter.next();
			value = ClassUtil.getFieldValue(itemObj, fieldName);
			remove(list, fieldName, value + "");
			if (isEmpty(list))
				break;
		}
		return list;
	}
}
