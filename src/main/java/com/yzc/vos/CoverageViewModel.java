package com.yzc.vos;
/**
 * 公私有资源库viewModel
 * @author yzc
 * @version 0.1
 * @date 2016年12月1日
 */
public class CoverageViewModel {
    /**
     * 范围覆盖的id
     */
    private String identifier;
    /**
     * 覆盖范围的类型，Org代表组织机构
     */
    private String targetType;
    /**
     * 覆盖目标的标识
     */
    private String target;
    /**
     * 资源操作类型，分享，分享可拷贝，上报
     */
    private String strategy;
    /**
     * 目标描述
     */
    private String targetTitle;
    /**
     * 资源的唯一标识
     */
    private String resource;
    /**
     * 资源类型
     */
    private String resType;
    
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
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
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

