package com.yzc.vos.paints;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yzc.vos.ResClassificationViewModel;
import com.yzc.vos.ResCoverageViewModel;
import com.yzc.vos.ResTechInfoViewModel;
import com.yzc.vos.valid.PaintDefault;


/**
 * 名画viewModel
 * 
 * @author yzc
 * @version 0.1
 * @date 2016年12月1日
 */
public class PaintViewModel {
	// 名画id
	private String identifier;
	// 原始标题
	@NotBlank(message = "{paintViewModel.title.notBlank.validmsg}",groups={PaintDefault.class})
	@Length(max = 500, message = "{paintViewModel.title.maxlength.validmsg}")
	private String title;
	// 描述
	@Length(max = 1000, message = "{paintViewModel.description.maxlength.validmsg}")
	private String description;
	// 中文标题
	@Length(max = 100, message = "{paintViewModel.titleCn.maxlength.validmsg}")
	private String titleCn;
	// 作品类型
	@Length(max = 200, message = "{paintViewModel.objectType.maxlength.validmsg}")
	private String objectType;
	// 创建时间
	@Length(max = 100, message = "{paintViewModel.writeDate.maxlength.validmsg}")
	private String writeDate;
	// 形式
	@Length(max = 500, message = "{paintViewModel.medium.maxlength.validmsg}")
	private String medium;
	// 现存地点
	@Length(max = 100, message = "{paintViewModel.currentLocation.maxlength.validmsg}")
	private String currentLocation;
	// 画作内容
	@Length(max = 500, message = "{paintViewModel.content.maxlength.validmsg}")
	private String content;
	// 创作背景
	@Length(max = 200, message = "{paintViewModel.background.maxlength.validmsg}")
	private String background;
	// 构图技法
	@Length(max = 100, message = "{paintViewModel.skill.maxlength.validmsg}")
	private String skill;
	// 作品欣赏
	@Length(max = 200, message = "{paintViewModel.appreciation.maxlength.validmsg}")
	private String appreciation;
	// 流行文化
	@Length(max = 100, message = "{paintViewModel.popularCulture.maxlength.validmsg}")
	private String popularCulture;
	// 作者id
	@NotBlank(message = "{paintViewModel.authorId.notBlank.validmsg}",groups={PaintDefault.class})
	private String authorId;
	// 作家姓名
	@JsonInclude(Include.NON_NULL)
	private String authorName;
	// 版权名称
	@Length(max = 100, message = "{paintViewModel.copyright.maxlength.validmsg}")
	private String copyright;
	// 名画高度
	@Length(max = 100, message = "{paintViewModel.height.maxlength.validmsg}")
	private String height;
	// 名画宽度
	@Length(max = 100, message = "{paintViewModel.width.maxlength.validmsg}")
	private String width;
	// 标签
	private List<String> tags;
	// 提供商
	@Length(max = 1000, message = "{paintViewModel.provider.maxlength.validmsg}")
	private String provider;
	// 来源
	@Length(max = 1000, message = "{paintViewModel.providerSource.maxlength.validmsg}")
	private String providerSource;
	// 具体来源
	@Length(max = 1000, message = "{paintViewModel.specificSource.maxlength.validmsg}")
	private String specificSource;
	// 创建者
	@Length(max = 200, message = "{paintViewModel.creator.maxlength.validmsg}")
	private String creator;
	// 资源状态
	@NotBlank(message = "{paintViewModel.status.notBlank.validmsg}",groups={PaintDefault.class})
	private String estatus;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;

	/**
	 * 资源相关的技术属性，包括格式，大小，存储位置，技术需求描述，md5码等。
	 */
	@Valid
	@NotNull(message = "{paintViewModel.techInfo.notNull.validmsg}")
	private Map<String, ? extends ResTechInfoViewModel> techInfo;

	/**
	 * 分类数据
	 */
	@Valid
	private Map<String, List<? extends ResClassificationViewModel>> categories;
	
	/**
	 * 资源的覆盖范围，包括时间上，空间上，角色以及组织等
	 */
	@Valid
	@JsonInclude(Include.NON_NULL)
	private List<? extends ResCoverageViewModel> coverages;

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

	public Map<String, ? extends ResTechInfoViewModel> getTechInfo() {
		return techInfo;
	}

	public void setTechInfo(Map<String, ? extends ResTechInfoViewModel> techInfo) {
		this.techInfo = techInfo;
	}

	public Map<String, List<? extends ResClassificationViewModel>> getCategories() {
		return categories;
	}

	public void setCategories(Map<String, List<? extends ResClassificationViewModel>> categories) {
		this.categories = categories;
	}

	public List<? extends ResCoverageViewModel> getCoverages() {
		return coverages;
	}

	public void setCoverages(List<? extends ResCoverageViewModel> coverages) {
		this.coverages = coverages;
	}

}
