package com.testproject.GraphProblem.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author GOVIND
 *
 */
@Entity
public class GraphData {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@ElementCollection
	private List<HoldingData> holdings;
	@Transient
	private List<String> exclusionHoldings;
	@Transient
	private List<Node> nodes;

	public GraphData() {
	}

	/**
	 * @return the exclusionHoldings
	 */
	public List<String> getExclusionHoldings() {
		return exclusionHoldings;
	}

	/**
	 * @param exclusionHoldings the exclusionHoldings to set
	 */
	public void setExclusionHoldings(List<String> exclusionHoldings) {
		this.exclusionHoldings = exclusionHoldings;
	}

	/**
	 * @return the holdings
	 */
	public List<HoldingData> getHoldings() {
		return holdings;
	}

	/**
	 * @param holdings the holdings to set
	 */
	public void setHoldings(List<HoldingData> holdings) {
		this.holdings = holdings;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}
}
