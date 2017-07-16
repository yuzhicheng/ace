package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.reflect.TypeToken;
import com.yzc.dao.CommonDao;
import com.yzc.dao.PaintAuthorDao;
import com.yzc.dao.PaintDao;
import com.yzc.entity.Category;
import com.yzc.entity.CategoryData;
import com.yzc.entity.Paint;
import com.yzc.entity.PaintAuthor;
import com.yzc.entity.ResCategory;
import com.yzc.entity.ResCoverage;
import com.yzc.entity.TechInfo;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.ResClassificationModel;
import com.yzc.models.ResCoverageModel;
import com.yzc.models.ResTechInfoModel;
import com.yzc.models.TechnologyRequirementModel;
import com.yzc.models.paints.AuthorModel;
import com.yzc.models.paints.PaintModel;
import com.yzc.repository.CategoryDataRepository;
import com.yzc.repository.CategoryRepository;
import com.yzc.repository.ResCategoryRepository;
import com.yzc.repository.ResCoverageRepository;
import com.yzc.repository.TechInfoRepository;
import com.yzc.service.PaintService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.OperationType;
import com.yzc.support.enums.RecordStatus;
import com.yzc.support.statics.CoverageConstant;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.utils.ObjectUtils;
import com.yzc.utils.ParamCheckUtil;
import com.yzc.utils.StringUtils;
import com.yzc.vos.CoverageViewModel;
import com.yzc.vos.ListViewModel;

@Service
@Transactional
public class PaintServiceImpl implements PaintService {

	private static final Logger LOG = LoggerFactory.getLogger(PaintServiceImpl.class);
	
	private static final String RES_TYPE = "paints";

	@Autowired
	private PaintDao paintDao;

	@Autowired
	private PaintAuthorDao paintAuthorDao;

	@Autowired
	private TechInfoRepository tiRepository;
	
    @Autowired
    private CategoryDataRepository categoryDataRepository;
    
    @Autowired
    private ResCategoryRepository resourceCategoryRepository;
    
	@Autowired
	private CategoryRepository categoryRepository;
    	
    @Autowired
    private ResCoverageRepository resCoverageRepository;

	@Autowired
	private CommonDao commonDao;

