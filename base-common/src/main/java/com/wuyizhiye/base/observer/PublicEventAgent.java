package com.wuyizhiye.base.observer;

/**
 * @ClassName PublicEventAgent
 * @Description 公共事件代理
 * @author li.biao
 * @date 2015-4-1
 */
public interface PublicEventAgent {
	/**
	 * 触发事件
	 * @param event
	 */
	void fireEvent(Event event);
	/**
	 * 订阅事件(同步)
	 * @param event 事件
	 */
	void subscribeEvent(Class<? extends Event> event,EventHandler handler);
	/**
	 * 订阅事件
	 * @param event 事件
	 * @param asynchronous 是否异步处理事件
	 */
	void subscribeEvent(Class<? extends Event> event,EventHandler handler,Boolean asynchronous);
	
}
