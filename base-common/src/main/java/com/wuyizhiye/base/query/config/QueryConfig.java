package com.wuyizhiye.base.query.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.wuyizhiye.base.query.Column;
import com.wuyizhiye.base.query.Query;

/**
 * @ClassName QueryConfig
 * @Description  查询配置,加载所有配置的查询sql,用于F7插件的调用
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="queryConfig")
public class QueryConfig {
	/**
	 * 查询上下文
	 */
	private Resource[] queryLocations;
	
	private Map<String,Query<?>> querys = new HashMap<String, Query<?>>();
	
	public Query<?> getQuery(String id){
		return querys.get(id);
	}
	
	public void setQueryLocations(Resource[] queryLocations) throws Exception{
		this.queryLocations = queryLocations;
		initQuerys();
	}
	
	private void initQuerys() throws Exception{
		if (!ObjectUtils.isEmpty(this.queryLocations)) {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            for (Resource queryLocation : this.queryLocations) {
                if (queryLocation == null) {
                    continue;
                }
                parseLocation(queryLocation,parser);
            }
        }
	}
	
	private void parseLocation(Resource location,SAXParser parser) throws Exception{
		parser.parse(location.getInputStream(), new DefaultHandler(){
			private Query<?> query;
			private Column column;
			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				if("query".equals(qName)){
					//开始解析一个query
					query = new Query<Object>();
					query.setId(attributes.getValue("id"));
					query.setColumns(new ArrayList<Column>());
					query.setMapper(attributes.getValue("mapper"));
				}else if("column".equals(qName)){
					//开始解析一个列
					column = new Column();
					column.setAlign(attributes.getValue("align"));
					column.setWidth(Integer.parseInt(attributes.getValue("width")));
					column.setDisplay(attributes.getValue("display"));
					column.setField(attributes.getValue("field"));
					column.setHidden(Boolean.parseBoolean(attributes.getValue("hidden")));
				}
			}
			
			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				if("query".equals(qName)){
					if(querys.containsKey(query.getId())){
						throw new RuntimeException("query id is more than one:("+query.getId()+")"+query.getMapper());
					}
					querys.put(query.getId(), query);
				}else if("column".equals(qName)){
					query.getColumns().add(column);
				}
			}
		});
	}
}
