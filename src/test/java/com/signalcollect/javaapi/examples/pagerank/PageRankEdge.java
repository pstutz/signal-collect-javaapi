package com.signalcollect.javaapi.examples.pagerank;

import com.signalcollect.Vertex;

import com.signalcollect.DefaultEdge;

/**
 * Represents an edge in a PageRank compute graph
 * 
 * @author Philip Stutz
 *
 */
@SuppressWarnings("serial")
public class PageRankEdge extends DefaultEdge<Object> {

	public PageRankEdge(Object targetId) {
		super(targetId);
	}
	
	/**
	 * Constructs the signal as the weighted vertex's state divided by the total weight of outgoing edges.
	 * When no weight is specified the signal is simply the vertex's state divided by the number of outgoing edges.
	 */
	public Object signal() {
		return ((Double) source().state()) * weight() / ((PageRankVertex) source()).sumOfOutWeights;
	}

}