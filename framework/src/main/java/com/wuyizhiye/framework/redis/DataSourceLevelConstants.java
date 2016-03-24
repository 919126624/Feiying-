package com.wuyizhiye.framework.redis;

import java.util.HashMap;
import java.util.Map;

import com.wuyizhiye.basedata.images.model.Photo;
import com.wuyizhiye.basedata.logoconfig.LogoConfig;

/**
 * 
 * @ClassName DataSourceLevelConstants
 * @Description 数据中心级别 应用权限 
 * @author li.biao
 * @date 2015-4-7
 */
public class DataSourceLevelConstants {
	public static class HEADER{
		public static String LOGO="LOGO-";
		public static String PERMISSIONMETHODS="WHOLE-PERMISSIONMETHODS-";
		
	}
	
	public static class LOGO{
		public static String LOGOCONFIG="logoConfig";
		public static String DOWNLOADURL="downLoadUrl";
		public static String LOGOPATH="logoPath";
		public static String LOGOPATH4LOGIN="logoPath4Login";
		public static String PHOTOLIST="photoList";
		public static Map<String,Class> LOGOMAP = new HashMap<String,Class>();
			static{
				LOGOMAP.put(LOGOCONFIG, LogoConfig.class);
				LOGOMAP.put(PHOTOLIST, Photo.class);
			}
	}
	
	public static class ALLPERMISSIONMAP{
		
	}
	
	
	
}
