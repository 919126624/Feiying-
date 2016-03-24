package com.wuyizhiye.basedata.attachment.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.basedata.attachment.dao.AttachmentDao;
import com.wuyizhiye.basedata.attachment.model.Attachment;

/**
 * @ClassName AttachmentDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="attachmentDao")
public class AttachmentDaoImpl extends BaseDaoImpl implements AttachmentDao {

	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.attachment.dao.AttachmentDao";
	}

	@Override
	public <T> void updateEntitySelective(T entity) {
		getSqlSession().update(getNameSpace()+".updateSelective", entity);
		
	}

	@Override
	public <T> void updateBatchSelective(List<T> entities) {
		for(T entity : entities){
			updateEntitySelective(entity);
		}		
	}

	@Override
	public List<Attachment> selectByCondition(Map param) {
		
		return this.getSqlSession().selectList("com.wuyizhiye.basedata.attachment.dao.AttachmentDao.select", param);
	}
}
