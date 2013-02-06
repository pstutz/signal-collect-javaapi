/*
 *  @author Philip Stutz
 *  
 *  Copyright 2011 University of Zurich
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

import scala.Option;
import com.signalcollect.Edge;
import com.signalcollect.GraphEditor;
import java.util.HashMap;

/**
 * Vertex implementation that collects the most recent signals that have arrived
 * on all edges. Users of the framework extend this class to implement a
 * specific algorithm by defining a `collect` function.
 */
@SuppressWarnings("serial")
public abstract class DataGraphVertex<Id, State, Signal> extends AbstractVertex {

	public Id id;
	public State state;

	/**
	 * @param vertexId
	 *            unique vertex id.
	 * @param initialState
	 *            the initial state of the vertex.
	 */
	public DataGraphVertex(Id vertexId, State initialState) {
		super();
		this.id = vertexId;
		this.state = initialState;
	}

	/**
	 * A map that has vertex ids as keys and stores the most recent signal
	 * received from the vertex with the given id as the value for that key.
	 */
	protected HashMap<Object, Signal> mostRecentSignalMap = new HashMap<Object, Signal>();

	public Iterable<Signal> signals() {
		return mostRecentSignalMap.values();
	}

	/**
	 * Function that gets called by the framework whenever this vertex is
	 * supposed to collect new signals.
	 * 
	 * @param signalMessages
	 *            new signals that have arrived since the last time this vertex
	 *            collected
	 * 
	 * @param messageBus
	 *            an instance of MessageBus which can be used by this vertex to
	 *            interact with the graph.
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void executeCollectOperation(GraphEditor graphEditor) {
		setState(collect());
	}

	/**
	 * Delivers signals that are addressed to this specific vertex
	 * 
	 * @param signal
	 *            the the signal to deliver to this vertex
	 * 
	 * @return true if the vertex decided to collect immediately.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean deliverSignal(Object signal, Option sourceId) {
		mostRecentSignalMap.put(sourceId.get(), (Signal) signal);
		return false;
	}

	@SuppressWarnings("unchecked")
	public void setState(Object s) {
		state = (State) s;
	}

	/**
	 * The abstract `collect` function is algorithm specific and calculates the
	 * new vertex state.
	 * 
	 * Beware of modifying and returning a reference to the same object that was
	 * used to represent oldState: default signal scoring and termination
	 * detection fail in this case.
	 * 
	 * If the edge along which a signal was sent is relevant, then
	 * mostRecentSignalMap can be used to access the edge id of a signal.
	 * 
	 * @param mostRecentSignals
	 *            An iterable that returns the most recently received signal for
	 *            each edge that has sent at least one signal already.
	 * 
	 * @return The new vertex state.
	 */
	public abstract State collect();

	public Double sumOfOutWeights = 0.0;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean addEdge(Edge e, GraphEditor graphEditor) {
		Boolean added = super.addEdge(e, graphEditor);
		if (added) {
			sumOfOutWeights += e.weight();
		}
		return added;
	}

	@SuppressWarnings({ "rawtypes" })
	public boolean removeEdge(Object targetId, GraphEditor graphEditor) {
		Double weightToSubtract = 0.0;
		Edge<?> outgoingEdge = outgoingEdges.get(targetId);
		if (outgoingEdge != null) {
			weightToSubtract = outgoingEdge.weight();
		}
		Boolean removed = super.removeEdge(targetId, graphEditor);
		if (removed) {
			sumOfOutWeights -= weightToSubtract;
		}
		return removed;
	}

	/**
	 * This method is used by the framework in order to decide if the vertex'
	 * collect operation should be executed.
	 * 
	 * @return the score value. The meaning of this value depends on the
	 *         thresholds set in the framework.
	 */
	public double scoreCollect() {
		if (!mostRecentSignalMap.isEmpty()) {
			return 1.0;
		} else if (edgesModifiedSinceCollectOperation) {
			return 1.0;
		} else {
			return 0.0;
		}
	}
}