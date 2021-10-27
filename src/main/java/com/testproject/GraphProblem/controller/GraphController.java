package com.testproject.GraphProblem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.testproject.GraphProblem.constants.GraphConstants;
import com.testproject.GraphProblem.constants.MessageConfig;
import com.testproject.GraphProblem.exception.GraphHandlingException;
import com.testproject.GraphProblem.model.GraphData;
import com.testproject.GraphProblem.model.GraphResponse;
import com.testproject.GraphProblem.service.GraphService;
import com.testproject.GraphProblem.utility.Graph;

/**
 * @author GOVIND
 *
 */
@RestController
@RequestMapping("/graph")
public class GraphController {

	private Logger logger = LoggerFactory.getLogger(GraphController.class);

	@Autowired
	GraphService graphService;

	/**
	 * Used to construct a Graph based on the JSON Data provided for Investors,
	 * Funds & Holdings
	 * 
	 * @param graphData
	 * @return
	 */
	@PostMapping
	public ResponseEntity<GraphResponse> constructGraph(@RequestBody GraphData graphData) {
		final GraphResponse graphResponse = new GraphResponse();
		try {
			// Service Layer Call for Creating the Graph
			graphService.constructGraph(graphData);
			Graph graph = new Graph();
			logger.info("Created Graph :: \n" + graph);
			graphResponse.setStatus(GraphConstants.SUCCESS);
			graphResponse.setMessage(MessageConfig.GRAPH_CREATED);
			graphResponse.setGraph(graph);
		} catch (GraphHandlingException ge) {
			logger.error("Error while creating Graph : ", ge);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(ge.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Error while creating Graph : ", e);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(e.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.CREATED);
	}

	/**
	 * Used to calculate market value for the created Graph on the basis of Investor
	 * Id or Fund Id
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/marketvalue/{id}")
	public ResponseEntity<GraphResponse> getMarketValue(@PathVariable("id") String id) {
		GraphResponse graphResponse = new GraphResponse();
		try {
			// Service Layer Call for Calculating Market Value
			int totalMarketValue = graphService.calculateGraphMarketValue(id);
			graphResponse.setStatus(GraphConstants.SUCCESS);
			graphResponse.setMessage(String.format(MessageConfig.GRAPH_MARKET_VALUE, totalMarketValue));
		} catch (GraphHandlingException ge) {
			logger.error("Error while calculating Market Value : ", ge);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(ge.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Error while calculating Market Value : ", e);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(e.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.OK);
	}

	/**
	 * Used to calculate market value for the created Graph on the basis of Investor
	 * Id or Fund Id with the provided Holdings Exclusion
	 * 
	 * @param id
	 * @param graphData
	 * @return
	 */
	@RequestMapping(value = "/marketvalue/{id}", method = RequestMethod.POST)
	public ResponseEntity<GraphResponse> getMarketValue(@PathVariable("id") String id,
			@RequestBody GraphData graphData) {
		GraphResponse graphResponse = new GraphResponse();
		try {
			// Service Layer Call for Calculating Market Value including Exclusion for
			// Holdings
			int totalMarketValue = graphService.calculateGraphMarketValue(id, graphData.getExclusionHoldings());
			graphResponse.setStatus(GraphConstants.SUCCESS);
			graphResponse.setMessage(String.format(MessageConfig.GRAPH_MARKET_VALUE, totalMarketValue));
		} catch (GraphHandlingException ge) {
			logger.error("Error while calculating Market Value with Exclusions : ", ge);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(ge.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Error while calculating Market Value with Exclusions : ", e);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(e.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.OK);
	}

	/**
	 * Used to Create/Save Holdings Value into In-memory Database(Apache Derby)
	 * 
	 * @param graphData
	 */
	@RequestMapping(value = "/holdings", method = RequestMethod.POST)
	public ResponseEntity<GraphResponse> addHoldingsData(@RequestBody GraphData graphData) {
		GraphResponse graphResponse = new GraphResponse();
		try {
			// Service Layer Call for Creating Holding Value
			graphService.addHoldingsData(graphData);
			graphResponse.setStatus(GraphConstants.SUCCESS);
			graphResponse.setMessage(MessageConfig.HOLDINGS_CREATED);
		} catch (GraphHandlingException ge) {
			logger.error("Error while adding holdings data : ", ge);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(ge.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Error while adding holdings data : ", e);
			graphResponse.setStatus(GraphConstants.FAILURE);
			graphResponse.setMessage(e.getMessage());
			return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GraphResponse>(graphResponse, HttpStatus.CREATED);
	}

	/**
	 * Used to fetch all Holdings Value stored into the Database
	 * 
	 * @return
	 */
	@GetMapping("/holdings")
	public List<GraphData> getAllHoldings() {
		return graphService.getAllHoldings();
	}
}
