/**
 *  Copyright 2012 Stephane Rufer
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
package com.signalcollect.javaapi.examples.betweennesscentrality;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.signalcollect.Edge;
import com.signalcollect.javaapi.DataGraphVertex;

/**
 * @author: Stephane Rufer The University of Z&uuml;rich<br>
 * 
 *          Date: Mar 11, 2012 Package: ch.uzh.ifi.ddis.betweenness_centrality
 */
public class BetweennessCentralityVertex
		extends
		DataGraphVertex<Integer, HashMap<Set<Integer>, PathValue>, HashMap<Set<Integer>, PathValue>> {

	/**
	 * Constructor for vertex. Initializes the state of the vertex at the
	 * beginning of the computation
	 * 
	 * @param id
	 *            The unique vertex id
	 * @param state
	 *            The initial state of the vertex
	 */
	public BetweennessCentralityVertex(Integer id,
			HashMap<Set<Integer>, PathValue> state) {
		super(id, state);
	}

	/**
	 * The collect method that finds the shortest paths from all vertices to all
	 * others. The vertex state represents all shortest paths of the vertex to
	 * all other vertices in the graph.
	 * 
	 * The vertex passes on changes to paths to its neighbors, who then
	 * determine if this information is relevant to their context.
	 */
	public HashMap<Set<Integer>, PathValue> collect(
			HashMap<Set<Integer>, PathValue> oldState,
			Iterable<HashMap<Set<Integer>, PathValue>> mostRecentSignals) {
		HashMap<Set<Integer>, PathValue> newState = (HashMap<Set<Integer>, PathValue>) ((HashMap) oldState)
				.clone();

		// Find neighbors
		Set<Integer> neighbors = new HashSet<Integer>();
		scala.collection.Iterator<scala.collection.Iterable<Edge>> it = this
				.getOutgoingEdges().iterator();
		while (it.hasNext()) {
			scala.collection.Iterator<Edge> eit = it.next().iterator();
			while (eit.hasNext()) {
				Edge e = eit.next();
				neighbors.add((Integer) e.id().targetId());
			}
		}

		for (HashMap<Set<Integer>, PathValue> signal : mostRecentSignals) {
			for (Set<Integer> key : signal.keySet()) {
				// only add a new entry if the key from the signal is not key
				// present
				if (oldState.get(key) == null) {
					// The value associated with the key (path plus distance)
					PathValue value = signal.get(key);
					// Add the key, value pair if the vertex is on the path
					if (value.getPath().contains(this.id())) { 
						newState.put(key, signal.get(key));
					}
					for (Integer n : neighbors) {
						// Check to see if neighbors are in the signal, in most
						// cases this means new paths will be added
						if (value.getPath().contains(n)) {
							int new_vertex = -1;
							// loop throught the key to find the vertex that is
							// not the neighbor i.e. unknown to the vertex
							Iterator<Integer> kit = key.iterator();
							int start_loop = 0;
							while (kit.hasNext()) {
								int next_kel = kit.next();
								if (!neighbors.contains(next_kel)) {
									new_vertex = next_kel;
								}
								start_loop++;
							}

							// Add the neighboring vertex if the initializing
							// phase is not yet done.
							// This is the case if the state of the vertesies
							// are still just their own vertex id
							if (start_loop != 2) {
								Set<Integer> new_key = new HashSet<Integer>();
								new_key.add(this.id());
								new_key.add(n);
								PathValue new_value = new PathValue();
								Set<Integer> path = new HashSet<Integer>();
								path.add(this.id());
								path.add(n);
								new_value.setKey(new_key);
								new_value.setPath(path);
								new_value.setDistance(value.getDistance());
								newState.put(new_key, new_value);
							} else if (new_vertex == -1) { // pass on paths that
															// might be
															// interesting
								newState.put(key, signal.get(key));
							} else { // Adds the path to the unknown vertex
								Set<Integer> new_key = new HashSet<Integer>();
								new_key.add(this.id());
								new_key.add(new_vertex);
								PathValue new_value = new PathValue();
								Set<Integer> path = new HashSet<Integer>();
								path.add(this.id());
								for (Integer i : value.getPath()) {
									path.add(i);
								}
								new_value.setKey(new_key);
								new_value.setPath(path);
								new_value.setDistance(value.getDistance());
								newState.put(new_key, new_value);
							}
						}
					}
				} else if (oldState.get(key).getDistance() >= signal.get(key)
						.getDistance()) { // Update the path if a shorter one is
											// found
					if (oldState.get(key).getPath().size() > signal.get(key)
							.getPath().size()) {
						newState.put(key, signal.get(key));
					}
				}
			}
		}

		return newState;

	}

}
