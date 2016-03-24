package com.wuyizhiye.hr.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wuyizhiye.base.CoreEntity;
import com.wuyizhiye.base.service.BaseService;
import com.wuyizhiye.basedata.org.model.Org;
import com.wuyizhiye.basedata.org.service.OrgService;
import com.wuyizhiye.basedata.person.enums.PersonStatusEnum;
import com.wuyizhiye.basedata.person.model.Person;
import com.wuyizhiye.basedata.util.SystemUtil;
import com.wuyizhiye.framework.base.controller.ListController;

/**
 * 人事分析
 * @author xqh
 * @since 2013-08-06
 */
@Controller
@RequestMapping("hr/positionReport/*")
public class PositionReportController extends ListController{
	
	@Autowired
	private OrgService orgService;

	@Override
	protected CoreEntity createNewEntity() {
		
		return null;
	}

	@Override
	protected String getListView() {
		
		return "hr/bi/positionReport";
	}
	
	@RequestMapping("getData")
	@ResponseBody
	public void getData(HttpServletResponse response){
		Map<String,Object> json=new HashMap<String, Object>();
		String orgId=getString("orgId");
		Org org;
		if(null == orgId || "".equals(orgId)){
			org=SystemUtil.getCurrentOrg();
		}else{
			org=this.orgService.getEntityById(orgId);
		}
		String searchType=getString("searchType");//查询类型
		/**
		 * z找下级组织
		 */
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("parent", org.getId());
		List<Org> child=queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.getChild", param, Org.class);
		List<Map<String,Object>> rlist=new ArrayList<Map<String,Object>>();
		if(null != child && child.size()>0){
			param.clear();
			param.put("longNumber", org.getLongNumber()+"%");
			List<Person> plist=queryExecutor.execQuery("com.wuyizhiye.basedata.person.dao.PersonDao.getPerson4Report", param, Person.class);
			rlist=getList(plist,child);
		}
		if("search".equals(searchType)){
			rlist=addTotalRow(rlist,org);
		}
		json.put("showList", rlist);
		outPrint(response, JSONObject.fromObject(json, getDefaultJsonConfig()));
	}
	
	public List<Map<String,Object>> addTotalRow(List<Map<String,Object>> rlist,Org o){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> title=new HashMap<String, Object>();
		title.put("ORG_NAME", o.getName());
		title.put("ORG_NUMBER", o.getNumber());
		title.put("ORG_ID", o.getId());
		title.put("ORG_LONGNUMBER", o.getLongNumber());
		title.put("ORG_LEVEL", o.getLevel());
		title.put("URL", "no");//有点击查看下级 图标
		int a=0;
		int b=0;
		int c=0;
		int d=0;
		int e=0;
		int f=0;
		int g=0;
		int h=0;
		int i=0;
		int j=0;
		int k=0;
		for(Map<String,Object> map:rlist){
			a+=Integer.parseInt(map.get("THE_A").toString());
			b+=Integer.parseInt(map.get("THE_B").toString());
			c+=Integer.parseInt(map.get("THE_C").toString());
			d+=Integer.parseInt(map.get("THE_D").toString());
			e+=Integer.parseInt(map.get("THE_E").toString());
			f+=Integer.parseInt(map.get("THE_F").toString());
			g+=Integer.parseInt(map.get("THE_G").toString());
			h+=Integer.parseInt(map.get("THE_H").toString());
			i+=Integer.parseInt(map.get("THE_I").toString());
			j+=Integer.parseInt(map.get("THE_J").toString());
			k+=Integer.parseInt(map.get("THE_K").toString());
		}
		title.put("THE_A", a);
		title.put("THE_B", b);
		title.put("THE_C", c);
		title.put("THE_D", d);
		title.put("THE_E", e);
		title.put("THE_F", f);
		title.put("THE_G", g);
		title.put("THE_H", h);
		title.put("THE_I", i);
		title.put("THE_J", j);
		title.put("THE_K", k);
		result.add(title);
		result.addAll(rlist);
		return result;
	}
	
