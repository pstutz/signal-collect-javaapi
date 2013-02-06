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
import com.signalcollect.GraphEditor;

/**
 * Vertex implementation that collects all the signals that have arrived since
 * the last time this vertex has collected. Users of the framework extend this
 * class to implement a specific algorithm by defining a `collect` function.
 */
@SuppressWarnings("serial")
public abstract class DataFlowVertex<Id, State, Signal> extends AbstractVertex {

	Id id;
	State state;

	/**
	 * @param vertexId
	 *            Unique vertex id.
	 * @param initialState
	 *            The initial state of the vertex.
	 */
	public DataFlowVertex(Id vertexId, State initialState) {
		this.id = vertexId;
		this.state = initialState;
	}

	public Id getId() {
		return id;
	}

	public State state() {
		return state;
	}

	@SuppressWarnings("unchecked")
	public void setState(Object s) {
		state = (State) s;
	}

	/**
	 * Delegates to superclass and resets the state to the initial state after
	 * signaling.
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void executeSignalOperation(GraphEditor graphEditor) {
		super.executeSignalOperation(graphEditor);
	}

	/**
	 * Function that gets called by the framework whenever this vertex is
	 * supposed to collect new signals.
	 * 
	 * @param signalMessages
	 *            new signal messages that have arrived since the last time this
	 *            vertex collected
	 * 
	 * @param messageBus
	 *            an instance of MessageBus which can be used by this vertex to
	 *            interact with the graph.
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void executeCollectOperation(GraphEditor graphEditor) {
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
		setState(collect((Signal) signal));
		return true;
	}

	/**
	 * The abstract `collect` function is algorithm specific and calculates the
	 * new vertex state.
	 * 
	 * Beware of modifying and returning a reference to the same object that was
	 * used to represent oldState: default signal scoring and termination
	 * detection fail in this case.
	 * 
	 * @param uncollectedSignals
	 *            all signals received by this vertex since the last time this
	 *            function was executed
	 * 
	 * @return The new vertex state.
	 */
	public abstract State collect(Signal signal);

	/**
	 * This method is used by the framework in order to decide if the vertex'
	 * collect operation should be executed.
	 * 
	 * @return the score value. The meaning of this value depends on the
	 *         thresholds set in the framework.
	 */
	public double scoreCollect() {
		return 0.0;
	}

}