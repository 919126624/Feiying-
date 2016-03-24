package com.wuyizhiye.basedata.util;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

/**
 * @ClassName QuartUtils
 * @Description 定时调用通用类
 * @author li.biao
 * @date 2015-4-2
 */
public class QuartUtils {
	private static Logger logger =Logger.getLogger(QuartUtils.class); 
	/**
	 * 
	 * @param id 将ID做为不重复的任务标识
	 * @param cls 出发的类
	 * @param type 定时调用/周期循环调用  1定时/0周期循环调用
	 */
	public static void setTimmer(String id,Class cls,String cronExpression){
		try {
			// 创建一个Scheduler
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			// 创建一个JobDetail，指明name，groupname
			JobDetail jobDetail = new JobDetail("myJob"+ id, "myJobGroup"+ id, cls);
			jobDetail.getJobDataMap().put("type", "FULL");
			CronTrigger trigger = new CronTrigger("Trigger" + id,Scheduler.DEFAULT_GROUP);
			trigger.setCronExpression(cronExpression);
			sched.scheduleJob(jobDetail,trigger);
			sched.start(); //调度开始
		} catch (SchedulerException e) {
			logger.error("", e);
		} catch (ParseException e) {
			logger.error("", e);
		}
	}
	
	//终止前调用此方法,进行任务关闭
    public static void contextDestroyed(SchedulerFactory schedFact) {
    		try {
				Scheduler sched = schedFact.getScheduler();
				sched.shutdown();
			} catch (SchedulerException e) {
				logger.error("", e);
			}
    }
}
