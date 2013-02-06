/*
 *  @author Philip Stutz
 *  
 *  Copyright 2013 University of Zurich
 *      
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *         http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  
 */

package com.signalcollect.javaapi;

import java.util.HashMap;

import com.signalcollect.GraphEditor;
import com.signalcollect.Vertex;
import com.signalcollect.Edge;

@SuppressWarnings("serial")
abstract class AbstractVertex extends VertexSpecializationBridge {

	/**
	 * hashCode is cached for better performance
	 */
	Integer hashCode;

	@SuppressWarnings("rawtypes")
	@Override
	public void afterInitialization(GraphEditor graphEditor) {
		hashCode = id().hashCode();
	}

	/**
	 * Access to the outgoing edges is required for some calculations and for
	 * executing the signal operations. It is a map so we can support fast edge
	 * removals.
	 * 
	 * Currently a Java HashMap is used as the implementation, but we will
	 * replace it with a more specialized implementation in a future release.
	 */
	public HashMap<Object, Edge<Object>> outgoingEdges = new HashMap<Object, Edge<Object>>(
			0);

	/** The state of this vertex when it last signaled. */
	public Object lastSignalState = null;

	/** Keeps track if edges get modified so we know we should signal again */
	public boolean edgesModifiedSinceSignalOperation = false;

	/** Keeps track if edges get modified so we know we should collect again */
	public boolean edgesModifiedSinceCollectOperation = false;

	/**
	 * Adds a new outgoing `Edge`
	 * 
	 * @param e
	 *            the edge to be added.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean addEdge(Edge edge, GraphEditor graphEditor) {
		Edge<Object> e = outgoingEdges.get(edge.targetId());
		if (e == null) {
			// Edge not present yet.
			edgesModifiedSinceSignalOperation = true;
			edgesModifiedSinceCollectOperation = true;
			outgoingEdges.put(edge.targetId(), edge);
			edge.onAttach(this, graphEditor);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes an outgoing {@link Edge} from this {@link FrameworkVertex}.
	 * 
	 * @param e
	 *            the edge to be added.
	 */
	@SuppressWarnings("rawtypes")
	public boolean removeEdge(Object targetId, GraphEditor graphEditor) {
		Object outgoingEdge = outgoingEdges.get(targetId);
		if (outgoingEdge == null) {
			return false;
		} else {
			edgesModifiedSinceSignalOperation = true;
			edgesModifiedSinceCollectOperation = true;
			outgoingEdges.remove(targetId);
			return true;
		}
	}

	/**
	 * Removes all outgoing {@link Edge}s from this {@link Vertex}.
	 * 
	 * @return returns the number of {@link Edge}s that were removed.
	 */
	@SuppressWarnings("rawtypes")
	public int removeAllEdges(GraphEditor graphEditor) {
		int edgesRemoved = outgoingEdges.size();
		for (Object outgoingEdge : outgoingEdges.keySet()) {
			removeEdge(outgoingEdge, graphEditor);
		}
		return edgesRemoved;
	}

	/**
	 * This method tells this Vertex to execute the signal operation on all its
	 * outgoing edges. This method is going to be called by the Signal/Collect
	 * framework during its execution (i.e. the Worker implementation.
	 * 
	 * @see Worker
	 * @see Edge#executeSignalOperation
	 */
	@SuppressWarnings("rawtypes")
	public void executeSignalOperation(GraphEditor graphEditor) {
		edgesModifiedSinceSignalOperation = false;
		lastSignalState = state();
		doSignal(graphEditor);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doSignal(GraphEditor graphEditor) {
		for (Edge<Object> e : outgoingEdges.values()) {
			e.executeSignalOperation(this, graphEditor);
		}
	}

	/**
	 * Function that gets called by the framework whenever this vertex is
	 * supposed to collect new signals.
	 * 
	 * @param graphEditor
	 *            an instance of GraphEditor which can be used by this vertex to
	 *            interact with the graph.
	 */
	@SuppressWarnings("rawtypes")
	public void executeCollectOperation(GraphEditor graphEditor) {
		edgesModifiedSinceCollectOperation = false;
	}

	/**
	 * This method is used by the framework in order to decide if the vertex'
	 * signal operation should be executed. The higher the returned value the
	 * more likely the vertex will be scheduled for executing its signal method.
	 * 
	 * @return the score value. The meaning of this value depends on the
	 *         thresholds set in {@link ComputeGraph#execute}.
	 */
	public double scoreSignal() {
		if (edgesModifiedSinceSignalOperation) {
			return 1.0;
		} else {
			if (lastSignalState != state()) {
				return 1.0;
			} else {
				return 0.0;
			}
		}
	}

	/**
	 * Returns the number of outgoing edges of this
	 * [com.signalcollect.interfaces.Vertex]
	 */
	public int edgeCount = outgoingEdges.size();

	/**
	 * Returns "VertexClassName(id=ID, state=STATE)"
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(id=" + id() + ", state="
				+ state() + ")";
	}

	/**
	 * This method gets called by the framework before the vertex gets removed.
	 */
	@SuppressWarnings("rawtypes")
	public void beforeRemoval(GraphEditor graphEditor) {
	}

}