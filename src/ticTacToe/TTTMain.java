package ticTacToe;


import java.util.Arrays;

import main.FinalSelectionPolicy;
import main.MCTS;
import main.Move;

public class TTTMain {

	public static void main(String[] args) {
		MCTS mcts = new MCTS();
		mcts.setExplorationConstant(0.2);
		mcts.setTimeDisplay(true);
		Move move;
		mcts.setOptimisticBias(0.0d);
		mcts.setPessimisticBias(0.0d);
		mcts.setMoveSelectionPolicy(FinalSelectionPolicy.robustChild);
		int []scores = new int[3];
		
		for (int i = 0; i < 100; i ++) {
			TicTacToe ttt = new TicTacToe();
			while (!ttt.gameOver()){
				move = mcts.runMCTS_UCT(ttt, 120000, false);
				ttt.makeMove(move);
			}
			
			System.out.println("---");
			ttt.bPrint();
						
			double []scr = ttt.getScore();
			if (scr[0] > 0.9) {
				scores[0]++; // player 1
			} else if (scr[1] > 0.9) {
				scores[1]++; // player 2
			} else
				scores[2]++; // draw
			
			System.out.println(Arrays.toString(scr));
			System.out.println(Arrays.toString(scores));
						
		}
	}
}
