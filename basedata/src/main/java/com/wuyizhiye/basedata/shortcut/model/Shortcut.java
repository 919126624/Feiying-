package com.wuyizhiye.basedata.shortcut.model;

import com.wuyizhiye.basedata.DataEntity;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.shortcut.enums.ShortcutTypeEnum;

/**
 * @ClassName Shortcut
 * @Description 快捷菜单
 * @author li.biao
 * @date 2015-4-3
 */
public class Shortcut extends DataEntity{

	private static final long serialVersionUID = 1L;

	private String url;//快捷方式地址   有可能为内部方法
	private String ioc;//图标地址
	private String remark;//备注
	private ShortcutTypeEnum type;//快捷类型
	private Menu menu;//选择菜单时关联的菜单
	private String isAccredit;//冗余字段  如果关联当前岗位为：true  否则为空
	private OpenTypeEnum openType;//连接打开方式
	private int idx;//排序号  sht  2014-08-27
	private int isFrameworkShortcut;//是否属于框架快捷方式  sht  2014-08-27
	private int isInlayShortcut;//是否属于内置快捷方式 (不允许界面维护) sht  2014-08-27
	
	
	public int getIsInlayShortcut() {
		return isInlayShortcut;
	}
	public void setIsInlayShortcut(int isInlayShortcut) {
		this.isInlayShortcut = isInlayShortcut;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getIsFrameworkShortcut() {
		return isFrameworkShortcut;
	}
	public void setIsFrameworkShortcut(int isFrameworkShortcut) {
		this.isFrameworkShortcut = isFrameworkShortcut;
	}
	public OpenTypeEnum getOpenType() {
		return openType;
	}
	public void setOpenType(OpenTypeEnum openType) {
		this.openType = openType;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public String getIsAccredit() {
		return isAccredit;
	}
	public void setIsAccredit(String isAccredit) {
		this.isAccredit = isAccredit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIoc() {
		return ioc;
	}
	public void setIoc(String ioc) {
		this.ioc = ioc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public ShortcutTypeEnum getType() {
		return type;
	}
	public void setType(ShortcutTypeEnum shortcutTypeEnum) {
		this.type = shortcutTypeEnum;
	}
	public String getTypeLabel() {
		return this.getType() == null ? "" : this.getType().getLabel();
	}
	public String getOpenTypeLabel() {
		return this.getOpenType() == null ? "" : this.getOpenType().getLabel();
	}
	
}
