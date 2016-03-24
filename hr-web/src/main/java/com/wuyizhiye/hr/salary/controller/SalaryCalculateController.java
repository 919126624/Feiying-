package com.wuyizhiye.hr.salary.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.framework.base.controller.BaseController;
import com.wuyizhiye.hr.salary.model.Duration;
import com.wuyizhiye.hr.salary.model.SalaryCalculateItem;
import com.wuyizhiye.hr.salary.model.SalaryCalculatePerson;
import com.wuyizhiye.hr.salary.model.SalaryCalculateScheme;
import com.wuyizhiye.hr.salary.model.SalaryItem;
import com.wuyizhiye.hr.salary.model.SalaryScheme;
import com.wuyizhiye.hr.salary.model.SalarySchemeItem;
import com.wuyizhiye.hr.salary.model.SalaryStandard;
import com.wuyizhiye.hr.salary.model.StandardItem;
import com.wuyizhiye.hr.salary.service.DurationService;
import com.wuyizhiye.hr.salary.service.SalaryCalculateSchemeService;
import com.wuyizhiye.hr.salary.service.SalarySchemeService;
import com.wuyizhiye.hr.utils.SalaryScriptRunner;
/**
 * 薪酬计算
 * @author hlz
 * @since 2014-02-26
 *
 */
@Controller
@RequestMapping(value="hr/calculate/*")
public class SalaryCalculateController extends BaseController {
	
	@Autowired
	private SalarySchemeService salarySchemeService;
	@Autowired
	private DurationService durationService;
	@Autowired
	private SalaryCalculateSchemeService salaryCalculateSchemeService;
	
	@RequestMapping(value="calculateList")
	public String calculateList(){
		return "hr/salary/salarycalculate/salaryCalculateList";
	}
	/**
	 * 计算
	 * @param pagination
	 * @param response
	 */
	@RequestMapping(value="calculate")
	public void calculate(Pagination<SalaryCalculatePerson> pagination,HttpServletResponse response){
		String schemeId = getString("schemeId");
		String durationId = getString("durationId");
		SalaryScheme salaryScheme = salarySchemeService.getEntityById(schemeId);
		//Duration duration = durationService.getEntityById(durationId);
		Map<String, Object> map = getListDataParam();
		map.put("schemeId", schemeId);
		map.put("durationId", durationId);
		Map<String, Object> result = new HashMap<String, Object>();
		JsonConfig jsonConfig = getDefaultJsonConfig();
		//根据薪酬方案和薪酬期间查找核算方案
		SalaryCalculateScheme calculateScheme = queryExecutor.execOneEntity("com.wuyizhiye.hr.salary.dao.SalaryCalculateSchemeDao.select", map, SalaryCalculateScheme.class);
		//核算方案中的审核状态，若未审批（0）则进行计算,审批后的不能再计算
		if(null==calculateScheme || "0".equals(calculateScheme.getState())){
			//map.clear();
			map.put("objType", salaryScheme.getObjectType().toString());
			//根据薪酬方案中的实施对象查找需要计算的人员
			pagination = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao.selectPersonByScheme", pagination, map);
			//得到薪酬方案中的需要计算的项目
			List<SalarySchemeItem> itemList = salaryScheme.getSalarySchemeItem();
			List<SalaryCalculatePerson> personList = pagination.getItems();
			//计算
			initCalculateScheme(personList,itemList);
			result.put("itemList", itemList);
			result.put("pag", pagination);
			result.put("msg", "SUCCESS");
			
			jsonConfig.setExcludes(new String[]{"salaryCalculatePerson"});
			
		}else{
			result.put("msg", "SUCCESS");
		}
		outPrint(response, JSONObject.fromObject(result,jsonConfig));
	}
	
	/**
	 * 查询参数
	 * @return
	 */
	protected Map<String,Object> getListDataParam(){
		Map<String,String> param = getParamMap();
		Map<String,Object> params = new HashMap<String, Object>();
		Set<String> keys = param.keySet();
		for(String key : keys){
			params.put(key, param.get(key));
		}
		return params;
	}
	
