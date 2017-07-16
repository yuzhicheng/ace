package com.yzc.utils;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yzc.utils.collection.CollectionFilter;
import com.yzc.utils.collection.MapExecutor;

/**
 * 集合工具类
 *
 * @bifeng.liu
 * @see org.apache.commons.collections.CollectionUtils
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {
    /**
     * 私有化构造函数，不允许实例化该类
     */
    private CollectionUtils() {
    }

    /**
     * 检查指定Map是否为空
     * <p><pre>
     * CollectionUtils.isEmpty(null) = true
     * CollectionUtils.isEmpty(new HashMap()) = true
     * </pre>
     *
     * @param map 要检查的Map
     * @return 如果Map为空，则返回<code>true</code>
     */
    @SuppressWarnings("rawtypes")
	public static boolean isEmpty(final Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 检查指定Map是否不为空
     * <p><pre>
     * CollectionUtils.isEmpty(null) = false
     * CollectionUtils.isEmpty(new HashMap()) = false
     * </pre>
     *
     * @param map 要检查的Map
     * @return 如果Map为空，则返回<code>false</code>
     */
    @SuppressWarnings("rawtypes")
	public static boolean isNotEmpty(final Map map) {
        return !isEmpty(map);
    }

    /**
     * 对Map对象中的所有元素执行某个执行器
     * <p/>
     * 如果map为空或者执行器为NULL，则不处理。
     *
     * @param map
     * @param executor
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void forAllDo(Map map, MapExecutor executor) {
        if (!isEmpty(map) && executor != null) {
            Iterator<Map.Entry> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                executor.execute(entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * 将Set中的null去掉,如果T为String,将元素为""的也去掉
     * <p>Create Time: 2015年6月29日   </p>
     * <p>Create author: xiezy   </p>
     * @param targetSet
     * @return
     */
    public static final <T> Set<T> removeEmptyDeep(Set<T> targetSet){
        Set<T> result = new HashSet<T>();
        
        if(isEmpty(targetSet)){
            return targetSet;
        }else{
            for (Iterator<T> iterator = targetSet.iterator(); iterator.hasNext(); ) {
                T data = iterator.next();
                if (data != null) {
                    if(data instanceof String){
                        String dataStr = (String)data;
                        if(!dataStr.equals("")){
                            result.add(data);
                        }
                    }else {
                        result.add(data);
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * 将List中的null去掉,如果T为String,将元素为""的也去掉
     * <p>Create Time: 2015年7月14日   </p>
     * <p>Create author: xiezy   </p>
     * @param targetList
     * @return
     */
    public static final <T> List<T> removeEmptyDeep(List<T> targetList){
        List<T> result = new ArrayList<T>();
        
        if(isEmpty(targetList)){
            return targetList;
        }else{
            for (Iterator<T> iterator = targetList.iterator(); iterator.hasNext(); ) {
                T data = iterator.next();
                if (data != null) {
                    if(data instanceof String){
                        String dataStr = (String)data;
                        if(!dataStr.equals("")){
                            result.add(data);
                        }
                    }else {
                        result.add(data);
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * 获取集合中第一个满足条件的对象
     * 集合过滤器返回true时表示满足条件
     *
     * @param collection       集合
     * @param collectionFilter 集合过滤器
     * @param <T>
     * @return 满足条件的对象 or null
     */
    public static final <T> T first(final Collection<T> collection, CollectionFilter<T> collectionFilter) {
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (collectionFilter.filter(t)) {
                return t;
            }
        }
        return null;
    }
}
