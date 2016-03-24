package com.wuyizhiye.basedata.org.service;

import java.util.List;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.model.Position;

/**
 * @ClassName PositionService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface PositionService extends BaseService<Position> {
	List<Position> getByOrg(String org);
	void savePositions(Org org, List<Position> positions);
}
