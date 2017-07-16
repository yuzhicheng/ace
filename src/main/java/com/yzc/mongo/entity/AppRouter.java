package com.yzc.mongo.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * Created by yzc on 2017/7/15.
 */
public class AppRouter {

    @NotBlank(message = "app_id can't be blank")
    @Pattern(regexp = "^[a-zA-Z0-9-]{1,36}$", message = "app_id max length 36 and must be made up of [a-zA-Z0-9-]")
    private String appId;

    /**
     * 组织id
     */
    @Length(max = 12, message = "org_id max length 12")
    private String orgId;

    /**
     * 业务属性
     */
    @Pattern(regexp = "^[\\w]{0,128}$", message = "biz_type max length 128 and must be made up of [a-zA-Z0-9_]")
    private String bizType;

    /**
     * 租户id
     */
    @Length(max = 24, message = "service_tenant_id max length 24")
    private String serviceTenantId;

    /**
     * 应用名称
     */
    @NotBlank(message = "app_name can't be blank")
    @Length(max = 64, message = "app_name max length 64")
    private String appName;

    /**
     * 开发者uid
     */
    private Long developerUid;

    /**
     * 应用是否暂停
     */
    private boolean pauseFlag;

    /**
     * 暂停应用返回信息
     */
    private String msg;

    /**
     * 连接的数据库
     */
    private String dbConn;

    /**
     * 租户，用于数据库分表
     */
    private String tenancy;

    /**
     * 模板
     */
    private String templateId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getServiceTenantId() {
        return serviceTenantId;
    }

    public void setServiceTenantId(String serviceTenantId) {
        this.serviceTenantId = serviceTenantId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Long getDeveloperUid() {
        return developerUid;
    }

    public void setDeveloperUid(Long developerUid) {
        this.developerUid = developerUid;
    }

    public boolean isPauseFlag() {
        return pauseFlag;
    }

    public void setPauseFlag(boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDbConn() {
        return dbConn;
    }

    public void setDbConn(String dbConn) {
        this.dbConn = dbConn;
    }

    public String getTenancy() {
        return tenancy;
    }

    public void setTenancy(String tenancy) {
        this.tenancy = tenancy;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}

