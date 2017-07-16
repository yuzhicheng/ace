package com.yzc.mongo.repository;

import com.yzc.mongo.domain.ImageDomain;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yzc on 2017/7/15.
 */
public interface ImageRespository extends MongoRepository<ImageDomain, String> {

}
