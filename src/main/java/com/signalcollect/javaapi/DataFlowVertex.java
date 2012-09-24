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

import com.signalcollect.interfaces.*;

import scala.collection.JavaConversions;
import com.signalcollect.GraphEditor;

import com.signalcollect.AbstractVertex;

import java.util.LinkedList;

/**
 * Vertex implementation that collects all the signals that have arrived since
 * the last time this vertex has collected. Users of the framework extend this
 * class to implement a specific algorithm by defining a `collect` function.
 */
@SuppressWarnings("serial")
public abstract class DataFlowVertex<Id, State, Signal> extends
		AbstractVertex<Id, State> {

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

	public Id id() {
		return id;
	}
	
	public State state() {
		return state;
	}

	public void setState(State s) {
		state = s;
	}

	public abstract State resetState();

	/**
	 * Delegates to superclass and resets the state to the initial state after
	 * signaling.
	 */
	@Override
	public void executeSignalOperation(GraphEditor graphEditor) {
		super.executeSignalOperation(graphEditor);
		setState(resetState());
	}

	/**
	 * List of messages that have not been collected yet (not just signals, as
	 * in the collect parameter).
	 */
	Iterable<SignalMessage<Signal>> uncollectedMessages = new LinkedList<SignalMessage<Signal>>();

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
	@Override
	public void executeCollectOperation(
			scala.collection.Iterable<SignalMessage<?>> signalMessages,
			GraphEditor graphEditor) {
		LinkedList<SignalMessage<Signal>> newUncollectedMessages = new LinkedList<SignalMessage<Signal>>();
		Iterable<SignalMessage<?>> javaMessages = JavaConversions
				.asJavaIterable(signalMessages);
		LinkedList<Signal> uncollectedSignals = new LinkedList<Signal>();
		for (SignalMessage<?> message : javaMessages) {
			@SuppressWarnings("unchecked")
			Signal castSignal = (Signal) message.signal();
			uncollectedSignals.add(castSignal);
			@SuppressWarnings("unchecked")
			SignalMessage<Signal> castMessage = (SignalMessage<Signal>) message;
			newUncollectedMessages.add(castMessage);
		}
		uncollectedMessages = newUncollectedMessages;
		setState(collect(state(), uncollectedSignals));
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
	public abstract State collect(State oldState,
			Iterable<Signal> uncollectedSignals);

}