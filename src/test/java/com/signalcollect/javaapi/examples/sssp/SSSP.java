package com.signalcollect.javaapi.examples.sssp;

import com.signalcollect.ExecutionInformation;
import com.signalcollect.Graph;
import com.signalcollect.Vertex;
import com.signalcollect.javaapi.*;

/**
 * Single Source Shortest Path (SSSP) graph construction and
 * asynchronous execution.
 * 
 * @author Daniel Strebel
 *
 */
public class SSSP {

	public static void main(String[] args) {
		Graph graph = new GraphBuilder().build();
		
		graph.addVertex(new SSSPNode(1, 0));
		graph.addVertex(new SSSPNode(2));
		graph.addVertex(new SSSPNode(3));
		graph.addVertex(new SSSPNode(4));
		graph.addVertex(new SSSPNode(5));
		graph.addVertex(new SSSPNode(6));
		
		graph.addEdge(new SSSPEdge(1, 2));
		graph.addEdge(new SSSPEdge(2, 3));
		graph.addEdge(new SSSPEdge(3, 4));
		graph.addEdge(new SSSPEdge(1, 5));
		graph.addEdge(new SSSPEdge(4, 6));
		graph.addEdge(new SSSPEdge(5, 6));
		
		ExecutionInformation stats = graph.execute();
		System.out.println(stats);
		
		// Print the state of every vertex in the graph.
		graph.foreachVertex(new VertexCommand(){
			public void f(Vertex v) {
				System.out.println(v);
			}
		});
		graph.shutdown();
		
	}

}
