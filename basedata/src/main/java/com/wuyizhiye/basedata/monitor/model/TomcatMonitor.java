package com.wuyizhiye.basedata.monitor.model;

import com.wuyizhiye.base.CoreEntity;

/**
 * @ClassName TomcatMonitor
 * @Description tomcat监控
 * @author li.biao
 * @date 2015-4-2
 */
public class TomcatMonitor extends CoreEntity {

	private static final long serialVersionUID = 233274218182293612L;

	public static final String MAPPER = "com.wuyizhiye.basedata.monitor.dao.TomcatMonitorDao" ;
	
	//应用名称
	private String tomcatName ;
	
	//已用内存
	private long totalMemory ;
	
	//空闲内存
	private long freeMemory ;
	
	//最大内存
	private long maxMemory ;
	
	//操作系统
	private String osName ;
	
	//总共session数
	private int totalSession ;
	
	//cpu使用率
	private double cpuRatio ;
	
	//内存使用率
	private double memoryRatio ;
	
	//当前用户数
	private int totalUser ;
	
	//tomcat路径
	private String tomcatPath ;
	
	//端口号
	private int port ;

	/**================*/
	private String pcOnline ;
	private String mobileOnline ;
	private boolean runRedis ;

	public boolean isRunRedis() {
		return runRedis;
	}

	public void setRunRedis(boolean runRedis) {
		this.runRedis = runRedis;
	}

	public String getTomcatName() {
		return tomcatName;
	}

	public void setTomcatName(String tomcatName) {
		this.tomcatName = tomcatName;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public int getTotalSession() {
		return totalSession;
	}

	public void setTotalSession(int totalSession) {
		this.totalSession = totalSession;
	}

	public double getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public double getMemoryRatio() {
		return memoryRatio;
	}

	public void setMemoryRatio(double memoryRatio) {
		this.memoryRatio = memoryRatio;
	}

	public int getTotalUser() {
		return totalUser;
	}

	public void setTotalUser(int totalUser) {
		this.totalUser = totalUser;
	}

	public String getTomcatPath() {
		return tomcatPath;
	}

	public void setTomcatPath(String tomcatPath) {
		this.tomcatPath = tomcatPath;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPcOnline() {
		return pcOnline;
	}

	public void setPcOnline(String pcOnline) {
		this.pcOnline = pcOnline;
	}

	public String getMobileOnline() {
		return mobileOnline;
	}

	public void setMobileOnline(String mobileOnline) {
		this.mobileOnline = mobileOnline;
	}
	
	
}
