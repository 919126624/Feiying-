package com.wuyizhiye.base.timing.service.impl;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.timing.service.TimeTestService;

/**
 * @ClassName TimeTestServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="timeTestService")
public class TimeTestServiceImpl implements TimeTestService {

	@Override
	public void say() {
//		System.out.println("==========================");
//		System.out.println("hello world!");
//		System.out.println("==========================");
	}

	@Override
	public void say(String a) {
//		System.out.println("==========================");
//		System.out.println("hello world ," + a + "!");
//		System.out.println("==========================");
	}

	@Override
	public void say(String a, String b) {
//		System.out.println("==========================");
//		System.out.println(a + "say:hello ," + b +"!");
//		System.out.println("==========================");
	}

}
