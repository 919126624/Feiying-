package com.wuyizhiye.basedata.images.model;

import com.wuyizhiye.basedata.DataEntity;

/**
 * @ClassName Photo
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
public class Photo extends DataEntity {
	private static final long serialVersionUID = -8497981325404311343L;
	
	/**
	 * 相片所属相册
	 */
	private PhotoAlbum album;
	
	/**
	 * 相片位置
	 */
	private String path;
	

	private int idx;//排列顺序   sht 2014-08-18
	

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setAlbum(PhotoAlbum album) {
		this.album = album;
	}

	public PhotoAlbum getAlbum() {
		return album;
	}
	
}
