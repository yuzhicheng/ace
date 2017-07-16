package com.yzc.mongo.domain;

import com.yzc.mongo.entity.Tenant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 租户
 *
 * Created by yzc on 2017/7/15.
 */
@Document(collection = "service")
@Data
@NoArgsConstructor
public class TenantDomain {

    @Id
    private String orgId;    // 机构id
    private String name;    // 服务名称
    private String appName;        // 应用名称
    private String realm;       //领域

    private Long developerUid;    // 开发者uid，即应用的创建者99U工号
    private Date createAt;        // 创建时间
    private String info;    // 备注信息
    private String addition;    // 扩展字段

    private Boolean pauseFlag = false;  //停服标志
    private String msg;        //停服提示信息

    private String accessAt;
    private String dbConn;
    private String tenancy;

    private Integer delStatus = 0;

    public static TenantDomain of(Tenant service) {

        TenantDomain tenantDomain = new TenantDomain();
        
        tenantDomain.setOrgId(service.getOrgId());
        tenantDomain.setName(service.getName());
        tenantDomain.setAppName(service.getAppName());
        tenantDomain.setRealm(service.getRealm());
        tenantDomain.setDeveloperUid(service.getDeveloperUid());
        tenantDomain.setCreateAt(service.getCreateAt());
        tenantDomain.setInfo(service.getInfo());
        tenantDomain.setAddition(service.getAddition());
        tenantDomain.pauseFlag = service.getPauseFlag();
        tenantDomain.msg = service.getMsg();
        tenantDomain.accessAt = service.getAccessAt();
        tenantDomain.dbConn = service.getDbConn();
        tenantDomain.tenancy = service.getTenancy();
        if (service.getDelStatus() != null) {
            tenantDomain.delStatus = service.getDelStatus();
        }
        return tenantDomain;
    }

}
