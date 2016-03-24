package com.wuyizhiye.basedata.attachment.service;

import java.util.List;
import java.util.Map;

import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.attachment.model.Attachment;

/**
 * @ClassName AttachmentService
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public interface AttachmentService extends BaseService<Attachment> {
	/**
	 * 按条件查找附件
	 * @param param
	 * @return
	 */
	List<Attachment> selectByCondition(Map param);
}
