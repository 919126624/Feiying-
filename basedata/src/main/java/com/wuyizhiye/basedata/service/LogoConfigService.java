package com.wuyizhiye.basedata.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;
import com.wuyizhiye.basedata.shortcut.model.Shortcut;

/**
 * @ClassName LogoConfigService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface LogoConfigService extends BaseService<LogoConfig> {
	void addLogoConfig(LogoConfig logoConfig, List<Photo> photoList,List<Shortcut> shortcutList);
	
	/**
	 * 传入ID删除
	 * @param id
	 */
	void deleteLogoConfig(String id);
	
}
