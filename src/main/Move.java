package main;

import java.util.ArrayList;

public interface Move {
	/**
	 * Have this function return a boolean indicating whether or not this move
	 * contains multiple sub moves
	 * 
	 * @return
	 */
	public boolean isStochastic();

	/**
	 * This function returns all sub moves stored in this move. Only called if
	 * isStochastic() returns true.
	 * 
	 * @return
	 */
	public ArrayList<Move> unpack();
}
