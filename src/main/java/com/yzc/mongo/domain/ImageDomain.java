package com.yzc.mongo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@CompoundIndex(name = "idx_id", def = "{'orgId':1,'createTime':-1, 'uid':-1}")
@Document(collection = "#{@tenantProvider.getTenant()}image")
@Data
@NoArgsConstructor
public class ImageDomain {

    @Id
    private String id;

    /**
     * 内容服务保存的id
     */
    @NotBlank(message = "非法图片ID")
    private String imageId;

    /**
     * 上传图片人的uid
     */
    @NotBlank(message = "非法用户ID")
    private String uid;

    /**
     * 组织id
     */
    private String orgId;

    /**
     * 图片上传的时间
     */
    private Date createTime;

}
