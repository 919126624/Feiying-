package com.wuyizhiye.basedata.permission.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.permission.model.Menu;
import com.wuyizhiye.basedata.permission.model.Shortcuts;
import com.wuyizhiye.basedata.permission.service.MenuService;
import com.wuyizhiye.basedata.permission.service.ShortcutsService;
import com.wuyizhiye.framework.base.controller.EditController;

/**
 * @ClassName ShortcutsEditController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="permission/shortcuts/*")
public class ShortcutsEditController extends EditController {
	@Autowired
	private ShortcutsService shortcutsService;
	@Autowired
	private MenuService menuService;
	@Override
	protected Class<Shortcuts> getSubmitClass() {
		return Shortcuts.class;
	}

	@Override
	protected BaseService<Shortcuts> getService() {
		return shortcutsService;
	}
	
	@Override
	protected boolean validate(Object data) {
		Shortcuts st = (Shortcuts) data;
		if(st.getMenu()==null){
			//无关联菜单，即手工建立的,必须包含名称和link
			if(StringUtils.isEmpty(st.getName()) || StringUtils.isEmpty(st.getLink())){
				getOutputMsg().put("STATE", 0);
				getOutputMsg().put("MSG", "快捷方式名称和链接不能为空");
				return false;
			}
		}else{
			//关联菜单
			Menu menu = menuService.getEntityById(st.getMenu().getId());
			if(menu==null){
				getOutputMsg().put("STATE", 0);
				getOutputMsg().put("MSG", "该菜单不存在,无法建立快捷方式");
				return false;
			}
			
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("menu", st.getMenu().getId());
			List<Shortcuts> sts = queryExecutor.execQuery("com.wuyizhiye.basedata.permission.dao.ShortcutsDao.select", new Pagination<Shortcuts>(10,0), param ).getItems();
			if(sts!=null && sts.size()>0){
				getOutputMsg().put("STATE", 0);
				getOutputMsg().put("MSG", "该菜单的快捷方式己存在,无法建立快捷方式");
				return false;
			}
			st.setName(menu.getName());
			st.setLink(menu.getLink());
			st.setMiniIconPath(menu.getMiniIcon());
			st.setIconPath(menu.getLargeIcon());
			st.setIndex(shortcutsService.getMaxIndex(new HashMap<String, Object>())+1);
		}
		return true;
	}
}
