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

import com.signalcollect.Graph;
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
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(1, 5));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(2, 5));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(3, 5));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(4, 5));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 1));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 2));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 3));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 4));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(5, 6));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(6, 5));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(6, 7));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(6, 8));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(6, 12));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(6, 15));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(7, 6));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(8, 6));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(12, 6));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(15, 6));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(7, 8));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(8, 7));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(8, 15));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(8, 12));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(8, 9));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(15, 8));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(12, 8));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(9, 8));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(9, 10));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(9, 12));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(10, 9));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(12, 9));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(10, 11));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(11, 10));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(11, 12));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(12, 11));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(12, 13));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(12, 15));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(13, 12));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(15, 12));

		graph.addEdge(new StateForwarderEdge<Integer, Integer>(13, 14));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(13, 15));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(14, 13));
		graph.addEdge(new StateForwarderEdge<Integer, Integer>(15, 13));

		ExecutionInformation stats = graph.execute(ExecutionConfiguration
				.withExecutionMode(ExecutionMode.Synchronous()));

		// stats for the purpose of debugging 
		System.out.println(stats);

		// print the state of every vertex in the graph. The state is the k core
		// the vertex belongs to
		graph.foreachVertex(FunUtil.convert(new VertexCommand() {
			public void f(Vertex v) {
				System.out.println(v);
			}
		}));

		graph.shutdown();
	}
}
