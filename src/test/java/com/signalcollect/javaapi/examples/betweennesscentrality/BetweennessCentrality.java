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
import java.util.Set;

import com.signalcollect.ExecutionInformation;
import com.signalcollect.interfaces.AggregationOperation;
import com.signalcollect.Graph;
import com.signalcollect.StateForwarderEdge;
import com.signalcollect.Vertex;
import com.signalcollect.javaapi.GraphBuilder;
import com.signalcollect.javaapi.VertexCommand;

/**
 * @author: Stephane Rufer The University of Z&uuml;rich<br>
 * 
 *          Date: Mar 11, 2012 Package: ch.uzh.ifi.ddis.betweenness_centrality
 *
 *          Example Graph:
 *          
 *          		 4
 *                  /|   
 *                 / |
 *            1___2__3__5__6
 *            
 *          Betweenness Centrality Result per Vertex:
 *          	- 1: 0
 *          	- 2: 0.26668
 *          	- 3: 0.4
 *          	- 4: 0
 *          	- 5: 0.33334
 *          	- 6: 0 
 */
public class BetweennessCentrality {

	public void executeGraph() {
		Graph graph = new GraphBuilder().build();

		// initialize the graph with 6 vertices 
		for (int i = 1; i <= 6; i++) {
			// HashMap that represents the state
			// the state is a combination of the vertex id as a key and the PathValue as a value.
			// The PathValue is a class that holds the path between the vertices as well as the distance
			HashMap<Set<Integer>, PathValue> hm = new HashMap<Set<Integer>, PathValue>();
			Set<Integer> key = new HashSet<Integer>();
			key.add(i);
			PathValue value = new PathValue();
			value.setKey(key);
			Set<Integer> path = new HashSet<Integer>();
			path.add(i);
			value.setPath(path);
			value.setDistance(0);
			hm.put(key, value);
			graph.addVertex(new BetweennessCentralityVertex(i, hm));
		}
		
		// initialize the edges
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(1, 2));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(2, 3));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(2, 1));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(2, 4));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(3, 5));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(3, 2));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(3, 4));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(4, 2));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(4, 3));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 6));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 3));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(6, 5));

		ExecutionInformation stats = graph.execute();

		System.out.println(stats);

		// get global shortest paths. All shortest paths between all vertices
		final HashMap<Set<Integer>, PathValue> globalShortestPaths = graph
				.aggregate(new GetGlobalShortestPaths());
		
		// debug output that is nice to see.
		System.out.println("Global Shortest Paths: " + globalShortestPaths);
		System.out.println("Global Shortest Paths Size: "
				+ globalShortestPaths.size());

		// print the state of every vertex in the graph. This is for debug
		// purposes
		graph.foreachVertex(new VertexCommand() {
			public void f(Vertex v) {
				System.out.println(v);
			}
		});

		// print the state of every vertex in the graph (The betweenness centrality).
		graph.foreachVertex(new VertexCommand() {
			public void f(Vertex v) {
				HashMap<Set<Integer>, Set<Integer>> v_state = (HashMap<Set<Integer>, Set<Integer>>) v
						.state();
				int v_on_shortest_path = 0;
				int v_global_minus = 0;

				for (Set<Integer> key : globalShortestPaths.keySet()) {
					if (key.contains(v.id())) {
						// This is a path that the vertex is a start/end point of.
						// These paths are ignored
						v_global_minus++;
					} else if (globalShortestPaths.get(key).getPath()
							.contains(v.id())) {
						// vertex is on the shortest path (passes through the vertex)
						v_on_shortest_path++;
					}
				}
				// calculate the betweenness centraility 
				// (all shortest paths the vertex is on divided by the number 
				// of unique shortest paths)
				float bc = (float) v_on_shortest_path
						/ (float) (globalShortestPaths.size() - v_global_minus);
				System.out.println("Vertex: " + v.id() 
						+ " COUNT: "+ v_state.size() 
						+ " SP: " + v_on_shortest_path
						+ " GL: " + (globalShortestPaths.size() - v_global_minus)
						+ " BC: " + bc);
			}
		});
		graph.shutdown();
	}

	public static void main(String[] args) {
		BetweennessCentrality bc = new BetweennessCentrality();
		bc.executeGraph();
	}

	// Inner class to aggregate global shortest paths in the graph
	private class GetGlobalShortestPaths implements
			AggregationOperation<HashMap<Set<Integer>, PathValue>> {

		@Override
		public HashMap<Set<Integer>, PathValue> aggregate(
				HashMap<Set<Integer>, PathValue> arg0,
				HashMap<Set<Integer>, PathValue> arg1) {
			HashMap<Set<Integer>, PathValue> state0 = (HashMap<Set<Integer>, PathValue>) arg0
					.clone();
			HashMap<Set<Integer>, PathValue> state1 = (HashMap<Set<Integer>, PathValue>) arg1
					.clone();
			state0.putAll(state1);
			return state0;
		}

		@Override
		public HashMap<Set<Integer>, PathValue> extract(Vertex arg0) {
			return (HashMap<Set<Integer>, PathValue>) ((HashMap) arg0.state())
					.clone();
		}

		@Override
		public HashMap<Set<Integer>, PathValue> neutralElement() {
			return new HashMap<Set<Integer>, PathValue>();
		}

	}
}
