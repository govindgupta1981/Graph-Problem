package com.testproject.GraphProblem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.testproject.GraphProblem.constants.GraphConstants;
import com.testproject.GraphProblem.constants.MessageConfig;
import com.testproject.GraphProblem.dao.GraphRepository;
import com.testproject.GraphProblem.exception.GraphHandlingException;
import com.testproject.GraphProblem.model.GraphData;
import com.testproject.GraphProblem.model.HoldingData;
import com.testproject.GraphProblem.model.Node;
import com.testproject.GraphProblem.utility.Graph;

/**
 * @author GOVIND
 *
 */
@Service
public class GraphServiceImpl implements GraphService {

	private Logger logger = LoggerFactory.getLogger(GraphServiceImpl.class);

	@Autowired
	private GraphRepository graphRepository;

	/**
	 * Used to construct a Graph based on the JSON Data provided for Investors,
	 * Funds & Holdings
	 * 
	 */
	@Override
	public void constructGraph(GraphData graphData) throws GraphHandlingException {
		try {
			// Making static Graph Map reference to Empty
			Graph.map = new HashMap<>();
			// Get Node Data
			List<Node> nodes = graphData.getNodes();
			// Null & Empty Check for Nodes
			if (nodes != null && !nodes.isEmpty()) {
				// Loop over Nodes
				for (Node node : nodes) {
					// Null Check for Node
					if (node != null) {
						// Validate the Node Data
						validateNodeData(node);
						// Add Node & Edge to Graph
						Graph.addEdge(node.getParentNode(), node.getChildNode(), node.getLevel());
					}
				}
			}
			// Throwing Exception in case of missing Node Data
			else
				throw new GraphHandlingException(MessageConfig.GRAPH_DATA_NOT_FOUND);
		} catch (Exception e) {
			throw new GraphHandlingException(e.getMessage());
		}
	}

	/**
	 * Used to validate Node Data
	 * 
	 * @param node
	 * @throws GraphHandlingException
	 */
	private void validateNodeData(Node node) throws GraphHandlingException {
		// If Parent Node or Child Node is Null or Empty
		if (StringUtils.isEmpty(node.getParentNode()) || StringUtils.isEmpty(node.getChildNode()))
			throw new GraphHandlingException(MessageConfig.NODE_DATA_NOT_FOUND);
		// If Node Level is not Correct
		if (node.getLevel() < 1 || node.getLevel() > 2)
			throw new GraphHandlingException(MessageConfig.NODE_LEVEL_INVALID);
	}

	/**
	 * Used to calculate market value for the created Graph on the basis of Investor
	 * Id or Fund Id
	 * 
	 */
	@Override
	public int calculateGraphMarketValue(String id) throws GraphHandlingException {
		try {
			// If Investor Id or Fund Id is Null or Empty
			if (StringUtils.isEmpty(id))
				throw new GraphHandlingException(MessageConfig.INVESTOR_FUND_ID_REQUIRED);
			// Fetch Created Graph
			final Map<String, List<String>> graphMap = Graph.map;
			// If Graph not Exists
			if (graphMap == null || graphMap.isEmpty())
				throw new GraphHandlingException(MessageConfig.GRAPH_NOT_CREATED);
			// HoldingsMap
			Map<String, Integer> holdingsMap = new HashMap<>();
			// Fetch Holdings Data from Database
			List<GraphData> holdingData = new ArrayList<>();
			graphRepository.findAll().forEach(holdingData::add);
			// Loop over Holdings Data
			for (GraphData data : holdingData) {
				// Null Check over data
				if (data != null) {
					// Get Holdings Data List
					List<HoldingData> holdingDataList = data.getHoldings();
					// Null & Empty check over Holdings Data List
					if (holdingDataList != null && !holdingDataList.isEmpty()) {
						// Loop over Holdings Data List
						for (HoldingData data1 : holdingDataList) {
							// Putting Holdings Data into Holdings Map
							holdingsMap.put(data1.getHoldingId(), data1.getHoldingValue());
						}
					}
				}
			}
			// If Holdings Data don't Exists
			if (holdingsMap == null || holdingsMap.isEmpty())
				throw new GraphHandlingException(MessageConfig.HOLDINGS_NOT_CREATED);
			// Compute Market Value
			return findMarketValue(id, holdingsMap, graphMap, null);
		} catch (Exception e) {
			throw new GraphHandlingException(e.getMessage());
		}
	}

