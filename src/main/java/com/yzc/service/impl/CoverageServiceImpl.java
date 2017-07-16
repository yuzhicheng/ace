package com.yzc.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzc.entity.ResCoverage;
import com.yzc.exception.repositoryException.EspStoreException;
import com.yzc.models.ResCoverageModel;
import com.yzc.repository.ResCoverageRepository;
import com.yzc.service.CoverageService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.enums.RecordStatus;
import com.yzc.support.statics.CoverageConstant;
import com.yzc.utils.BeanMapperUtils;
import com.yzc.utils.CollectionUtils;
import com.yzc.vos.CoverageViewModel;

@Service(value="coverageServiceImpl")
@Transactional
public class CoverageServiceImpl  implements CoverageService{
	
	private static final Logger LOG = LoggerFactory.getLogger(CoverageServiceImpl.class);
	
    @Autowired
    private ResCoverageRepository resCoverageRepository;

	@Override
	public List<CoverageViewModel> batchCreateCoverage(List<ResCoverageModel> coverageModels,
			boolean isCreateWithResource) {
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

}
