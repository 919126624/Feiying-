package com.wuyizhiye.framework.compoment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.dao.SqlExecutor;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.BaseController;

/**
 * @ClassName SqlController
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
@Controller
@RequestMapping(value="sql/*")
public class SqlController extends BaseController {
	@Autowired
	private SqlExecutor sqlExecutor;
	
	@RequestMapping(value="main")
	public String main(){
		return "framework/compoment/sql";
	}
	
	@RequestMapping(value="execute")
	public void executor(){
		String sql = getString("sql");
		if(!StringUtils.isEmpty("sql")){
			String[] sqls = sql.split(";");
			List<String> sqlList = new ArrayList<String>();
			for(String s : sqls){
				if(!StringUtils.isEmpty(s)){
					sqlList.add(s);
				}
			}
			sqlExecutor.executeSqls(sqlList,false);
		}
	}
}
