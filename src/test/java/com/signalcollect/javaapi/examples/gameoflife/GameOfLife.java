/*
 *  @author Daniel Strebel
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

package com.signalcollect.javaapi.examples.gameoflife;

import java.util.Map;
import java.util.Random;

import com.signalcollect.ExecutionInformation;
import com.signalcollect.javaapi.*;
import com.signalcollect.Graph;
import com.signalcollect.StateForwarderEdge;
import com.signalcollect.configuration.ExecutionMode;

/**
 * A simulation of a "Conway's Game of Life"
 * (http://en.wikipedia.org/wiki/Conway's_Game_of_Life) based on the signal
 * collect computation model.
 * 
 * To ensure termination and enable the output of intermediate steps a
 * synchronous execution model is chosen here.
 * 
 * @author Daniel Strebel
 * 
 */
public class GameOfLife {

	private final int COLUMNS = 20; // Number of columns in the simulation grid
	private final int ROWS = 20; // Number of rows in the simulation grid
	private static final int NUMBER_OF_ITERATIONS = 100; // Maximal number of
	// synchronization steps.
	private PixelMap image = new PixelMap("Game of Life Simulation");
	private Graph g;

	/**
	 * Sets up the graph and generate all vertices and edges. Each vertex links
	 * to its direct neighbors.
	 * 
	 * All neighbors of cell 'O' are marked with an 'X'
	 * 
	 * -------------------- | | | | | | -------------------- | | X | X | X | |
	 * -------------------- | | X | O | X | | -------------------- | | X | X | X
	 * | | -------------------- | | | | | | --------------------
	 */
	public void init(boolean[] seed) {
		g = new GraphBuilder().build();
		// initialize vertices
		for (int i = 0; i < seed.length; i++) {
			g.addVertex(new GameOfLifeCell(i, seed[i]));
		}

		// set up edges
		for (int row = 0; row < ROWS; row++) {
			for (int collumn = 0; collumn < COLUMNS; collumn++) {
				addOutgoingLinks(g, row, collumn);
			}

		}
	}

	public void executionStep(boolean showImage) {

		ExecutionInformation stats = g.execute(ExecutionConfiguration
				.withExecutionMode(ExecutionMode.Synchronous()).withStepsLimit(
						1));
		if (showImage) {
			byte[] data = new byte[ROWS * COLUMNS];
			Map<Integer, Boolean> idValueMap = g
					.aggregate(new JavaIdStateAggregator<Integer, Boolean>());
			for (int id : idValueMap.keySet()) {
				if (idValueMap.get(id)) {
					data[id] = (byte) 1;
				} else {
					data[id] = (byte) 0;
				}
			}
			image.setData(data);
			while (image.isNotUpdated()) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(stats);
	}

	public void shutDown() {
		g.shutdown();
	}

	/**
	 * Creates a list of all edges that go from a cell to its neighbors. Edges
	 * are only included if the coordinates are within the grid.
	 * 
	 * @param row
	 *            horizontal coordinate of the source cell
	 * @param column
	 *            vertical coordinate of the source cell
	 * @return list of all outgoing edges form the cell at the specified
	 *         coordinates.
	 */
	private void addOutgoingLinks(Graph graph, int row, int column) {
		int sourceId = generateVertexId(row, column);
		// iterates over all neighbors and the cell itself but the link to
		// itself is excluded.
		for (int colIt = (column - 1); colIt <= (column + 1); colIt++) {
			for (int rowIt = (row - 1); rowIt <= (row + 1); rowIt++) {
				if ((rowIt != row || colIt != column)
						&& isValidCoordinate(rowIt, colIt)) {
					graph.addEdge(sourceId, new StateForwarderEdge<Integer>(
							generateVertexId(rowIt, colIt)));
				}
			}
		}
	}

	/**
	 * utility method to convert grid coordinates to ids. Id starts with 0 in
	 * the top left corner and increments row wise.
	 * 
	 * if x1<x2 then generateVertexId(x1, y) < generateVertexId(x2, y) if y1<y2
	 * then generateVertexId(x, y1) < generateVertexId(x, y2)
	 * 
	 * @param row
	 * @param collumn
	 * @return the unique id for the cell at the specified location
	 */
	private int generateVertexId(int row, int collumn) {
		return row * COLUMNS + collumn;
	}

	/**
	 * Utility that checks if a coordinate is within the grid. All negative
	 * values and all values bigger or equal to the size constraints of the grid
	 * are considered invalid.
	 * 
	 * @param row
	 * @param collumn
	 * @return
	 */
	private boolean isValidCoordinate(int row, int collumn) {
		if (row < 0 || row >= ROWS) {
			return false;
		} else if (collumn < 0 || collumn >= COLUMNS) {
			return false;
		}
		return true;
	}

	/**
	 * Generates random initialization data for the game of life grid
	 * 
	 * @return initialization data for the grid
	 */
	private boolean[] randomSeed() {
		Random rand = new Random();
		boolean[] seed = new boolean[ROWS * COLUMNS];
		for (int i = 0; i < seed.length; i++) {
			seed[i] = rand.nextBoolean();
		}
		return seed;
	}

	// /**
	// * Generates the initialization data for a glider form that moves
	// diagonally
	// * across the field
	// *
	// * @return initialization data for a glider figure
	// */
	// private boolean[] glider() {
	// boolean[] seed = new boolean[ROWS * COLUMNS];
	// for (int i = 0; i < seed.length; i++) {
	// seed[i] = false;
	// }
	// seed[generateVertexId(0, 1)] = true;
	// seed[generateVertexId(1, 2)] = true;
	// seed[generateVertexId(2, 0)] = true;
	// seed[generateVertexId(2, 1)] = true;
	// seed[generateVertexId(2, 2)] = true;
	// return seed;
	// }

	/**
	 * Runs the Game of Live simulation with simple visualization.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		GameOfLife simulation = new GameOfLife();
		// simulation.init(simulation.glider());
		simulation.init(simulation.randomSeed());
		for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
			simulation.executionStep(true);
		}
		simulation.shutDown();
	}
}
