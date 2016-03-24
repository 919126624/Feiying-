package com.wuyizhiye.framework.compoment;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.query.Query;
import com.wuyizhiye.base.query.config.QueryConfig;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * @ClassName SimpleDataPickerController
 * @Description 普通数据选择器，普通单选列表型
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="/framework/dataPicker/*")
public class SimpleDataPickerController extends ListController {
	@Resource(name="queryConfig")
	private QueryConfig queryConfig;
	@Override
	protected CoreEntity createNewEntity() {
		return null;
	}

	@Override
	protected String getListView() {
		String id = getString("query");
		if(StringUtils.isEmpty(id)){
			throw new RuntimeException("query id cann't be null.");
		}
		Query<?> q = queryConfig.getQuery(id);
		if(q==null){
			throw new RuntimeException("query is not exists.");
		}
		getRequest().setAttribute("query", q);
		Map<String,Object> param = getListDataParam();
		param.remove("query");
		getRequest().setAttribute("param", param);
		return "framework/compoment/simpleDataPicker";
	}

	@Override
	protected String getEditView() {
		return null;
	}

	@Override
	protected String getListMapper() {
		String id = getString("query");
		Query<?> query = queryConfig.getQuery(id);
		if(query==null){
			throw new RuntimeException("query cann't be null.");
		}
		return query.getMapper();
	}

	@Override
	protected BaseService getService() {
		return null;
	}
}
