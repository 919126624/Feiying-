/**
 * com.wuyizhiye.hr.salary.dao.impl.WagesTemplateDaoImpl.java
 */
package com.wuyizhiye.hr.salary.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.hr.salary.dao.WagesTemplateDao;

/**
 * @author hyl
 * 
 * @since 2014-02-12
 */
@Component(value="wagesTemplateDao")
public class WagesTemplateDaoImpl extends BaseDaoImpl implements WagesTemplateDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.hr.salary.dao.WagesTemplateDao";
	}
}
