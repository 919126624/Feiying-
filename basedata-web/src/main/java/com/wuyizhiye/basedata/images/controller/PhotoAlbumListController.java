package com.wuyizhiye.basedata.images.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.images.model.PhotoAlbum;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName PhotoAlbumListController
 * @Description 相册列表Controller
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="/basedata/photoAlbum/*")
public class PhotoAlbumListController extends ListController {

	@Override
	protected CoreEntity createNewEntity() {
		return new PhotoAlbum();
	}

	@Override
	protected String getListView() {
		return "basedata/images/photoAlbumList";
	}

	@Override
	protected String getEditView() {
		return "basedata/images/photoAlbumEdit";
	}

	@Override
	protected String getListMapper() {
		return "";
	}

	@Override
	protected BaseService<PhotoAlbum> getService() {
		return null;
	}
	
	@RequestMapping(value="demo")
	public String demo(){
		return "basedata/images/demo";
	}

}
