package com.wuyizhiye.base.query;

import java.util.List;

/**
 * @ClassName QueryResult
 * @Description 查询结果
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public class QueryResult<T> {
	private List<T> result;
	private int totalCount;
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
