package com.yzc.controller.assets;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yzc.models.resource.AssetModel;
import com.yzc.service.resource.AssetService;
import com.yzc.support.CommonHelper;
import com.yzc.support.ErrorMessageMapper;
import com.yzc.support.MessageException;
import com.yzc.support.ValidResultHelper;
import com.yzc.support.enums.OperationType;
import com.yzc.support.enums.ResourceNdCode;
import com.yzc.utils.CollectionUtils;
import com.yzc.vos.ResClassificationViewModel;
import com.yzc.vos.ResTechInfoViewModel;
import com.yzc.vos.resource.AssetViewModel;
import com.yzc.vos.valid.ValidGroup;

@RestController
@RequestMapping("/assets")
public class AssetController {
	
	@Autowired
	@Qualifier("assetService")
	private AssetService assetService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public AssetViewModel create(@Validated(ValidGroup.class) @RequestBody AssetViewModel avm, BindingResult validResult,@PathVariable String id){
		
		//入参合法性校验
		ValidResultHelper.valid(validResult, "LC/CREATE_ASSET_PARAM_VALID_FAIL", "AssetController", "create");
		avm.setIdentifier(id);
		
		//业务校验
		CommonHelper.ResourceParamValid(avm,"10111",OperationType.CREATE);
		
		//课件模板需要转码tech_info单独作校验
		Map<String,? extends ResTechInfoViewModel> techInfoMap = avm.getTechInfo();
		if(!checkTechInfoData(avm)){
			
			throw new MessageException(HttpStatus.INTERNAL_SERVER_ERROR,ErrorMessageMapper.CheckTechInfoFail);
		}
		
		//tech_info属性特殊处理
		CommonHelper.copyTechInfoValue(techInfoMap);

		//入参转换model,部分数据初始化
		AssetModel am = CommonHelper.convertViewModelIn(avm, AssetModel.class,ResourceNdCode.assets);
				
		//创建素材
		am = assetService.createAsset(am);
		
		//出参model转换
		avm = CommonHelper.convertViewModelOut(am,AssetViewModel.class,"assets_type");
		
		return avm;
		
	}
	
	
	/**
	 * 校验tech_info数据合法性
	 * 1、素材的维度数据如果包含$RA06|07开头的不需要tech_info
	 * 2、教学目标的集合组合成的套件，维度数据中包含$RA0502、$RA0503不需要校验tech_info
	 * 3、其它素材必须包含href或source属性
	 * 
	 * @author:xuzy
	 * @date:2016年1月7日
	 * @param avm
	 * @return
	 */
	private boolean checkTechInfoData(AssetViewModel avm){
		Map<String, List<? extends ResClassificationViewModel>> categoryMap = avm.getCategories();
		if(CollectionUtils.isNotEmpty(categoryMap)){
			Set<String> keys = categoryMap.keySet();
			for (String key : keys) {
				List<? extends ResClassificationViewModel> categories = categoryMap.get(key);
				if(CollectionUtils.isNotEmpty(categories)){
					for (ResClassificationViewModel c : categories) {
						String taxoncode = c.getTaxoncode();
						if (taxoncode != null && (taxoncode.startsWith("$RA06") || taxoncode.startsWith("$RA07")|| taxoncode.equals("$RA0502") || taxoncode.equals("$RA0503"))) {
							return true;
						}
					}
				}
			}
		}

		Map<String, ? extends ResTechInfoViewModel> techInfoMap = avm.getTechInfo();
		if(techInfoMap != null && (techInfoMap.containsKey("href") || techInfoMap.containsKey("source"))){
			return true;
		}	
		return false;
	}

}
