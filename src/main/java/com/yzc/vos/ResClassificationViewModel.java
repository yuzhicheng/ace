package com.yzc.vos;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author yzc
 * @version 1.0
 * @date 2016年12月1日
 */
public class ResClassificationViewModel implements Comparable<ResClassificationViewModel>{

	/**
	 * 标识
	 */
	private String identifier;
	/**
	 * 分类路径
	 */
	private String taxonpath;
	/**
	 * 分类体系维度的编码
	 */
	private String taxonname;
	/**
	 * 分类体系维度数据的编码
	 */
	private String taxoncode;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String operation;

	public ResClassificationViewModel(){
		operation = null;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getTaxonpath() {
		return taxonpath;
	}

	public void setTaxonpath(String taxonpath) {
		this.taxonpath = taxonpath;
	}

	public String getTaxonname() {
		return taxonname;
	}

	public void setTaxonname(String taxonname) {
		this.taxonname = taxonname;
	}

	public String getTaxoncode() {
		return taxoncode;
	}

	public void setTaxoncode(String taxoncode) {
		this.taxoncode = taxoncode;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public int compareTo(ResClassificationViewModel o) {
		if(o != null && o.getTaxoncode() != null){
			return this.getTaxoncode().compareTo(o.getTaxoncode());
		}
		return 0;
	}
}
