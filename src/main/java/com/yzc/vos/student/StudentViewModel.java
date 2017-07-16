package com.yzc.vos.student;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class StudentViewModel {

	private String identifier;

	@NotBlank(message = "{StudentViewModel.username.notBlank.validmsg}")
	@Length(max = 50, message = "{StudentViewModel.username.maxlength.validmsg}")
	private String username;

	@NotBlank(message = "{StudentViewModel.password.notBlank.validmsg}")
	@Length(min = 6, message = "{StudentViewModel.password.minlength.validmsg}")
	private String password;

	private String name;

	private Integer sex;

	private String school;

	private String grade;

	private String classes;

	private String title;

	@Length(max = 100, message = "{StudentViewModel.description.maxlength.validmsg}")
	private String description;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
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

}
