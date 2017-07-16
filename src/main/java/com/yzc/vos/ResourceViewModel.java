package com.yzc.vos;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.yzc.support.annotation.MapValid;
import com.yzc.vos.valid.BasicInfoDefault;
import com.yzc.vos.valid.CategoriesDefault;


/**
 * 教育资源模型。
 * @author johnny
 * @version 1.0
 * @created 08-7月-2015 10:18:49
 */
public class ResourceViewModel {

	//lc默认值
	private static ResLifeCycleViewModel lc = new ResLifeCycleViewModel();
	static{
		lc.setVersion("v0.1");
		lc.setStatus("CREATED");
		lc.setEnable(true);
		lc.setCreator("{USER}");
		lc.setPublisher("UNKNOW");
		lc.setProvider("NetDragon Inc.");
		lc.setProviderSource("UNKNOW");
	}

	/**
	 * 对象的主键，主键类型采用UUID的形式进行存储
	 */
	private String identifier;
		
	/**
	 * 学习对象的标题名称
	 */
    @NotBlank(message="{resourceViewModel.title.notBlank.validmsg}",groups={BasicInfoDefault.class})
    @Length(message="{resourceViewModel.title.maxlength.validmsg}",max=200,groups={BasicInfoDefault.class})
	private String title;

	@JsonInclude(Include.NON_NULL)
	private Map<String, String> globalTitle;
	/**
	 * 学习对象的文字描述，对于文字描述的长度约定为100个汉字
	 */
//    @Length(message="{resourceViewModel.description.maxlength.validmsg}",max=500,groups={BasicInfoDefault.class, LessPropertiesDefault.class, UpdateKnowledgeDefault.class})
	private String description;

	@JsonInclude(Include.NON_NULL)
	private Map<String, String> globalDescription;

	@JsonInclude(Include.NON_NULL)
	private Map<String, String> globalKeywords;

	@JsonInclude(Include.NON_NULL)
	private Map<String, String> globalTags;

//	@JsonInclude(Include.NON_NULL)
//	private Map<String, String> globalEduDescription;
//
//	@JsonInclude(Include.NON_NULL)
//	private Map<String, String> globalCrDescription;
	/**
	 * 学习对象的语言标识
	 */
    @NotBlank(message="{resourceViewModel.language.notBlank.validmsg}",groups={BasicInfoDefault.class})
    @Length(message="{resourceViewModel.language.maxlength.validmsg}",max=16,groups={BasicInfoDefault.class})
	private String language;
	/**
	 * 预览的路径
	 */
	@NotNull(message="{resourceViewModel.preview.notNull.validmsg}",groups={BasicInfoDefault.class})
	private Map<String,String> preview;
	/**
	 * 社会化标签
	 */
	private List<String> tags;
	/**
	 * 关键字
	 */
	private List<String> keywords;
	/**
	 * 资源编码
	 */
	@Length(message="{resourceViewModel.ndresCode.maxlength.validmsg}",max=100,groups={BasicInfoDefault.class})
	@JsonInclude(Include.NON_NULL)
	private String ndresCode;
	/**
	 * 资源关系
	 */
	@JsonInclude(Include.NON_NULL)
	private String relationId;

	/**
	 * 统计数量
	 */
	@JsonInclude(Include.NON_NULL)
	private Double statisticsNum;
	
	/**
	 * 自定义扩展属性
	 */
	private Map<String,Object> customProperties;
	
	/**
	 * 维度数据
	 */
	@NotNull(message="{resourceViewModel.categories.notNull.validmsg}",groups={CategoriesDefault.class})
	@JsonInclude(Include.NON_NULL)
	@MapValid(message="",groups={CategoriesDefault.class})
	private Map<String,List<? extends ResClassificationViewModel>> categories;
	
	/**
	 * 生命周期
	 */
	@Valid
	@JsonInclude(Include.NON_NULL)
	private ResLifeCycleViewModel lifeCycle = lc;
	
	/**
	 * 资源的教育属性
	 */
	@JsonInclude(Include.NON_NULL)
	private ResEducationalViewModel educationInfo;
	
