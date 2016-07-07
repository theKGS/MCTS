package ticTacToe;

import main.Move;

public class TicTacToeMove implements Move {
	int x;
	int y;
	
	TicTacToeMove(int x, int y){
		this.x = x;
		this.y = y;
	}
}
