package com.yzc.utils.email;

import java.util.Date;
import java.util.List;

public class EmailInfo {

	// 邮件发送者的地址
	private String fromAddress;

	// 邮件接收者的地址
	private List<String> toAddress;

	// 登陆邮件发送服务器的用户名
	private String userName;

	// 登陆邮件发送服务器的密码
	private String password;

	// 是否需要身份验证
	private boolean validate = false;

	// 邮件主题
	private String subject;

	// 邮件的文本内容
	private String content;

	// 邮件的发送时间
	private Date date;

	// 邮件附件的文件名
	private List<String> attachFileNames;

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public List<String> getToAddress() {
		return toAddress;
	}

	public void setToAddress(List<String> toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(List<String> attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

}
