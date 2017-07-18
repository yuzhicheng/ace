package com.yzc.mongo.service.impl;

import com.yzc.mongo.domain.ImageDomain;
import com.yzc.mongo.repository.ImageRespository;
import com.yzc.mongo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author yzc
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRespository imageRespository;

    @Override
    public ImageDomain save(ImageDomain image) {
        image.setCreateTime(new Date());
        return imageRespository.save(image);
    }
}
