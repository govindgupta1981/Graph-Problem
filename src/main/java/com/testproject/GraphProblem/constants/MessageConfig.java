package com.testproject.GraphProblem.constants;

/**
 * @author GOVIND
 *
 */
public class MessageConfig {

	public static final String GRAPH_CREATED = "Graph Created Successfully";
	public static final String GRAPH_MARKET_VALUE = "Calculated Market Value for the Graph : %d";
	public static final String GRAPH_DATA_NOT_FOUND = "Graph Data not Found for generating a graph";
	public static final String NODE_DATA_NOT_FOUND = "Parent or Child Node required for generating a graph";
	public static final String NODE_LEVEL_INVALID = "Invalid Level value for generating a graph(Valid Level - 1 or 2)";
	public static final String INVESTOR_FUND_ID_REQUIRED = "Either Investor or Fund Id is required for calculating market value";
	public static final String GRAPH_NOT_CREATED = "Graph not created yet, please create graph first using 'graph' endpoint then calculate market value";
	public static final String HOLDINGS_NOT_CREATED = "Holdings value not found in the database for calculating market value, create holding value first using 'holdings' endpoint";
	public static final String HOLDING_VALUE_NOT_FOUND = "Holding value for : %s not found in the database for calculating market value, create holding value first using 'holdings' endpoint";
	public static final String INVESTOR_FUND_ID_NOT_FOUND = "Either Investor or Fund Id is incorrect";
	public static final String HOLDINGS_CREATED = "Holdings Created Successfully";
	public static final String HOLDINGS_DATA_NOT_FOUND = "Holdings Data not Found for creating holdings";
	public static final String HOLDING_ID_VALUE_NOT_FOUND = "Both Holding ID & Holding Value are required for creating holdings";
}
