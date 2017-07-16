package com.yzc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.yzc.support.annotation.NoIndexBean;

@Entity
@Table(name = "res_categories")
@NoIndexBean
@NamedQueries({
    @NamedQuery(name="deleteResourceCategoryByResource",query="delete from ResCategory ti where resType=:rts AND resource=:resourceId"),
    @NamedQuery(name="commonQueryGetCategories", query="SELECT rc FROM ResCategory rc WHERE resType IN (:rts) AND resource IN  (:sids)"),
    @NamedQuery(name="batchGetCategories", query="SELECT ti FROM ResCategory ti WHERE resType=:rts AND resource IN  (:resIds)")
})
public class ResCategory extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="resource")
	private String resource;
	
	@Column(name="taxonpath")
	private String taxonpath;
	
	@Column(name="taxoncode")
	private String taxoncode;
	
	@Column(name="taxonname")
	private String taxonname;
	
	@Column(name="taxoncodeid")
	private String taxoncodeid;
	
	@Column( name = "short_name")
	private String shortName;
	
	@Column( name = "category_code")
	private String categoryCode;
	
	@Column( name = "category_name")
	private String categoryName;
	
    @Column(name="res_type")
    private String resType;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getTaxonpath() {
		return taxonpath;
	}

	public void setTaxonpath(String taxonpath) {
		this.taxonpath = taxonpath;
	}

	public String getTaxoncode() {
		return taxoncode;
	}

	public void setTaxoncode(String taxoncode) {
		this.taxoncode = taxoncode;
	}

	public String getTaxonname() {
		return taxonname;
	}

	public void setTaxonname(String taxonname) {
		this.taxonname = taxonname;
	}

	public String getTaxoncodeid() {
		return taxoncodeid;
	}

	public void setTaxoncodeid(String taxoncodeid) {
		this.taxoncodeid = taxoncodeid;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}
    
}

