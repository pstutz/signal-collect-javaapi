package com.signalcollect.javaapi.examples.sssp;

import com.signalcollect.javaapi.DataGraphVertex;
import java.lang.Iterable;

/**
 * Vertex in a graph for a Single Source Shortest Path (SSSP) computation.
 * 
 * @author Daniel Strebel
 *
 */
@SuppressWarnings("serial")
public class SSSPNode extends DataGraphVertex<Integer, Integer, Integer> {

	public SSSPNode(int id) {
		this(id, Integer.MAX_VALUE);
	}
	
	public SSSPNode(int id, int initialDistance) {
		super(id, initialDistance);
	}

	/**
	 * Computes the new state of the vertex as the minimum of all incoming signal and the vertex's current state.
	 * 
	 * @param mostRecentSignals stores all signals received per incoming edge. 
	 * 
	 * @return The new vertex state.
	 */
	@Override
	public Integer collect(Integer oldState, Iterable<Integer> mostRecentSignals) {
		int minDistance = oldState;
		for (int signal : mostRecentSignals) {
			if (signal < minDistance) {
				minDistance = signal;
			}
		}
		return minDistance;
	}

}
