package com.yzc.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yzc.repository.Impl.DataConverter;

@Entity
@Table(name = "paints")
public class Paint extends BaseEntity {

	private static final long serialVersionUID = 1L;
	// 中文标题
	@Column(name = "title_cn")
	private String titleCn;
	// 作品类型
	@Column(name = "object_type")
	private String objectType;
	// 创建时间
	@Column(name = "write_date")
	private String writeDate;
	// 形式
	@Column(name = "medium")
	private String medium;
	// 现存地点
	@Column(name = "current_location")
	private String currentLocation;
	// 画作内容
	@Column(name = "content")
	private String content;
	// 创作背景
	@Column(name = "background")
	private String background;
	// 构图技法
	@Column(name = "skill")
	private String skill;
	// 作品欣赏
	@Column(name = "appreciation")
	private String appreciation;
	// 流行文化
	@Column(name = "popular_culture")
	private String popularCulture;
	// 作者id
	@Column(name = "author_id")
	private String authorId;
	// 版权名称
	@Column(name = "copyright")
	private String copyright;
	// 名画高度
	@Column(name = "height")
	private String height;
	// 名画高度
	@Column(name = "width")
	private String width;
	// 标签
	@Transient
	private List<String> tags;
	@DataConverter(target = "tags", type = List.class)
	@Column(name = "tags")
	private String dbtags;

	// 提供商
	@Column(name = "provider")
	private String provider;
	// 来源
	@Column(name = "provider_source")
	private String providerSource;
	// 具体来源
	@Column(name = "specific_source")
	private String specificSource;
	// 创建者
	@Column(name = "creator")
	private String creator;
	// 资源状态
	@Column(name = "estatus")
	private String estatus;
	
	public String getTitleCn() {
		return titleCn;
	}
	public void setTitleCn(String titleCn) {
		this.titleCn = titleCn;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getAppreciation() {
		return appreciation;
	}
	public void setAppreciation(String appreciation) {
		this.appreciation = appreciation;
	}
	public String getPopularCulture() {
		return popularCulture;
	}
	public void setPopularCulture(String popularCulture) {
		this.popularCulture = popularCulture;
	}

	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getDbtags() {
		return dbtags;
	}
	public void setDbtags(String dbtags) {
		this.dbtags = dbtags;
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
	public String getSpecificSource() {
		return specificSource;
	}
	public void setSpecificSource(String specificSource) {
		this.specificSource = specificSource;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getEstatus() {
		return estatus;
	}
	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

}
