package com.yzc.mongo.controller;

import com.yzc.config.MultiTenantMongoDbFactory;
import com.yzc.core.TenantProvider;
import com.yzc.core.exception.BizException;
import com.yzc.mongo.domain.AppRouterDomain;
import com.yzc.mongo.domain.ImageDomain;
import com.yzc.mongo.entity.AppRouter;
import com.yzc.mongo.entity.Items;
import com.yzc.mongo.entity.Tenant;
import com.yzc.mongo.entity.TenantPause;
import com.yzc.mongo.search.Condition;
import com.yzc.mongo.search.ConditionBridge;
import com.yzc.mongo.search.OffsetPage;
import com.yzc.mongo.service.AppRouterService;
import com.yzc.mongo.service.DbNameService;
import com.yzc.mongo.service.TenantService;
import com.yzc.utils.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yzc on 2017/7/15.
 */
@RestController
@RequestMapping({"/v0.1/app_routers"})
public class AppRouterController {

    private static final Logger logger = LoggerFactory.getLogger(AppRouterController.class);

    @Autowired
    private AppRouterService appRouterService;

    @Autowired
    private DbNameService dbNameService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 2.1.4 开通应用 [POST] /app_routers
     *
     * @param appRouter 应用路由入参
     * @return AppRouterDomain 应用路由
     */
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

    /**
     * 2.1.5 删除应用 [DELETE] /app_routers/{id}
     *
     * @param id 应用路由id
     * @return 应用路由
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppRouterDomain delete(@PathVariable String id) {

        AppRouterDomain appRouter = appRouterService.findOne(id);

        if (appRouter == null) {
            throw new BizException(HttpStatus.NOT_FOUND, "APP_NOT_FOUND", "路由不存在");
        }
        appRouter.setDeleted(true);
        appRouter = appRouterService.save(appRouter);

        return appRouter;
    }

    /**
     * 2.1.6 获取应用详情 [GET] /app_routers/{id}
     *
     * @param id 应用路由id
     * @return 应用路由
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AppRouterDomain get(@PathVariable String id) {

        AppRouterDomain appRouter = appRouterService.findOne(id);

        if (appRouter == null) {
            throw new BizException(HttpStatus.NOT_FOUND, "APP_NOT_FOUND", "路由不存在");
        }

        return appRouter;
    }

    /**
     * 2.1.7 获取应用详情 [GET] /app_routers/info/{app_id}?org_id={org_id}&biz_type={biz_type}
     *
     * @param appId   应用id
     * @param orgId   组织id
     * @param bizType 业务属性
     * @return AppRouterDomain
     */
    @RequestMapping(value = "/info/{appId}", method = RequestMethod.GET)
    public AppRouterDomain appRouterInfo(
            @PathVariable String appId,
            @RequestParam(value = "org_id", required = false, defaultValue = "0") String orgId,
            @RequestParam(value = "biz_type", required = false) String bizType) {

        AppRouterDomain appRouter = appRouterService.findAppRouter(appId, orgId, bizType);
        if (appRouter == null) {
            if (StringUtils.isNotBlank(orgId) && !"0".equals(orgId)) {
                if (StringUtils.isNotBlank(bizType)) {
                    appRouter = appRouterService.findAppRouter(appId, orgId, bizType);
                    if (appRouter == null) {
                        appRouter = appRouterService.findAppRouter(appId, orgId, "");
                        if (appRouter == null) {
                            appRouter = this.routeByAppId(appId);
                        }
                    }
                } else {
                    appRouter = this.routeByAppId(appId);
                }
            } else if (StringUtils.isNotBlank(bizType)) {
                appRouter = this.routeByAppId(appId);
            } else {
                throw new BizException(HttpStatus.NOT_FOUND, "APP_NOT_FOUND", "路由不存在");
            }
        }

        return appRouter;
    }

    /**
     * 2.1.8 获取应用列表 [GET] /app_routers
     *
     * @param request  request
     * @return 应用路由列表
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    public Items<AppRouterDomain> list(HttpServletRequest request) throws Exception {

        try {
            Condition condition = ConditionBridge.getConditionFromRequest(request, AppRouterDomain.class);
            OffsetPage offsetPage = OffsetPage.createPageWithClass(request, AppRouterDomain.class);
            String countStr = com.yzc.utils.StringUtils.AorB(request.getParameter("$count"), request.getParameter("count"));

            if (countStr != null && countStr.equals("true")) {
                return appRouterService.getAppRouterList(condition, offsetPage, Boolean.valueOf(countStr));
            } else {
                // 默认不返回count
                return appRouterService.getAppRouterList(condition, offsetPage);
            }

        } catch (Exception e) {
            logger.error("查询应用路由列表失败", e);
            throw new BizException(HttpStatus.BAD_REQUEST,"INVALID_QUERY_VALUE","invalid.query.value");
        }
    }

    /**
     * 2.1.9 判断应用是否存在 [GET] /app_routers/exist?id={id}
     *
     * @param id 应用路由id
     * @return 应用路由存在信息map
     */
    @RequestMapping(value = "/exist", method = RequestMethod.GET)
    public Map<String, Boolean> exist(@RequestParam(value = "id") String id) {


        Map<String, Boolean> map = new HashMap<>();

        AppRouterDomain appRouter = appRouterService.findOne(id);
        if (appRouter == null) {
            map.put("is_exist", false);
        } else {
            map.put("is_exist", true);
        }

        return map;
    }

    /**
     * 2.1.10 暂停/启用应用 [POST] /app_routers/pause/{id}
     *
     * @param id          应用路由id
     * @param tenantPause 应用暂停入参
     * @return 应用路由
     */
    @RequestMapping(value = "/pause/{id}", method = RequestMethod.POST)
    public AppRouterDomain pause(@PathVariable String id,
            @Valid @RequestBody TenantPause tenantPause) {

        AppRouterDomain appRouter = appRouterService.findOne(id);

        if (appRouter == null) {
            throw new BizException(HttpStatus.NOT_FOUND, "APP_NOT_FOUND", "路由不存在");
        }

        appRouter.setPauseFlag(tenantPause.getPauseFlag());
        appRouter.setMsg(StringUtils.defaultIfBlank(tenantPause.getMsg(), "app temporary unavailable"));
        appRouter.setUpdateTime(new Date());

        appRouter = appRouterService.save(appRouter);

        return appRouter;
    }

    /**
     * 2.1.11 获取当前租户信息 [GET] /app_routers/tenant_info?biz_type={biz_type}
     *
     * @return 租户信息
     */
    @RequestMapping(value = "/tenant_info", method = RequestMethod.GET)
    public Tenant getCurrentTenantInfo() {

        String orgId = TenantProvider.getTenantId();

        return tenantService.getTenant(orgId);
    }


    /**
     * 根据应用id查询应用路由
     *
     * @param appId 应用id
     * @return AppRouterDomain
     */
    private AppRouterDomain routeByAppId(String appId) {

        AppRouterDomain appRouter = appRouterService.findAppRouter(appId, "0", "");

        if (appRouter == null) {
            throw new BizException(HttpStatus.NOT_FOUND, "APP_NOT_FOUND", "路由不存在");
        }
        return appRouter;
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

