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

import scala.Some;
import scala.collection.JavaConversions;
import com.signalcollect.EdgeId;

import com.signalcollect.interfaces.MessageBus;
import com.signalcollect.interfaces.SignalMessage;

import java.util.HashMap;

/**
 * Vertex implementation that collects the most recent signals that have arrived
 * on all edges. Users of the framework extend this class to implement a
 * specific algorithm by defining a `collect` function.
 */
@SuppressWarnings("serial")
public abstract class DataGraphVertex<IdTypeParameter, StateTypeParameter, SignalTypeParameter>
		extends
		JavaVertex<IdTypeParameter, StateTypeParameter, SignalTypeParameter> {

	/**
	 * @param vertexId
	 *            unique vertex id.
	 * @param initialState
	 *            the initial state of the vertex.
	 */
	public DataGraphVertex(IdTypeParameter vertexId,
			StateTypeParameter initialState) {
		super(vertexId, initialState);
	}

	/**
	 * A map that has edge ids as keys and stores the most recent signal
	 * received along the edge with that id as the value for that key.
	 */
	protected HashMap<EdgeId<?, IdTypeParameter>, SignalTypeParameter> mostRecentSignalMap = new HashMap<EdgeId<?, IdTypeParameter>, SignalTypeParameter>();

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
	public void executeCollectOperation(
			scala.collection.Iterable<SignalMessage<?, ?, ?>> signalMessages,
			MessageBus messageBus) {
		Iterable<SignalMessage<?, ?, ?>> javaMessages = JavaConversions
				.asJavaIterable(signalMessages);
		for (SignalMessage<?, ?, ?> message : javaMessages) {
			@SuppressWarnings("unchecked")
			SignalMessage<?, IdTypeParameter, SignalTypeParameter> castMessage = (SignalMessage<?, IdTypeParameter, SignalTypeParameter>) message;
			mostRecentSignalMap.put(castMessage.edgeId(), castMessage.signal());
		}
		setState(collect(getState(), mostRecentSignalMap.values()));
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
	public abstract StateTypeParameter collect(StateTypeParameter oldState,
			Iterable<SignalTypeParameter> mostRecentSignals);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public scala.Option<scala.collection.Iterable<Object>> getVertexIdsOfPredecessors() {
		scala.collection.mutable.ListBuffer<Object> result = new scala.collection.mutable.ListBuffer<Object>();
		for (EdgeId id : mostRecentSignalMap.keySet()) {
			result.$plus$eq(id.sourceId());
		}
		return new Some(result);
	}

}