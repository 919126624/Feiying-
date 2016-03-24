package com.wuyizhiye.base.export;

import com.wuyizhiye.base.common.Pagination;

/**
 * @ClassName ExportDataSource
 * @Description 描述: 数据导出,数据源
 * @author li.biao
 * @date 2015-4-1
 * @param <T>
 */
public interface ExportDataSource<T>{
	
	/**
	 * @Description 获取数据
	 * @param pagination
	 * @return
	 */
	Pagination<T> getData(Pagination<T> pagination);
}
