package com.yzc.repository.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yzc.entity.resource.Asset;
import com.yzc.repository.ResourceRepository;

public interface AssetRepository extends ResourceRepository<Asset>,JpaRepository<Asset, String>{

}
