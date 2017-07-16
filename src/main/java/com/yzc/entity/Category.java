package com.yzc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "categorys")
public class Category extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "gb_code")
	private String gbCode;

	@Column(name = "nd_code")
	private String ndCode;

	@Column(name = "purpose")
	private String purpose;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "source")
	private String source;

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

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	

}
