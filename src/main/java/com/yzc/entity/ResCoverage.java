package com.yzc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.yzc.support.annotation.NoIndexBean;

@Entity
@Table(name = "res_coverages")
@NoIndexBean
@NamedQuery(name = "batchGetCoverageByResource", query = "SELECT rc FROM ResCoverage rc WHERE rc.resType=:rt AND resource IN (:rids)")
public class ResCoverage extends BaseEntity{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name ="target_type")
	private String targetType;
	
	@Column(name ="strategy")
	private String strategy;
	
	@Column(name ="target")
	private String target;
	
	@Column(name ="target_title")
	private String targetTitle;
	
	@Column(name="resource")
	private String resource;
	
	@Column(name ="res_type")
	private String resType;

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetTitle() {
		return targetTitle;
	}

	public void setTargetTitle(String targetTitle) {
		this.targetTitle = targetTitle;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}
	
	

}
