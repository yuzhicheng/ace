package com.yzc.models.paints;

import java.util.Date;

import com.yzc.support.enums.RecordStatus;

public class AuthorModel {

	// 作者id
	public String authorId;
	// 原始标题
	private String title;
	// 描述
	private String description;
	// 作者姓名
	public String authorName;
	// 出生日期
	public String birthdate;
	// 去世日期
	public String deathdate;
	// 国籍
	public String nationality;
	// 流派
	public String genre;
	// 记录状态
	private RecordStatus state;
	// 创建时间
	private Date createTime;
	// 更新时间
	private Date updateTime;

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
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

}
