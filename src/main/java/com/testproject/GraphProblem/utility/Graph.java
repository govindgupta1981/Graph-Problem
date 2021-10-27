package com.testproject.GraphProblem.utility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author GOVIND
 *
 */
public class Graph {

	// HashMap to store the edges in the graph
	public static Map<String, List<String>> map = new HashMap<>();

	// Default Constructor
	public Graph() {
	}

	// This function adds a new vertex to the graph
	private static void addVertex(String node, Integer level) {
		map.put(level + "-" + node, new LinkedList<String>());
	}

	// This function adds the edge between source to destination
	public static void addEdge(String source, String destination, Integer level) {
		int childLevel = level + 1;
		if (!map.containsKey(level + "-" + source))
			addVertex(source, level);
		if (!map.containsKey(childLevel + "-" + destination))
			addVertex(destination, childLevel);
		map.get(level + "-" + source).add(destination);
	}

	// Prints the adjancency list of each vertex.
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String v : map.keySet()) {
			builder.append(v.toString() + ": ");
			for (String w : map.get(v)) {
				builder.append(w.toString() + " ");
			}
			builder.append("\n");
		}
		return (builder.toString());
	}

	/**
	 * @return the map
	 */
	public Map<String, List<String>> getMap() {
		return map;
	}
}
