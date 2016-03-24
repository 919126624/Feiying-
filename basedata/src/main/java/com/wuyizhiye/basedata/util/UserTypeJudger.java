package com.wuyizhiye.basedata.util;

import com.wuyizhiye.basedata.org.model.Position;
import com.wuyizhiye.basedata.person.model.Person;

/**
 * @ClassName UserTypeJudger
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface UserTypeJudger {
	boolean judge(Person person,Position position);
}