	/**
	 * 教育资源相关的技术属性，包括格式，大小，存储位置，技术需求描述，md5码等。
	 */
	@Valid
	@JsonInclude(Include.NON_NULL)
	private Map<String,? extends ResTechInfoViewModel> techInfo;
	/**
	 * 资源的覆盖范围，包括时间上，空间上，角色以及组织等
	 */
	@Valid
	@JsonInclude(Include.NON_NULL)
	private List<? extends ResCoverageViewModel> coverages;

	/**
	 * 描述资源和资源之间的关系
	 */
	@Valid
	@JsonInclude(Include.NON_NULL)
	private List<? extends ResRelationViewModel> relations;
	
	/**
	 * 资源的版权信息
	 */
	@JsonInclude(Include.NON_NULL)
	@Valid
	private ResRightViewModel copyright;

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

	public Map<String, String> getGlobalTitle() {
		return globalTitle;
	}

	public void setGlobalTitle(Map<String, String> globalTitle) {
		this.globalTitle = globalTitle;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, String> getGlobalDescription() {
		return globalDescription;
	}

	public void setGlobalDescription(Map<String, String> globalDescription) {
		this.globalDescription = globalDescription;
	}

	public Map<String, String> getGlobalKeywords() {
		return globalKeywords;
	}

	public void setGlobalKeywords(Map<String, String> globalKeywords) {
		this.globalKeywords = globalKeywords;
	}

	public Map<String, String> getGlobalTags() {
		return globalTags;
	}

	public void setGlobalTags(Map<String, String> globalTags) {
		this.globalTags = globalTags;
	}

	//	public void setGlobalEduDescription(Map<String, String> globalEduDescription) {
//		this.globalEduDescription = globalEduDescription;
//	}
//
//	public void setGlobalCrDescription(Map<String, String> globalCrDescription) {
//		this.globalCrDescription = globalCrDescription;
//	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public Map<String, String> getGlobalEduDescription() {
//		return globalEduDescription;
//	}
//
//	public Map<String, String> getGlobalCrDescription() {
//		return globalCrDescription;
//	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Map<String, String> getPreview() {
		return preview;
	}

	public void setPreview(Map<String, String> preview) {
		this.preview = preview;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public Map<String, List<? extends ResClassificationViewModel>> getCategories() {
		return categories;
	}

	public void setCategories(
			Map<String,List<? extends ResClassificationViewModel>> categories) {
		this.categories = categories;
	}

	public ResLifeCycleViewModel getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(ResLifeCycleViewModel lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public ResEducationalViewModel getEducationInfo() {
		return educationInfo;
	}

	public void setEducationInfo(ResEducationalViewModel educationInfo) {
		/*if (educationInfo != null) {
			// 设置global edu_description
			this.globalEduDescription = educationInfo.getGlobalEduDescription();
		}*/
		this.educationInfo = educationInfo;
	}

	public Map<String, ? extends ResTechInfoViewModel> getTechInfo() {
		return techInfo;
	}

	public void setTechInfo(Map<String, ? extends ResTechInfoViewModel> techInfo) {
		this.techInfo = techInfo;
	}

	public List<? extends ResCoverageViewModel> getCoverages() {
		return coverages;
	}

	public void setCoverages(List<? extends ResCoverageViewModel> coverages) {
		this.coverages = coverages;
	}

	public List<? extends ResRelationViewModel> getRelations() {
		return relations;
	}

	public void setRelations(List<? extends ResRelationViewModel> relations) {
		this.relations = relations;
	}

	public ResRightViewModel getCopyright() {
		return copyright;
	}

	public void setCopyright(ResRightViewModel copyright) {
		/*if (copyright != null) {
			// 设置global edu_description
			this.globalCrDescription = copyright.getGlobalCrDescription();
		}*/
		this.copyright = copyright;
	}

	public Map<String,Object> getCustomProperties() {
		return customProperties;
	}

	public void setCustomProperties(Map<String,Object> customProperties) {
		this.customProperties = customProperties;
	}

	public String getNdresCode() {
		return ndresCode;
	}

	public void setNdresCode(String ndresCode) {
		this.ndresCode = ndresCode;
	}
	
	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public Double getStatisticsNum() {
		return statisticsNum;
	}

	public void setStatisticsNum(Double statisticsNum) {
		this.statisticsNum = statisticsNum;
	}
}