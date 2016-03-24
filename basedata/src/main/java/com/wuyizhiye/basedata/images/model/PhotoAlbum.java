package com.wuyizhiye.basedata.images.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName PhotoAlbum
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class PhotoAlbum extends DataEntity {
	private static final long serialVersionUID = 788962989173545179L;
	
	/**
	 * 相册所属数据id
	 */
	private String belong;

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getBelong() {
		return belong;
	}
}