	/**
	 * 整理数据
	 * @param plist 
	 * @param olist
	 */
	@SuppressWarnings("static-access")
	public List<Map<String,Object>> getList(List<Person> plist,List<Org> olist){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> param=new HashMap<String, Object>();
		for(Org o:olist){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("ORG_NAME", o.getName());
			map.put("ORG_NUMBER", o.getNumber());
			map.put("ORG_ID", o.getId());
			map.put("ORG_LONGNUMBER", o.getLongNumber());
			map.put("ORG_LEVEL", o.getLevel());
			param.put("parent", o.getId());
			List<Org> clist=queryExecutor.execQuery("com.wuyizhiye.basedata.org.dao.OrgDao.getChild", param, Org.class);
			if(null != clist && clist.size()>0){
				map.put("URL", "yes");//有点击查看下级 图标  
			}else{
				map.put("URL", "no");//有点击查看下级 图标  
			}
			int a=0;
			int b=0;
			int c=0;
			int d=0;
			int e=0;
			int f=0;
			int g=0;
			int h=0;
			int i=0;
			int j=0;
			int k=0;
			for(Person p:plist){
				if(p.getOrg().getLongNumber().contains(o.getLongNumber())){//是该组织下级
					if(null != p.getPersonStatus()){
						a++;
						if(p.getPersonStatus().equals(PersonStatusEnum.TRIAL)){//试用员工
							b++;
						}
						if(p.getPersonStatus().equals(PersonStatusEnum.REGULAR)){//正式员工
							c++;
						}
						if(null != p.getInnerDate()){
							Date start=p.getInnerDate();//最后入职日期
							Date end=new Date();//当前日期
							int days=0;//入职日期 距当前日期 天数
							try {
								days=this.getDayArrays(start, end);
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if(days <= 365){//一年以内
								d++;
							}
							if(days > 365 && days <= 730){//一到两年
								e++;
							}
							if(days > 730 && days <= 1095){//2到3年
								f++;
							}
							if(days > 1095){//3年以上
								g++;
							}
						}
						if(null != p.getHighestEducation()){
							String xi=p.getHighestEducation().getValue();
							if("PRIMARYSCHOOL".equals(xi) || "MIDDLESCHOOL".equals(xi) || "SENIORSCHOOL".equals(xi)){//高中及以下
								h++;
							}
							if("SPECIALSCHOOL".equals(xi) || "JUNIORCOLLEGE".equals(xi)){//专科
								i++;
							}
							if("UNIVERSITY".equals(xi)){//本科
								j++;
							}
							if("POSTGRADUATE".equals(xi) || "MASTER".equals(xi) || "DOCTOR".equals(xi)){//硕士及以上
								k++;
							}
						}
					}
				}
			}
			map.put("THE_A", a);
			map.put("THE_B", b);
			map.put("THE_C", c);
			map.put("THE_D", d);
			map.put("THE_E", e);
			map.put("THE_F", f);
			map.put("THE_G", g);
			map.put("THE_H", h);
			map.put("THE_I", i);
			map.put("THE_J", j);
			map.put("THE_K", k);
			result.add(map);
		}
		return result;
	}
	
	 /**
	  * 取出 两个时间段之间的所有数据集合
	  * @param start
	  * @param end
	  * @return
	  * @throws ParseException
	  */
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public static int getDayArrays(Date start,Date end) throws ParseException {
	      ArrayList ret = new ArrayList();
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(start);
	      Date tmpDate = calendar.getTime();
	      long endTime = end.getTime();
	      while (tmpDate.before(end) || tmpDate.getTime() == endTime) {
	        ret.add(calendar.getTime());
	        calendar.add(7, 1);//2 按月分   7  按天分
	        tmpDate = calendar.getTime();
	      }
	      return ret.size();
	    }
	
	/**
	 * 拼组织ID
	 * @param olist
	 * @return
	 */
	public String getOrgIds(List<Org> olist){
		String str="(";
		for(Org o:olist){
			str+="'"+o.getId()+"',";
		}
		if(!"(".equals(str)){
			str=str.substring(0, str.length()-1)+")";
		}else{
			str="('')";
		}
		return str;
	}

	@Override
	protected String getEditView() {
		
		return null;
	}

	@Override
	protected String getListMapper() {
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BaseService getService() {
		
		return null;
	}

}
