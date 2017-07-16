package com.yzc.mongo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yzc.mongo.service.DbNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by yzc on 2017/7/15.
 */
@Service
public class DbNameServiceImpl implements DbNameService {

    @Value("${mongo.dbName}")
    private String dbName;

    private static final String DBNAME_KEY = "dbname";

    @Autowired
    private ServletContext servletContext;


    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<String> cacheDbName() {
        List<String> list = new ArrayList<String>();
        list.add(dbName);

        String jsonStr = JSON.toJSONString(list);
        servletContext.setAttribute(DBNAME_KEY, jsonStr);

        return list;
    }

    @Override
    public boolean validateDbName(String dbName) {
        String jsonStr = (String) servletContext.getAttribute(DBNAME_KEY);

        List<String> list = JSON.parseObject(jsonStr, new TypeReference<List<String>>(){});

        if(list == null || list.size() == 0){
            list = cacheDbName();
        }

        if(list.contains(dbName) == true){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String getDefaultDbName() {

        String jsonStr = (String) servletContext.getAttribute(DBNAME_KEY);

        List<String> list = JSON.parseObject(jsonStr, new TypeReference<List<String>>(){});

        if(list == null || list.size() == 0){
            list = cacheDbName();
        }

        return list.get(0);
    }

    @Async
    @Override
    public void createIndexForTenantServiceOpen(String tenancy, boolean flag) {

        createIndexForImage(tenancy, flag);
    }

    private void createIndexForImage(String tenancy, boolean flag){

        String document = tenancy + "_image";

        Index idxId = new Index().on("orgId", Sort.Direction.ASC).on("createTime", Sort.Direction.DESC)
                .on("uid", Sort.Direction.DESC).named("idx_id");

        mongoTemplate.indexOps(document).ensureIndex(idxId);
    }
}
