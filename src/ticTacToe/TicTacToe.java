package ticTacToe;

import java.util.ArrayList;

import main.Board;
import main.CallLocation;
import main.Move;

public class TicTacToe implements Board {
	int [][]board;
	int currentPlayer;
	int winner;
	boolean draw;
	boolean gameWon;
	int freeslots;
	
	public TicTacToe() {
		board = new int[3][3];
		freeslots = 9;
	}
	
	@Override
	public Board duplicate() {
		TicTacToe t = new TicTacToe();
		t.winner = winner;
		t.currentPlayer = currentPlayer;
		t.draw = draw;
		t.freeslots = freeslots;
		t.gameWon = gameWon;
		t.board = new int[3][3];
		for (int x = 0; x < 3; x++){
			for (int y = 0; y < 3; y++){
				t.board[x][y] = board[x][y];
			}			
		}
		
		return t;
	}

	@Override
	public boolean gameOver() {
		return gameWon;
	}
	
	@Override
	public ArrayList<Move> getMoves(CallLocation location) {
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int x = 0; x < 3; x++){
			for (int y = 0; y < 3; y++){
				if (board[x][y] == 0)
					moves.add(new TicTacToeMove(x, y));
			}	
		}

		return moves;
	}

	@Override
	public void makeMove(Move m) {
			TicTacToeMove move = (TicTacToeMove)m;
		
		board[move.x][move.y] = currentPlayer + 1;		
		freeslots--;
		final int cp = currentPlayer + 1;
		
		if (board[0][0] == cp && board[0][1] == cp && board[0][2] == cp ||
			board[1][0] == cp && board[1][1] == cp && board[1][2] == cp ||
			board[2][0] == cp && board[2][1] == cp && board[2][2] == cp ||
			board[0][0] == cp && board[1][0] == cp && board[2][0] == cp ||
			board[0][1] == cp && board[1][1] == cp && board[2][1] == cp ||
			board[0][2] == cp && board[1][2] == cp && board[2][2] == cp ||
			board[0][0] == cp && board[1][1] == cp && board[2][2] == cp ||
			board[0][2] == cp && board[1][1] == cp && board[2][0] == cp)
		{
			gameWon = true;
			winner = currentPlayer;
		}
		
		if (freeslots == 0){
			gameWon = true;
			draw = true;
		}
				
		if (currentPlayer == 0){
			currentPlayer = 1;
		} else
			currentPlayer = 0;
	}
	

	
	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	
	@Override
	public double[] getScore() {
		double []score;
		score = new double[2];
		if (!draw) {
			score[winner] = 1.0d;
		} else {
			score[0] = 0.5d;
			score[1] = 0.5d;
		}

		return score;
	}
	
	@Override
	public int getQuantityOfPlayers() {
		return 2;
	}
	
	@Override
	public double[] getMoveWeights() {
		return null;
	}

	@Override
	public void bPrint() {
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				System.out.print(board[x][y] + " ");
			}
			System.out.println("");
		}
	}
}
