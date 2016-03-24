package com.wuyizhiye.base.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassName ObjectCopyUtils
 * @Description 对象拷贝工具类
 * @author li.biao
 * @date 2015-4-1
 */
public class ObjectCopyUtils {

	static Log log = LogFactory.getLog(ObjectCopyUtils.class);

	/**
	 * <p>Title: ObjectCopyUtils.java</p>  
	 * <p>Description: 构造函数</p>
	 */
	public ObjectCopyUtils() {

	}

	/**
	 * @Description 复制对象
	 * @param source
	 * @param dest
	 */
	public static void copy(Object source, Object dest) {

		Method s[] = source.getClass().getMethods();

		Method d[] = dest.getClass().getMethods();

		try {

			for (int i = 0; i < d.length; i++) {

				String dname = d[i].getName();

				String dformat = dname.substring(3);
				if (dname.startsWith("set")) {

					for (int j = 0; j < s.length; j++) {

						String sname = s[j].getName();

						String sformat = sname.substring(3);

						if

						(sname.startsWith("get") && sformat.equals(dformat)) {

							Object value = null;

							value = s[j].invoke(source);

							Object[] args = new Object[1];

							args[0] = value;

							d[i].invoke(dest, args);

						}

					}

				}

			}

		} catch (IllegalAccessException e) {
			log.debug(e.getMessage(), e);
			// TODO

		} catch (InvocationTargetException e) {

			// TODO

		}

	}

	public static void copyOnlyNull(Object source, Object dest) {

		Method s[] = source.getClass().getMethods();
		Method d[] = dest.getClass().getMethods();

		try {
			for (int i = 0; i < d.length; i++) {
				String dname = d[i].getName();
				String dformat = dname.substring(3);
				if (dname.startsWith("set")) {
					for (int m = 0; m < d.length; m++) {
						if (m == i) {
							continue;
						}
						String mname = d[m].getName();
						String mformat = mname.substring(3);
						if (mname.startsWith("get")
								&& mformat.equalsIgnoreCase(dformat)) {
							Object getVal = null;
							getVal = d[m].invoke(dest);
							if (getVal == null) {
								for (int j = 0; j < s.length; j++) {
									String sname = s[j].getName();
									String sformat = sname.substring(3);
									if (sname.startsWith("get")
											&& sformat.equals(dformat)) {
										Object value = null;
										value = s[j].invoke(source);
										Object[] args = new Object[1];
										args[0] = value;
										d[i].invoke(dest, args);
										break;
									}
								}
							}
							break;
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			log.debug(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.debug(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param oldObj
	 * @param newObj
	 * @param checkField
	 * @return
	 */
	public static Boolean objectCheckChange(Object oldObj, Object newObj,
			Vector checkField) {
		Method s[] = oldObj.getClass().getMethods();
		Method d[] = newObj.getClass().getMethods();
		Boolean ret = false;
		try {
			if (checkField == null) {
				for (int i = 0; i < s.length; i++) {
					String sName = s[i].getName();
					String sfName = s[i].getName().substring(3);
					if (sName.startsWith("get")) {
						Object sValue = s[i].invoke(oldObj);
						for (int j = 0; j < d.length; i++) {
							String dName = d[i].getName();
							String dfName = d[i].getName().substring(3);
							if (dName.startsWith("get")
									&& dfName.equalsIgnoreCase(sfName)) {
								Object dValue = d[i].invoke(newObj);
								if (!dValue.equals(sValue)) {
									return true;
								}
								break;
							}
						}
					}
				}
			} else {
				Iterator fIter = checkField.iterator();
				while (fIter.hasNext()) {
					String pName = (String) fIter.next();
					for (int i = 0; i < s.length; i++) {
						String sName = s[i].getName();
						String sfName = s[i].getName().substring(3);
						if (sName.startsWith("get")
								&& sfName.equalsIgnoreCase(pName)) {
							Object sValue = s[i].invoke(oldObj);
							for (int j = 0; j < d.length; i++) {
								String dName = d[i].getName();
								String dfName = d[i].getName().substring(3);
								if (dName.startsWith("get")
										&& dfName.equalsIgnoreCase(sfName)) {
									Object dValue = d[i].invoke(newObj);
									if (!dValue.equals(sValue)) {
										return true;
									}
									break;
								}
							}
							break;
						}
					}
				}
			}

		} catch (Exception ex) {

		}

		return ret;

	}

	public static void main(String[] args) {

	}

}
