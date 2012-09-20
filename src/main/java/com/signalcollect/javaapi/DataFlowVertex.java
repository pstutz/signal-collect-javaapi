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
import java.util.LinkedList;

/**
 * Vertex implementation that collects all the signals that have arrived since
 * the last time this vertex has collected. Users of the framework extend this
 * class to implement a specific algorithm by defining a `collect` function.
 */
@SuppressWarnings("serial")
public abstract class DataFlowVertex<IdTypeParameter, StateTypeParameter, SignalTypeParameter>
		extends
		JavaVertexWithResetStateAfterSignaling<IdTypeParameter, StateTypeParameter, SignalTypeParameter> {

	/**
	 * @param vertexId
	 *            Unique vertex id.
	 * @param initialState
	 *            The initial state of the vertex.
	 * @param resetState
	 *            The state will be set to `resetState` after signaling.
	 */
	public DataFlowVertex(IdTypeParameter vertexId,
			StateTypeParameter initialState, StateTypeParameter resetState) {
		super(vertexId, initialState, resetState);
	}

	/**
	 * List of messages that have not been collected yet (not just signals, as
	 * in the collect parameter).
	 */
	protected Iterable<SignalMessage<Object, IdTypeParameter, SignalTypeParameter>> uncollectedMessages = new LinkedList<SignalMessage<Object, IdTypeParameter, SignalTypeParameter>>();

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
	public void executeCollectOperation(
			scala.collection.Iterable<SignalMessage<?, ?, ?>> signalMessages,
			MessageBus messageBus) {
		LinkedList<SignalMessage<Object, IdTypeParameter, SignalTypeParameter>> newUncollectedMessages = new LinkedList<SignalMessage<Object, IdTypeParameter, SignalTypeParameter>>();
		Iterable<SignalMessage<?, ?, ?>> javaMessages = JavaConversions
				.asJavaIterable(signalMessages);
		LinkedList<SignalTypeParameter> uncollectedSignals = new LinkedList<SignalTypeParameter>();
		for (SignalMessage<?, ?, ?> message : javaMessages) {
			@SuppressWarnings("unchecked")
			SignalTypeParameter castSignal = (SignalTypeParameter) message
					.signal();
			uncollectedSignals.add(castSignal);
			@SuppressWarnings("unchecked")
			SignalMessage<Object, IdTypeParameter, SignalTypeParameter> castMessage = (SignalMessage<Object, IdTypeParameter, SignalTypeParameter>) message;
			newUncollectedMessages.add(castMessage);
		}
		uncollectedMessages = newUncollectedMessages;
		setState(collect(getState(), uncollectedSignals));
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
	public abstract StateTypeParameter collect(StateTypeParameter oldState,
			Iterable<SignalTypeParameter> uncollectedSignals);
	
}