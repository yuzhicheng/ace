package com.yzc.mongo.controller;

import com.yzc.config.MultiTenantMongoDbFactory;
import com.yzc.core.TenantProvider;
import com.yzc.core.exception.BizException;
import com.yzc.mongo.domain.AppRouterDomain;
import com.yzc.mongo.domain.ImageDomain;
import com.yzc.mongo.entity.AppRouter;
import com.yzc.mongo.entity.Tenant;
import com.yzc.mongo.service.AppRouterService;
import com.yzc.mongo.service.DbNameService;
import com.yzc.mongo.service.TenantService;
import com.yzc.utils.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by yzc on 2017/7/15.
 */
@RestController
@RequestMapping({"/v0.1/app_routers", "/v0.1/{bizType}/app_routers"})
public class AppRouterController {

    @Autowired
    private AppRouterService appRouterService;

    @Autowired
    private DbNameService dbNameService;

    @Autowired
    private TenantService tenantService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public AppRouterDomain addRouter(@RequestBody @Valid AppRouter appRouter) {

        AppRouterDomain oldAppRouter = appRouterService.findAppRouter(appRouter.getAppId(), appRouter.getOrgId(),
                appRouter.getBizType(), true);

        if (oldAppRouter != null) {

            //如果service_tenant_id为空，则默认为原来的service_tenant_id
            if (StringUtils.isNotBlank(appRouter.getServiceTenantId())) {
                if (!StringUtils.equalsIgnoreCase(appRouter.getServiceTenantId(), oldAppRouter.getServiceTenantId())) {
                    throw new BizException(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", "应用路由已存在，但租户id不一致");
                }
            }

            oldAppRouter.setAppName(appRouter.getAppName());
            oldAppRouter.setDeleted(false);
            appRouterService.save(oldAppRouter);

            return oldAppRouter;
        }

        appRouter.setOrgId(StringUtils.defaultIfBlank(appRouter.getOrgId(), "0"));
        appRouter.setBizType(StringUtils.defaultIfBlank(appRouter.getBizType(), ""));
        appRouter.setPauseFlag(false);
        appRouter.setMsg("app temporary unavailable");

        AppRouterDomain appRouterDomain = AppRouterDomain.of(appRouter);

        if (StringUtils.isNotBlank(appRouterDomain.getServiceTenantId())) {
            Tenant tenant = tenantService.getTenant(appRouterDomain.getServiceTenantId(), true);
            if (tenant == null) {
                throw new BizException(HttpStatus.NOT_FOUND, "ACE/SERVICE_NOT_FOUND", "租户不存在");
            }
            appRouterDomain = appRouterService.save(appRouterDomain);
        } else {
            appRouterDomain = this.addAssociatedWithAppRouter(appRouterDomain, appRouter);
        }

        return appRouterDomain;
    }


    private AppRouterDomain addAssociatedWithAppRouter(AppRouterDomain AppRouterDomain, AppRouter appRouter) {

        String tenantId = String.valueOf(IdUtils.dummyDistributedLongId());

        Tenant tenant = new Tenant();
        tenant.setOrgId(tenantId);
        tenant.setAppName(AppRouterDomain.getAppName());
        tenant.setDeveloperUid(AppRouterDomain.getDeveloperUid());
        tenant.setRealm(AppRouterDomain.getOrgId() + ".sdp.nd");
        tenant.setCreateAt(new Date());

        String dbConn = appRouter.getDbConn();
        if (StringUtils.isNotBlank(dbConn) && !dbNameService.validateDbName(dbConn)) {
            throw new BizException(HttpStatus.NOT_FOUND, "ACE/DBCONN_NOT_FOUND", "mongo实例不存在");
        } else {
            tenant.setDbConn(dbNameService.getDefaultDbName());
        }

        String tenancy = appRouter.getTenancy();
        if (StringUtils.isBlank(tenancy)) {
            tenancy = appRouter.getOrgId();
        }

        tenant.setTenancy(tenancy);
        tenant.setTemplateId(appRouter.getTemplateId());
        tenant = tenantService.addTenant(tenant);

        TenantProvider.setTenant(tenant);
        MultiTenantMongoDbFactory.setDatabaseNameForCurrentThread(tenant.getDbConn());
        if (!mongoTemplate.collectionExists(ImageDomain.class)) {
            dbNameService.createIndexForTenantServiceOpen(tenant.getTenancy(), false);
        }
        MultiTenantMongoDbFactory.clearDatabaseNameForCurrentThread();

        AppRouterDomain.setServiceTenantId(tenantId);
        appRouterService.save(AppRouterDomain);

        return AppRouterDomain;
    }

}

