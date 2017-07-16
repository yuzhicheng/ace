package com.yzc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.yzc.support.annotation.NoIndexBean;

/**
 * @author yzc
 * @version 1.0
 * @date 2016年9月20日 下午4:38:37
 */
@Entity
@Table(name = "tech_infos")
@NoIndexBean
@NamedQueries({
		@NamedQuery(name = "deleteTechInfoByResource", query = "delete from TechInfo ti where ti.resource=:resourceId"),
		@NamedQuery(name = "commonQueryGetTechInfos", query = "SELECT ti from TechInfo ti where ti.resType IN (:rts) AND ti.resource IN  (:sids)") })
public class TechInfo extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "format")
	private String format;

	@Column(name = "size")
	private Long size;

	@Column(name = "md5")
	private String md5;

	@Column(name = "location")
	private String location;

	@Column(name = "resource")
	private String resource;

	@Column(name = "secure_key")
	private String secureKey;

	@Column(name = "res_type")
	private String resType;

	@Column(name = "entry")
	private String entry;

	@Column(name = "requirements", length = 60000)
	private String requirements;

	@Column(name = "printable")
	private Boolean printable;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getSecureKey() {
		return secureKey;
	}

	public void setSecureKey(String secureKey) {
		this.secureKey = secureKey;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public Boolean getPrintable() {
		return printable;
	}

	public void setPrintable(Boolean printable) {
		this.printable = printable;
	}

}
