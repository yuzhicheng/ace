package com.yzc.mongo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.reflect.ClassPath;
import com.yzc.core.exception.BizException;
import com.yzc.mongo.service.DbNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
    MongoMappingContext mongoMappingContext;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<String> cacheDbName() {
        List<String> list = new ArrayList<>();
        list.add(dbName);

        String jsonStr = JSON.toJSONString(list);
        servletContext.setAttribute(DBNAME_KEY, jsonStr);

        return list;
    }

    @Override
    public boolean validateDbName(String dbName) {

        String jsonStr = (String) servletContext.getAttribute(DBNAME_KEY);

        List<String> list = JSON.parseObject(jsonStr, new TypeReference<List<String>>() {
        });

        if (list == null || list.size() == 0) {
            list = cacheDbName();
        }

        return list.contains(dbName);
    }

    @Override
    public String getDefaultDbName() {

        String jsonStr = (String) servletContext.getAttribute(DBNAME_KEY);

        List<String> list = JSON.parseObject(jsonStr, new TypeReference<List<String>>() {
        });

        if (list == null || list.size() == 0) {
            list = cacheDbName();
        }

        return list.get(0);
    }

    @Async
    @Override
    public void createIndexForTenantServiceOpen(String tenancy, boolean flag) {

        MongoPersistentEntityIndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

        try {
            List<Class> classIndexs = new ArrayList<>();
            Object[] classInfos = ClassPath.from(this.getClass().getClassLoader())
                    .getTopLevelClasses("com.yzc.mongo.domain").toArray();

            for(Object object:classInfos){
                Class clazz = Class.forName(((ClassPath.ClassInfo) object).getName());
                Document document = (Document) clazz.getAnnotation(Document.class);
                if (document != null && document.collection().contains("tenantProvider.getTenant()")) {
                    classIndexs.add(clazz);
                }
            }

            Iterator<Class> classI$ = classIndexs.iterator();
            while (classI$.hasNext()) {
                Class toIndexClazz = classI$.next();
                IndexOperations indexOperations = mongoTemplate.indexOps(toIndexClazz);
                indexOperations.dropAllIndexes();
                List<MongoPersistentEntityIndexResolver.IndexDefinitionHolder> indexDefinitionHolders = resolver.resolveIndexForEntity(mongoMappingContext.getPersistentEntity(toIndexClazz));

                for (MongoPersistentEntityIndexResolver.IndexDefinitionHolder indexDefinitionHolder : indexDefinitionHolders) {
                    indexOperations.ensureIndex(indexDefinitionHolder.getIndexDefinition());
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new BizException("create.cache.error");
        }

    }

    private void createIndexForImage(String tenancy, boolean flag) {

        String document = tenancy + "_image";

        Index idxId = new Index().on("orgId", Sort.Direction.ASC).on("createTime", Sort.Direction.DESC)
                .on("uid", Sort.Direction.DESC).named("idx_id");

        mongoTemplate.indexOps(document).ensureIndex(idxId);
    }
}
