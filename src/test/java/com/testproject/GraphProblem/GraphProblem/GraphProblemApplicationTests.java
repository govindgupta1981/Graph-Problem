package com.testproject.GraphProblem.GraphProblem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.testproject.GraphProblem.model.GraphData;
import com.testproject.GraphProblem.model.GraphResponse;
import com.testproject.GraphProblem.model.Node;
import com.testproject.GraphProblem.service.GraphServiceImpl;

/**
 * @author GOVIND
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GraphProblemApplicationTests {

	@InjectMocks
	GraphServiceImpl graphService;

	@Autowired
	private TestRestTemplate restTemplate;

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void constructGraph() throws Exception {
		final String baseUrl = "http://localhost:" + 8083 + "/graph/";
		URI uri = new URI(baseUrl);
		List<Node> nodeList = new ArrayList<>();
		for (int i = 1; i <= 2; i++) {
			Node node = new Node();
			node.setParentNode("Inv" + i);
			node.setChildNode("F" + i);
			node.setLevel(1);
			nodeList.add(node);
			node = new Node();
			node.setParentNode("F" + i);
			node.setChildNode("H" + i);
			node.setLevel(2);
			nodeList.add(node);
		}
		GraphData graphData = new GraphData();
		graphData.setNodes(nodeList);
		ResponseEntity<GraphResponse> result = restTemplate.postForEntity(uri, graphData, GraphResponse.class);
		// Verify request succeed
		assertEquals(201, result.getStatusCodeValue());
		System.out.println(result.getBody().getMessage());
		System.out.println(result.getBody().getGraph());
	}

	//@Test
	public void getMarketValue() throws Exception {
		GraphResponse response = restTemplate
				.getForObject(new URI("http://localhost:" + 8083 + "/graph/marketvalue/F1"), GraphResponse.class);
		// Verify request succeed
		assertEquals(200, response.getStatus());
		System.out.println(response.getMessage());
	}

	//@Test
	public void getMarketValueWithExclusions() throws Exception {
		final String baseUrl = "http://localhost:" + 8083 + "/graph/marketvalue/F1";
		URI uri = new URI(baseUrl);
		GraphData graphData = new GraphData();
		graphData.setExclusionHoldings(Arrays.asList("H1"));
		ResponseEntity<GraphResponse> result = restTemplate.postForEntity(uri, graphData, GraphResponse.class);
		// Verify request succeed
		assertEquals(200, result.getStatusCodeValue());
		System.out.println(result.getBody().getMessage());
	}
}
