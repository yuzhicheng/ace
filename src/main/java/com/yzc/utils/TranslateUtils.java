package com.yzc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 翻译工具类
 * @author xiezy
 * @date 2016年11月23日
 */
public class TranslateUtils {
	
	public static Map<String, String> accountMap = new LinkedHashMap<String, String>();
	static {
		//363279904@qq.com
		accountMap.put("esp-lifecycle-1", "123854141");
		accountMap.put("esp-lifecycle-2", "123854140");
		accountMap.put("esp-lifecycle-3", "123854139");
		accountMap.put("esp-lifecycle-4", "123854138");
		accountMap.put("esp-lifecycle-5", "123854137");
		accountMap.put("esp-lifecycle-6", "123854136");
		accountMap.put("esp-lifecycle-7", "123854135");
		accountMap.put("esp-lifecycle-8", "123854134");
		accountMap.put("esp-lifecycle-9", "123854133");
		accountMap.put("esp-lifecycle-10", "455488973");
		accountMap.put("esp-lifecycle-11", "455488974");
		accountMap.put("esp-lifecycle-12", "455488975");
		accountMap.put("esp-lifecycle-13", "455488976");
		accountMap.put("esp-lifecycle-14", "455488977");
		accountMap.put("esp-lifecycle-15", "455488978");
		accountMap.put("esp-lifecycle-16", "455488979");
		accountMap.put("esp-dev", "1454182070");
		accountMap.put("esp-debug", "1604613560");
	}
	
	public static void main(String[] args) {
		Set<String> set = accountMap.keySet();
		System.out.println(set);
		
		System.out.println(getTranslation("good"));
	}
	
	/**
	 * 需要申请
	 * 申请地址：http://fanyi.youdao.com/openapi?path=data-mode
	 * 使用API key 时，请求频率限制为每小时1000次，超过限制会被封禁。
	 * 如果您的应用确实需要超过每小时1000次请求，请参考“常见问题”中的相关信息与我们联系。
	 */
	public static String KEY_FROM = "CaptainZe";
	public static String KEY = "443510095";
	
	//常量
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String USER_AGENT =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	/**
	 * 有道翻译
	 * 
	 * 版本：1.1，请求方式：get，编码方式：utf-8 
	 * 主要功能：中英互译，同时获得有道翻译结果和有道词典结果（可能没有） 
	 * 参数说明： 　
	 * type- 返回结果的类型，固定为data 　
	 * doctype - 返回结果的数据格式，xml或json或jsonp 　
	 * version - 版本，当前最新版本为1.1 　
	 * q - 要翻译的文本，必须是UTF-8编码，字符长度不能超过200个字符，需要进行urlencode编码 　
	 * only - 可选参数，dict表示只获取词典数据，translate表示只获取翻译数据，默认为都获取 　
	 * 注：词典结果只支持中英互译，翻译结果支持英日韩法俄西到中文的翻译以及中文到英语的翻译 
	 * errorCode： 　
	 * 0 - 正常 　
	 * 20 - 要翻译的文本过长 　
	 * 30 - 无法进行有效的翻译 　
	 * 40 - 不支持的语言类型 　
	 * 50 - 无效的key 　
	 * 60 - 无词典结果，仅在获取词典结果生效
	 * 
	 * @author xiezy
	 * @date 2016年11月23日
	 * @param q
	 * @return
	 */
	public static String translateByYouDao(String q){
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		
		//api地址
		String apiUrl = "http://fanyi.youdao.com/openapi.do";
		
		try {
			//请求参数
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("keyfrom", KEY_FROM);
			params.put("key", KEY);
			params.put("type", "data");
			params.put("doctype", "json");
			params.put("version", "1.1");
//			params.put("only", "translate");
			params.put("q", q);
			
			//完整url地址,经过urlencode
			apiUrl = apiUrl + "?" + urlencode(params);
			URL url = new URL(apiUrl);
			
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-agent", USER_AGENT);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            
            StringBuffer sb = new StringBuffer();
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null){
            	sb.append(strRead);
            }
            rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (reader != null) {
                try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
            if (conn != null) {
                conn.disconnect();
            }
		}
		
		return rs;
	}
	
	/**
	 * 将map型转为请求参数型 -- 经过urlencode
	 * @author xiezy
	 * @date 2016年11月23日
	 * @param data
	 * @return
	 */
	public static String urlencode(Map<String, Object> data){
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : data.entrySet()){
			try {
				sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue()+"", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	
	public static int COUNT = 0;
	public static int INDEX = 0;
	private static String getTranslation(String q){
		if(COUNT == 999){
 			List<String> list = new ArrayList<String>(TranslateUtils.accountMap.keySet());
			TranslateUtils.KEY_FROM = list.get(INDEX);
			TranslateUtils.KEY = TranslateUtils.accountMap.get(list.get(INDEX));
			
			COUNT = 0;
			
			if(INDEX == 16){
				TranslateUtils.KEY_FROM = "CaptainZe";
				TranslateUtils.KEY = "443510095";
				INDEX = 0;
			}else{
				INDEX++;
			}
		}
		
		COUNT++;
		
		try {
			String result = TranslateUtils.translateByYouDao(q);
			System.out.println(result);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = ObjectUtils.fromJson(result, Map.class);
			if(CollectionUtils.isNotEmpty(map) && map.containsKey("errorCode")){
				if((double)map.get("errorCode") == 0){
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) map.get("translation");
					if(CollectionUtils.isNotEmpty(list)){
						String temp = list.get(0).trim();
						return temp;
					}
				}
			}
			
			return "翻译出错,需要校验！--" + q;
		} catch (Exception e) {
			return "翻译出错,需要校验！--" + q;
		}
	}
	
}

