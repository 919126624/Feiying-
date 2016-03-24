package com.wuyizhiye.base.datasource;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * @ClassName DbcpDataSource
 * @Description 数据源
 * @author li.biao
 * @date 2015-4-1
 */
public class DbcpDataSource extends BasicDataSource {
	@Override
	public void setUsername(String username) {
		username = EncryptionTools.decry(username);
		super.setUsername(username);
	}
	
	@Override
	public void setPassword(String password) {
		password = EncryptionTools.decry(password);
		super.setPassword(password);
	}
	
	@Override
	public synchronized void setUrl(String url) {
		url = EncryptionTools.decry(url);
		super.setUrl(url);
	}
}
