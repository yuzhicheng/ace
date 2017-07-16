package com.yzc.config;

import com.google.common.collect.ImmutableMap;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoDbUtils;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.Assert;

public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantMongoDbFactory.class);

    public static ImmutableMap<String, Mongo> mongoMap;

    private static final ThreadLocal<String> dbName = new ThreadLocal<>();

    private String defaultName;

    private WriteConcern writeConcern;

    public MultiTenantMongoDbFactory(Mongo mongo, String databaseName) {
        super(mongo, databaseName);
        logger.debug("Instantiating " + MultiTenantMongoDbFactory.class.getName() + " with default database name: " + databaseName);
        this.defaultName = databaseName;
    }

    @Override
    public DB getDb() {
        return this.getDb(defaultName);
    }

    @Override
    public DB getDb(String dbName) throws DataAccessException {

        Assert.hasText(dbName, "Database name must not be empty.");
        Assert.notNull(mongoMap, "mongoMap must not be empty");

        String curDbName = MultiTenantMongoDbFactory.dbName.get();

        if (!StringUtils.isBlank(curDbName) && !mongoMap.isEmpty() && mongoMap.get(curDbName) != null) {

            Mongo mongo = mongoMap.get(curDbName);

            DB db = MongoDbUtils.getDB(mongo, curDbName, UserCredentials.NO_CREDENTIALS, curDbName);

            if (writeConcern != null) {
                db.setWriteConcern(writeConcern);
            }

            return db;
        } else {

            return super.getDb(dbName);
        }
    }

    public static void setDatabaseNameForCurrentThread(String databaseName) {
        logger.debug("Switching to database: " + databaseName);
        MultiTenantMongoDbFactory.dbName.set(databaseName);
    }

    public static String getDatabaseNameForCurrentThread() {
        return MultiTenantMongoDbFactory.dbName.get();
    }

    public static void clearDatabaseNameForCurrentThread() {
        if (logger.isDebugEnabled()) {
            logger.debug("Removing database [" + dbName.get() + "]");
        }
        MultiTenantMongoDbFactory.dbName.remove();
    }

    public void setWriteConcern(WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
    }
}
