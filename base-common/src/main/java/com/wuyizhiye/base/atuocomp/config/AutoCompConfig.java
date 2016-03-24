package com.wuyizhiye.base.atuocomp.config;

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

import com.wuyizhiye.base.atuocomp.AutoComp;
import com.wuyizhiye.base.atuocomp.Column;

/**
 * @ClassName AutoCompConfig
 * @Description  查询配置,加载所有配置的查询sql,用于自动补全的调用
 * @author li.biao
 * @date 2015-4-1
 */
@Component(value="autoCompConfig")
public class AutoCompConfig {
	/**
	 * 查询上下文
	 */
	private Resource[] autoLocations;
	
	private Map<String,AutoComp<?>> autos = new HashMap<String, AutoComp<?>>();
	
	public AutoComp<?> getAuto(String id){
		return autos.get(id);
	}
	
	public void setAutoLocations(Resource[] autoLocations) throws Exception{
		this.autoLocations = autoLocations;
		initAutos();
	}
	
	private void initAutos() throws Exception{
		if (!ObjectUtils.isEmpty(this.autoLocations)) {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            for (Resource autoLocation : this.autoLocations) {
                if (autoLocation == null) {
                    continue;
                }
                parseLocation(autoLocation,parser);
            }
        }
	}
	
	private void parseLocation(Resource location,SAXParser parser) throws Exception{
		parser.parse(location.getInputStream(), new DefaultHandler(){
			private AutoComp<?> auto;
			private Column column;
			@Override
			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				if("auto".equals(qName)){
					//开始解析一个auto
					auto = new AutoComp<Object>();
					auto.setId(attributes.getValue("id"));
					auto.setColumns(new ArrayList<Column>());
					auto.setMapper(attributes.getValue("mapper"));
				}else if("column".equals(qName)){
					//开始解析一个列
					column = new Column();
					column.setAlign(attributes.getValue("align"));
					column.setKey(Boolean.parseBoolean(attributes.getValue("key")));
					column.setField(attributes.getValue("field"));
					column.setHidden(Boolean.parseBoolean(attributes.getValue("hidden")));
				}
			}
			
			@Override
			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				if("auto".equals(qName)){
					if(autos.containsKey(auto.getId())){
						throw new RuntimeException("auto id is more than one:("+auto.getId()+")"+auto.getMapper());
					}
					autos.put(auto.getId(), auto);
				}else if("column".equals(qName)){
					auto.getColumns().add(column);
				}
			}
		});
	}
}
