package com.yzc.mongo.service.impl;

import com.yzc.mongo.domain.AppRouterDomain;
import com.yzc.mongo.repository.AppRouterRepository;
import com.yzc.mongo.service.AppRouterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yzc on 2017/7/15.
 */
@Service
public class AppRouterServiceImpl implements AppRouterService {

    @Autowired
    private AppRouterRepository appRouterRepository;

    @Override
    public AppRouterDomain findAppRouter(String appId, String orgId, String bizType) {

        return this.findAppRouter(appId,orgId,bizType,false);
    }

    @Override
    public AppRouterDomain findAppRouter(String appId, String orgId, String bizType, Boolean isIncludeDeleted) {

        orgId = StringUtils.defaultIfBlank(orgId, "0");
        bizType = StringUtils.defaultIfBlank(bizType, "");

        if(isIncludeDeleted){
            return appRouterRepository.findByAppIdAndOrgIdAndBizType(appId, orgId, bizType);
        }
        return appRouterRepository.findByAppIdAndOrgIdAndBizTypeAndDeleted(appId, orgId, bizType, false);
    }

    @Override
    public AppRouterDomain save(AppRouterDomain appRouter) {
        return appRouterRepository.save(appRouter);
    }
}
