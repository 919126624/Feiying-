package com.wuyizhiye.framework.util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.wuyizhiye.framework.listener.OnlineListener;

/**
 * @ClassName TaskLogoutService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Component("taskLogoutService")
public class TaskLogoutService {
	private static Log log = LogFactory.getLog(TaskLogoutService.class);

	// 定时注销在线人员
	public void logoutOnline() throws Exception {
		try {
			OnlineListener.inValidateAllUser();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("定时任务 清理在线人员logoutOnline：" + e.getMessage());
			throw new Exception(e);
		}
	}
}
