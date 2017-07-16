package com.yzc.vos.paints;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 名画作者viewModel
 * 
 * @author yzc
 *
 */
public class AuthorViewModel {
	// 作者id
	public String authorId;
	// 作者姓名
	@NotBlank(message = "{authorViewModel.authorName.notBlank.validmsg}")
	@Length(max = 100, message = "{authorViewModel.authorName.maxlength.validmsg}")
	public String authorName;
	// 出生日期
	@Length(max = 100, message = "{authorViewModel.birthdate.maxlength.validmsg}")
	public String birthdate;
	// 去世日期
	@Length(max = 100, message = "{authorViewModel.deathdate.maxlength.validmsg}")
	public String deathdate;
	// 国籍
	@Length(max = 500, message = "{authorViewModel.nationality.maxlength.validmsg}")
	public String nationality;
	// 流派
	@Length(max = 50, message = "{authorViewModel.genre.maxlength.validmsg}")
	public String genre;

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
