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

import com.signalcollect.Vertex;
import com.signalcollect.ExecutionInformation;
import com.signalcollect.StateForwarderEdge;
import com.signalcollect.configuration.ExecutionMode;
import com.signalcollect.javaapi.*;

/**
 * @author: Stephane Rufer The University of Z&uuml;rich<br>
 * 
 *          Date: Mar 11, 2012 Package: ch.uzh.ifi.ddis.k_core
 *          
 *          Example Graph:
 *          
 *               4
 *        	   3 |
 *           2  \|  
 *            \__5_____6___15
 *               |    /|\  /|\
 *               1   7 | \/ | 13__14
 *             	     | | /\ | /
 *                   \_|/  \|/
 *                     8____12____
 *                      \_ _/     |
 *                        9__10__11
 *                        
 * 			 Kcore Results per Vertex:
 * 				- 1: 1
 * 				- 2: 1
 * 				- 3: 1
 * 				- 4: 1
 * 				- 5: 1
 * 				- 6: 3
 * 				- 7: 2
 * 				- 8: 3
 * 				- 9: 2
 * 				- 10: 2
 * 				- 11: 2
 * 				- 12: 3
 * 				- 13: 2
 * 				- 14: 1
 * 				- 15: 3
 */
public class KCore {
	public static void main(String[] args) {
		Graph graph = new GraphBuilder().build();

		// initialize the vertices
		graph.addVertex(new KCoreVertex(1, -1));
		graph.addVertex(new KCoreVertex(2, -1));
		graph.addVertex(new KCoreVertex(3, -1));
		graph.addVertex(new KCoreVertex(4, -1));
		graph.addVertex(new KCoreVertex(5, -1));
		graph.addVertex(new KCoreVertex(6, -1));
		graph.addVertex(new KCoreVertex(7, -1));
		graph.addVertex(new KCoreVertex(8, -1));
		graph.addVertex(new KCoreVertex(9, -1));
		graph.addVertex(new KCoreVertex(10, -1));
		graph.addVertex(new KCoreVertex(11, -1));
		graph.addVertex(new KCoreVertex(12, -1));
		graph.addVertex(new KCoreVertex(13, -1));
		graph.addVertex(new KCoreVertex(14, -1));
		graph.addVertex(new KCoreVertex(15, -1));

		// initialize the edges
		graph.addEdge(1, new StateForwarderEdge<Integer>(5));
		graph.addEdge(2, new StateForwarderEdge<Integer>(5));
		graph.addEdge(3, new StateForwarderEdge<Integer>(5));
		graph.addEdge(4, new StateForwarderEdge<Integer>(5));
		graph.addEdge(5, new StateForwarderEdge<Integer>(1));
		graph.addEdge(5, new StateForwarderEdge<Integer>(2));
		graph.addEdge(5, new StateForwarderEdge<Integer>(3));
		graph.addEdge(5, new StateForwarderEdge<Integer>(4));

		graph.addEdge(5, new StateForwarderEdge<Integer>(6));
		graph.addEdge(6, new StateForwarderEdge<Integer>(5));

		graph.addEdge(6, new StateForwarderEdge<Integer>(7));
		graph.addEdge(6, new StateForwarderEdge<Integer>(8));
		graph.addEdge(6, new StateForwarderEdge<Integer>(12));
		graph.addEdge(6, new StateForwarderEdge<Integer>(15));
		graph.addEdge(7, new StateForwarderEdge<Integer>(6));
		graph.addEdge(8, new StateForwarderEdge<Integer>(6));
		graph.addEdge(12, new StateForwarderEdge<Integer>(6));
		graph.addEdge(15, new StateForwarderEdge<Integer>(6));

		graph.addEdge(7, new StateForwarderEdge<Integer>(8));
		graph.addEdge(8, new StateForwarderEdge<Integer>(7));

		graph.addEdge(8, new StateForwarderEdge<Integer>(15));
		graph.addEdge(8, new StateForwarderEdge<Integer>(12));
		graph.addEdge(8, new StateForwarderEdge<Integer>(9));
		graph.addEdge(15, new StateForwarderEdge<Integer>(8));
		graph.addEdge(12, new StateForwarderEdge<Integer>(8));
		graph.addEdge(9, new StateForwarderEdge<Integer>(8));

		graph.addEdge(9, new StateForwarderEdge<Integer>(10));
		graph.addEdge(9, new StateForwarderEdge<Integer>(12));
		graph.addEdge(10, new StateForwarderEdge<Integer>(9));
		graph.addEdge(12, new StateForwarderEdge<Integer>(9));

		graph.addEdge(10, new StateForwarderEdge<Integer>(11));
		graph.addEdge(11, new StateForwarderEdge<Integer>(10));

		graph.addEdge(11, new StateForwarderEdge<Integer>(12));
		graph.addEdge(12, new StateForwarderEdge<Integer>(11));

		graph.addEdge(12, new StateForwarderEdge<Integer>(13));
		graph.addEdge(12, new StateForwarderEdge<Integer>(15));
		graph.addEdge(13, new StateForwarderEdge<Integer>(12));
		graph.addEdge(15, new StateForwarderEdge<Integer>(12));

		graph.addEdge(13, new StateForwarderEdge<Integer>(14));
		graph.addEdge(13, new StateForwarderEdge<Integer>(15));
		graph.addEdge(14, new StateForwarderEdge<Integer>(13));
		graph.addEdge(15, new StateForwarderEdge<Integer>(13));

		ExecutionInformation stats = graph.execute(ExecutionConfiguration
				.withExecutionMode(ExecutionMode.Synchronous()));

		// stats for the purpose of debugging 
		System.out.println(stats);

		// print the state of every vertex in the graph. The state is the k core
		// the vertex belongs to
		graph.foreachVertex(new VertexCommand() {
			public void f(Vertex<?, ?> v) {
				System.out.println(v);
			}
		});

		graph.shutdown();
	}
}
