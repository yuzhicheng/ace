package com.yzc.mongo.service;

import com.yzc.mongo.entity.Tenant;

/**
 * 租户服务
 *
 * Created by yzc on 2017/7/15.
 */
public interface TenantService {

    Tenant getTenant(String id);

    Tenant getTenant(String id,Boolean deleted);

    Tenant addTenant(Tenant tenant);
}
