package com.testproject.GraphProblem.service;

import java.util.List;

import com.testproject.GraphProblem.exception.GraphHandlingException;
import com.testproject.GraphProblem.model.GraphData;

/**
 * @author GOVIND
 *
 */
public interface GraphService {

	/**
	 * 
	 * @param id
	 * @return
	 * @throws GraphHandlingException
	 */
	int calculateGraphMarketValue(String id) throws GraphHandlingException;

	/**
	 * 
	 * @param id
	 * @param exclusionList
	 * @return
	 * @throws GraphHandlingException
	 */
	int calculateGraphMarketValue(String id, List<String> exclusionList) throws GraphHandlingException;

	/**
	 * 
	 * @param graphData
	 */
	void addHoldingsData(GraphData graphData);

	/**
	 * 
	 * @return
	 */
	List<GraphData> getAllHoldings();

	/**
	 * 
	 * @param graphData
	 * @throws GraphHandlingException
	 */
	void constructGraph(GraphData graphData) throws GraphHandlingException;

}
