package com.yzc.mongo.controller;

import com.yzc.mongo.domain.ImageDomain;
import com.yzc.mongo.service.ImageService;
import com.yzc.support.ValidResultHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v0.1/images")
public class ImageController {

    private Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    /**
     * 创建图片记录
     *
     * @param image 图片记录
     * @return 图片记录
     */
    @RequestMapping(method = RequestMethod.POST)
    public ImageDomain save(@Valid @RequestBody ImageDomain image, BindingResult validResult) {

        ValidResultHelper.valid(validResult, "ACE/SAVE_IMAGE_PARAM_VALID_FAIL", "ImageController", "save");
        return imageService.save(image);
    }


}
