package com.wuyizhiye.base.common;

import java.util.Map;

/**
 * @ClassName XmlResp
 * @Description 请求解析接口类
 * @author li.biao
 * @date 2015-4-1
 */
public interface XmlResp{
	
	/**
	 * @Title praseResp
	 * @Description 解析xml
	 * @param @param map
	 * @param @return
	 * @return XmlResp
	 * @throws
	 */
	public XmlResp praseResp(Map map);
}