	@Override
	public PaintModel savePaintModel(PaintModel paintModel) {
		// 判断主键id是否已经存在
		boolean flag = isDuplicateId(paintModel.getIdentifier());
		if (flag) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.CheckDuplicateIdFail);
		}

		Timestamp ts = new Timestamp(System.currentTimeMillis() / 1000 * 1000);
		paintModel.setCreateTime(ts);
		paintModel.setUpdateTime(ts);
		paintModel.setState(RecordStatus.NORMAL);
		return operatePaintModel(paintModel, OperationType.CREATE);
	}

	/**
	 * 判断名画id是否重复
	 * 
	 * @author xiezy
	 * @date 2016年11月8日
	 * @param identifier
	 * @return
	 */
	private boolean isDuplicateId(String identifier) {
		int num = paintDao.queryCountByResId(identifier);

		if (num > 0) {
			return true;
		}

		return false;
	}

	@Override
	public PaintModel updatePaintModel(PaintModel paintModel) {
		// 1、判断是否存在
		Paint paint = checkExists(paintModel.getIdentifier());
		paintModel.setCreateTime(paint.getCreateTime());
		paintModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		paintModel.setState(paint.getState());
		return operatePaintModel(paintModel, OperationType.UPDATE);
	}

	@Override
	public void deletePaintModel(String id) {
		// 1、判断是否存在
		Paint paint = checkExists(id);
		// 2、修改status=false
		paint.setState(RecordStatus.REMOVED);
		paintDao.updatePaint(paint);
	}

	@Override
	public PaintModel getPaintModel(String id) {
		Paint paint = checkExists(id);
		PaintModel pm = BeanMapperUtils.beanMapper(paint, PaintModel.class);
		List<ResTechInfoModel> resTechInfoModels = new ArrayList<ResTechInfoModel>();
		TechInfo techInfo = new TechInfo();
		techInfo.setResource(id);
		try {
			// 获取TI值
			List<TechInfo> techInfos = tiRepository.getAllByExample(techInfo);
			if (!CollectionUtils.isEmpty(techInfos)) {
				for (TechInfo ti : techInfos) {
					resTechInfoModels.add(changeTechInfoToModel(ti));
				}
			}
			pm.setTechInfoList(resTechInfoModels);
			//获取CG值
	        List<ResClassificationModel> resClassificationModels = new ArrayList<ResClassificationModel>();
	        ResCategory resourceCategory = new ResCategory();
	        resourceCategory.setResource(id);
	        List<ResCategory> resourceCategories =resourceCategoryRepository.getAllByExample(resourceCategory);
	        if(!CollectionUtils.isEmpty(resourceCategories)){
	            for (ResCategory rc : resourceCategories) {
	                resClassificationModels.add(changeResourceCategoryToModel(rc));
	            }
	        }
	        pm.setCategoryList(resClassificationModels);

		} catch (EspStoreException e) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
					e.getLocalizedMessage());
		}
		if (StringUtils.isNotEmpty(pm.getAuthorId())) {
			PaintAuthor ui = paintAuthorDao.getPaintAuthor(pm.getAuthorId());
			if (ui != null) {
				pm.setAuthorName(ui.getAuthorName());
			}
		}
		return pm;
	}

	@Override
	public AuthorModel saveAuthorModel(AuthorModel authorModel) {
		PaintAuthor pa = BeanMapperUtils.beanMapper(authorModel, PaintAuthor.class);
		pa.setIdentifier(UUID.randomUUID().toString());
		pa.setCreateTime(new Timestamp(System.currentTimeMillis()));
		pa.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		pa.setState(RecordStatus.NORMAL);
		pa = paintAuthorDao.savePaintAuthor(pa);
		return changeToAuthorModel(pa);
	}

	@Override
	public AuthorModel updateAuthorModel(AuthorModel authorModel) {
		AuthorModel am = getAuthorModel(authorModel.getAuthorId());
		PaintAuthor pa = BeanMapperUtils.beanMapper(authorModel, PaintAuthor.class);
		pa.setIdentifier(authorModel.getAuthorId());
		pa.setCreateTime(am.getCreateTime());
		pa.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		pa.setState(am.getState());
		pa = paintAuthorDao.updatePaintAuthor(pa);
		return changeToAuthorModel(pa);
	}

	@Override
	public void deleteAuthorModel(String id) {
		getAuthorModel(id);
		paintAuthorDao.deletePaintAuthor(id);
	}

	@Override
	public AuthorModel getAuthorModel(String id) {
		PaintAuthor pa = paintAuthorDao.getPaintAuthor(id);
		if (pa == null) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.AuthorNotFound);
		}
		return changeToAuthorModel(pa);
	}

	@Override
	public ListViewModel<AuthorModel> queryAuthorList(String authorName, String limit) {
		ListViewModel<AuthorModel> returnValue = new ListViewModel<AuthorModel>();
		ListViewModel<PaintAuthor> value = paintAuthorDao.queryPaintAuthorList(authorName, limit);
		if (CollectionUtils.isNotEmpty(value.getItems())) {
			List<AuthorModel> amList = new ArrayList<AuthorModel>();
			for (PaintAuthor ui : value.getItems()) {
				amList.add(changeToAuthorModel(ui));
			}
			returnValue.setItems(amList);
		}
		returnValue.setLimit(value.getLimit());
		returnValue.setTotal(value.getTotal());
		return returnValue;
	}

	@Override
	public ListViewModel<PaintModel> queryListByCond(String title, List<String> tags, String userName,
			String nationality, String creator, String limit) {
		ListViewModel<PaintModel> returnValue = new ListViewModel<PaintModel>();
		List<PaintModel> pmList = new ArrayList<PaintModel>();

		// 获取用户id列表
		List<String> authorIds = new ArrayList<String>();
		if (StringUtils.hasText(userName) || StringUtils.hasText(nationality)) {
			authorIds = paintAuthorDao.getAuthorIdByNameAndNationality(userName, nationality);

			if (CollectionUtils.isEmpty(authorIds)) {
				returnValue.setTotal(0L);
				returnValue.setLimit(limit);
				return returnValue;
			}
		}

		Integer result[] = ParamCheckUtil.checkLimit(limit);
		long count = paintDao.getCountByCond(title, tags, creator, authorIds);
		returnValue.setTotal(count);
		returnValue.setLimit(limit);
		if (count > 0) {
			Set<String> idSet = new HashSet<String>();
			List<String> authorIdList = new ArrayList<String>();
			List<Paint> paintList = paintDao.queryListByCond(title, tags, creator, authorIds, result[0], result[1]);

			if (CollectionUtils.isNotEmpty(paintList)) {
				for (Paint paint : paintList) {
					idSet.add(paint.getIdentifier());
					authorIdList.add(paint.getAuthorId());
				}
				
				//查找TI
				List<String> resourceTypes = new ArrayList<String>();
				resourceTypes.add(RES_TYPE);
				List<TechInfo> techInfos = commonDao.queryTechInfosByResourceSet(resourceTypes, idSet);
				Map<String, List<ResTechInfoModel>> resTechInfoModelMap = new HashMap<String, List<ResTechInfoModel>>();
			    if(CollectionUtils.isNotEmpty(techInfos)){
			        for(TechInfo techInfo:techInfos){
			            if(techInfo != null){
			                String key = techInfo.getResource();
			                List<ResTechInfoModel> resTechInfoModels = resTechInfoModelMap.get(key);
			                if(resTechInfoModels == null){
			                    resTechInfoModels = new ArrayList<ResTechInfoModel>();
			                    resTechInfoModelMap.put(key, resTechInfoModels);
			                }
			                resTechInfoModels.add(changeTechInfoToModel(techInfo));
			            }
			        }
			    }
			  //查找CG
		        List<ResCategory> resourceCategories = commonDao.queryCategoriesUseHql(resourceTypes, idSet);
		        Map<String, List<ResClassificationModel>> resClassificationModelMap = new HashMap<String, List<ResClassificationModel>>();
		        if(CollectionUtils.isNotEmpty(resourceCategories)){
		            for(ResCategory resourceCategory:resourceCategories){
		                if(resourceCategory != null){
		                    String key = resourceCategory.getResource();
		                    List<ResClassificationModel> resClassificationModels = resClassificationModelMap.get(key);
		                    if(resClassificationModels == null){
		                        resClassificationModels = new ArrayList<ResClassificationModel>();
		                        resClassificationModelMap.put(key, resClassificationModels);
		                    }
		                    resClassificationModels.add(changeResourceCategoryToModel(resourceCategory));
		                }
		            }
		        }

				// 批量获取userId对应的userName
				List<PaintAuthor> uiList = paintAuthorDao.batchGetPaintAuthor(authorIdList);
				Map<String, String> authorMap = new HashMap<String, String>();
				if (CollectionUtils.isNotEmpty(uiList)) {
					for (PaintAuthor author : uiList) {
						authorMap.put(author.getIdentifier(), author.getAuthorName());
					}
				}

				for (Paint paint : paintList) {
					PaintModel pm = BeanMapperUtils.beanMapper(paint, PaintModel.class);
					pm.setTags(ObjectUtils.fromJson(paint.getDbtags(), new TypeToken<List<String>>() {
					}));
					pm.setAuthorName(authorMap.get(pm.getAuthorId()));
					pm.setTechInfoList(resTechInfoModelMap.get(pm.getIdentifier()));
					pm.setCategoryList(resClassificationModelMap.get(pm.getIdentifier()));
					pmList.add(pm);
				}
				returnValue.setItems(pmList);
			}
		}

		return returnValue;
	}
	
	@Override
	public ListViewModel<AuthorModel> queryAuthorByNameAndNationality(String authorName, String nationality,
			String limit) {
		List<PaintAuthor> authorList = paintAuthorDao.queryAuthorByNameAndNationality(authorName,nationality,limit);

		Long total = paintAuthorDao.queryCountByNameAndNationality(authorName,nationality,limit);

		List<AuthorModel> authorModelList = new ArrayList<AuthorModel>();
		if (CollectionUtils.isNotEmpty(authorList)) {
			for (PaintAuthor author : authorList) {
				AuthorModel Model = BeanMapperUtils.beanMapper(author, AuthorModel.class);
				Model.setAuthorId(author.getIdentifier());
				authorModelList.add(Model);
			}
		}
		ListViewModel<AuthorModel> result = new ListViewModel<AuthorModel>();
		result.setTotal(total);
		result.setLimit(limit);
		result.setItems(authorModelList);
		return result;
	}

	private AuthorModel changeToAuthorModel(PaintAuthor pa) {
		AuthorModel uim = BeanMapperUtils.beanMapper(pa, AuthorModel.class);
		uim.setAuthorId(pa.getIdentifier());
		return uim;
	}

	/**
	 * 判断名画有没有存在
	 * 
	 * @param id
	 * @return
	 */
	private Paint checkExists(String id) {
		Paint paint = paintDao.getPaint(id);
		if (paint == null || RecordStatus.REMOVED.equals(paint.getState())) {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.PaintNotFound.getCode(),
					ErrorMessageMapper.PaintNotFound.getMessage());
		}
		return paint;
	}

	/**
	 * 操作名画数据
	 * 
	 * @param paintModel
	 * @param ot
	 * @return
	 */
	private PaintModel operatePaintModel(PaintModel paintModel, OperationType ot) {
		// 1、判断authorId是否有效
		if (StringUtils.isNotEmpty(paintModel.getAuthorId())) {
			PaintAuthor pa = paintAuthorDao.getPaintAuthor(paintModel.getAuthorId());
			if (pa == null) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.AuthorNotFound);
			}
		}
		// 2、保存paint(这里根据操作类型进行了创建与更新的区别，实际上底层的实现都调用save方法，并没有区别)
		Paint paint = BeanMapperUtils.beanMapper(paintModel, Paint.class);
		if (ot.equals(OperationType.CREATE)) {
			paint = paintDao.savePaint(paint);
		} else if (ot.equals(OperationType.UPDATE)) {
			paint = paintDao.updatePaint(paint);
		} else {
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, "OperationTypeError", "操作类型错误");
		}
		PaintModel pm = BeanMapperUtils.beanMapper(paint, PaintModel.class);
		// 3、保存tech_info
		if (CollectionUtils.isNotEmpty(paintModel.getTechInfoList())) {
			List<TechInfo> tiList = new ArrayList<TechInfo>();
			List<ResTechInfoModel> returnList = new ArrayList<ResTechInfoModel>();
			List<ResTechInfoModel> list = paintModel.getTechInfoList();
			for (ResTechInfoModel rtim : list) {
				TechInfo ti = BeanMapperUtils.beanMapper(rtim, TechInfo.class);
				ti.setIdentifier(UUID.randomUUID().toString());
				ti.setCreateTime(new Timestamp(System.currentTimeMillis()));
				ti.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				ti.setState(RecordStatus.NORMAL);
				ti.setResource(paint.getIdentifier());
				ti.setResType(RES_TYPE);
				ti.setRequirements(ObjectUtils.toJson(rtim.getRequirements()));
				tiList.add(ti);
			}
			try {
				if (OperationType.UPDATE == ot) {
					commonDao.deleteTechInfoByResource(RES_TYPE, paint.getIdentifier());
				}

				tiList = tiRepository.batchAdd(tiList);
				for (TechInfo techInfo : tiList) {
					// 数据回填
					ResTechInfoModel rtim = BeanMapperUtils.beanMapper(techInfo, ResTechInfoModel.class);
					// requirements单独处理
					List<TechnologyRequirementModel> req = ObjectUtils.fromJson(techInfo.getRequirements(),
							new TypeToken<List<TechnologyRequirementModel>>() {
							});
					rtim.setRequirements(req);
					returnList.add(rtim);
				}
				pm.setTechInfoList(returnList);

			} catch (EspStoreException e) {
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessageMapper.StoreSdkFail.getCode(),
						e.getLocalizedMessage());
			}
		}
		//4 保存coverages
		if(CollectionUtils.isNotEmpty(paintModel.getCoverages())){
			if (OperationType.CREATE== ot) {
				List<ResCoverageModel> resCoverageModelList = new ArrayList<ResCoverageModel>();
				for(ResCoverageModel rcm:paintModel.getCoverages()){
					rcm.setIdentifier(UUID.randomUUID().toString());
					rcm.setResource(paint.getIdentifier());
					rcm.setResType(RES_TYPE);
					resCoverageModelList.add(rcm);
				}
				if(CollectionUtils.isNotEmpty(resCoverageModelList)){
					 batchCreateCoverage(resCoverageModelList, true);   
				}		   
			}
		}
		//5、保存category
		if(CollectionUtils.isNotEmpty(paintModel.getCategoryList())){
	        List<ResClassificationModel> categories = paintModel.getCategoryList();
	        Set<ResClassificationModel> resClassificationModelSet = new HashSet<ResClassificationModel>(categories);
	        List<ResCategory> resourceCategories = new ArrayList<ResCategory>();
	        Set<String> ndCodeSet = new HashSet<String>();
            List<CategoryData> categoryDatas = null;
            for (ResClassificationModel resClassificationModel : categories) {
                ndCodeSet.add(resClassificationModel.getTaxoncode());
            }
            try {
                categoryDatas = categoryDataRepository.getListWhereInCondition("ndCode",new ArrayList<String>(ndCodeSet));
            } catch (EspStoreException e) {
                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
            }
            if (CollectionUtils.isEmpty(categoryDatas) || categoryDatas.size() != ndCodeSet.size()) {
                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckNdCodeFail);
            }

            // 取 分类维度到shortName的映射
            Map<String, String> categoryNdCodeToShortNameMap =getCategoryByData(categoryDatas);     
            for (CategoryData cd : categoryDatas) {
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
                        resourceCategory.setResource(paint.getIdentifier());
                        resourceCategory.setIdentifier(UUID.randomUUID().toString());                   
                        //资源分类维度
                        resourceCategory.setResType(RES_TYPE);
                        resourceCategory.setIdentifier(UUID.randomUUID().toString());
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
                    if (OperationType.UPDATE == ot) {
                        commonDao.deleteResourceCategoryByResource(RES_TYPE,paint.getIdentifier());
                    }
                    resourceCategories = resourceCategoryRepository.batchAdd(resourceCategories);
                    // 返回值转换为model
                    if (CollectionUtils.isNotEmpty(resourceCategories)) {
                        categories = new ArrayList<ResClassificationModel>();
                        for (ResCategory resourceCategory : resourceCategories) {
                            ResClassificationModel model = BeanMapperUtils.beanMapper(resourceCategory,ResClassificationModel.class);
                            categories.add(model);
                        }
                        pm.setCategoryList(categories);
                    }
                    } catch (EspStoreException e) {
                    throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(), e.getLocalizedMessage());
                }
            }
		}
		return pm;
	}

	/**
	 * @author yzc
	 * @param techInfo
	 * @return
	 */
	private ResTechInfoModel changeTechInfoToModel(TechInfo techInfo) {
		ResTechInfoModel rtim = BeanMapperUtils.beanMapper(techInfo, ResTechInfoModel.class);
		// requirements单独处理
		List<TechnologyRequirementModel> req = ObjectUtils.fromJson(techInfo.getRequirements(),new TypeToken<List<TechnologyRequirementModel>>() {});
		rtim.setRequirements(req);
		return rtim;
	}
	
    /**
     * @author yzc
     * @param resourceCategory
     * @return
     * @since 
     */
    private ResClassificationModel changeResourceCategoryToModel(ResCategory resourceCategory) {
        return  BeanMapperUtils.beanMapper(resourceCategory, ResClassificationModel.class);
    }
	
    private List<CoverageViewModel> batchCreateCoverage(List<ResCoverageModel> coverageModels, boolean isCreateWithResource){
    	for(ResCoverageModel cm : coverageModels){
            if(isCreateWithResource){
                //参数校验,保证批量传入的覆盖范围,一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略
                CommonHelper.checkCoverageHaveOnlyOneOwner(coverageModels, true);            
                //判断覆盖范围类型是否在可选范围内
                if(!CoverageConstant.isCoverageTargetType(cm.getTargetType(),false)){                   
                    LOG.error("覆盖范围类型不在可选范围内");                 
                    throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CoverageTargetTypeNotExist);
                }
                //判断资源操作类型是否在可选范围内
                if(!CoverageConstant.isCoverageStrategy(cm.getStrategy(),false)){                   
                    LOG.error("资源操作类型不在可选范围内");               
                    throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CoverageStrategyNotExist);
                }
            }else{
                //判断一个资源是否已经有OWNER的覆盖策略
                if(cm.getStrategy().equals(CoverageConstant.STRATEGY_OWNER)){
                    this.checkResourceHaveOwnerOnlyOne(cm.getResType(), cm.getResource(),null,true);
                }
            }
        }
        
        //生成SDK的入参对象,并进行model转换
        List<ResCoverage> params = new ArrayList<ResCoverage>();
        for (ResCoverageModel rc : coverageModels) {
            ResCoverage cvm = BeanMapperUtils.beanMapper(rc, ResCoverage.class);
            cvm.setCreateTime(new Timestamp(System.currentTimeMillis()));
            cvm.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            cvm.setState(RecordStatus.NORMAL);
            params.add(cvm);
        }
        
        List<ResCoverage> resCoverages = null;
        try {
            //调用SDK,批量添加
            resCoverages = resCoverageRepository.batchAdd(params);
        } catch (EspStoreException e) {            
            LOG.error("批量添加资源覆盖范围失败", e);         
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getMessage());
        }    
        //如果返回null,则抛出异常
        if (null == resCoverages) {          
            LOG.error("批量添加资源覆盖范围失败");         
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.BatchCreateCoverageFail);
        }
        
        //处理返回结果
        List<CoverageViewModel> resultList = new ArrayList<CoverageViewModel>();
        for (ResCoverage rc : resCoverages) {
            CoverageViewModel cvm = BeanMapperUtils.beanMapper(rc, CoverageViewModel.class);
            resultList.add(cvm);
        }              
        return resultList;
    }
    
    /**
     * 判断一个资源是否已经有OWNER的覆盖策略 
     * @param resType     资源类型
     * @param resourceId  资源id
     * @param coverageId  覆盖范围id(修改时才传)
     * @param isCreate    是否是创建
     */
    private void checkResourceHaveOwnerOnlyOne(String resType,String resourceId,String coverageId,boolean isCreate){
        ResCoverage resCoverage = new ResCoverage();
        resCoverage.setResource(resourceId);
        resCoverage.setResType(resType);
        resCoverage.setStrategy(CoverageConstant.STRATEGY_OWNER);
        
        List<ResCoverage> resCoverages = new ArrayList<ResCoverage>();
        try {
            resCoverages = resCoverageRepository.getAllByExample(resCoverage);
        } catch (EspStoreException e) {          
            LOG.error("判断一个资源是否已经有OWNER的覆盖策略时--根据条件获取资源覆盖范围列表失败", e);      
            throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.GetCoverageListByConditionFail.getCode(),e.getMessage());
        }      
        if(isCreate){
            if(CollectionUtils.isNotEmpty(resCoverages)){//说明已经存在Strategy=OWNER的覆盖范围
                LOG.error("该资源已存在strategy=OWNER的覆盖范围--一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略");         
                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,"CoverageAleadyHaveOwner",
                        "该资源已存在strategy=OWNER的覆盖范围--一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略");
            } 
        }else{
            if((CollectionUtils.isNotEmpty(resCoverages) && resCoverages.size()>1) || 
               (CollectionUtils.isNotEmpty(resCoverages) && resCoverages.size()==1 && !resCoverages.get(0).getIdentifier().equals(coverageId))){
                LOG.error("该资源已存在strategy=OWNER的覆盖范围--一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略");            
                throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,"CoverageAleadyHaveOwner",
                        "该资源已存在strategy=OWNER的覆盖范围--一个资源的覆盖范围，有且仅有一个OWNNER的覆盖范围策略");
            }
        }
    }
    
	/**
	 * 获取维度的shortName
	 * 
	 * @author:xuzy
	 * @date:2015年8月4日
	 * @param beanListResult
	 * @return
	 */
	public Map<String,String> getCategoryByData(List<CategoryData> beanListResult){
		Map<String,String> returnMap = new HashMap<String, String>();
		List<String> cList = new ArrayList<String>();
		List<Category> categoryList = new ArrayList<Category>();
		if(CollectionUtils.isNotEmpty(beanListResult)){
			for (CategoryData categoryData : beanListResult) {
				//维度
				String s = categoryData.getNdCode().substring(0, 2);
				if(!cList.contains(s)){
					cList.add(s);
				}
			}
			try {
				categoryList = categoryRepository.getListWhereInCondition("ndCode", cList);
				for (Category category : categoryList) {
					String ndCode = category.getNdCode();
					String shortName = category.getShortName();
					if(StringUtils.isNotEmpty(ndCode)){
						returnMap.put(ndCode, shortName);
					}
				}
			} catch (EspStoreException e) {
			    
			    LOG.error(ErrorMessageMapper.StoreSdkFail.getMessage(),e);
			    
				throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.StoreSdkFail.getCode(),e.getLocalizedMessage());
			}
		}
		return returnMap;
	}

}
