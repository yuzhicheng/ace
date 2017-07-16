package com.yzc.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
  
@Entity
@Table(name="category_datas")
public class CategoryData extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="category")
 	private String category; 
	
	@Column(name="dimension_path")
 	private String dimensionPath; 

	@Column(name="gb_code")
 	private String gbCode; 

	@Column(name="nd_code")
 	private String ndCode; 

	@Column(name="order_num")
 	private Integer orderNum; 

	@Column(name="parent")
 	private String parent; 

	@Column(name="short_name")
 	private String shortName; 
	
	@Column(name="preview")
	private String preview;
	/**
	 * 是否默认（0否，1是）
	 */
	@Column(name="is_default")
	private Integer isDefault;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDimensionPath() {
		return dimensionPath;
	}

	public void setDimensionPath(String dimensionPath) {
		this.dimensionPath = dimensionPath;
	}

	public String getGbCode() {
		return gbCode;
	}

	public void setGbCode(String gbCode) {
		this.gbCode = gbCode;
	}

	public String getNdCode() {
		return ndCode;
	}

	public void setNdCode(String ndCode) {
		this.ndCode = ndCode;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
	
}
