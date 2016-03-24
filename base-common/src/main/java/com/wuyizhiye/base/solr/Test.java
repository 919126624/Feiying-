package com.wuyizhiye.base.solr;

import java.util.Map;

import com.wuyizhiye.base.common.Pagination;


public class Test {

	/**
	 * @param args
	 * @throws SolrException 
	 */
	public static void main(String[] args) throws SolrException {
//		String[] strs = new String[]{"这样下次再使用此线程的时候就会获得上次的上下文了"
//				,"而是放回到池中",
//				"完成后执行",
//				"，也可以说在请求过程"
//				,"线程的生命周期还没有结束"
//				,"完成之后执行。"
//				,"该方法可以用来清理资源"
//				,"在请求结束后"};
//			Random ran = new Random();
//			BeanDemo bean;
//			for(int i = 4000; i < 5000000; i++){
//				bean = new BeanDemo();
//				bean.setId(UUID.randomUUID().toString());
//				bean.setName( strs[(int)(ran.nextDouble()*8)] + i);
//				bean.setComments( strs[(int)(ran.nextDouble()*8)] + i);
//				bean.setTitle( strs[(int)(ran.nextDouble()*8)] + i);
//				SolrRemoteServer.addEntity(bean, "fengmy");
//				System.out.println(i);
//			}
		Pagination<Map<String,Object>> pagination = new Pagination<Map<String,Object>>();
		SolrRemoteServer.search(pagination, "fengmy", "name:上下文", null);
//		System.out.println(pagination.getRecordCount());
//		System.out.println(pagination.getExceTime());
	}

}
