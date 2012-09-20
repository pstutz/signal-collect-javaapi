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

package com.signalcollect.javaapi

import com.signalcollect.{ Vertex, DefaultGraph => ScalaDefaultGraph }
import com.signalcollect.configuration.GraphConfiguration
import com.signalcollect.Graph

class DefaultGraph(config: GraphConfiguration = GraphConfiguration()) extends ScalaDefaultGraph(config) with JavaGraphFunctions

trait JavaGraphFunctions extends Graph {

  /**
   *  Executes the function `f` on the vertex with id `vertexId` and returns the result.
   *
   *  @return The result of function `f`
   *
   *  @note The function `f` may be executed in another thread or on another computer.
   *
   *  @note References to objects that are not reachable from the vertex passed to
   *  the function as a parameter may not be accessible or may be subject to race conditions.
   *
   *  @param f The function that gets executed on the vertex with id `vertexId`
   *
   *  @example `forVertexWithId(vertexId = 1, f = new VertexFunction[Any]() {
   *        public void f(Vertex v) {
   *          return v.state;
   *        })`
   *
   *  @usecase def forVertexWithId(vertexId: Any, f: VertexFunction[String]): String
   */
  def forVertexWithId[VertexType <: Vertex, ResultType](vertexId: Any, f: VertexFunction[ResultType]): ResultType = {
    forVertexWithId(vertexId, FunUtil.convert(f))
  }

  /**
   *  Executes the command `c` on all vertices.
   *
   *  @note The command `c` may be executed in multiple other threads, beware of race conditions.
   *
   *  @note This command may be executed on other machines and references
   *  		to objects that are not reachable from the vertex-parameter may not be accessible.
   */
  def foreachVertex(c: VertexCommand) {
    foreachVertex(FunUtil.convert(c))
  }
  
  
}