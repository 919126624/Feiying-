package com.wuyizhiye.base.module.model;

/**
 * @ClassName ModuleLic
 * @Description 模块许可
 * @author li.biao
 * @date 2015-4-1
 */
public class ModuleLic extends Module {
	private static final long serialVersionUID = -5568171950666414193L;
	
	private boolean hasLic;

	public boolean isHasLic() {
		return hasLic;
	}

	public void setHasLic(boolean hasLic) {
		this.hasLic = hasLic;
	}
}
