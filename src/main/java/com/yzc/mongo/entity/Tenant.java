package com.yzc.mongo.entity;

import com.yzc.mongo.domain.TenantDomain;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 租户模型
 * <p>
 * Created by yzc on 2017/7/15.
 */
public class Tenant {


    @NotBlank(message = "org_id不能为空")
    private String orgId;    // 机构id
    @NotBlank(message = "name不能为空")
    private String name;    // 服务名称
    @NotBlank(message = "app_name不能为空")
    private String appName;    // 应用名称
    @NotBlank(message = "realm不能为空")
    private String realm;   //领域
    private Long developerUid;    // 开发者uid，即应用的创建者99U工号
    private Date createAt;    // 创建时间
    private String info;    // 备注信息
    private String addition;    // 扩展字段
    private Boolean pauseFlag = false;  //停服标志
    private String msg;        //停服提示信息
    private String accessAt;
    private String dbConn;
    private String tenancy;
    private Integer delStatus;
    private String bizType;
    private String templateId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public Long getDeveloperUid() {
        return developerUid;
    }

    public void setDeveloperUid(Long developerUid) {
        this.developerUid = developerUid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public Boolean getPauseFlag() {
        return pauseFlag;
    }

    public void setPauseFlag(Boolean pauseFlag) {
        this.pauseFlag = pauseFlag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccessAt() {
        return accessAt;
    }

    public void setAccessAt(String accessAt) {
        this.accessAt = accessAt;
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

    public Integer getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(Integer delStatus) {
        this.delStatus = delStatus;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public static Tenant of(TenantDomain tenantDomain) {

        Tenant tenant = new Tenant();

        tenant.setOrgId(tenantDomain.getOrgId());
        tenant.setName(tenantDomain.getName());
        tenant.setAppName(tenantDomain.getAppName());
        tenant.setRealm(tenantDomain.getRealm());
        tenant.setDeveloperUid(tenantDomain.getDeveloperUid());
        tenant.setCreateAt(tenantDomain.getCreateAt());
        tenant.setInfo(tenantDomain.getInfo());
        tenant.setAddition(tenantDomain.getAddition());
        tenant.setPauseFlag(tenantDomain.getPauseFlag());
        tenant.setMsg(tenantDomain.getMsg());
        tenant.setAccessAt(tenantDomain.getAccessAt());
        tenant.setDbConn(tenantDomain.getDbConn());
        tenant.setTenancy(tenantDomain.getTenancy());
        tenant.setDelStatus(tenantDomain.getDelStatus());

        return tenant;
    }
}
