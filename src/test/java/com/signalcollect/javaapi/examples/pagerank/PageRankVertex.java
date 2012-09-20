package com.signalcollect.javaapi.examples.pagerank;

import com.signalcollect.javaapi.*;

/**
 * Represents a page in a PageRank compute graph
 * @see <a href="http://en.wikipedia.org/wiki/PageRank">PageRank algorithm</a>
 *
 * @author Philip Stutz
 */
@SuppressWarnings("serial")
public class PageRankVertex extends DataGraphVertex<Integer, Double, Double> {

	Double baseRank;
	Double dampingFactor;

	/**
	 * Constructs a new page in a PageRank compute graph
	 * 
	 * @param vertexId a unique identifier for a page
	 * @param baseRank is the minimum PageRank of a page and serves as starting point of the computation.
	 */
	public PageRankVertex(Integer vertexId, Double baseRank) {
		super(vertexId, baseRank);
		this.baseRank = baseRank;
		this.dampingFactor = 1 - baseRank;
	}

	/**
	 * Computes the PageRank of this page as the sum of the most recent signals
	 * combined with the predefined base rank and the dumping factor.
	 * 
	 * @param oldState the page's previous PageRank
	 * @param mostRecentSignals all signals from pages that link to this page.
	 * @return the new PageRank of this page.
	 */
	public Double collect(Double oldState, Iterable<Double> mostRecentSignals) {
		Double rankSum = 0.0;
		for (Double signal : mostRecentSignals) {
			rankSum += signal;
		}
		return baseRank + dampingFactor * rankSum;
	}
}