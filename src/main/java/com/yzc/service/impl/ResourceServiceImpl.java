package com.yzc.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;
import com.yzc.dao.CommonDao;
import com.yzc.dao.ResourceDao;
import com.yzc.dao.impl.ResourceDaoImpl;
import com.yzc.entity.CategoryData;
import com.yzc.entity.ResCategory;
import com.yzc.entity.TechInfo;
import com.yzc.entity.resource.Education;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.ResClassificationModel;
import com.yzc.models.ResCoverageModel;
import com.yzc.models.ResEducationalModel;
import com.yzc.models.ResLifeCycleModel;
import com.yzc.models.ResRightModel;
import com.yzc.models.ResTechInfoModel;
import com.yzc.models.ResourceModel;
import com.yzc.models.TechnologyRequirementModel;
import com.yzc.repository.CategoryDataRepository;
import com.yzc.repository.ResCategoryRepository;
import com.yzc.repository.ResourceRepository;
import com.yzc.repository.TechInfoRepository;
import com.yzc.service.CategoryService;
import com.yzc.service.CoverageService;
import com.yzc.service.ResourceService;
import com.yzc.support.CommonHelper;
import com.yzc.support.CommonServiceHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.OperationType;
import com.yzc.support.enums.RecordStatus;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.ListViewModel;

