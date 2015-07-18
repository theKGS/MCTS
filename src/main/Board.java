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
	 * Create one copy of the board.
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
	 * Returns true if the game is over
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
	
	public int[] getOpponents();
	//public int getOpponent();
	
	public int getQuantityOfPlayers();
	
	/**
	 * Prints the board
	 */
	public void print();

	public double[] pessimisticBounds();
	public double[] optimisticBounds();
	public double[] getScore();
}