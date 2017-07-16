package com.yzc.utils;

import java.util.HashMap;
import java.util.Map;

import com.yzc.support.MessageMapper;

/**
 * 消息统一接口状态转换
 * @author yzc
 * @date 2016年9月30日
 *
 */
public class MessageConvertUtil {
	public static Map<String,String> getMessageString(MessageMapper messageMapper){
		Map<String,String> m = new HashMap<String, String>();
		String code = messageMapper.getCode();
		String message = messageMapper.getMessage();		
		m.put("process_state", message);
		m.put("process_code", code);
		return m;
	}

}