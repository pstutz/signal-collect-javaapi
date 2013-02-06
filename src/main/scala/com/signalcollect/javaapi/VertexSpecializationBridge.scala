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

/**
 * Traits with specialization such as Vertex can not be implemented
 * from Java. This class acts as a bridge to support it.
 */
class VertexSpecializationBridge extends Vertex[Object, Object] {
  def id: Object = null.asInstanceOf[Object]
  def state: Object = null.asInstanceOf[Object]
  def setState(s: Object) = {}
  def addEdge(e: Edge[_], graphEditor: GraphEditor[Any, Any]): Boolean = null.asInstanceOf[Boolean]
  def removeEdge(targetId: Any, graphEditor: GraphEditor[Any, Any]): Boolean = null.asInstanceOf[Boolean]
  def removeAllEdges(graphEditor: GraphEditor[Any, Any]): Int = null.asInstanceOf[Int]
  def deliverSignal(signal: Any, sourceId: Option[Any]): Boolean = null.asInstanceOf[Boolean]
  def executeSignalOperation(graphEditor: GraphEditor[Any, Any]) = {}
  def executeCollectOperation(graphEditor: GraphEditor[Any, Any]) = {}
  def scoreSignal: Double = null.asInstanceOf[Double]
  def scoreCollect: Double = null.asInstanceOf[Double]
  def edgeCount: Int = null.asInstanceOf[Int]
  def afterInitialization(graphEditor: GraphEditor[Any, Any]) = {}
  def beforeRemoval(graphEditor: GraphEditor[Any, Any]) = {}
}