	/**
	 * 保存
	 * @param response
	 */
	@RequestMapping(value="save")
	public void save(HttpServletResponse response){
		String schemeId = getString("schemeId");
		String durationId = getString("durationId");
		SalaryScheme salaryScheme = salarySchemeService.getEntityById(schemeId);
		Duration duration = durationService.getEntityById(durationId);
		Map<String, Object> map = getListDataParam();
		map.put("objType", salaryScheme.getObjectType().toString());
		List<SalaryCalculatePerson> personList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryCalculatePersonDao.selectPersonByScheme",map,SalaryCalculatePerson.class);
		List<SalarySchemeItem> itemList = salaryScheme.getSalarySchemeItem();
		initCalculateScheme(personList,itemList);
		
		SalaryCalculateScheme calculateScheme = new SalaryCalculateScheme();
		calculateScheme.setDuration(duration);
		calculateScheme.setSalaryScheme(salaryScheme);
		calculateScheme.setPersons(personList);
		calculateScheme.setName(salaryScheme.getName()+"-"+duration.getName());
		salaryCalculateSchemeService.addEntity(calculateScheme);
		getOutputMsg().put("STATE", "SUCCESS");
		outPrint(response, JSONObject.fromObject(getOutputMsg()));
	}
	
	
	private void initCalculateScheme(List<SalaryCalculatePerson> personList,List<SalarySchemeItem> itemList){
		//List<SalarySchemeItem> itemList = salaryScheme.getSalarySchemeItem();
		Map<String, Object> map = new HashMap<String, Object>();
		//得到所有的薪酬项目
		List<SalaryItem> allItemList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryItemDao.select", null, SalaryItem.class);
		for(SalaryCalculatePerson person : personList){
			List<SalaryCalculateItem> calculateItemList = new ArrayList<SalaryCalculateItem>();
			map.clear();
			map.put("jobId", person.getJob()!=null?person.getJob().getId():null);
			map.put("levelId", person.getJobLevel()!=null?person.getJobLevel().getId():null);
			//根据所计算的人员所在的岗位和职级得到薪酬标准
			List<SalaryStandard> standardList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.SalaryStandardDao.select", map, SalaryStandard.class);
			for(SalarySchemeItem schemeItem : itemList){
				SalaryCalculateItem calculateItem = new SalaryCalculateItem();
				calculateItem.setSalaryItem(schemeItem.getSalaryItem());
				calculateItem.setSalaryCalculatePerson(person);
				String script = schemeItem.getScript();
				//储存项目与金额的键值对
				Map<String, BigDecimal> data = new HashMap<String, BigDecimal>();
				if(standardList.size()>0){
					
					for(SalaryItem item : allItemList){
						map.clear();
						map.put("salaryStandardId", standardList.get(0).getId());
						map.put("salaryItemId", item.getId());
						//得到薪酬标准中的项目
						List<StandardItem> stItemList = queryExecutor.execQuery("com.wuyizhiye.hr.salary.dao.StandardItemDao.select", map, StandardItem.class);
						if(stItemList.size()>0){
							data.put(item.getNumber(), new BigDecimal(stItemList.get(0).getAmount()));
						}
					}
					//若为公式获取，则计算公式所得值
					if(!StringUtils.isEmpty(script)){
						Map<String, String> scripts = new HashMap<String, String>();
						scripts.put(schemeItem.getSalaryItem().getNumber(), script);
						SalaryScriptRunner runner = new SalaryScriptRunner(scripts);
						runner.run(data);
						//System.out.println("计算脚本后的值"+schemeItem.getSalaryItem().getNumber()+"：=================="+data.get(schemeItem.getSalaryItem().getNumber()));
						
					}
					calculateItem.setMoney(data.get(schemeItem.getSalaryItem().getNumber()));
				}else{
					calculateItem.setMoney(new BigDecimal(0));
				}
				
				calculateItemList.add(calculateItem);
			}
			person.setItems(calculateItemList);
		}
		
	}
}
