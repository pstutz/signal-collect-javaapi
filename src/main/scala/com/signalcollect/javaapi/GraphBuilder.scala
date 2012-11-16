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

import com.signalcollect.{ GraphBuilder => ScalaGraphBuilder }
import com.signalcollect.configuration._
import com.signalcollect.interfaces.LogMessage
import com.signalcollect.interfaces.MessageBusFactory
import com.signalcollect.interfaces.WorkerFactory
import com.signalcollect.interfaces.StorageFactory
import com.signalcollect.nodeprovisioning.NodeProvisioner
import scala.reflect.ClassTag

class GraphBuilder(config: GraphConfiguration) extends ScalaGraphBuilder[Object, Object](config) {

  def this() = this(GraphConfiguration())

  override def build: Graph = new DefaultGraph(config)

  /**
   *  Internal function to create a new builder instance that has a configuration which defaults
   *  to parameters that are the same as the ones in this instance, unless explicitly set differently.
   */
  override protected def newLocalBuilder(
    consoleEnabled: Boolean = config.consoleEnabled,
    loggingLevel: Int = config.loggingLevel,
    logger: LogMessage => Unit = config.logger,
    workerFactory: WorkerFactory = config.workerFactory,
    messageBusFactory: MessageBusFactory = config.messageBusFactory,
    storageFactory: StorageFactory = config.storageFactory,
    statusUpdateIntervalInMilliseconds: Long = config.statusUpdateIntervalInMilliseconds,
    akkaDispatcher: AkkaDispatcher = config.akkaDispatcher,
    akkaMessageCompression: Boolean = config.akkaMessageCompression,
    nodeProvisioner: NodeProvisioner = config.nodeProvisioner,
    heartbeatIntervalInMilliseconds: Long = config.heartbeatIntervalInMilliseconds,
    throttleInboxThresholdPerWorker: Int = config.throttleInboxThresholdPerWorker,
    throttleWorkerQueueThresholdInMilliseconds: Int = config.throttleWorkerQueueThresholdInMilliseconds): GraphBuilder = {
    new GraphBuilder(
      GraphConfiguration(
        consoleEnabled = consoleEnabled,
        loggingLevel = loggingLevel,
        logger = logger,
        workerFactory = workerFactory,
        messageBusFactory = messageBusFactory,
        storageFactory = storageFactory,
        statusUpdateIntervalInMilliseconds = statusUpdateIntervalInMilliseconds,
        akkaDispatcher = akkaDispatcher,
        akkaMessageCompression = akkaMessageCompression,
        nodeProvisioner = nodeProvisioner,
        heartbeatIntervalInMilliseconds = heartbeatIntervalInMilliseconds,
        throttleInboxThresholdPerWorker = throttleInboxThresholdPerWorker,
        throttleWorkerQueueThresholdInMilliseconds = throttleWorkerQueueThresholdInMilliseconds))
  }

}