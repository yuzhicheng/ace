package com.yzc.models;

/**
 * @author yzc
 * @version 1.0
 * @date 2016年12月1日
 */
public class ResCoverageModel {

	/**
	 * 标识
	 */
	private String identifier;
	/**
	 * Org，Role，User，Time，Space，Group；必填值，举例：“Org”
	 * 描述资源的覆盖范围的类型，主要是机构，组织，角色，用户，空间描述和时间上的描述
	 */
	private String targetType;
	/**
	 * 资源的唯一标识
	 */
	private String resource;
	/**
	 * 目标类型确定后，目标的内容需要进行填写，Org，Role，User，Group需要记录Id。空间和时间上的描述主要是字符串描述
	 */
	private String target;
	/**
	 * 目标对象的中文标识或者描述信息
	 */
	private String targetTitle;
	/**
	 * VIEW，PLAY，SHAREING，REPORTING,COPY，NONE；必填值，举例：“SHAREING” 策略信息
	 */
	private String strategy;
	/**
	 * 资源类型
	 */
	private String resType;

	public ResCoverageModel() {

	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
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

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}


}
