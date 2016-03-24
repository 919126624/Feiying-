package com.wuyizhiye.basedata.basic.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.basic.model.Mark;

/**
 * @ClassName MarkService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface MarkService extends BaseService<Mark> {

	String getMarkCode(String numberM);
	
	public void updateEntity2(Mark entity,List<Mark> marks);
}
