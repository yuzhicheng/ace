package com.yzc.service.resource.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yzc.models.resource.AssetModel;
import com.yzc.service.ResourceService;
import com.yzc.service.resource.AssetService;
import com.yzc.support.enums.ResourceNdCode;

@Service("assetService")
@Transactional
public class AssetServiceImpl implements AssetService{
	
    @Autowired
    private ResourceService resourceService;

	@Override
	public AssetModel createAsset(AssetModel am) {
		
		return (AssetModel)resourceService.create(ResourceNdCode.assets.toString(), am);
	}

}
