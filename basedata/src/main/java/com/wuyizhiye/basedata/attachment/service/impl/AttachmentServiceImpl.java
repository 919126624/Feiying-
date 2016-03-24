package com.wuyizhiye.basedata.attachment.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.basedata.attachment.dao.AttachmentDao;
import com.wuyizhiye.basedata.attachment.model.Attachment;
import com.wuyizhiye.basedata.attachment.service.AttachmentService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName AttachmentServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="attachmentService")
@Transactional
public class AttachmentServiceImpl extends DataEntityService<Attachment>
		implements AttachmentService {
	@Autowired
	private AttachmentDao attachmentDao;
	@Override
	protected AttachmentDao getDao() {
		return attachmentDao;
	}
	@Override
	public List<Attachment> selectByCondition(Map param) {
		
		return this.attachmentDao.selectByCondition(param);
	}


}
