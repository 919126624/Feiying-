package com.wuyizhiye.base.solr;

/**
 * @ClassName SolrException
 * @Description Solr异常
 * @author li.biao
 * @date 2015-4-1
 */
public class SolrException extends Exception {
	private static final long serialVersionUID = 1253379349342945811L;
	
	public SolrException(String msg){
		super(msg);
	}
	
	public SolrException(Exception exception){
		super(exception);
	}
}
