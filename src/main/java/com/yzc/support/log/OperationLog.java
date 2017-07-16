package com.yzc.support.log;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import com.yzc.entity.LogInfo;
import com.yzc.service.LogService;

@Aspect
@Component//让此切面成为spring容器管理的Bean
public class OperationLog {
	
	private static final Logger LOG = LoggerFactory.getLogger(OperationLog.class);
	
    public static final String TOKENS = "/tokens";
    public static final String TOKENS_VALID = "/tokens/{access_token}/actions/valid";
    public static final List<String> FILTERS = Arrays.asList(TOKENS,TOKENS_VALID);
	
	@Autowired
    private LogService logService;
	
	private final static String POINT_CUT="access()";
	
    private final static String PREFIX="========";

    @Pointcut("execution(* com.yzc.controller.*.*(..))")
    public void access(){}
    
    @Before(POINT_CUT)
    public void beforeAccess(){
        System.out.println(PREFIX+"beforeAccess");
    }

    @After(POINT_CUT)
    public void afterAccess(){
        System.out.println(PREFIX+"afterAccess");
    }
    
    @AfterReturning(POINT_CUT)
    public void returnAccess(JoinPoint jp){
        System.out.println(PREFIX+"returnAccess");
        try {
            handle(jp,null);
        } catch (Exception e) {
        	LOG.error("记录操作日志时发生错误",e);
        }
    }
    
    @AfterThrowing(value = POINT_CUT,throwing = "e")
    public void throwAccess(JoinPoint jp,Throwable e) {
        System.out.println(PREFIX + "throwAccess");
        handle(jp,e);
    }
    
    private void handle(JoinPoint jp,Throwable throwable){
        //记录属性 时间|ip|用户|什么操作(uri)|操作方法(method)|操作参数|异常

        ServletRequestAttributes attr= (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request=attr.getRequest();
        if(!request.getMethod().equals(RequestMethod.GET.name())){
            RequestMapping requestMapping=((MethodSignature)jp.getSignature()).getMethod().getAnnotation(RequestMapping.class);
            if(requestMapping==null)
                return;

            LogInfo logInfo=new LogInfo();
            logInfo.setDate(new Date());
            logInfo.setIp(request.getRemoteAddr());

//            Object user=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if(user!=null && user instanceof UserInfo) {
//                UserInfo info = (UserInfo) user;
//                logInfo.setUserId(Long.valueOf(info.getUserId()));
//                logInfo.setUserName(info.getUserName());
//            }


            String[] subMapping=requestMapping.value();
            requestMapping=jp.getTarget().getClass().getAnnotation(RequestMapping.class);
            String mapping;
            if(subMapping.length==0)
                mapping="/";
            else
                mapping=subMapping[0];
            if(requestMapping!=null){
                String[] rootMapping=requestMapping.value();
                mapping=rootMapping[0]+mapping;
            }
            if (FILTERS.contains(mapping)) {
                return;
            }
            logInfo.setAction(mapping);

            logInfo.setUri(request.getRequestURI());
            logInfo.setMethod(request.getMethod());
            logInfo.setArgs(getParam(jp).toString());

            addLog(logInfo);

        }else{
            if(LOG.isDebugEnabled())
            	LOG.debug("a get request:"+request.getRequestURI());
        }
    }
    
    private StringBuilder getParam(JoinPoint jp){
        StringBuilder sb=new StringBuilder();
        Object[] params=jp.getArgs();
        if(params!=null && params.length>0) {
            for(Object item:params){
                if(item instanceof ExtendedServletRequestDataBinder || item instanceof BindingResult || item instanceof CommonsMultipartFile || item instanceof HttpServletRequest || item instanceof HttpServletResponse)
                    continue;
                if(item!=null){
                    if(item.getClass().isPrimitive()){
                        sb.append(item.toString());
                    }else{
                    	sb.append(com.yzc.utils.ObjectUtils.toJson(item));
                    }
                }else{
                    sb.append("NULL");
                }
                sb.append("&");
            }
        }
        if(sb.toString().endsWith("&")){
        	sb.deleteCharAt(sb.length()-1);
        }
        return sb;
    }
    
    private void addLog(LogInfo log){
    	logService.createLog(log);
        if(LOG.isDebugEnabled())
        	LOG.debug("operation:"+com.yzc.utils.ObjectUtils.toJson(log));
    }
 
}
