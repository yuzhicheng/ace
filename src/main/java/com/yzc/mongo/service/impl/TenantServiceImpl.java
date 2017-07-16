package com.yzc.mongo.service.impl;

import com.yzc.mongo.domain.TenantDomain;
import com.yzc.mongo.entity.Tenant;
import com.yzc.mongo.repository.TenantRepository;
import com.yzc.mongo.service.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户服务
 *
 * Created by yzc on 2017/7/15.
 */
@Service
public class TenantServiceImpl implements TenantService{

    private Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    @Autowired
    private TenantRepository tenantRepository;

//    @Autowired
//    private ApplicationEventPublisher publisher;

    @Override
    public Tenant getTenant(String id) {
        return this.getTenant(id,false);
    }

    @Override
    public Tenant getTenant(String id, Boolean deleted) {

        TenantDomain tenantDomain;
        if(deleted){
            tenantDomain = tenantRepository.findByOrgIdAndDelStatus(id, 1);
        }else{
            tenantDomain = tenantRepository.findByOrgIdAndDelStatus(id, 0);
        }

        if (tenantDomain == null) {
            return null;
        }

        return Tenant.of(tenantDomain);
    }

    @Override
    public Tenant addTenant(Tenant tenant) {

        TenantDomain tenantDomain = TenantDomain.of(tenant);

        tenantDomain = tenantRepository.save(tenantDomain);

//        publisher.publishEvent(new TenantCreateEvent(tenant));

        return Tenant.of(tenantDomain);
    }
}
