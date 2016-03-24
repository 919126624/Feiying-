package com.wuyizhiye.basedata.org.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wuyizhiye.base.dao.BaseDao;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.org.dao.PostDao;
import com.wuyizhiye.basedata.org.dao.PostLevelDao;
import com.wuyizhiye.basedata.org.model.Post;
import com.wuyizhiye.basedata.org.model.PostLevel;
import com.wuyizhiye.basedata.org.service.PostLevelService;
import com.wuyizhiye.basedata.org.service.PostService;
import com.wuyizhiye.basedata.service.impl.DataEntityService;

/**
 * @ClassName PostServiceImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="postService")
@Transactional
public class PostServiceImpl extends DataEntityService<Post> implements PostService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private PostLevelService postLevelService;
	@Autowired
	private PostLevelDao postLevelDao;
	@Override
	protected BaseDao getDao() {
		return postDao;
	}
	
	@Override
	public void addEntity(Post entity) {
		super.addEntity(entity);
		List<PostLevel> postLevels = entity.getPostLevels();
		for(PostLevel pl : postLevels){
			pl.setPost(entity);
			postLevelService.addEntity(pl);
		}
	}
	
	@Override
	public void updateEntity(Post entity) {
		List<PostLevel> oldPostLevel = postLevelDao.getByPost(entity.getId());
		List<PostLevel> postLevels = entity.getPostLevels();
		List<PostLevel> updatePostLevel = new ArrayList<PostLevel>(postLevels);
		List<PostLevel> newPostLevel = new ArrayList<PostLevel>();
		for(PostLevel d : postLevels){
			d.setPost(entity);
			if(StringUtils.isEmpty(d.getId())){
				d.setId(UUID.randomUUID().toString());
				newPostLevel.add(d);
				newPostLevel.remove(d);
			}else{
				for(PostLevel o : oldPostLevel){
					if(o.getId().equals(d.getId())){
						oldPostLevel.remove(o);
						break;
					}
				}
			}
		}
		for(PostLevel o : oldPostLevel){
			postLevelService.deleteById(o.getId());
		}
		for(PostLevel o : newPostLevel){
			postLevelService.addEntity(o);
		}
		for(PostLevel o : updatePostLevel){
			postLevelService.updateEntity(o);
		}
		super.updateEntity(entity);
	}
	
	@Override
	public void deleteEntity(Post entity) {
		postLevelService.deleteByPost(entity.getId());
		super.deleteEntity(entity);
	}
	
	@Override
	public void deleteById(String id) {
		postLevelService.deleteByPost(id);
		super.deleteById(id);
	}
}
