package com.testproject.GraphProblem.model;

import javax.persistence.Embeddable;

/**
 * @author GOVIND
 *
 */
@Embeddable
public class HoldingData {

	private String holdingId;
	private Integer holdingValue;

	/**
	 * @return the holdingId
	 */
	public String getHoldingId() {
		return holdingId;
	}

	/**
	 * @param holdingId the holdingId to set
	 */
	public void setHoldingId(String holdingId) {
		this.holdingId = holdingId;
	}

	/**
	 * @return the holdingValue
	 */
	public Integer getHoldingValue() {
		return holdingValue;
	}

	/**
	 * @param holdingValue the holdingValue to set
	 */
	public void setHoldingValue(Integer holdingValue) {
		this.holdingValue = holdingValue;
	}
}
