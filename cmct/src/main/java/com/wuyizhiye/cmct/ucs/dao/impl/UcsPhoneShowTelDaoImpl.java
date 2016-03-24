package com.wuyizhiye.cmct.ucs.dao.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.cmct.ucs.dao.UcsPhoneShowTelDao;

/**
 * @ClassName UcsPhoneShowTelDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-5-26
 */
@Component(value="ucsPhoneShowTelDao")
public class UcsPhoneShowTelDaoImpl extends BaseDaoImpl implements UcsPhoneShowTelDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.cmct.ucs.dao.UcsPhoneShowTelDao";
	}
}
