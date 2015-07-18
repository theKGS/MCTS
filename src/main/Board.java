package main;

import java.util.ArrayList;

public interface Board {

	/**
	 * Create n copies of the board and return them in a list
	 * 
	 * @param n
	 * @return
	 */
	public ArrayList<Board> duplicate(int n);

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

	/**
	 * Returns true if the given player has won the game
	 * 
	 * @param player
	 * @return
	 */
	public boolean checkWinCondition(int player);

	public void makeMove(Move m);

	/**
	 * Returns true if the given player has lost the game
	 * 
	 * @param player
	 * @return
	 */
	public boolean checkLossCondition(int player);

	/**
	 * Returns true if the game is over.
	 * @return
	 */
	public boolean gameOver();	
	
	/**
	 * Returns true if the game is a draw
	 * @return
	 */
	public boolean checkDraw();
	
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