package com.yzc.utils;



import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UrlParamParseUtil {
    /** 
     * 去掉url中的路径，留下请求参数部分 
     * @param strURL url地址 
     * @return url请求参数部分 
     */ 
     private static String TruncateUrlPage(String strURL) 
     { 
         String strAllParam=null; 
         strURL=strURL.trim(); 
         if(strURL.indexOf("?")>0 && strURL.indexOf("?")<strURL.length()-1) {
             strAllParam = strURL.substring(strURL.indexOf("?")+1);
         }
         return strAllParam; 
     } 
     /** 
      * 解析出url参数中的键值对 
      * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中 
      * @param URL url地址 
      * @return url请求参数部分 
      */ 
     public static Map<String, String> URLRequest(String URL) 
     {
         Map<String, String> mapRequest = new HashMap<String, String>(); 
         String[] arrSplit=null; 
         String strUrlParam=TruncateUrlPage(URL); 
         if(strUrlParam==null) 
         { 
             return mapRequest; 
         } 
         //每个键值为一组 
         arrSplit=strUrlParam.split("[&]"); 
         for(String strSplit:arrSplit) 
         { 
             String[] arrSplitEqual=null; 
             arrSplitEqual= strSplit.split("[=]"); 
             //解析出键值 
             if(arrSplitEqual.length>1) 
             { 
                 List<Object> values = Arrays.asList(ArrayUtils.subarray(arrSplitEqual, 1, arrSplitEqual.length));
                 //正确解析 
                 mapRequest.put(arrSplitEqual[0], StringUtils.join(values, "=")); 
             } 
             else 
             { 
                 if(arrSplitEqual[0]!="") 
                 { 
                     //只有参数没有值，不加入 
                     mapRequest.put(arrSplitEqual[0], ""); 
                 } 
             } 
         } 
         return mapRequest; 
     }
     
     public static void main(String[] args) {
         String url = "localhost:8080/esp-lifecycle/v0.6/assets/actions/query?words&limit=(0,20)&coverage=Org/nd/owner&coverage=Org/nd/&include=TI,LC,CG,CR,EDU&prop=publisher eq NetDragon";
         String params=TruncateUrlPage(url);
         String[] arrSplit=params.split("[&]");
         for(String s:arrSplit){
        	 System.out.println(s);
         }
         
         Map<String,String> map = UrlParamParseUtil.URLRequest(url);
         System.out.println(ObjectUtils.toJson(map));
     }
}
