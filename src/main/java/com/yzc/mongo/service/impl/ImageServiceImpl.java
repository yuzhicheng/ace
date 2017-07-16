package com.yzc.mongo.service.impl;

import com.yzc.mongo.domain.ImageDomain;
import com.yzc.mongo.repository.ImageRespository;
import com.yzc.mongo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yzc on 2017/7/15.
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRespository imageRespository;

    @Override
    public ImageDomain save(ImageDomain image) {
        return imageRespository.save(image);
    }
}
