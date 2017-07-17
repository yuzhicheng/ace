package com.yzc.mongo.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by yzc on 2017/7/16.
 */
public class TenantPause {

    @NotNull(message = "pause_flag can\'t be null")
    private Boolean pauseFlag;

    @Length(max = 256, message = "msg max length 256")
    private String msg;

    public TenantPause() {
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
}
