package com.signalcollect.javaapi.examples.pagerank;

import com.signalcollect.ExecutionInformation;
import com.signalcollect.Vertex;
import com.signalcollect.javaapi.*;

/**
 * Test driver for a PageRank computation
 * 
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
		Graph graph = new GraphBuilder().build();
		graph.addVertex(new PageRankVertex(1, 0.15));
		graph.addVertex(new PageRankVertex(2, 0.15));
		graph.addVertex(new PageRankVertex(3, 0.15));
		graph.addEdge(1, new PageRankEdge(2));
		graph.addEdge(2, new PageRankEdge(1));
		graph.addEdge(2, new PageRankEdge(3));
		graph.addEdge(3, new PageRankEdge(2));
		ExecutionInformation stats = graph.execute();
		System.out.println(stats);
		// Print the state of every vertex in the graph.
		graph.foreachVertex(new VertexCommand() {
			public void f(Vertex<?, ?> v) {
				System.out.println(v);
			}
		});
		graph.shutdown();
	}
}
