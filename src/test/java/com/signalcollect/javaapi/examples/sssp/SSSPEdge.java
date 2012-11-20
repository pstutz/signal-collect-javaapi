package com.signalcollect.javaapi.examples.sssp;

import com.signalcollect.Vertex;

import com.signalcollect.DefaultEdge;

/**
 * Edge in a graph for a Single Source Shortest Path (SSSP) computation.
 * 
 * @author Daniel Strebel
 * 
 */
@SuppressWarnings("serial")
public class SSSPEdge extends DefaultEdge<Object> {

	SSSPEdge(Object targetId) {
		super(targetId);
	}

	/**
	 * Builds new signals as the sum of the edges weight (which is equal to 1 in
	 * the default case) and the source vertex's distance to the source of the
	 * SSSP graph. The value of the signal is therefore equal to the shortest
	 * distance from the source to the target vertex of this edge by using a
	 * path over the source vertex.
	 */
	@Override
	public Object signal(Vertex<?, ?> sourceVertex) {
		Integer distanceToSource = (Integer) sourceVertex.state();
		if (distanceToSource != Integer.MAX_VALUE) {
			return distanceToSource + (int) this.weight();
		} else {
			return distanceToSource;
		}
	}
}
