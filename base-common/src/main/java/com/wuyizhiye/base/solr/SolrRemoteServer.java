package com.wuyizhiye.base.solr;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import com.wuyizhiye.base.common.Pagination;
import com.wuyizhiye.base.util.StringUtils;
import com.wuyizhiye.base.util.SystemConfig;

/**
 * @ClassName SolrRemoteServer
 * @Description Solr远程服务工具
 * @author li.biao
 * @date 2015-4-1
 */
public class SolrRemoteServer {
	private static final Map<String,SolrServer> solrServers = new HashMap<String, SolrServer>();
	private static final BigDecimal MILLISECOND = new BigDecimal("1000.000");
	
	private static String getSolrServerPath(String namespace){
		return SystemConfig.getParameter("solrAddress") + "/" + namespace;
//		return "http://dev:8888/solr/"+namespace;
	}
	
	/**
	 * 新增实体到solr服务器
	 * @param entity
	 * @param namespace
	 * @return
	 * @exception SolrException
	 */
	public static boolean addEntity(Object entity,String namespace) throws SolrException{
		if(entity == null){
			throw new SolrException("非法参数");
		}
		SolrServer server = getSorServer(namespace);
		try {
			server.addBean(entity);
			server.commit(true, false);
			return true;
		} catch (IOException e) {
			throw new SolrException(e);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
	}
	
	/**
	 * 新增实体到solr服务器
	 * @param entity
	 * @param namespace
	 * @return
	 * @exception SolrException
	 */
	public static boolean addEntity(List<Object> entitys,String namespace) throws SolrException{
		if(entitys == null){
			throw new SolrException("非法参数");
		}
		SolrServer server = getSorServer(namespace);
		try {
			server.addBeans(entitys);
			server.commit(true, false);
			return true;
		} catch (IOException e) {
			throw new SolrException(e);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
	}
	
	/**
	 * 删除索引数据
	 * @param id
	 * @param namespace
	 * @return
	 * @throws SolrException
	 */
	public static boolean deleteEntity(String id,String namespace) throws SolrException{
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(namespace)){
			throw new SolrException("非法参数");
		}
		SolrServer server = getSorServer(namespace);
		try {
			server.deleteById(id);
			server.commit(true, false);
			return true;
		} catch (IOException e) {
			throw new SolrException(e);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
	}
	
	/**
	 * 删除索引数据
	 * @param id
	 * @param namespace
	 * @return
	 * @throws SolrException
	 */
	public static boolean deleteEntity(List<String> id,String namespace) throws SolrException{
		if(StringUtils.isEmpty(namespace)){
			throw new SolrException("非法参数");
		}
		SolrServer server = getSorServer(namespace);
		try {
			server.deleteById(id);
			server.commit(true, false);
			return true;
		} catch (IOException e) {
			throw new SolrException(e);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
	}
	
	/**
	 * 新增文档到solr服务器
	 * @param entity
	 * @param namespace
	 * @return
	 * @exception SolrException
	 */
	public static boolean addDocumenty(SolrInputDocument doc,String namespace) throws SolrException{
		if(doc == null || StringUtils.isEmpty(namespace)){
			throw new SolrException("非法参数");
		}
		SolrServer server = getSorServer(namespace);
		try {
			server.add(doc);
			server.commit(true, false);
			return true;
		} catch (IOException e) {
			throw new SolrException(e);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
	}
	
	
	
	public static synchronized SolrServer getSorServer(String namespace) throws SolrException{
		if(StringUtils.isEmpty(namespace)){
			throw new SolrException("非法的solr命名空间");
		}
		SolrServer server = null;
		if(solrServers.get(namespace)!=null){
			return solrServers.get(namespace);
		}
		server = new HttpSolrServer(getSolrServerPath(namespace));
		try {
			server.ping();
			solrServers.put(namespace, server);
			return server;
		} catch (SolrServerException e) {
			throw new SolrException(e);
		} catch (IOException e) {
			throw new SolrException(e);
		}
	}
	
	/**
	 * 从solr中检索数据
	 * @param pagination 分页
	 * @param namespace solr实例名
	 * @param query 查询序列,没任何条件的话，需要传入"*"，否则查询不到数据 
	 * @param fields 查询的字段，不指定则查询所有字段
	 * @return
	 * @throws SolrException 
	 */
	public static Pagination<Map<String,Object>> search(Pagination<Map<String,Object>> pagination,String namespace,String query,String[] fields) throws SolrException{
		SolrServer server = getSorServer(namespace);
		SolrQuery params = new SolrQuery(query);
		params.setIncludeScore(true);
		if(fields != null && fields.length > 0){
			params.setFields(fields);
		}else{
			params.setFields("*");
		}
		params.setSort("score", ORDER.asc);
		params.setStart((pagination.getCurrentPage() - 1) * pagination.getPageSize());
		params.setRows(pagination.getPageSize());
		try {
			QueryResponse response = server.query(params, METHOD.POST);
			pagination.setRecordCount(((Long)response.getResults().getNumFound()).intValue());
			BigDecimal time = new BigDecimal(response.getQTime()).divide(MILLISECOND).setScale(3);
			pagination.setExceTime(time.toString() + "秒");
			pagination.setExceSql("从Solr服务器中检索");
			int size = response.getResults().size();
			SolrDocument doc;
			List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item;
			for(int i = 0; i < size; i++){
				doc = response.getResults().get(i);
				Collection<String> keys = doc.getFieldNames();
				item = new HashMap<String, Object>();
				for(String key : keys){
					item.put(key, doc.get(key));
				}
				items.add(item);
			}
			pagination.setItems(items);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
		return pagination;
	}
	
	public static <T> Pagination<T> search(Pagination<T> pagination,String namespace,String query,String[] fields,Class<T> T) throws SolrException{
		SolrServer server = getSorServer(namespace);
		SolrQuery params = new SolrQuery(query);
		params.setIncludeScore(true);
		if(fields != null && fields.length > 0){
			params.setFields(fields);
		}else{
			params.setFields("*");
		}
		params.setSort("score", ORDER.asc);
		params.setStart((pagination.getCurrentPage() - 1) * pagination.getPageSize());
		params.setRows(pagination.getPageSize());
		try {
			QueryResponse response = server.query(params, METHOD.POST);
			pagination.setRecordCount(((Long)response.getResults().getNumFound()).intValue());
			BigDecimal time = new BigDecimal(response.getQTime()).divide(MILLISECOND).setScale(3);
			pagination.setExceTime(time.toString() + "秒");
			pagination.setExceSql("从Solr服务器中检索");
			List<T> items = response.getBeans(T);
			pagination.setItems(items);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
		return pagination;
	}
	
	public static <T> List<T> searchList(String namespace,String query,String[] fields,Class<T> c) throws SolrException{
		SolrServer server = getSorServer(namespace);
		SolrQuery params = new SolrQuery(query);
		params.setIncludeScore(true);
		if(fields != null && fields.length > 0){
			params.setFields(fields);
		}else{
			params.setFields("*");
		}
		params.setSort("score", ORDER.asc);
		//params.setStart(0);
		//params.setRows(1);
		List<T> items = null ;
		try {
			QueryResponse response = server.query(params, METHOD.POST);
			items = response.getBeans(c);
			
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
		return items;
	}
	
	/**
	 * 从solr中检索数据
	 * @throws SolrException 
	 */
	public static Pagination<Map<String,Object>> search(Pagination<Map<String,Object>> pagination,String namespace,SolrQuery query) throws SolrException{
		SolrServer server = getSorServer(namespace);
		query.setStart((pagination.getCurrentPage() - 1) * pagination.getPageSize());
		query.setRows(pagination.getPageSize());
		try {
			QueryResponse response = server.query(query, METHOD.POST);
			pagination.setRecordCount(((Long)response.getResults().getNumFound()).intValue());
			BigDecimal time = new BigDecimal(response.getQTime()).divide(MILLISECOND).setScale(3);
			pagination.setExceTime(time.toString() + "秒");
			pagination.setExceSql("从Solr服务器中检索");
			int size = response.getResults().size();
			SolrDocument doc;
			List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			Map<String,Object> item;
			for(int i = 0; i < size; i++){
				doc = response.getResults().get(i);
				Collection<String> keys = doc.getFieldNames();
				item = new HashMap<String, Object>();
				for(String key : keys){
					item.put(key, doc.get(key));
				}
				items.add(item);
			}
			pagination.setItems(items);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
		return pagination;
	}
	
	/**
	 * 从solr中检索数据
	 * @throws SolrException 
	 */
	public static <T> Pagination<T> search(Pagination<T> pagination,String namespace,SolrQuery query,Class<T> T) throws SolrException{
		SolrServer server = getSorServer(namespace);
		query.setStart((pagination.getCurrentPage() - 1) * pagination.getPageSize());
		query.setRows(pagination.getPageSize());
		try {
			QueryResponse response = server.query(query, METHOD.POST);
			pagination.setRecordCount(((Long)response.getResults().getNumFound()).intValue());
			BigDecimal time = new BigDecimal(response.getQTime()).divide(MILLISECOND).setScale(3);
			pagination.setExceTime(time.toString() + "秒");
			pagination.setExceSql("从Solr服务器中检索");
			List<T> items = response.getBeans(T);
			pagination.setItems(items);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
		return pagination;
	}
	
	public static QueryResponse queryCount(final SolrQuery solrQuery,String namespace) throws SolrException{			
		SolrServer server = getSorServer(namespace);
		QueryResponse response = null;;
		try {
			response = server.query(solrQuery);
		} catch (SolrServerException e) {
			throw new SolrException(e);
		}
		return response;
	}
	
	public static void main(String[] args) throws SolrException {
//		SolrQuery query  = new SolrQuery("*");
		//query.addFacetQuery("parentAreaId:006a9fea-3773-4c85-b2e1-45ae6e42bb0d");
		Pagination<Map<String,Object>> pagination = search(new Pagination<Map<String,Object>>(), "resourceCustomer","*",null);
		//System.out.println(pagination.getRecordCount());
		List<Map<String,Object>> items = pagination.getItems();
		for(Map<String,Object> item : items){
			Set<String> keys = item.keySet();
			for(String key : keys){
//				System.out.print(key + ":" + item.get(key));
//				System.out.print(",");
			}
//			System.out.println();
		}
	}
}
