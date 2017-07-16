package com.yzc.mongo.repository;

import com.yzc.mongo.domain.TenantDomain;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TenantRepository extends MongoRepository<TenantDomain, String> {

    TenantDomain findByOrgIdAndDelStatus(String id, int deleted);
}
