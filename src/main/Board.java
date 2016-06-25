package main;

import java.util.ArrayList;

public interface Board {

	/**
	 * Create one copy of the board. It is important that the copies do
	 * not store references to objects shared by other boards unless
	 * those objects are immutable.
	 * 
	 * @return
	 */
	public Board duplicate();

	/**
	 * Get a list of all available moves for the current state
	 * 
	 * @return
	 */
	public ArrayList<Move> getMoves();

	public void makeMove(Move m);

	/**
	 * Returns true if the game is over.
	 * @return
	 */
	public boolean gameOver();	
		
	/**
	 * Returns the player ID for the player whose turn is active. This method is
	 * called by the MCTS.
	 * 
	 * @return
	 */
	public int getCurrentPlayer();
	
	public int getQuantityOfPlayers();
	
	/**
	 * Prints the board
	 */
	public void print();
	
	public double[] pessimisticBounds();
	public double[] optimisticBounds();
	
	/**
	 * Returns a score vector. 
	 * [1.0, 0.0] indicates a win for player 0.
	 * [0.0, 1.0] indicates a win for player 1
	 * [0.5, 0.5] indicates a draw
	 * @return
	 */
	public double[] getScore();
}