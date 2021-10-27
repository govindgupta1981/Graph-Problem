package com.testproject.GraphProblem.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.testproject.GraphProblem.model.GraphData;

/**
 * @author GOVIND
 *
 */
@Repository
public interface GraphRepository extends CrudRepository<GraphData, String> {
}
