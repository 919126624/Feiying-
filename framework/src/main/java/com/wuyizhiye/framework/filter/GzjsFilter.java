package com.wuyizhiye.framework.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName GzjsFilter
 * @Description TODO
 * @author li.biao
 * @date 2015-4-7
 */
public class GzjsFilter implements Filter{
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		((HttpServletResponse)response).setHeader("Content-Encoding", "gzip");
		((HttpServletResponse)response).setContentType("text/plain");
		filter.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}
