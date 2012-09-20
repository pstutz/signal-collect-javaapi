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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: Stephane Rufer The University of Z&uuml;rich<br>
 * 
 *          Date: Mar 29, 2012 Package: ch.uzh.ifi.ddis.betweenness_centrality
 * 
 *          This class is an abstraction that represents the full path between
 *          two vertisies in addition the distance.
 */
public class PathValue implements Serializable {
	private Set<Integer> key; // the key associated with the path (source and target vertex id)
	private Set<Integer> path; // the path between the two vertisies
	private double distance; // the distance between the two vertisies

	public PathValue() {
		key = new HashSet<Integer>();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		PathValue that = (PathValue) o;
		if (key != null ? !key.equals(that.key) : that.key != null)
			return false;
		if (path != null ? !path.equals(that.path) : that.path != null)
			return false;
		if (distance != that.distance)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + key.hashCode();
		result = 37 * result + path.hashCode();
		result = 37 * result + (int) distance;

		return result;
	}

	@Override
	public String toString() {
		return "path:" + path.toString();
	}

	// Getters and Setters
	public Set<Integer> getKey() {
		return key;
	}

	public Set<Integer> getPath() {
		return path;
	}

	public double getDistance() {
		return distance;
	}

	public void setKey(Set<Integer> key) {
		this.key = key;
	}

	public void setPath(Set<Integer> path) {
		this.path = path;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
