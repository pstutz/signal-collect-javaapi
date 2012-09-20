package com.signalcollect.javaapi.examples.pagerank;

import com.signalcollect.ExecutionInformation;
import com.signalcollect.Graph;
import com.signalcollect.javaapi.GraphBuilder;

/**
 * Test driver for a PageRank computation
 * @see <a href="http://en.wikipedia.org/wiki/PageRank">PageRank algorithm</a>
 * 
 * @author Philip Stutz
 */
public class PageRank {

	public static void main(String[] args) {
		PageRank pr = new PageRank();
		pr.executePageRank();
	}

	/**
	 * Executes an example PageRank computation on a small synthetic graph with
	 * default execution parameters.
	 * 
	 * the graph has the following structure:
	 *  
	 * (1) <=> (2) <=> (3)
	 */
	public void executePageRank() {
		Graph cg = new GraphBuilder().build();
		cg.addVertex(new PageRankVertex(1, 0.15));
		cg.addVertex(new PageRankVertex(2, 0.15));
		cg.addVertex(new PageRankVertex(3, 0.15));
		cg.addEdge(new PageRankEdge(1, 2));
		cg.addEdge(new PageRankEdge(2, 1));
		cg.addEdge(new PageRankEdge(2, 3));
		cg.addEdge(new PageRankEdge(3, 2));
		ExecutionInformation stats = cg.execute();
		System.out.println(stats);
		cg.shutdown();
	}
}
