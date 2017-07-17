package com.yzc.mongo.service;

import com.yzc.mongo.domain.AppRouterDomain;
import com.yzc.mongo.entity.Items;
import com.yzc.mongo.search.Condition;
import com.yzc.mongo.search.OffsetPage;


/**
 * 应用路由服务
 *
 * Created by yzc on 2017/7/15.
 */
public interface AppRouterService {

    AppRouterDomain findAppRouter(String appId, String orgId, String bizType);

    AppRouterDomain findAppRouter(String appId, String orgId, String bizType,Boolean isIncludeDeleted);

    AppRouterDomain save(AppRouterDomain appRouter);

    AppRouterDomain findOne(String id);

    Items<AppRouterDomain> getAppRouterList(Condition condition, OffsetPage offsetPage);

    Items<AppRouterDomain> getAppRouterList(Condition condition, OffsetPage offsetPage, Boolean isCount);
}
