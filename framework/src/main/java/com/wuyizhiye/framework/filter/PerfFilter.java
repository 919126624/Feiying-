package com.wuyizhiye.framework.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * @ClassName PerfFilter
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class PerfFilter implements Filter {
	private static Logger log=Logger.getLogger(PerfFilter.class); 
	@Override
	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String path = req.getServletPath();
		@SuppressWarnings("rawtypes")
		Map param = request.getParameterMap();
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		long end = System.currentTimeMillis();
		if(log.isDebugEnabled()){
			log.debug("性能日志:(" + (end - start) + "毫秒)[" + path + "]" + JSONObject.fromObject(param).toString());
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
