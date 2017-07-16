//package com.yzc.controller;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.yzc.config.WebApplicationInitializer;
//import com.yzc.models.ResourceModel;
//import com.yzc.service.ResourceService;
//import com.yzc.support.CommonHelper;
//import com.yzc.support.CommonServiceHelper;
//import com.yzc.support.ErrorMessageMapper;
//import com.yzc.support.MessageException;
//import com.yzc.support.ParameterVerificationHelper;
//import com.yzc.support.enums.IndexSourceType;
//import com.yzc.support.statics.Constant;
//import com.yzc.utils.CollectionUtils;
//import com.yzc.utils.StringUtils;
//import com.yzc.vos.ListViewModel;
//import com.yzc.vos.ResourceViewModel;
//import com.yzc.vos.constant.IncludesConstant;
//import com.yzc.vos.constant.PropOperationConstant;
//
//@RestController
//@RequestMapping("/resource/{res_type}")
//public class ResourceController {
//	
//	private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);
//	
//    @Autowired
//    CommonServiceHelper commonServiceHelper;
//    
//    @Autowired
//    private ResourceService resourceService;
//	
//	
//    @RequestMapping(value = "/actions/db", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE },params = { "words","limit"})
//    public ListViewModel<ResourceViewModel> requestQueringByDB(
//            @PathVariable(value="res_type") String resType,
//            @RequestParam(required=false,value="rescode") String resCodes,
//            @RequestParam(required=false,value="include") String includes,
//            @RequestParam(required=false,value="category") Set<String> categories,
//            @RequestParam(required=false,value="category_exclude") Set<String> categoryExclude,
//            @RequestParam(required=false,value="relation") Set<String> relations,
//            @RequestParam(required=false,value="coverage") Set<String> coverages,
//            @RequestParam(required=false,value="prop") List<String> props,
//            @RequestParam(required=false,value="orderby") List<String> orderBy,
//            @RequestParam(required=false,value="reverse") String reverse,
//            @RequestParam(required=false,value="tags") List<String> tags,
//            @RequestParam String words,@RequestParam String limit){
//
//        return requestQuering4DB(resType, resCodes, includes, categories, categoryExclude, relations,coverages,props,orderBy,reverse,tags,words,limit);
//    }
//
//	@SuppressWarnings("unchecked")
//	private ListViewModel<ResourceViewModel> requestQuering4DB(String resType, String resCodes, String includes,Set<String> categories, Set<String> categoryExclude, 
//			Set<String> relations, Set<String> coverages,List<String> props, List<String> orderBy, String reverse, List<String> tags, String words, String limit) {
//       
//        //参数校验和处理
//        Map<String, Object> paramMap =requestParamVerifyAndHandle(resType,resCodes, includes, categories, categoryExclude,relations,coverages, props, orderBy,reverse,tags,words,limit);
//
//        // include
//        List<String> includesList = (List<String>)paramMap.get("include");
//
//        //categories
//        categories = (Set<String>)paramMap.get("category");
//
//        //categoryExclude
//        categoryExclude = (Set<String>)paramMap.get("categoryExclude");
//
//        // relations,格式:stype/suuid/r_type
//        List<Map<String,String>> relationsMap = (List<Map<String,String>>)paramMap.get("relation");
//
//        // coverages,格式:Org/uuid/SHAREING
//        List<String> coveragesList = (List<String>)paramMap.get("coverage");
////        if(CollectionUtils.isNotEmpty(coveragesList)){
////            List<String> sharingCoverageList = dealCoverageSharing(coveragesList);
////            if(CollectionUtils.isNotEmpty(sharingCoverageList)){
////                coveragesList.addAll(sharingCoverageList);
////            }
////        }
//
//        // props,语法 [属性] [操作] [值]
//        Map<String,Set<String>> propsMap = (Map<String,Set<String>>)paramMap.get("prop");
//
//        // orderBy
//        Map<String,String> orderMap = (Map<String,String>)paramMap.get("orderby");
//
//        //reverse,默认为false
//        boolean reverseBoolean = (boolean)paramMap.get("reverse");
//
//        //limit
//        limit = (String)paramMap.get("limit");
//
//        //调用service,获取到业务模型的list
//        ListViewModel<ResourceModel> rListViewModel = new ListViewModel<ResourceModel>();      
//        rListViewModel = resourceService.resourceQueryByDB(resType,resCodes, includesList, categories, categoryExclude,relationsMap, 
//        		coveragesList, propsMap, orderMap, words,limit, reverseBoolean,tags);
//   
//        
//        //ListViewModel<ResourceModel> 转换为  ListViewModel<ResourceViewModel>
//        ListViewModel<ResourceViewModel> result = new ListViewModel<ResourceViewModel>();
//        result.setTotal(rListViewModel.getTotal());
//        result.setLimit(rListViewModel.getLimit());
//        //items处理
//        List<ResourceViewModel> items = new ArrayList<ResourceViewModel>();
//        for(ResourceModel resourceModel : rListViewModel.getItems()){
//            ResourceViewModel resourceViewModel = changeToView(resourceModel, resType,includesList);
//            items.add(resourceViewModel);
//        }
//        result.setItems(items);
//
////        if (StringUtils.isNotEmpty(words)) {
////            // 记录用户的搜索关键字
////            Set<String> resTypeSet = checkAndDealResType(resType, resCodes);
////            String userId = userInfo == null ? null : userInfo.getUserId();
////            recordQueryHistory(words, resTypeSet, categories, result.getTotal(), bsyskey, userId);
////        }
//        return result;
//	}
//
//	private ResourceViewModel changeToView(ResourceModel model, String resourceType,List<String> includes) {
//		
//		return  CommonHelper.changeToView(model,resourceType,includes,commonServiceHelper);
//	}
//
//	private Map<String, Object> requestParamVerifyAndHandle(String resType, String resCodes, String includes,
//			Set<String> categories, Set<String> categoryExclude, Set<String> relations, Set<String> coverages,
//			List<String> props, List<String> orderBy, String reverse, List<String> tags, String words, String limit) {
//        //reverse,默认为false
//        boolean reverseBoolean = false;
//        if(StringUtils.isNotEmpty(reverse) && reverse.equals("true")){
//            reverseBoolean = true;
//        }
//        
//       // 0.res_type
//       checkResourceType(resType, resCodes);
//       
//       // 1.includes
//       List<String> includesList = IncludesConstant.getValidIncludes(includes);
//       
//       //先将参数中的5个Set中的null和""去掉
//       categories = CollectionUtils.removeEmptyDeep(categories);
//       categoryExclude = CollectionUtils.removeEmptyDeep(categoryExclude);
//       relations = CollectionUtils.removeEmptyDeep(relations);
//       coverages = CollectionUtils.removeEmptyDeep(coverages);
//       props = CollectionUtils.removeEmptyDeep(props);
//       
//       // 2.categories
//       if(CollectionUtils.isEmpty(categories)){
//           if(resType.equals(IndexSourceType.TeachingMaterialType.getName())){
//               categories = new HashSet<String>();
//               categories.add("K12/*");
//           }else{
//               categories = null;
//           }
//       }else {
//           categories = ParameterVerificationHelper.doAdapterCategories(categories);
//       }
//       
//       //categoryExclude
//       if(CollectionUtils.isEmpty(categoryExclude)){
//           categoryExclude = null;
//       }
//       
//       // 3.relations,格式:stype/suuid/r_type
//       List<Map<String,String>> relationsMap = new ArrayList<Map<String,String>>();
//       if(CollectionUtils.isEmpty(relations)){
//           relationsMap = null;
//       }else{
//           for(String relation : relations){
//               Map<String,String> map = ParameterVerificationHelper.relationVerification(relation);
//               relationsMap.add(map);
//           }
//       }
//       
//       // 4.coverages,格式:Org/uuid/SHAREING
//       List<String> coveragesList = new ArrayList<String>();
//       if(CollectionUtils.isEmpty(coverages)){
//           coveragesList = null;
//       }else{
//           for(String coverage : coverages){
//               String c = ParameterVerificationHelper.coverageVerification(coverage);
//               coveragesList.add(c);
//           }
//       }
//       
//       // 5.props,语法 [属性] [操作] [值]
//       Map<String,Set<String>> propsMap = new HashMap<String, Set<String>>();
//       //获取props的.properties文件,目的是筛选匹配支持的属性
//       Properties properties = WebApplicationInitializer.props_properties_db;
//       
//       if(CollectionUtils.isEmpty(props)){
//           propsMap = null;
//       }else{
//           for(String prop : props){
//               if(ParameterVerificationHelper.isRangeQuery(prop)){
//                   if (ParameterVerificationHelper.isRangeQuery(prop) && judgeOnlyContainTheOneRangOp(prop, PropOperationConstant.OP_GT)) {//Only GT
//
//                       this.dealTimeParam(resType, prop, properties, propsMap, "GT");
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && judgeOnlyContainTheOneRangOp(prop, PropOperationConstant.OP_LT)) {//Only LT
//
//                       this.dealTimeParam(resType, prop, properties, propsMap, "LT");
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && judgeOnlyContainTheOneRangOp(prop, PropOperationConstant.OP_LE)) {//Only LE
//
//                       this.dealTimeParam(resType, prop, properties, propsMap, "LE");
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && judgeOnlyContainTheOneRangOp(prop, PropOperationConstant.OP_GE)) {//Only GE
//
//                       this.dealTimeParam(resType, prop, properties, propsMap, "GE");
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && prop.contains(PropOperationConstant.OP_GT)
//                           && prop.contains(PropOperationConstant.OP_LT)) {//GT,LT
//
//                       dealTimeParam4HaveAndOp(resType, prop, properties, propsMap, PropOperationConstant.OP_GT, PropOperationConstant.OP_LT);
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && prop.contains(PropOperationConstant.OP_GT)
//                           && prop.contains(PropOperationConstant.OP_LE)) {//GT,LE
//
//                       dealTimeParam4HaveAndOp(resType, prop, properties, propsMap, PropOperationConstant.OP_GT, PropOperationConstant.OP_LE);
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && prop.contains(PropOperationConstant.OP_GT)
//                           && prop.contains(PropOperationConstant.OP_GE)) {//GT,GE
//
//                       dealTimeParam4HaveAndOp(resType, prop, properties, propsMap, PropOperationConstant.OP_GT, PropOperationConstant.OP_GE);
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && prop.contains(PropOperationConstant.OP_LT)
//                           && prop.contains(PropOperationConstant.OP_LE)) {//LT,LE
//
//                       dealTimeParam4HaveAndOp(resType, prop, properties, propsMap, PropOperationConstant.OP_LT, PropOperationConstant.OP_LE);
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && prop.contains(PropOperationConstant.OP_LT)
//                           && prop.contains(PropOperationConstant.OP_GE)) {//LT,GE
//
//                       dealTimeParam4HaveAndOp(resType, prop, properties, propsMap, PropOperationConstant.OP_LT, PropOperationConstant.OP_GE);
//                   }else if (ParameterVerificationHelper.isRangeQuery(prop) && prop.contains(PropOperationConstant.OP_LE)
//                           && prop.contains(PropOperationConstant.OP_GE)) {//LE,GE
//
//                       dealTimeParam4HaveAndOp(resType, prop, properties, propsMap, PropOperationConstant.OP_LE, PropOperationConstant.OP_GE);
//                   }
//               }
//               else if(prop.contains(" " + PropOperationConstant.OP_EQ + " ")){//eq
//                   List<String> elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_EQ + " "));
//                   //格式错误判断
//                   if(CollectionUtils.isEmpty(elements) || elements.size() != 2 || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))){
//
//                       LOG.error(prop + "--prop格式错误");
//                       throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                               ErrorMessageMapper.CommonSearchParamError.getCode(),prop + "--prop格式错误");
//                   }
//
//                   if(propsMap.containsKey(properties.getProperty(resType + "_" + elements.get(0)))){//已存在该属性
//                       Set<String> propValues = propsMap.get(properties.getProperty(resType + "_" + elements.get(0)));
//                       propValues.add(elements.get(1));
//                   }else{//新属性
//                       if(properties.containsKey(resType + "_" + elements.get(0))){
//                           Set<String> propValuesNew = new HashSet<String>();
//                           propValuesNew.add(elements.get(1));
//                           propsMap.put(properties.getProperty(resType + "_" + elements.get(0)), propValuesNew);
//                       }else{
//
//                           LOG.error(prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//                           throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                        		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                                   prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//                       }
//                   }
//               }else if(prop.contains(" " + PropOperationConstant.OP_IN + " ")){//in
//                   List<String> elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_IN + " "));
//                   //格式错误判断
//                   if(CollectionUtils.isEmpty(elements) || elements.size() != 2 || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))){
//
//                       LOG.error(prop + "--prop格式错误");
//
//                       throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                               prop + "--prop格式错误");
//                   }
//
//                   if(propsMap.containsKey(properties.getProperty(resType + "_" + elements.get(0)))){//已存在该属性
//                       Set<String> propValues = propsMap.get(properties.getProperty(resType + "_" + elements.get(0)));
//                       propValues.addAll(Arrays.asList(elements.get(1).split("\\|")));
//                   }else{//新属性
//                       if(properties.containsKey(resType + "_" + elements.get(0))){
//                           Set<String> propValuesNew = new HashSet<String>();
//                           propValuesNew.addAll(Arrays.asList(elements.get(1).split("\\|")));
//                           propsMap.put(properties.getProperty(resType + "_" + elements.get(0)), propValuesNew);
//                       }else{
//
//                           LOG.error(prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//                           throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                        		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                                   prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//                       }
//                   }
//               }else if(prop.contains(" " + PropOperationConstant.OP_NE + " ")){//ne
//                   List<String> elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_NE + " "));
//                   //格式错误判断
//                   if(CollectionUtils.isEmpty(elements) || elements.size() != 2 || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))){
//
//                       LOG.error(prop + "--prop格式错误");
//
//                       throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                               prop + "--prop格式错误");
//                   }
//
//                   if(propsMap.containsKey(properties.getProperty(resType + "_" + elements.get(0)) + "_NE")){//已存在该属性
//                       Set<String> propValues = propsMap.get(properties.getProperty(resType + "_" + elements.get(0)) + "_NE");
//                       propValues.add(elements.get(1));
//                   }else{//新属性
//                       if(properties.containsKey(resType + "_" + elements.get(0))){
//                           Set<String> propValuesNew = new HashSet<String>();
//                           propValuesNew.add(elements.get(1));
//                           propsMap.put(properties.getProperty(resType + "_" + elements.get(0)) + "_NE", propValuesNew);
//                       }else{
//
//                           LOG.error(prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//
//                           throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                        		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                                   prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//                       }
//                   }
//               }else if(prop.contains(" " + PropOperationConstant.OP_LIKE + " ")){//like
//                   List<String> elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_LIKE + " "));
//                   //格式错误判断
//                   if(CollectionUtils.isEmpty(elements) || elements.size() != 2 || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))){
//
//                       LOG.error(prop + "--prop格式错误");
//
//                       throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                               prop + "--prop格式错误");
//                   }
//
//                   if(propsMap.containsKey(properties.getProperty(resType + "_" + elements.get(0)) + "_LIKE")){//已存在该属性
//                       Set<String> propValues = propsMap.get(properties.getProperty(resType + "_" + elements.get(0)) + "_LIKE");
//                       propValues.add(elements.get(1));
//                   }else{//新属性
//                       if(properties.containsKey(resType + "_" + elements.get(0))){
//                           Set<String> propValuesNew = new HashSet<String>();
//                           propValuesNew.add(elements.get(1));
//                           propsMap.put(properties.getProperty(resType + "_" + elements.get(0)) + "_LIKE", propValuesNew);
//                       }else{
//
//                           LOG.error(prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//
//                           throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                        		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                                   prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//                       }
//                   }
//               }else{
//
//                   LOG.error(prop + "--prop目前支持eq,in,ne,like操作,以及支持create_time和lastupdate的gt,lt操作");
//
//                   throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                		   ErrorMessageMapper.CommonSearchParamError.getCode(),
//                           prop + "--prop目前支持eq,in,ne,like操作,以及支持create_time和lastupdate的gt,lt操作");
//               }
//           }
//       }
//       
//       //6.orderBy
//       Map<String,String> orderMap = new LinkedHashMap<String, String>();
//       if(CollectionUtils.isEmpty(orderBy)){
//           orderMap = null;
//       }else{
//           for(String order : orderBy){
//               List<String> elements = Arrays.asList(order.split(" "));
//
//               //格式错误判断
//               if(CollectionUtils.isEmpty(elements) || elements.size() != 2 || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))){
//
//                   LOG.error(orderBy + "--orderBy格式错误");
//                   throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
//                           orderBy + "--orderBy格式错误");
//               }
//
//               if(!properties.containsKey("order_" + elements.get(0))){
//                   throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
//                           elements.get(0) + "--该属性暂不支持排序");
//               }
//
//               if(!elements.get(1).equalsIgnoreCase(PropOperationConstant.OP_DESC) &&
//                       !elements.get(1).equalsIgnoreCase(PropOperationConstant.OP_ASC)){
//
//                   throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
//                           orderBy + "--orderBy格式错误,排序方式仅有DESC和ASC");
//               }
//
//               if(!orderMap.containsKey(properties.getProperty("order_" + elements.get(0)))){
//                   orderMap.put(properties.getProperty("order_" + elements.get(0)), elements.get(1));
//               }
//           }
//       }
//       
//       //7. limit
//       limit = CommonHelper.checkLimitMaxSize(limit);
//
//       Map<String, Object> paramMap = new HashMap<String, Object>();
//       paramMap.put("include", includesList);
//       paramMap.put("category", categories);
//       paramMap.put("categoryExclude", categoryExclude);
//       paramMap.put("relation", relationsMap);
//       paramMap.put("coverage", coveragesList);
//       paramMap.put("prop", propsMap);
//       paramMap.put("orderby", orderMap);
//       paramMap.put("reverse", reverseBoolean);
//       paramMap.put("limit", limit);
//       paramMap.put("words", words);
//
//       return paramMap;
//	}
//
//
//	private void checkResourceType(String resType, String resCodes) {
//		
//        if (resType.equals(IndexSourceType.ChapterType.getName())) {
//
//            LOG.error("resType不能为chapters");
//            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(), "resType不能为chapters");
//        } else if (resType.equals(Constant.RESTYPE_EDURESOURCE)) {
//        	
//            if (StringUtils.isEmpty(resCodes)) {
//                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(), "resType为"+ Constant.RESTYPE_EDURESOURCE+ "时,rescode不能为空");
//            }
//        } else {
//        	
//            commonServiceHelper.getRepository(resType);
//        }	
//	}
//	
//    /**
//     * 判断是哪个时间范围操作符
//     * @author yzc
//     * @date 2017年1月1日
//     * @param prop
//     * @param op
//     * @return
//     */
//    private boolean judgeOnlyContainTheOneRangOp(String prop,String op){
//        if(op.equals(PropOperationConstant.OP_GT)){
//            if(prop.contains(PropOperationConstant.OP_GT)
//                    && !prop.contains(PropOperationConstant.OP_LT)
//                    && !prop.contains(PropOperationConstant.OP_LE)
//                    && !prop.contains(PropOperationConstant.OP_GE)){
//                return true;
//            }
//        }else if(op.equals(PropOperationConstant.OP_LT)){
//            if(!prop.contains(PropOperationConstant.OP_GT)
//                    && prop.contains(PropOperationConstant.OP_LT)
//                    && !prop.contains(PropOperationConstant.OP_LE)
//                    && !prop.contains(PropOperationConstant.OP_GE)){
//                return true;
//            }
//        }else if(op.equals(PropOperationConstant.OP_LE)){
//            if(!prop.contains(PropOperationConstant.OP_GT)
//                    && !prop.contains(PropOperationConstant.OP_LT)
//                    && prop.contains(PropOperationConstant.OP_LE)
//                    && !prop.contains(PropOperationConstant.OP_GE)){
//                return true;
//            }
//        }else if(op.equals(PropOperationConstant.OP_GE)){
//            if(!prop.contains(PropOperationConstant.OP_GT)
//                    && !prop.contains(PropOperationConstant.OP_LT)
//                    && !prop.contains(PropOperationConstant.OP_LE)
//                    && prop.contains(PropOperationConstant.OP_GE)){
//                return true;
//            }
//        }
//
//        return false;
//    }
//    
//    /**
//     * 处理时间常数
//     * 
//     * @param resType
//     * @param prop
//     * @param properties
//     * @param propsMap
//     * @param gtOrLt
//     */
//    private void dealTimeParam(String resType,String prop,Properties properties,Map<String,Set<String>> propsMap,String op){
//    	
//        List<String> elements = new ArrayList<String>();
//        if(op.equals("LT")){
//            elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_LT + " "));
//        }else if(op.equals("GT")){
//            elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_GT + " "));
//        }else if(op.equals("GE")){
//            elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_GE + " "));
//        }else if(op.equals("LE")){
//            elements = Arrays.asList(prop.split(" " + PropOperationConstant.OP_LE + " "));
//        }
//
//        //格式错误判断
//        if(CollectionUtils.isEmpty(elements) || elements.size() != 2 || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))){
//        	
//            LOG.error(prop + "--prop格式错误");
//            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
//                    prop + "--prop格式错误");
//        }
//
//        if(propsMap.containsKey(properties.getProperty(resType + "_" + elements.get(0)) + "_" + op)){//已有属性
//            Set<String> propValues = propsMap.get(properties.getProperty(resType + "_" + elements.get(0)) + "_" + op);
//            String time = this.verificateAndFormatTime(elements.get(1));
//            propValues.add(time);
//        }else{
//            if(properties.containsKey(resType + "_" + elements.get(0))){
//                Set<String> propValuesNew = new HashSet<String>();
//                String time = this.verificateAndFormatTime(elements.get(1));
//                propValuesNew.add(time);
//                propsMap.put(properties.getProperty(resType + "_" + elements.get(0)) + "_" + op, propValuesNew);
//            }else{
//                LOG.error(prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//
//                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CommonSearchParamError.getCode(),
//                        prop + ":" + elements.get(0) + "--不支持该属性查询 OR 属性名错误(驼峰形式)");
//            }
//        }
//    }
//    
//    /**
//     * 时间的校验和格式化
//     * 
//     * @param time
//     * @return
//     */
//    private String verificateAndFormatTime(String time){
//    	
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//        //验证time是否符合时间格式
//        Date date = null;
//        try {
//            if(StringUtils.isNotEmpty(time)){
//                date  = sdf2.parse(time);
//                if(date != null){
//                    time = sdf2.format(date);
//                }
//            }
//        } catch (ParseException e) {
//            try {
//                if(StringUtils.isNotEmpty(time)){
//                    date  = sdf1.parse(time);
//                    if(date != null){
//                        time = sdf1.format(date);
//                    }
//                }
//            } catch (ParseException e2) {
//                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                        ErrorMessageMapper.CommonSearchParamError.getCode(),
//                        "时间格式错误,格式为:yyyy-MM-dd HH:mm:ss或 yyyy-MM-dd HH:mm:ss.SSS");
//            }
//        }
//
//        return time;
//    }
//    
//    /**
//     * 通用查询-时间参数带and的处理
//     * @author yzc
//     * @date 2017年1月1日
//     * @param resType
//     * @param prop
//     * @param properties
//     * @param propsMap
//     * @param op1
//     * @param op2
//     */
//    private void dealTimeParam4HaveAndOp(String resType,String prop,Properties properties,Map<String,Set<String>> propsMap,String op1,String op2){
//        List<String> elements = Arrays.asList(prop.split(" and "));
//        // 格式错误判断
//        if (CollectionUtils.isEmpty(elements) || elements.size() != 2
//                || StringUtils.isEmpty(elements.get(0)) || StringUtils.isEmpty(elements.get(1))) {
//
//            LOG.error(prop + "--prop格式错误");
//
//            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    ErrorMessageMapper.CommonSearchParamError.getCode(), prop + "--prop格式错误");
//        }
//
//        if (elements.get(0).contains(op1)) {
//            this.dealTimeParam(resType, elements.get(0), properties, propsMap, op1.toUpperCase());
//        }else {
//            this.dealTimeParam(resType, elements.get(0), properties, propsMap, op2.toUpperCase());
//        }
//
//        if (elements.get(1).contains(op1)) {
//            if(prop.startsWith("create_time")){
//                this.dealTimeParam(resType, "create_time " + elements.get(1), properties, propsMap, op1.toUpperCase());
//            }else{
//                this.dealTimeParam(resType, "lastupdate " + elements.get(1), properties, propsMap, op1.toUpperCase());
//            }
//        }else {
//            if(prop.startsWith("create_time")){
//                this.dealTimeParam(resType, "create_time " + elements.get(1), properties, propsMap, op2.toUpperCase());
//            }else{
//                this.dealTimeParam(resType, "lastupdate " + elements.get(1), properties, propsMap, op2.toUpperCase());
//            }
//        }
//    }
//    
//
//}
