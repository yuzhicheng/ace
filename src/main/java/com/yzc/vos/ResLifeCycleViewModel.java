package com.yzc.vos;


import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.yzc.support.annotation.Reg;
import com.yzc.vos.valid.LifecycleDefault;
import com.yzc.vos.valid.LifecycleDefault4Update;


/**
 * @author johnny
 * @version 1.0
 * @created 08-7月-2015 10:18:49
 */
public class ResLifeCycleViewModel {
	
	@NotBlank(message="{resourceViewModel.lifeCycle.version.notBlank.validmsg}",groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	@Length(message="{resourceViewModel.lifeCycle.version.maxLength.validmsg}",max=100, groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	private String version;
	
	@NotBlank(message="{resourceViewModel.lifeCycle.status.notBlank.validmsg}",groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	@Reg(message = "{resourceViewModel.lifeCycle.status.reg.validmsg}", pattern = "^(CREATING|CREATED|EDITING|EDITED|TRANSCODE_WAITING|TRANSCODING|TRANSCODED|TRANSCODE_ERROR|AUDIT_WAITING|AUDITING|AUDITED|PUBLISH_WAITING|PUBLISHING|PUBLISHED|ONLINE|OFFLINE|AUDIT_REJECT|REMOVED|CREATE|INIT|TRANSCODE|AUDIT|REJECT)$", isNullValid = false,groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	//FIXME CREATE|INIT|TRANSCODE|AUDIT|REJECT 这5个应该去掉,在LifecycleStatus枚举类中不存在
	private String status;
	
	@NotNull(message="{resourceViewModel.lifeCycle.enable.notNull.validmsg}",groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	private boolean enable = true;
	
	@NotBlank(message="{resourceViewModel.lifeCycle.creator.notBlank.validmsg}",groups={LifecycleDefault.class})
	private String creator;
	
	private String publisher;
	
	@Length(message="{resourceViewModel.lifeCycle.provider.maxLength.validmsg}",max=100, groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	private String provider;
	
	@Length(message="{resourceViewModel.lifeCycle.providerSource.maxLength.validmsg}",max=500, groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	private String providerSource;
	
	@Length(message="{resourceViewModel.lifeCycle.providerMode.maxLength.validmsg}",max=500, groups={LifecycleDefault.class,LifecycleDefault4Update.class})
	private String providerMode;
	
	private Date createTime;
	
	private Date updateTime;

	public ResLifeCycleViewModel(){

	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderSource() {
		return providerSource;
	}

	public void setProviderSource(String providerSource) {
		this.providerSource = providerSource;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getProviderMode() {
		return providerMode;
	}

	public void setProviderMode(String providerMode) {
		this.providerMode = providerMode;
	}
	
}
