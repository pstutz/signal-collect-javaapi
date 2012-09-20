package com.signalcollect.javaapi.examples.gameoflife;

import java.lang.Iterable;

import com.signalcollect.javaapi.*;

/**
 * Represents a cell in a "Conway's Game of Life" (http://en.wikipedia.org/wiki/Conway's_Game_of_Life) simulation
 * The cell calculates a new state according to the states of its surrounding neighbors.
 * 
 * @author Daniel Strebel
 *
 */
@SuppressWarnings("serial")
public class GameOfLifeCell extends DataGraphVertex<Integer, Boolean, Boolean> {
	
	/**
	 * Constructor for a Game of Life cell.
	 * 
	 * @param id a unique integer for identifying the cell
	 * @param state sets the current state i.e. if the cell is alive or not
	 */
	public GameOfLifeCell(Integer id, Boolean state) {
		super(id, state);
	}
	
	/**
	 *  Defines a collect operation which determines how a cell should calculate
	 *  its state depending on the messages of its neighbors which contain their
	 *  current state.
	 *  
	 *  A message with state true means that the sender of this message is alive and
	 *  a message with state false means that the sender is currently not alive.
	 *  
	 *  Each cell will determine if it should resurrect, die or stay in the current
	 *  state according to the number of alive neighbors:
	 *  
	 *  #of alive neighbors		consequence
	 *  =====================================================
	 *  count <= 1				Cell dies of loneliness
	 *  count = 2				Cell stays in current state
	 *  count = 3				Cell resurrects
	 *  count > 3				Cell dies because of overcrowding
	 *  
	 *  @param signals the newest signals that were sent to the cell
	 *  @param messageBus
	 */
	public Boolean collect(Boolean oldState, Iterable<Boolean> signals) {		
		//counts all alive neighbors of a cell.
		int sumOfAliveNeighbors = 0;
		for (Boolean isAlive : signals) {
			if(isAlive) {
				sumOfAliveNeighbors+=1;
			}
		}
		Boolean alive = oldState;
		
		//checks if cell should change its state according to the rules stated above
		if(sumOfAliveNeighbors<=1 || sumOfAliveNeighbors > 3) {
			alive = false;
		}
		else if(sumOfAliveNeighbors == 3) {
			alive = true;
		}
		return alive;
	}
	
}
