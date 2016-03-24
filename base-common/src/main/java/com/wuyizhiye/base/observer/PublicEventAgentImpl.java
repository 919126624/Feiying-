package com.wuyizhiye.base.observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.spring.DataSourceHolder;

/**
 * @ClassName PublicEventAgentImpl
 * @Description 公共事件代理实现
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="eventAgent")
public class PublicEventAgentImpl implements PublicEventAgent {
	
	/**
	 * 每个事件最大启动的线程数(异步任务)
	 */
	private static final int MAX_ASYN = 5;
	
	/**
	 * 同步执行事件处理器
	 */
	private final Map<Class<? extends Event>,Vector<EventHandler>> eventHandlers = new HashMap<Class<? extends Event>, Vector<EventHandler>>();
	
	/**
	 * 异步执行事件处理器
	 */
	private final Map<Class<? extends Event>,Vector<EventHandler>> asynEventHandlers = new HashMap<Class<? extends Event>, Vector<EventHandler>>();
	
	@Override
	public void fireEvent(final Event event) {
		//事件为空，不处理
		if(event == null){
			return;
		}
		//先处理异步事件,避免异步事件延误
		Vector<EventHandler> asynHandlers = asynEventHandlers.get(event.getClass());
		final String dataSource = DataSourceHolder.getDataSource();
		if(asynHandlers != null && asynHandlers.size() > 0){
			final Vector<EventHandler> handlerVector = new Vector<EventHandler>(asynHandlers);
			for(int i = 0; i < MAX_ASYN; i++){
				Thread t = new Thread(){
					@Override
					public void run() {
						DataSourceHolder.setDataSource(dataSource);
						EventHandler handler;
						try{
							while(handlerVector.size()>0){
								handler = handlerVector.remove(0);
								if(handler!=null){
									handler.handling(event);
								}
							}
						}catch(ArrayIndexOutOfBoundsException e){
							//不处理
						}
					}
				};
				t.start();
			}
		}
		//处理同步事件
		if(eventHandlers.containsKey(event.getClass())){
			List<EventHandler> handlers = eventHandlers.get(event.getClass());
			for(EventHandler handler : handlers){
				handler.handling(event);
			}
		}
	}
	
	@Override
	public void subscribeEvent(Class<? extends Event> event,EventHandler handler) {
		//默认为同步事件
		subscribeEvent(event,handler, false);
	}

	@Override
	public void subscribeEvent(Class<? extends Event> event,EventHandler handler, Boolean asynchronous) {
		//不处理空事件和空事件处理器
		if(event == null || handler == null){
			return ;
		}
		if(asynchronous){
			Vector<EventHandler> asynHandlers;
			if(!asynEventHandlers.containsKey(event.getClass()) || asynEventHandlers.get(event.getClass()) == null){
				asynHandlers = new Vector<EventHandler>();
				asynEventHandlers.put(event, asynHandlers);
			}else{
				asynHandlers = asynEventHandlers.get(event.getClass());
			}
			asynHandlers.add(handler);
		}else{
			Vector<EventHandler> handlers;
			if(!eventHandlers.containsKey(event.getClass()) || eventHandlers.get(event.getClass()) == null){
				handlers = new Vector<EventHandler>();
				eventHandlers.put(event, handlers);
			}else{
				handlers = eventHandlers.get(event.getClass());
			}
			handlers.add(handler);
		}
	}
}
