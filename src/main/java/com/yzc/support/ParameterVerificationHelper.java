package com.yzc.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.yzc.support.enums.ResourceNdCode;
import com.yzc.support.statics.CoverageConstant;
import com.yzc.utils.StringUtils;

/**
 * 参数校验帮助类
 * 
 */
public class ParameterVerificationHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(ParameterVerificationHelper.class);
	
    /**
     * 针对category K12模式path自动添加【K12/】的适配
     */
	public static Set<String> doAdapterCategories(Set<String> categories){
		
        Set<String> afterDeal = new HashSet<String>(); 
        for(String category : categories){      
            //category为path时特殊处理
            if(category!=null && category.contains("/")){
            	String categoryPattern = category.split("/")[0];
            	if(categoryPattern.startsWith("$O")){
            		category = "K12/" + category;
            	}
            }
            //加入处理后的结果集
            afterDeal.add(category);
        }
        
        return afterDeal;
    }

    /**
     * 关系参数校验
     * @author yzc
     * @date 2017年1月1日
     * @param relation
     * @return
     */
	public static Map<String, String> relationVerification(String relation) {
		
    	Map<String,String> map = new HashMap<String, String>();
        //对于入参的relation每个在最后追加一个空格，以保证elemnt的size为3
        relation = relation + " ";
        List<String> elements = Arrays.asList(relation.split("/"));
        //格式错误判断
        if(elements.size() != 3){ 
            LOG.error(relation + "--relation格式错误");
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
                    relation + "--relation格式错误");
        }
        
        String resourceType = elements.get(0).trim();
        String resourceUuid = elements.get(1).trim();
        String relationType = elements.get(2).trim();
		
        //判断源资源是否存在,stype + suuid
        if(resourceUuid.endsWith("$")){	
			// "relation参数进行递归查询时,目前仅支持:chapters,knowledges"
			if (!ResourceNdCode.chapters.toString().equals(elements.get(0)) &&!ResourceNdCode.knowledges.toString().equals(elements.get(0))) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
						"relation参数进行递归查询时,目前仅支持:chapters,knowledges");
			}	
		}
        //relationType的特殊处理
        if(StringUtils.isEmpty(relationType) || "ASSOCIATE".equalsIgnoreCase(relationType)){
            relationType ="ASSOCIATE";
        }
        
        map.put("stype", resourceType);
        map.put("suuid", resourceUuid);
        map.put("rtype", relationType);
        
        return map;
	}

	public static String coverageVerification(String coverage) {
		
    	//对于入参的coverage每个在最后追加一个空格，以保证elemnt的size为3,用于支持Org/LOL/的模糊查询
        coverage = coverage + " ";
        List<String> elements = Arrays.asList(coverage.split("/"));
        //格式错误判断
        if(elements.size() != 3 || elements.get(0).trim().equals("") || elements.get(1).trim().equals("")){
            
            LOG.error(coverage + "--coverage格式错误");  
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
                    coverage + "--coverage格式错误");
        }

        //覆盖范围参数处理
        String ctType = elements.get(0).trim();
        String cTarget = elements.get(1).trim();
        String ct = elements.get(2).trim();
        
        if(!CoverageConstant.isCoverageTargetType(ctType,true)){
            
            LOG.error("覆盖范围类型不在可选范围内");       
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CoverageTargetTypeNotExist);
        }
        
        if(StringUtils.isEmpty(ct)){
        	ct = "*";
        }else{
        	if(!CoverageConstant.isCoverageStrategy(ct,true)){
                
                LOG.error("资源操作类型不在可选范围内");
                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CoverageStrategyNotExist);
            }
        }
        
        return ctType + "/" + cTarget + "/" + ct;
	}

    /**
     * 判断是否是时间范围查询
     * 
     */
	public static boolean isRangeQuery(String prop) {
		
		if(prop.startsWith("create_time") || prop.startsWith("lastupdate")){
    		return true;
    	}
    	return false;
	}

}
