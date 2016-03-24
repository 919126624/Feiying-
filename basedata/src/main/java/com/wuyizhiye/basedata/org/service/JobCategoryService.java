package com.wuyizhiye.basedata.org.service;
import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.JobCategory;

/**
 * @ClassName JobCategoryService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface JobCategoryService extends BaseService<JobCategory>{
	List<JobCategory> getJobCategoryList(Map<String,Object> param);
	/**
	 * 得到所有大类
	 * @return
	 */
	List<JobCategory> getParentJobCategoryList();
	/**
	 * 得到所有的子类  
	 * @return
	 */
	/*List<NoteCategory> getAllSubNoteCategoryList();*/
	/**
	 * 根据父id得到子类 
	 * @param parentId
	 * @return
	 */
/*	List<NoteCategory> getSubNoteCategoryList(String parentId);*/
}