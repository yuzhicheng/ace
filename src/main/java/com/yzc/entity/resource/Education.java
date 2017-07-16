package com.yzc.entity.resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yzc.entity.BaseEntity;
import com.yzc.repository.Impl.DataConverter;

@Entity
@Table(name = "resource")
@Inheritance(strategy=InheritanceType.JOINED)
public class Education extends BaseEntity{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	private Map<String, String> preview = new HashMap<String, String>();
	
	@DataConverter(target="preview", type=Map.class)
	@Column(name = "preview")
	private String dbpreview;
	
	@Transient
	protected List<String> relations;
	
	@Transient
	protected List<String> tags;
	
	@DataConverter(target = "tags", type = List.class)
	@Column(name = "tags")
	protected String dbtags;
	
	@Transient
	protected List<String> keywords;
	
	@DataConverter(target = "keywords", type = List.class)
	@Column(name = "keywords")
	protected String dbkeywords;
	
	@Column(name = "language")
	protected String language;
		
	@Column(name = "custom_properties")
	private String customProperties;
	
	@Column(name="code")
	private String resCode;
	
	@Column(name="primary_category")
	private String primaryCategory;

	@Transient
	private Map<String, String> globalTitle;

	@DataConverter(target="globalTitle", type=Map.class)
	@Column(name = "global_title")
	private String dbglobalTitle;
	
	@Transient
	private Map<String, String> globalDescription;

	@DataConverter(target="globalDescription", type=Map.class)
	@Column(name = "global_description")
	private String dbglobalDescription;

	@Transient
	private Map<String, String> globalKeywords;

	@DataConverter(target="globalKeywords", type=Map.class)
	@Column(name = "global_keywords")
	private String dbglobalKeywords;

	@Transient
	private Map<String, String> globalTags;

	@DataConverter(target="globalTags", type=Map.class)
	@Column(name = "global_tags")
	private String dbglobalTags;
	
	/*** ******************生命周期属性 start **************** **/
		
	@Column(name = "enable")
	private Boolean enable = true;
	
	/**
	* 资源来源
	*/
	@Column(name = "provider_source")
	protected String providerSource;
		
	/**
	 * 资源来源方式
	 */
	@Column(name = "provider_mode")
	protected String providerMode;
	
	/**
	* 资源提供商
	*/
	@Column(name = "provider")
	protected String provider;
	
	/**
	* 资源版本
	*/
	@Column(name = "version")
	protected String version;
	
	/**
	* 资源状态
	*/
	@Column(name = "status")
	protected String status;
	
	/**
	* 资源发行者
	*/
	@Column(name = "publisher")
	protected String publisher;
	
	/**
	* 资源创建者
	*/
	@Column(name = "creator")
	protected String creator;
	
	/*** ******************生命周期属性 end **************** **/
	
	/*** ******************版权属性 start **************** **/
	/**
	* 资源版权信息
	*/
	@Column(name = "cr_right")
	protected String crRight;
	
	/**
	* 资源版权描述
	*/
	@Column(name = "cr_description")
	protected String crDescription;
	
	/**
	* 资源作者
	*/
	@Column(name="author")
	protected String author;
	
	/**
	* 资源版权开始时间
	*/
	@Column(name="right_start_date")
	protected BigDecimal rightStartDate;
	
	/**
	* 资源版权结束时间
	*/
	@Column(name="right_end_date")
	protected BigDecimal rightEndDate;
	
	/**
	* 资源是否有版权
	*/
	@Column(name="has_right")
	protected Boolean hasRight;
	
	@Transient
	private Map<String, String> globalCrDescription;

	@DataConverter(target="globalCrDescription", type=Map.class)
	@Column(name = "global_cr_description")
	private String dbglobalCrDescription;
	
	/*** ******************版权属性 end **************** **/
	
	/*** ******************教育属性 start **************** **/
	@Column(name = "interactivity")
	private Integer interactivity;

	@Column(name = "interactivity_level")
	private Integer interactivityLevel;

	@Column(name = "end_user_type")
	private String endUserType;

	@Column(name = "semantic_density")
	private Integer semanticDensity;

	@Column(name = "age_range")
	private String ageRange;

	@Column(name = "context")
	private String context;

	@Column(name = "difficulty")
	private String difficulty;

	@Column(name = "learning_time")
	private String learningTime;

	@Column(name = "edu_description")
	private String eduDescription;

	@Column(name = "edu_language")
	private String eduLanguage;
	
	@Transient
	private Map<String, String> globalEduDescription;

	@DataConverter(target="globalEduDescription", type=Map.class)
	@Column(name = "global_edu_description")
	private String dbglobalEduDescription;
	
	/*** ******************教育属性 end **************** **/

	public Map<String, String> getPreview() {
		return preview;
	}

	public void setPreview(Map<String, String> preview) {
		this.preview = preview;
	}

	public String getDbpreview() {
		return dbpreview;
	}

	public void setDbpreview(String dbpreview) {
		this.dbpreview = dbpreview;
	}

	public List<String> getRelations() {
		return relations;
	}

