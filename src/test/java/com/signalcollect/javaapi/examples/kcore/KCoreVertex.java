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
package com.signalcollect.javaapi.examples.kcore;

import com.signalcollect.javaapi.DataGraphVertex;

/**
 * @author: Stephane Rufer The University of Z&uuml;rich<br>
 * 
 *          Date: Mar 18, 2012 Package: ch.uzh.ifi.ddis.k_core
 */
public class KCoreVertex extends DataGraphVertex<Integer, Integer, Integer> {

	private int threshold; // indication of the stage of the computation

	/**
	 * Constructor for the kcore vertex class. This initializes the state of the
	 * vertex at the beginning of the computation.
	 * 
	 * This initializes the threshold at 0. The threshold is used to determine
	 * in what stage the computation is in. All k cores below the current
	 * threshold are ignored in that signal/collect step.
	 * 
	 * @param id
	 *            The unique vertex id
	 * @param state
	 *            The initial state of the vertex
	 */
	public KCoreVertex(Integer id, Integer state) {
		super(id, state);
		this.threshold = 0;
	}

	/**
	 * The collect method that calculates the kcore of the vertex. The kcore is
	 * represented by the vertex state.
	 */
	public Integer collect(Integer oldState, Iterable<Integer> mostRecentSignals) {
		int count = 0;
		// count the number of neighbors above the threshold. These are the
		// neighbors relevant to the computation at this step.
		// Always count neighbors if the graph is in the initial state (-1)
		for (Integer neighbor_degree : mostRecentSignals) {
			if (neighbor_degree > threshold || neighbor_degree == -1) {
				count++;
			}
		}

		// new threshold is one k core higher.
		// The new state of this vertex is the number of neighbors with a k core
		// higher than the threshold only this is higher than the current
		// threshold.
		// Otherwise the state is the current threshold. Analogously this means
		// that this vertex is no longer relevant to the computation.
		if (count > threshold) {
			oldState = count;
			threshold++;
			return oldState;
		} else {
			oldState = threshold;
			return oldState;
		}
	}
}
