package com.yzc.config;

import com.google.common.collect.ImmutableMap;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.yzc.mongo.repository")
@ComponentScan
@PropertySource("classpath:mongo.properties")
public class MongoConfig extends AbstractMongoConfiguration {

//	@Autowired
//	private Environment env;
//
//    /**MongoClient配置**/
//	@Bean
//	public MongoClientFactoryBean mongo() {
//		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
//		mongo.setHost("localhost");
//		MongoCredential credential=MongoCredential.createCredential(env.getProperty("mongo.username"), "OrdersDB",env.getProperty("mongo.password").toCharArray());
//		mongo.setCredentials(new MongoCredential[]{credential});
//		return mongo;
//	}
//
//    /**Mongo Template配置**/
//	@Bean
//	public MongoOperations mongoTemplate(Mongo mongo) {
//		return new MongoTemplate(mongo, "OrdersDB");
//	}


    @Value("${mongo.dbName}")
    private String dbName;
//    @Override
//    protected String getDatabaseName() {
//        return dbName;
//
//    }
//
//    @Override
//    public Mongo mongo() throws Exception {
//        return new MongoClient("127.0.0.1");
//    }


    @Autowired
    private ApplicationContext appContext;

    @Override
    protected String getDatabaseName() {

        String curDbName = MultiTenantMongoDbFactory.getDatabaseNameForCurrentThread();

        if (StringUtils.isBlank(curDbName) == false) {
            return curDbName;
        } else {
            return dbName;
        }
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {

        /** MongoClientURI 字符串格式：mongodb://[username:password@]host1[:port1][,host2[:port2],…[,hostN[:portN]]][/[database][?options]] **/
//        MongoClientURI mongoClientURIDefault = new MongoClientURI("mongodb://yzc:1234@127.0.0.1:27017/OrdersDB?autoConnectRetry=true");
        MongoClient mongoClientDefault = new MongoClient("127.0.0.1",27017);

        MultiTenantMongoDbFactory.mongoMap = new ImmutableMap.Builder<String, Mongo>()
                .put(dbName, mongoClientDefault)
                .build();

        return mongoClientDefault;
    }

    @Override
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {

        return new MultiTenantMongoDbFactory(mongo(), getDatabaseName());
    }

    @Override
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        MongoDbFactory factory = mongoDbFactory();

        MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setApplicationContext(appContext);

        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return new MongoTemplate(factory, converter);
    }

    @Override
    protected String getMappingBasePackage() {
        return "com.yzc.mongo.entity";
    }

}
