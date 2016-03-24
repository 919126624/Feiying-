package com.wuyizhiye.basedata.basic.dao.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.wuyizhiye.base.dao.impl.BaseDaoImpl;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.basedata.basic.dao.MarkDao;
import com.wuyizhiye.basedata.basic.model.Mark;

/**
 * @ClassName MarkDaoImpl
 * @Description TODO
 * @author li.biao
 * @date 2015-4-2
 */
@Component(value="markBasicDao")
public class MarkDaoImpl extends BaseDaoImpl implements MarkDao {
	@Override
	protected String getNameSpace() {
		return "com.wuyizhiye.basedata.basic.dao.MarkDao";
	}

	@Override
	public synchronized String getMarkCode(String prefix) {
		
		SimpleDateFormat fmt=new SimpleDateFormat("yyMMdd");
		DecimalFormat nfmt=new DecimalFormat("00000");
		
		int sq = 1;
		
		//默认为非系统级编码
		if(StringUtils.isEmpty(prefix)){
			prefix = Mark.NUMBER_M ;
		}
		String ntype = prefix ;
		if(Mark.NUMBER_M.equals(prefix)){
			ntype += fmt.format(new Date());
		}
		
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("numberType", ntype);
		Object maxNumber = this.getSqlSession().selectOne(getNameSpace()+".getMarkCode", param);
		if(maxNumber!=null && !StringUtils.isEmpty(maxNumber.toString())){ 
			sq = new Integer(maxNumber.toString().replace(ntype,"")).intValue();
			sq++;
		} 
		return ntype += nfmt.format(sq);
	}
}
