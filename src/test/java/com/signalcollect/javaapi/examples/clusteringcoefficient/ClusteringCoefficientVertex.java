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
package com.signalcollect.javaapi.examples.clusteringcoefficient;

import java.util.ArrayList;
import com.signalcollect.javaapi.DataGraphVertex;

/**
 * @author: Stephane Rufer The University of Zurich<br>
 * 
 *          Date: Mar 11, 2012 Package: ch.uzh.ifi.ddis.clustering_coefficient
 */
public class ClusteringCoefficientVertex extends
		DataGraphVertex<Integer, ArrayList<Integer>, ArrayList<Integer>> {

	/**
	 * Constructor for the vertex. Initializes the vertex with the given state
	 * 
	 * @param id
	 *            The unique id of the vertex
	 * @param state
	 *            The initial vertex state
	 */
	public ClusteringCoefficientVertex(Integer id, ArrayList<Integer> state) {
		super(id, state);
	}

	/**
	 * The collect function for the vertex. This sets the state of the vertex,
	 * so that it represents the immediate neighborhood of the vertex. The state
	 * includes the connections to the nodes that the neighbors are connected
	 * to.
	 * 
	 * This is then used to calculate the clustering coefficient. The clustering
	 * coefficient is calculated globally by going through the whole graph.
	 */
	public ArrayList<Integer> collect(ArrayList<Integer> oldState,
			Iterable<ArrayList<Integer>> mostRecentSignals) {
		ArrayList<Integer> newState = (ArrayList<Integer>) (((ArrayList<Integer>) oldState)
				.clone());

		// Get all the neighbors of the vertex and add their neighbors to the
		// vertex state.
		for (ArrayList<Integer> neighbors : mostRecentSignals) {
			for (Integer n : neighbors) {
				newState.add(n);
			}
		}

		return newState;

	}
}
