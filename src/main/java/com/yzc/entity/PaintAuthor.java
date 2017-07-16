package com.yzc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "author")
public class PaintAuthor extends BaseEntity {

	/**
	 * 名画作者
	 */
	private static final long serialVersionUID = 1L;

	// 用户名
	@Column(name = "author_name")
	private String authorName;
	// 出生日期
	@Column(name = "birthdate")
	private String birthdate;
	// 去世日期
	@Column(name = "deathdate")
	private String deathdate;
	// 国籍
	@Column(name = "nationality")
	private String nationality;
	// 流派
	@Column(name = "genre")
	private String genre;

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getDeathdate() {
		return deathdate;
	}

	public void setDeathdate(String deathdate) {
		this.deathdate = deathdate;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
