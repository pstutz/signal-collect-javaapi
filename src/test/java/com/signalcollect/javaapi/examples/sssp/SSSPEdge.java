package com.signalcollect.javaapi.examples.sssp;

import com.signalcollect.javaapi.*;

/**
 * Edge in a graph for a Single Source Shortest Path (SSSP) computation.
 * 
 * @author Daniel Strebel
 *
 */
@SuppressWarnings("serial")
public class SSSPEdge extends DefaultEdge<SSSPNode> {

	SSSPEdge(int sourceID, int targetID) {
		super(sourceID, targetID);
	}

	/**
	 * Builds new signals as the sum of the edges weight (which is equal to 1 in the
	 * default case) and the source vertex's distance to the source of the SSSP graph.
	 * The value of the signal is therefore equal to the shortest distance from the source to the
	 * target vertex of this edge by using a path over the source vertex.
	 */
	@Override
	public Object signal(SSSPNode sourceVertex) {
		if(sourceVertex.getState()<Integer.MAX_VALUE) { //To prevent integer overflow.
			return sourceVertex.getState() + (int)this.weight();
		}
		else {
			return sourceVertex.getState();
		}
	}
}
