/*
 *  @author Philip Stutz
 *  
 *  Copyright 2013 University of Zurich
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

package com.signalcollect.javaapi

import com.signalcollect._
import java.util.HashMap

/**
 * Traits with specialization such as Vertex cannot be implemented
 * from Java. This class acts as a bridge to support it.
 */
abstract class VertexSpecializationBridge[Id, State] extends Vertex[Id, State] {
  val id: Id
  var state: State
  def setState(s: State) = {
    state = s
  }
  var outgoingEdges: HashMap[Object, Edge[Object]]
  def edgeCount: Int = outgoingEdges.size
}