@Service
public class ResourceServiceImpl implements ResourceService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceServiceImpl.class);
	private final static ExecutorService executorService = CommonHelper.getPrimaryExecutorService();
	
    @Autowired
    private ResourceDao resourceDao;
    
    @Autowired
    private CommonServiceHelper commonServiceHelper;
    
    @Autowired
    private CommonDao commonDao;
    
    @Autowired
    private TechInfoRepository techInfoRepository;
    
    @Autowired
    private CategoryDataRepository categoryDataRepository;
    
    @Autowired
    private ResCategoryRepository resourceCategoryRepository;
    
    @Autowired
    @Qualifier("coverageServiceImpl")
    private CoverageService coverageService;
    
	@Autowired
	@Qualifier("CategoryServiceImpl")
	private CategoryService categoryService;

	@Override
	public ListViewModel<ResourceModel> resourceQueryByDB(String resType, String resCodes, List<String> includes,
			Set<String> categories, Set<String> categoryExclude, List<Map<String, String>> relations,
			List<String> coverages, Map<String, Set<String>> propsMap, Map<String, String> orderMap, String words,
			String limit, boolean reverse, List<String> tags) {

		ListViewModel<ResourceModel> rListViewModel = new ListViewModel<ResourceModel>();
		rListViewModel.setLimit(limit);
		// 判断使用IN还是EXISTS
		boolean useIn = resourceDao.judgeUseInOrExists(resType, resCodes, categories, categoryExclude, relations,
				coverages, propsMap, words,reverse,tags);
		
		// 查总数和Items使用线程同时查询
		List<Callable<QueryThread>> threads = new ArrayList<Callable<QueryThread>>();
		QueryThread countThread = new QueryThread(true, resType, resCodes, includes, categories, categoryExclude, relations,coverages, propsMap, null, words, limit,reverse, useIn, tags);
		QueryThread queryThread = null;
		if (ResourceDaoImpl.judgeUseRedisOrNot("(0,1)",coverages, orderMap)) {// 如果是走Redis的,useIn=true
			queryThread = new QueryThread(false, resType, resCodes, includes, categories, categoryExclude, relations,coverages, propsMap, orderMap, words, limit,reverse, true,tags);
		} else {
			queryThread = new QueryThread(false, resType, resCodes, includes, categories, categoryExclude, relations,coverages, propsMap, orderMap, words, limit, reverse, useIn,tags);
		}
		threads.add(countThread);
		threads.add(queryThread);

		try {
			List<Future<QueryThread>> results = executorService.invokeAll(threads, 10 * 60, TimeUnit.SECONDS);
			for (Future<QueryThread> result : results) {
				try {
					if (result.get().isCount()) {
						rListViewModel.setTotal(result.get().getTotal());
					} else {
						rListViewModel.setItems(result.get().getItems());
					}
				} catch (ExecutionException e) {
					LOG.error("LC/COMMON_QUERY_GET_FAIL");
					throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, "LC/COMMON_QUERY_GET_FAIL",
							e.getMessage());
				}
			}
		} catch (InterruptedException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, "LC/QUERY_THREAD_FAIL", e.getMessage());
		}

		Integer result[] = ParamCheckUtil.checkLimit(limit);
		if (result[0] == 0 && rListViewModel.getItems().size() < result[1]) {
			rListViewModel.setTotal(new Long(rListViewModel.getItems().size()));
		}

		return rListViewModel;
	}
	
    /**
     * 内部类
     * <p>Create Time: 2015年11月30日           </p>
     * @author xiezy
     */
    class QueryThread implements Callable<QueryThread> {
        private boolean isCount;
        private String resType;
        private String resCodes;
        private List<String> includes;
        private Set<String> categories;
        private Set<String> categoryExclude;
        private List<Map<String, String>> relations;
        private List<String> coverages;
        private Map<String, Set<String>> propsMap;
        private Map<String, String>orderMap;
        private String words;
        private String limit;
        private boolean reverse;
        private boolean useIn;
        private List<String> tags;
        
        //返回值
        private Long total;
        private List<ResourceModel> items;
        
        public Long getTotal() {
            return total;
        }

        public List<ResourceModel> getItems() {
            return items;
        }

        public boolean isCount() {
            return isCount;
        }

        QueryThread(boolean isCount, String resType,String resCodes, List<String> includes,Set<String> categories, Set<String> categoryExclude, List<Map<String, String>> relations,List<String> coverages,
            Map<String, Set<String>> propsMap,Map<String, String> orderMap, String words, String limit,boolean reverse,boolean useIn,List<String> tags){
            this.isCount = isCount;
            this.resType = resType;
            this.resCodes = resCodes;
            this.includes = includes;
            this.categories = categories;
            this.categoryExclude = categoryExclude;
            this.relations = relations;
            this.coverages = coverages;
            this.propsMap = propsMap;
            this.orderMap = orderMap;
            this.words = words;
            this.limit = limit;
            this.reverse = reverse;
            this.useIn = useIn;
            this.tags = tags;
        }
        
        @Override
        public QueryThread call() throws Exception {
            if(isCount){
                this.total = resourceDao.commomQueryCount(resType, resCodes, categories, categoryExclude, relations, coverages, propsMap, words, limit,reverse,useIn,tags);
            }else{
                this.items = resourceDao.commomQueryByDB(resType, resCodes, includes, categories, categoryExclude, relations,coverages, propsMap, orderMap, words, limit,reverse,useIn,tags);
            }
            
            return this;
        }
    }

	@Override
	public ResourceModel create(String resourceType, ResourceModel resourceModel) {
		
		//1、判断主键id是否已经存在
    	boolean flag = isDuplicateId(resourceType,resourceModel.getIdentifier());
    	if(flag){
    		throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckDuplicateIdFail);
    	}
    	
    	//2、判断资源编码是否唯一
    	if(StringUtils.isNotEmpty(resourceModel.getResCode())){
        	boolean flagCode = isDuplicateCode(resourceType,resourceModel.getIdentifier(),resourceModel.getResCode());
        	if(flagCode){
        		throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckDuplicateCodeFail);
        	}
    	}
 	
        // 3、基本属性的处理
        dealBasicInfo(resourceType, resourceModel, OperationType.CREATE, null);

        // 4、tech_info属性处理
        dealTechInfo(resourceType, resourceModel, OperationType.CREATE);

        // 5、coverages属性处理
        dealCoverages(resourceType, resourceModel);

        // 6、categories属性处理
        dealCategories(resourceType, resourceModel, OperationType.CREATE);

        // 7、relations属性处理
