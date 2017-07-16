package com.yzc.mongo.domain;

import com.yzc.mongo.entity.AppRouter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by yzc on 2017/7/15.
 */
@Document(collection = "app_router")
@Data
@NoArgsConstructor
public class AppRouterDomain {

    @Id
    private String id;

    private String appId;

    private String orgId;

    private String bizType;

    private String serviceTenantId;

    private String appName;

    private Long developerUid;

    private boolean pauseFlag;

    private String msg;

    private boolean deleted;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    public static AppRouterDomain of(AppRouter appRouter) {

        AppRouterDomain appRouterModel = new AppRouterDomain();
        appRouterModel.setAppId(appRouter.getAppId());
        appRouterModel.setOrgId(appRouter.getOrgId());
        appRouterModel.setBizType(appRouter.getBizType());
        appRouterModel.setServiceTenantId(appRouter.getServiceTenantId());
        appRouterModel.setAppName(appRouter.getAppName());
        appRouterModel.setDeveloperUid(appRouter.getDeveloperUid());
        appRouterModel.setPauseFlag(appRouter.isPauseFlag());
        appRouterModel.setMsg(appRouter.getMsg());

        return appRouterModel;
    }
}
