package com.yzc.models.paints;

import java.util.Date;
import java.util.List;

import com.yzc.models.ResClassificationModel;
import com.yzc.models.ResCoverageModel;
import com.yzc.models.ResTechInfoModel;
import com.yzc.support.enums.RecordStatus;

public class PaintModel {
	// 名画id
	private String identifier;
	// 原始标题
	private String title;
	// 描述
	private String description;
	// 中文标题
	private String titleCn;
	// 作品类型
	private String objectType;
	// 创建时间
	private String writeDate;
	// 形式
	private String medium;
	// 现存地点
	private String currentLocation;
	// 画作内容
	private String content;
	// 创作背景
	private String background;
	// 构图技法
	private String skill;
	// 作品欣赏
	private String appreciation;
	// 流行文化
	private String popularCulture;
	// 用户id
	private String authorId;
	// 用户名(冗余)
	private String authorName;
	// 版权名称
	private String copyright;
	// 名画高度
	private String height;
	// 名画高度
	private String width;
	// 标签
	private List<String> tags;
	// 提供商
	private String provider;
	// 来源
	private String providerSource;
	// 具体来源
	private String specificSource;
	// 创建者
	private String creator;
	// 资源状态
	private String estatus;
	// 记录状态
	private RecordStatus state;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;
	// 分类数据
	private List<ResClassificationModel> categoryList;
	// 技术属性
	private List<ResTechInfoModel> techInfoList;
	// 资源的覆盖范围
	private List<ResCoverageModel> coverages;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
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

	public RecordStatus getState() {
		return state;
	}

	public void setState(RecordStatus state) {
		this.state = state;
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

	public List<ResClassificationModel> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<ResClassificationModel> categoryList) {
		this.categoryList = categoryList;
	}

	public List<ResTechInfoModel> getTechInfoList() {
		return techInfoList;
	}

	public void setTechInfoList(List<ResTechInfoModel> techInfoList) {
		this.techInfoList = techInfoList;
	}

	public List<ResCoverageModel> getCoverages() {
		return coverages;
	}

	public void setCoverages(List<ResCoverageModel> coverages) {
		this.coverages = coverages;
	}

}