//        dealRelations(resourceType, resourceModel);
        
        return resourceModel;
		
	}
	
	/**
     * 判断主键ID是否重复
     * 
     * @param resourceType
     * @param identifier
     * @return
     */
    private boolean isDuplicateId(String resourceType,String identifier){
    	boolean uploadable = commonServiceHelper.isUploadable(resourceType);
    	//有上传接口的资源id是由外部传进来的，需要做下id重复性判断
    	if(uploadable){
    		int num = resourceDao.queryResCountByResId(resourceType, identifier);
    		if(num > 0){
    			return true;
    		}
    	}
    	return false;
    }  
    
    /**
     * 判断资源编码是否重复
     * 
     * @param resourceType
     * @param identifier
     * @return
     */
    private boolean isDuplicateCode(String resourceType,String identifier,String code){
    	
    	int num=resourceDao.queryCodeCountByResId(resourceType, identifier, code);
	
		if(num > 0){
			return true;
		}
    	return false;
    }
    
    
	@SuppressWarnings("unchecked")
	private boolean dealBasicInfo(String resourceType, ResourceModel resourceModel, OperationType operationType, Education oldBean) {
	
		@SuppressWarnings("rawtypes")
    	ResourceRepository resourceRepository =  commonServiceHelper.getRepository(resourceType);
        // 转换为数据模型
        // 所有通用接口支持的资源在sdk 都继承了Education
        Education education = changeModelToBean(resourceType, resourceModel);
        try {
            if (operationType == OperationType.CREATE) {
                // 默认值
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                education.setCreateTime(ts);
                education.setUpdateTime(ts);
//                if(ResourceNdCode.knowledges.toString().equals(resourceType)){
//                    //需要处理下树型结构
//                    Chapter knowledge = (Chapter)education;
//                    KnowledgeModel knowledgeModel = (KnowledgeModel) resourceModel;
//                    if(StringUtils.isNotBlank(knowledgeModel.getExtProperties().getRootNode())){
//                    	knowledge.setTeachingMaterial(knowledgeModel.getExtProperties().getRootNode());
//                    }else{
//                    	knowledge.setTeachingMaterial(getSubjectWithCheck(resourceModel));
//                    }
//                    TreeDirection treeDirection = TreeDirection.fromString(knowledgeModel.getExtProperties().getDirection());
//                    TreeModel current = treeService.insertLeaf(getTreeTargetAndParent(knowledgeModel,knowledge), treeDirection);
//
//                    // 3.转换为SDK的Knowledge
//                    knowledge.setLeft(current.getLeft());
//                    knowledge.setRight(current.getRight());
//                    knowledge.setParent(current.getParent());
//                }
                LOG.debug("调用sdk方法：add");
                LOG.debug("创建资源类型:{},uuid:{}", resourceType, education.getIdentifier());
                education = (Education) resourceRepository.add(education);

            } else if (operationType == OperationType.UPDATE) {
                
//                if(ResourceNdCode.knowledges.toString().equals(resourceType)){
//                    //树型结构不变
//                    Chapter knowledge = (Chapter) education;
//                    Chapter oldKnowledge = (Chapter) oldBean;
//                    //left, right, teaching_material,parent
//                    knowledge.setLeft(oldKnowledge.getLeft());
//                    knowledge.setRight(oldKnowledge.getRight());
//                    knowledge.setTeachingMaterial(oldKnowledge.getTeachingMaterial());
//                    knowledge.setParent(oldKnowledge.getParent());
//                }
                // 不可变的值特别处理
                education.setCreateTime(oldBean.getCreateTime());              
                ResLifeCycleModel lc = resourceModel.getLifeCycle();
                if(lc == null || StringUtils.isEmpty(lc.getCreator())){
                	education.setCreator(oldBean.getCreator());
                }              
                education.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				// 更新global字段
				updateGlobalField(education, oldBean, false);
                LOG.debug("调用sdk方法：update");
                LOG.debug("修改资源类型:{},uuid:{}", resourceType, education.getIdentifier());
                education = (Education) resourceRepository.update(education);
            }
        } catch (EspStoreException e) {
            LOG.error("创建操作出错了", e);
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
        }
        // 转换为业务模型
        // 只将主表的内容转换出来
        changeBeanToModel4Basic(resourceModel, education);
        return true;
		
	}
	
    /**
     * @param resourceType
     * @param resourceModel
     * @param operationType
     * @since
     */
    private boolean dealTechInfo(String resourceType, ResourceModel resourceModel, OperationType operationType) {
        
        String uuid = resourceModel.getIdentifier();
        List<ResTechInfoModel> techInfoList = resourceModel.getTechInfoList();
        List<TechInfo> list = new ArrayList<TechInfo>();
        List<ResTechInfoModel> returnList = new ArrayList<ResTechInfoModel>();
        if (CollectionUtils.isNotEmpty(techInfoList)) {
            for (ResTechInfoModel rtim : techInfoList) {
                TechInfo ti = BeanMapperUtils.beanMapper(rtim, TechInfo.class);
				ti.setCreateTime(new Timestamp(System.currentTimeMillis()));
				ti.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				ti.setState(RecordStatus.NORMAL);
                ti.setIdentifier(UUID.randomUUID().toString());
                ti.setResource(uuid);
                ti.setResType(resourceType);
                ti.setRequirements(ObjectUtils.toJson(rtim.getRequirements()));
                list.add(ti);
            }
        }
        try {
            // 先清空旧数据
            // 应该也需要resourceType (xuzy)
            // 但要求外界创建时，id必须不一样。
            if (OperationType.UPDATE == operationType) {
                commonDao.deleteTechInfoByResource(resourceType,uuid);
            }
            if (!list.isEmpty()) {
                LOG.debug("调用sdk方法：batchAdd");
                list = techInfoRepository.batchAdd(list);
                for (TechInfo techInfo : list) {
                    // 数据回填
                    ResTechInfoModel rtim = BeanMapperUtils.beanMapper(techInfo, ResTechInfoModel.class);
                    // requirements单独处理
                    List<TechnologyRequirementModel> req = ObjectUtils.fromJson(techInfo.getRequirements(), new TypeToken<List<TechnologyRequirementModel>>() {});
                    rtim.setRequirements(req);
                    returnList.add(rtim);
                }

            }
            resourceModel.setTechInfoList(returnList);
        } catch (EspStoreException e) {
            LOG.error("技术属性创建操作出错了", e);
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
        }
        return true;
    }
    
	/**
     * @param resourceType
     * @param resourceModel
     * @since
     */
    private boolean dealCoverages(String resourceType, ResourceModel resourceModel) {
        
        List<ResCoverageModel> coverages = resourceModel.getCoverages();
        List<ResCoverageModel> coverageList=new ArrayList<ResCoverageModel>();
        if (CollectionUtils.isNotEmpty(coverages)) {
            for (ResCoverageModel rcm : coverages) {
            	rcm.setIdentifier(UUID.randomUUID().toString());
            	rcm.setResource(resourceModel.getIdentifier());
            	rcm.setResType(resourceType);
            	coverageList.add(rcm);
            }
        }       
        if(CollectionUtils.isNotEmpty(coverageList)){
        	
        	coverageService.batchCreateCoverage(coverageList, true);          
        }
        return true;

    }
    
	/**
     * @author linsm
     * @param resourceType
     * @param resourceModel
     * @param operationType
     * @since
     */
    private boolean dealCategories(String resourceType, ResourceModel resourceModel, OperationType operationType) {
        // 后期应该使用上resourceType
        // rsource uuid
        String uuid = resourceModel.getIdentifier();
        List<ResClassificationModel> categories = resourceModel.getCategoryList();
        Set<ResClassificationModel> resClassificationModelSet = new HashSet<ResClassificationModel>(categories);
        List<ResCategory> resourceCategories = new ArrayList<ResCategory>();
        if (CollectionUtils.isNotEmpty(categories)) {
            Set<String> ndCodeSet = new HashSet<String>();
            List<CategoryData> categoryDataSet = null;
            for (ResClassificationModel resClassificationModel : categories) {
                ndCodeSet.add(resClassificationModel.getTaxoncode());
            }
            LOG.debug("调用sdk方法：getListWhereInCondition");
			try {
				categoryDataSet = categoryDataRepository.getListWhereInCondition("ndCode",new ArrayList<String>(ndCodeSet));
			} catch (EspStoreException e) {

				LOG.error("根据ndCode获取维度数据操作出错了", e);

				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
			}
			if (CollectionUtils.isEmpty(categoryDataSet) || categoryDataSet.size() != ndCodeSet.size()) {

                LOG.error(ErrorMessageMapper.CheckNdCodeFail.getMessage() + ndCodeSet);

                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNdCodeFail);
            }
            // 取 分类维度到shortName的映射
            Map<String, String> categoryNdCodeToShortNameMap = commonServiceHelper.getCategoryByData(categoryDataSet);
           
            for (CategoryData cd : categoryDataSet) {
                for (ResClassificationModel resClassificationModel : resClassificationModelSet) {
                    if (resClassificationModel.getTaxoncode().equals(cd.getNdCode())) {
                        // 通过取维度数据详情，补全resource_category中间表的数据
                        resClassificationModel.setShortName(cd.getShortName());
                        resClassificationModel.setTaxoncodeId(cd.getIdentifier());
                        resClassificationModel.setTaxonname(cd.getTitle());
                        resClassificationModel.setCategoryCode(cd.getNdCode().substring(0, 2));

                        // 取维度的shortName
                        if (categoryNdCodeToShortNameMap.get(resClassificationModel.getCategoryCode()) != null) {
                            resClassificationModel.setCategoryName(categoryNdCodeToShortNameMap.get(resClassificationModel.getCategoryCode()));
                        }
                        // 转换到sdk bean
                        ResCategory resourceCategory = BeanMapperUtils.beanMapper(resClassificationModel,ResCategory.class);
                        resourceCategory.setResource(uuid);
                        resourceCategory.setIdentifier(UUID.randomUUID().toString());                   
                        //资源分类维度
                        resourceCategory.setResType(resourceType);
                        resourceCategory.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        resourceCategory.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        resourceCategory.setState(RecordStatus.NORMAL);
                        resourceCategories.add(resourceCategory);
                    }
                }
            }
            if (!resourceCategories.isEmpty()) {
                try {
                    // 先将关联关系数据删除
                    // 同tech_info
                    if (OperationType.UPDATE == operationType) {
                        commonDao.deleteResourceCategoryByResource(resourceType,uuid);
                    }
                    LOG.debug("调用sdk方法：batchAdd");
                    resourceCategories = resourceCategoryRepository.batchAdd(resourceCategories);
                    // 返回值转换为model
                    if (CollectionUtils.isNotEmpty(resourceCategories)) {
                        categories = new ArrayList<ResClassificationModel>();
                        for (ResCategory resourceCategory : resourceCategories) {
                            ResClassificationModel model = BeanMapperUtils.beanMapper(resourceCategory,ResClassificationModel.class);
                            categories.add(model);
                        }
                        resourceModel.setCategoryList(categories);
                    }
                } catch (EspStoreException e) {
                    LOG.error(ErrorMessageMapper.StoreSdkFail.getMessage(), e);
                    throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
                }
            }
        }
        
        return true;
    }

	/**
	 * 更新国际化域
	 * @param education
	 * @param oldBean
	 * @param isToSetOld（是否设置旧的bean）
     * @return
     */
	private void updateGlobalField(Education education, Education oldBean, boolean isToSetOld) {
		
		if (isToSetOld) {
			setGlobalField(oldBean, education, oldBean);
		} else {
			setGlobalField(education, education, oldBean);
		}
		
	}
	
	private void setGlobalField(Education change, Education education, Education oldBean) {
		Map<String, String> title = mergeGlobalField(education.getGlobalTitle(), oldBean.getGlobalTitle());
		Map<String, String> desc = mergeGlobalField(education.getGlobalDescription(), oldBean.getGlobalDescription());
		Map<String, String> keyword = mergeGlobalField(education.getGlobalKeywords(), oldBean.getGlobalKeywords());
		Map<String, String> tag = mergeGlobalField(education.getGlobalTags(), oldBean.getGlobalTags());
		Map<String, String> eduDesc = mergeGlobalField(education.getGlobalEduDescription(), oldBean.getGlobalEduDescription());
		Map<String, String> crDesc = mergeGlobalField(education.getGlobalCrDescription(), oldBean.getGlobalCrDescription());

		change.setGlobalTitle(title);
		change.setGlobalDescription(desc);
		change.setGlobalKeywords(keyword);
		change.setGlobalTags(tag);
		change.setGlobalEduDescription(eduDesc);
		change.setGlobalCrDescription(crDesc);
	}

	/**
	 * 合并map
	 * @param newGlobal
	 * @param oldGlobal
     * @return
     */
	private Map<String, String> mergeGlobalField(Map<String, String> newGlobal, Map<String, String> oldGlobal) {
		if (CollectionUtils.isEmpty(oldGlobal)) {
			return newGlobal;
		} else {
			if (CollectionUtils.isNotEmpty(newGlobal)) {
				oldGlobal.putAll(newGlobal);
			}
			return oldGlobal;
		}
	}
	
	private Education changeModelToBean(String resourceType, ResourceModel resourceModel) {
	       // 基础属性
        Education education = null;
        education = (Education) BeanMapperUtils.beanMapper(resourceModel,commonServiceHelper.getBeanClass(resourceType));
        education.setPrimaryCategory(resourceType);
        // 生命周期属性
        if (resourceModel.getLifeCycle() != null) {
            //直接手动转换，个数是已知，目前对方法的控制力不够
            ResLifeCycleModel resLifeCycleModel = resourceModel.getLifeCycle();
            education.setVersion(resLifeCycleModel.getVersion());
            education.setStatus(resLifeCycleModel.getStatus());
            education.setEnable(resLifeCycleModel.isEnable());
            education.setCreator(resLifeCycleModel.getCreator());
            education.setPublisher(resLifeCycleModel.getPublisher());
            education.setProvider(resLifeCycleModel.getProvider());
            education.setProviderSource(resLifeCycleModel.getProviderSource());
            education.setProviderMode(resLifeCycleModel.getProviderMode());
            if (resLifeCycleModel.getCreateTime() != null) {
                education.setCreateTime(new Timestamp(resLifeCycleModel.getCreateTime().getTime()));
            }
            if (resLifeCycleModel.getUpdateTime() != null) {
                education.setUpdateTime(new Timestamp(resLifeCycleModel.getUpdateTime().getTime()));
            }
        }

        // 资源的教育属性
        if (resourceModel.getEducationInfo() != null) {
            ResEducationalModel resEducationalModel = resourceModel.getEducationInfo();
            education.setAgeRange(resEducationalModel.getAgeRange());
            education.setContext(resEducationalModel.getContext());
            education.setEduDescription(resEducationalModel.getDescription());
			education.setGlobalEduDescription(resEducationalModel.getGlobalEduDescription());
            education.setDifficulty(resEducationalModel.getDifficulty());
            education.setEndUserType(resEducationalModel.getEndUserType());
            education.setInteractivity(resEducationalModel.getInteractivity());
            education.setInteractivityLevel(resEducationalModel.getInteractivityLevel());
            education.setEduLanguage(resEducationalModel.getLanguage()); 
            education.setLearningTime(resEducationalModel.getLearningTime());
            if(resEducationalModel.getSemanticDensity() != null){
                education.setSemanticDensity(resEducationalModel.getSemanticDensity().intValue());
            }          
        }
        // 版权属性
        if (resourceModel.getCopyright() != null) {
            ResRightModel resRightModel = resourceModel.getCopyright();
            education.setAuthor(resRightModel.getAuthor());
            education.setCrDescription(resRightModel.getDescription());
			education.setGlobalCrDescription(resRightModel.getGlobalCrDescription());
            education.setCrRight(resRightModel.getRight());
            education.setHasRight(resRightModel.getHasRight());
            education.setRightStartDate(resRightModel.getRightStartDate());
            education.setRightEndDate(resRightModel.getRightEndDate());
        }
        // 扩展属性：
        try {
            Field[] fs = resourceModel.getClass().getDeclaredFields();
            Field f = null;
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].getName().equals("extProperties")) {
                    f = fs[i];
                    break;
                }
            }
            if (f != null) {
                Method m = resourceModel.getClass().getMethod("getExtProperties");
                Object object = m.invoke(resourceModel);
                //注意有可能会有属性值被重写。
                if(object !=null){
                    BeanUtils.copyProperties(education, object); 
                }
                
            }
        } catch (Exception e) {
            LOG.error("反射处理扩展属性出错！", e);
        }

        return education;
	}
	
    /**
     * 更新存在于主表的信息到resourceModel
     * 
     * @param resourceModel
     * @param education
     */
    private void changeBeanToModel4Basic(ResourceModel resourceModel, Education education) {

        ResourceModel keepResourceModel = new ResourceModel();
        keepResourceModel.setTechInfoList(resourceModel.getTechInfoList());
        keepResourceModel.setCoverages(resourceModel.getCoverages());
        keepResourceModel.setCategoryList(resourceModel.getCategoryList());
        keepResourceModel.setRelations(resourceModel.getRelations());

        // 基础属性
        try {
            BeanUtils.copyProperties(resourceModel, education);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e); // 这种错误，有没有必要使用lc code. (应该是调试阶段处理的)
        }
        // 生命周期属性
        ResLifeCycleModel resLifeCycleModel = BeanMapperUtils.beanMapper(education, ResLifeCycleModel.class);
        resourceModel.setLifeCycle(resLifeCycleModel);
        // 版权属性
        ResRightModel resRightModel = BeanMapperUtils.beanMapper(education, ResRightModel.class);
        if (resRightModel != null) {
            resRightModel.setDescription(education.getCrDescription());
        }
        resourceModel.setCopyright(resRightModel);
        // 资源的教育属性
        ResEducationalModel resEducationalModel = BeanMapperUtils.beanMapper(education, ResEducationalModel.class);
        if (resEducationalModel != null) {
            resEducationalModel.setDescription(education.getEduDescription());
            //注意下这里与commonhelper不一致，但认为这个是对的。
            resEducationalModel.setLanguage(education.getEduLanguage());
        }
        resourceModel.setEducationInfo(resEducationalModel);
        // 扩展属性：
        try {
            Field[] fs = resourceModel.getClass().getDeclaredFields();
            Field f = null;
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].getName().equals("extProperties")) {
                    f = fs[i];
                    break;
                }
            }
            if (f != null) {
                Object o = null;
                // resourceModel-> education
                o = BeanMapperUtils.beanMapper(education, f.getType());
                Method m = resourceModel.getClass().getMethod("setExtProperties", f.getType());
                m.invoke(resourceModel, o);
            }
        } catch (Exception e) {
            LOG.warn("反射处理扩展属性出错！", e);
        }
        // 恢复
        resourceModel.setTechInfoList(keepResourceModel.getTechInfoList());
        resourceModel.setCoverages(keepResourceModel.getCoverages());
        resourceModel.setCategoryList(keepResourceModel.getCategoryList());
        resourceModel.setRelations(keepResourceModel.getRelations());
    }
	
}
