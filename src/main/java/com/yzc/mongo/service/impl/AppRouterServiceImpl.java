package com.yzc.mongo.service.impl;

import com.yzc.core.exception.BizException;
import com.yzc.mongo.domain.AppRouterDomain;
import com.yzc.mongo.entity.Items;
import com.yzc.mongo.repository.AppRouterRepository;
import com.yzc.mongo.search.Condition;
import com.yzc.mongo.search.ConditionUtils;
import com.yzc.mongo.search.OffsetPage;
import com.yzc.mongo.service.AppRouterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
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

    @Override
    public AppRouterDomain findOne(String id) {

        return appRouterRepository.findByIdAndDeletedIsFalse(id);
    }

    @Override
    public Items<AppRouterDomain> getAppRouterList(Condition condition, OffsetPage offsetPage) {

        return getAppRouterList(condition, offsetPage, false);
    }

    @Override
    public Items<AppRouterDomain> getAppRouterList(Condition condition, OffsetPage offsetPage, Boolean isCount) {

        List<AppRouterDomain> domainList;

        try {
            domainList = ConditionUtils.query(condition, offsetPage, AppRouterDomain.class);
        } catch (Exception e) {
            throw new BizException(HttpStatus.BAD_REQUEST,"INVALID_QUERY_VALUE","invalid.query.value");
        }

        if (isCount) {
            long count;
            try {
                count = ConditionUtils.count(condition, offsetPage, AppRouterDomain.class);
            } catch (Exception e) {
                throw new BizException(HttpStatus.BAD_REQUEST,"INVALID_QUERY_VALUE","invalid.query.value");
            }
            return new Items<>(domainList, count);
        } else {
            return new Items<>(domainList);
        }
    }
}