	public void setRelations(List<String> relations) {
		this.relations = relations;
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

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getDbkeywords() {
		return dbkeywords;
	}

	public void setDbkeywords(String dbkeywords) {
		this.dbkeywords = dbkeywords;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCustomProperties() {
		return customProperties;
	}

	public void setCustomProperties(String customProperties) {
		this.customProperties = customProperties;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(String primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public Map<String, String> getGlobalTitle() {
		return globalTitle;
	}

	public void setGlobalTitle(Map<String, String> globalTitle) {
		this.globalTitle = globalTitle;
	}

	public String getDbglobalTitle() {
		return dbglobalTitle;
	}

	public void setDbglobalTitle(String dbglobalTitle) {
		this.dbglobalTitle = dbglobalTitle;
	}

	public Map<String, String> getGlobalDescription() {
		return globalDescription;
	}

	public void setGlobalDescription(Map<String, String> globalDescription) {
		this.globalDescription = globalDescription;
	}

	public String getDbglobalDescription() {
		return dbglobalDescription;
	}

	public void setDbglobalDescription(String dbglobalDescription) {
		this.dbglobalDescription = dbglobalDescription;
	}

	public Map<String, String> getGlobalKeywords() {
		return globalKeywords;
	}

	public void setGlobalKeywords(Map<String, String> globalKeywords) {
		this.globalKeywords = globalKeywords;
	}

	public String getDbglobalKeywords() {
		return dbglobalKeywords;
	}

	public void setDbglobalKeywords(String dbglobalKeywords) {
		this.dbglobalKeywords = dbglobalKeywords;
	}

	public Map<String, String> getGlobalTags() {
		return globalTags;
	}

	public void setGlobalTags(Map<String, String> globalTags) {
		this.globalTags = globalTags;
	}

	public String getDbglobalTags() {
		return dbglobalTags;
	}

	public void setDbglobalTags(String dbglobalTags) {
		this.dbglobalTags = dbglobalTags;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getProviderSource() {
		return providerSource;
	}

	public void setProviderSource(String providerSource) {
		this.providerSource = providerSource;
	}

	public String getProviderMode() {
		return providerMode;
	}

	public void setProviderMode(String providerMode) {
		this.providerMode = providerMode;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
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

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCrRight() {
		return crRight;
	}

	public void setCrRight(String crRight) {
		this.crRight = crRight;
	}

	public String getCrDescription() {
		return crDescription;
	}

	public void setCrDescription(String crDescription) {
		this.crDescription = crDescription;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public BigDecimal getRightStartDate() {
		return rightStartDate;
	}

	public void setRightStartDate(BigDecimal rightStartDate) {
		this.rightStartDate = rightStartDate;
	}

	public BigDecimal getRightEndDate() {
		return rightEndDate;
	}

	public void setRightEndDate(BigDecimal rightEndDate) {
		this.rightEndDate = rightEndDate;
	}

	public Boolean getHasRight() {
		return hasRight;
	}

	public void setHasRight(Boolean hasRight) {
		this.hasRight = hasRight;
	}

	public Map<String, String> getGlobalCrDescription() {
		return globalCrDescription;
	}

	public void setGlobalCrDescription(Map<String, String> globalCrDescription) {
		this.globalCrDescription = globalCrDescription;
	}

	public String getDbglobalCrDescription() {
		return dbglobalCrDescription;
	}

	public void setDbglobalCrDescription(String dbglobalCrDescription) {
		this.dbglobalCrDescription = dbglobalCrDescription;
	}

	public Integer getInteractivity() {
		return interactivity;
	}

	public void setInteractivity(Integer interactivity) {
		this.interactivity = interactivity;
	}

	public Integer getInteractivityLevel() {
		return interactivityLevel;
	}

	public void setInteractivityLevel(Integer interactivityLevel) {
		this.interactivityLevel = interactivityLevel;
	}

	public String getEndUserType() {
		return endUserType;
	}

	public void setEndUserType(String endUserType) {
		this.endUserType = endUserType;
	}

	public Integer getSemanticDensity() {
		return semanticDensity;
	}

	public void setSemanticDensity(Integer semanticDensity) {
		this.semanticDensity = semanticDensity;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getLearningTime() {
		return learningTime;
	}

	public void setLearningTime(String learningTime) {
		this.learningTime = learningTime;
	}
	
	public String getEduDescription() {
		return eduDescription;
	}

	public void setEduDescription(String eduDescription) {
		this.eduDescription = eduDescription;
	}

	public String getEduLanguage() {
		return eduLanguage;
	}

	public void setEduLanguage(String eduLanguage) {
		this.eduLanguage = eduLanguage;
	}

	public Map<String, String> getGlobalEduDescription() {
		return globalEduDescription;
	}

	public void setGlobalEduDescription(Map<String, String> globalEduDescription) {
		this.globalEduDescription = globalEduDescription;
	}

	public String getDbglobalEduDescription() {
		return dbglobalEduDescription;
	}

	public void setDbglobalEduDescription(String dbglobalEduDescription) {
		this.dbglobalEduDescription = dbglobalEduDescription;
	}
	
}
