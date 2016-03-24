package com.wuyizhiye.base.observer;

import javax.annotation.PostConstruct;

/**
 * @ClassName EventHandler
 * @Description 事件处理接口
 * @author li.biao
 * @date 2015-4-1
 */
public interface EventHandler {
	/**
	 * 订阅事件
	 */
	@PostConstruct
	void subscribeEvent();
	/**
	 * 事件处理
	 * @param event
	 */
	void handling(Event event);
}
