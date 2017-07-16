package com.yzc.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.yzc.support.enums.RecordStatus;


/**
 * <p>
 * 所有实体的超类，定义了实体共同的属性。
 * </p>
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;



	/**
	 * 记录唯一标识符
	 */
	@Id
	@Column(name = "identifier")
//	@GeneratedValue(generator = "guid")
//	@GenericGenerator(name = "guid", strategy = "guid")
	private String identifier;

	/**
	 * 记录创建时间
	 */
	@Column(name = "create_time")
	// @Temporal(TemporalType.TIMESTAMP)
	// private Date createTime = new Date();
	private Date createTime;

	/**
	 * 记录最近更新时间
	 */
	@Column(name = "update_time")
	// @Temporal(TemporalType.TIMESTAMP)
	// private Date updateTime = new Date();
	private Date updateTime;

	/**
	 * 标识记录的逻辑状态，用于逻辑删除使用，禁止用作其它业务含义
	 */
	@Column(name = "state")
	@Enumerated(EnumType.ORDINAL)
	private RecordStatus state;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

	// @PreUpdate
	// public void preUpdate() {
	// this.setUpdateTime(new Date());
	// }

	public RecordStatus getState() {
		return state;
	}

	public void setState(RecordStatus state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
