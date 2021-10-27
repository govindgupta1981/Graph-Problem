package com.testproject.GraphProblem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.testproject.GraphProblem.model.GraphData;

/**
 * @author GOVIND
 *
 */
@Repository
public interface GraphRepository extends JpaRepository<GraphData, String> {
}
