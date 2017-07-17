/* =============================================================
 * Created: [2015/3/23 10:59] by wuzj(971643)
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */
package com.yzc.core;

import com.yzc.mongo.entity.Tenant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;


/**
 * 获取租户，用于分表前缀
 */
@Component(value = "tenantProvider")
public class TenantProvider {

    private static ThreadLocal<Tenant> tenantHolder = new NamedThreadLocal<>("tenant");

    public static void setTenant(Tenant tenant) {
        tenantHolder.set(tenant);
    }

    public static String getTenant() {

        Tenant tenant = tenantHolder.get();

        System.out.println("===========================:");
        if (tenant != null) {
            if (StringUtils.isNotBlank(tenant.getTenancy()))
                return tenant.getTenancy() + "_";
            else if (StringUtils.isNotBlank(tenant.getOrgId())) {
                return tenant.getOrgId() + "_";
            }
        }
        return "";
    }

    public static String getTenantId() {

        Tenant tenant = tenantHolder.get();
        if(tenant == null) {
            return "";
        } else {
            return tenant.getOrgId();
        }
    }

    public static void clear() {
        tenantHolder.remove();
    }
}
