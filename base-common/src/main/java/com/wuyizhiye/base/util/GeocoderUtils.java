package com.wuyizhiye.base.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import net.sf.json.JSONObject;
/**
 * 利用百度api 根据地址查询经纬度
 * 
 * @author hyl
 */
public class GeocoderUtils {
	private static String ak = "5Lt6O6NtXwgTIxCQs8vfgfNt";//次ak为个人申请ak  仅供测试 使用
	static String callHttpsUrl(String url,Map<String, String> map){
		return HttpClientUtil.callHttpsUrl(url, null, true, map);
	}
	static String callHttpsUrl(String url,String paramStr){
		return HttpClientUtil.callHttpsUrl(url, paramStr);
	}
	public static JSONObject getLatAndLngByAddr(String address,String city){
		   JSONObject resultMap = new JSONObject();	    
		   if(StringUtils.isEmpty(address)){
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "地址不能为空");
			return resultMap;
		   } 
		   try {
				address = java.net.URLEncoder.encode(address, "UTF-8");
				  if(StringUtils.isNotNull(city)){
					  city= java.net.URLEncoder.encode(city, "UTF-8");;  
				   }
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
		        return null;
			}
		   String httpUrl ="http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak="+ak;
		   if(StringUtils.isNotNull(city)){
			   httpUrl+="&city="+city;  
		   }
			String res=null;
			
			res = callHttpsUrl(httpUrl,"");
			
//			System.out.println(res);
			JSONObject jsonObject = JSONObject.fromObject(res);
			if(jsonObject.getInt("status")==0){
			JSONObject result = JSONObject.fromObject(jsonObject.get("result"));
			resultMap.put("STATE", "SUCCESS");
			resultMap.put("precise", result.get("precise"));
			resultMap.put("confidence", result.get("confidence"));
			resultMap.put("level", result.get("level"));
			JSONObject location = JSONObject.fromObject(result.get("location"));
			resultMap.put("lng", location.get("lng"));//经度
			resultMap.put("lat", location.get("lat"));//维度
			return resultMap;
			}else{
			resultMap.put("STATE", "FAIL");
			resultMap.put("MSG", "查询失败,"+jsonObject.get("msg"));
			return resultMap;
			}
			
		}
	public static void main(String[] args) throws IOException {
		JSONObject map = GeocoderUtils.getLatAndLngByAddr("新郑县观音镇宋庄村","郑州市");
		//{"lng":113.88624963497,"lat":34.503212576576}
		
		//{"lng":113.88577479427,"lat":34.497873942151}
		//{"lng":113.73611501497,"lat":34.459442752589}
		//{"lng":113.73611501497,"lat":34.459442752589}
//		System.out.println(map);
		
		String aa = Geohash.encode(22.567035, 113.948);
//		System.out.println(aa);//sfrun7kn2j1u
		String bb = Geohash.encode(22.564777, 113.9489);
//		System.out.println(bb);//sfrun7kn2j1u
	}
}