	/**
	 * Sub-Function which is used to calculate Market Value
	 * 
	 * @param id
	 * @param holdingsMap
	 * @param map
	 * @return
	 */
	private int findMarketValue(String id, Map<String, Integer> holdingsMap, Map<String, List<String>> graphMap,
			List<String> exclusionList) throws GraphHandlingException {
		int marketValue = 0;
		// Get Funds or Holdings List Based on the Id from GraphMap
		List<String> funds_Holdings_List = graphMap.get(GraphConstants.GRAPH_LEVEL_1 + "-" + id);
		if (funds_Holdings_List == null || funds_Holdings_List.isEmpty())
			funds_Holdings_List = graphMap.get(GraphConstants.GRAPH_LEVEL_2 + "-" + id);
		// Null & Empty Check for Funds Holdings List
		if (funds_Holdings_List != null && !funds_Holdings_List.isEmpty()) {
			// If Holdings to be Excluded then exclude them from the List
			if (exclusionList != null && !exclusionList.isEmpty())
				funds_Holdings_List.removeAll(exclusionList);
			// Loop over Funds Holdings List
			for (String funds_holdings : funds_Holdings_List) {
				// Get Funds or Holdings List Based on the Id from GraphMap
				List<String> funds_Holdings_List_New = graphMap
						.get(GraphConstants.GRAPH_LEVEL_2 + "-" + funds_holdings);
				if (funds_Holdings_List_New == null || funds_Holdings_List_New.isEmpty())
					funds_Holdings_List_New = graphMap.get(GraphConstants.GRAPH_LEVEL_3 + "-" + funds_holdings);
				// Null & Empty Check for Funds Holdings List
				if (funds_Holdings_List_New != null && !funds_Holdings_List_New.isEmpty()) {
					// If Holdings to be Excluded then exclude them from the List
					if (exclusionList != null && !exclusionList.isEmpty())
						funds_Holdings_List_New.removeAll(exclusionList);
					// Loop over Funds Holdings List
					for (String funds_holdings_val : funds_Holdings_List_New) {
						if (!holdingsMap.containsKey(funds_holdings_val))
							throw new GraphHandlingException(
									String.format(MessageConfig.HOLDING_VALUE_NOT_FOUND, funds_holdings_val));
						// Formulae for Market Value Calculation
						marketValue = marketValue
								+ (funds_Holdings_List_New.size() * holdingsMap.get(funds_holdings_val));
					}
				} else {
					if (!holdingsMap.containsKey(funds_holdings))
						throw new GraphHandlingException(
								String.format(MessageConfig.HOLDING_VALUE_NOT_FOUND, funds_holdings));
					// Formulae for Market Value Calculation
					marketValue = marketValue + (funds_Holdings_List.size() * holdingsMap.get(funds_holdings));
				}
			}
		} else {
			throw new GraphHandlingException(MessageConfig.INVESTOR_FUND_ID_NOT_FOUND);
		}
		logger.debug("Calculated Market Value for Id : " + id + " is :: ", marketValue);
		// Return
		return marketValue;
	}

	/**
	 * Used to calculate market value for the created Graph on the basis of Investor
	 * Id or Fund Id with the provided Holdings Exclusion
	 * 
	 */
	@Override
	public int calculateGraphMarketValue(String id, List<String> exclusionList) throws GraphHandlingException {
		try {
			// If Investor Id or Fund Id is Null or Empty
			if (StringUtils.isEmpty(id))
				throw new GraphHandlingException(MessageConfig.INVESTOR_FUND_ID_REQUIRED);
			// Fetch Created Graph
			final Map<String, List<String>> graphMap = Graph.map;
			// If Graph not Exists
			if (graphMap == null || graphMap.isEmpty())
				throw new GraphHandlingException(MessageConfig.GRAPH_NOT_CREATED);
			// HoldingsMap
			Map<String, Integer> holdingsMap = new HashMap<>();
			// Fetch Holdings Data from Database
			List<GraphData> holdingData = new ArrayList<>();
			graphRepository.findAll().forEach(holdingData::add);
			// Loop over Holdings Data
			for (GraphData data : holdingData) {
				// Null Check over data
				if (data != null) {
					// Get Holdings Data List
					List<HoldingData> holdingDataList = data.getHoldings();
					// Null & Empty check over Holdings Data List
					if (holdingDataList != null && !holdingDataList.isEmpty()) {
						// Loop over Holdings Data List
						for (HoldingData data1 : holdingDataList) {
							// Putting Holdings Data into Holdings Map
							holdingsMap.put(data1.getHoldingId(), data1.getHoldingValue());
						}
					}
				}
			}
			// If Holdings Data don't Exists
			if (holdingsMap == null || holdingsMap.isEmpty())
				throw new GraphHandlingException(MessageConfig.HOLDINGS_NOT_CREATED);
			// Compute Market Value
			return findMarketValue(id, holdingsMap, graphMap, exclusionList);
		} catch (Exception e) {
			throw new GraphHandlingException(e.getMessage());
		}
	}

	/**
	 * Used to Create/Save Holdings Value into In-memory Database(Apache Derby)
	 * 
	 */
	@Override
	public void addHoldingsData(GraphData graphData) throws GraphHandlingException {
		try {
			// Null Check for Graph Data
			if (graphData != null) {
				// Validate Holdings Data
				validateHoldingData(graphData);
				// Saving Holdings Data
				graphRepository.save(graphData);
			} else
				throw new GraphHandlingException(MessageConfig.HOLDINGS_DATA_NOT_FOUND);
		} catch (Exception e) {
			throw new GraphHandlingException(e.getMessage());
		}
	}

	/**
	 * Used to Validate Holdings Data
	 * 
	 * @param graphData
	 */
	private void validateHoldingData(GraphData graphData) {
		// Get Holdings Data List from GraphData
		List<HoldingData> holdingData = graphData.getHoldings();
		// If Holding Data List is Null or Empty
		if (holdingData == null || holdingData.isEmpty())
			throw new GraphHandlingException(MessageConfig.HOLDINGS_DATA_NOT_FOUND);
		// Loop over Holdings Data List
		for (HoldingData data : holdingData) {
			// If Holding Id or Holding Value is Null or Empty
			if (StringUtils.isEmpty(data.getHoldingId()) || data.getHoldingValue() == null)
				throw new GraphHandlingException(MessageConfig.HOLDING_ID_VALUE_NOT_FOUND);
		}
	}

	/**
	 * Used to fetch all Holdings Value stored into the Database
	 * 
	 */
	@Override
	public List<GraphData> getAllHoldings() {
		List<GraphData> holdingData = new ArrayList<>();
		// Fetch Holdings Data from Database
		graphRepository.findAll().forEach(holdingData::add);
		// Return
		return holdingData;
	}
}
