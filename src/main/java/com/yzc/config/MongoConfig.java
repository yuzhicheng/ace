package com.yzc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories(basePackages = "com.yzc.mongo.repository")
@ComponentScan
public class MongoConfig extends AbstractMongoConfiguration{

//	@Autowired
//	private Environment env;
	// MongoClient配置
//	@Bean
//	public MongoClientFactoryBean mongo() {
//		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
//		mongo.setHost("localhost");
////		MongoCredential credential=MongoCredential.createCredential(env.getProperty("mongo.username"), "OrdersDB",env.getProperty("mongo.password").toCharArray());
////		mongo.setCredentials(new MongoCredential[]{credential});
//		return mongo;
//	}
//
	// Mongo Template配置
	@Bean
	public MongoOperations mongoTemplate(Mongo mongo) {
		return new MongoTemplate(mongo, "OrdersDB");
	}
	
    @Override  
    protected String getDatabaseName() {  
        return "OrdersDB";  
    }  
  
    @Override  
    public Mongo mongo() throws Exception {  
        return new MongoClient("127.0.0.1");  
    }  

}
