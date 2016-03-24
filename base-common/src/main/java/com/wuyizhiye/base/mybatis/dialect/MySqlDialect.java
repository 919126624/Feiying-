package com.wuyizhiye.base.mybatis.dialect;

/**
 * @ClassName MySqlDialect
 * @Description MySql分页
 * @author li.biao
 * @date 2015-4-1
 */
public class MySqlDialect extends Dialect {
	public boolean supportsLimitOffset(){
		return true;
	}
	
    public boolean supportsLimit() {   
    	
        return true;   
    }  
    
	public String getLimitString(String sql, int offset,String offsetPlaceholder, int limit, String limitPlaceholder) {
        if (offset > 0) {   
        	return sql + " limit "+offsetPlaceholder+","+limitPlaceholder; 
        } else {   
            return sql + " limit "+limitPlaceholder;
        }  
	}   
}
