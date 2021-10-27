package com.testproject.GraphProblem.model;

/**
 * @author GOVIND
 *
 */
public class Node {

	private String parentNode;
	private String childNode;
	private Integer level;

	/**
	 * @return the parentNode
	 */
	public String getParentNode() {
		return parentNode;
	}

	/**
	 * @param parentNode the parentNode to set
	 */
	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * @return the childNode
	 */
	public String getChildNode() {
		return childNode;
	}

	/**
	 * @param childNode the childNode to set
	 */
	public void setChildNode(String childNode) {
		this.childNode = childNode;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
}
