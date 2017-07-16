package com.yzc.utils.tree;

import java.util.List;

/**
 * @title 树节点
 * @author yzc
 * @version 1.0
 * @create 2016年12月22日
 */
public class TreeNode {
    //节点id
    protected String nodeId;
    //节点名
    protected String nodeName;
    //父节点id
    protected String parentId;
    //子节点集合
    private List<TreeNode> children;
    //深度
    private Integer level;
   //是否为叶子节点
    private Boolean isLeaf;
    
    public String getNodeId() {
        return nodeId;
    }
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public List<TreeNode> getChildren() {
        return children;
    }
    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    public Boolean getIsLeaf() {
        return isLeaf;
    }
    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }
    
    @Override
    public String toString() {
        return "TreeNode [nodeId=" + nodeId + ", nodeName=" + nodeName + "]";
    }
}
