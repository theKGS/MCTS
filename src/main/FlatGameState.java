package main;

public class FlatGameState {
	public int wins;
	public int games;
	public Board board;
	public Move move;
	
	public FlatGameState(Board b, Move m){
		board = b;
		move = m;
	}
}
