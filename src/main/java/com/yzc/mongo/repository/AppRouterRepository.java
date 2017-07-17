package com.yzc.mongo.repository;

import com.yzc.mongo.domain.AppRouterDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yzc on 2017/7/15.
 */
public interface AppRouterRepository extends MongoRepository<AppRouterDomain, String> {

    AppRouterDomain findByAppIdAndOrgIdAndBizType(String appId, String orgId, String bizType);

    AppRouterDomain findByAppIdAndOrgIdAndBizTypeAndDeleted(String appId, String orgId, String bizType, boolean deleted);

    AppRouterDomain findByIdAndDeletedIsFalse(String id);
}
