package com.wuyizhiye.base.common;

/**
 * @ClassName ImageAttribute
 * @Description 图片属性类
 * @author li.biao
 * @date 2015-4-1
 */
public class ImageAttribute {

	//图片宽度
	private Integer width;

	//图片高度
	private Integer height;
	
	//是否等比例压缩
	private boolean isEqualRatio;		
	
	//是否需要增加文件名字后缀,一組里面只能存在一个true
	private boolean noShowSuffix;

	public ImageAttribute(Integer width, Integer height, boolean isEqualRatio) {
		this.width = width;
		this.height = height;
		this.isEqualRatio = isEqualRatio;
		this.noShowSuffix = false;
	}
	
	public ImageAttribute(Integer width, Integer height, boolean isEqualRatio,boolean noShowSuffix) {
		this.width = width;
		this.height = height;
		this.isEqualRatio = isEqualRatio;
		this.noShowSuffix = noShowSuffix;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}

	public boolean isEqualRatio() {
		return isEqualRatio;
	}
	public void setEqualRatio(boolean isEqualRatio) {
		this.isEqualRatio = isEqualRatio;
	}
	public boolean isNoShowSuffix() {
		return noShowSuffix;
	}
	public void setNoShowSuffix(boolean noShowSuffix) {
		this.noShowSuffix = noShowSuffix;
	}

}
