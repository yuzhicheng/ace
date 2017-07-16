package com.yzc.mongo.service;

import java.util.List;

/**
 * Created by yzc on 2017/7/15.
 */
public interface DbNameService {


    public List<String> cacheDbName();

    public boolean validateDbName(String dbName);

    public String getDefaultDbName();

    void createIndexForTenantServiceOpen(String tenancy, boolean b);
}
