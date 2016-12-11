package twothousandfortyeight;

import java.util.Random;

import main.MCTS;
import main.Move;

public class TTFEMain {

	public static void main(String[] args) {
		TTFE b = new TTFE(4);
		MCTS mcts = new MCTS();
		mcts.setExplorationConstant(1.4);
		Move move;
		Random random = new Random();
		
		// Start the game with two random pieces placed
		b.currentPlayer = -1;
		move = b.makeRandomChoice(random);
		b.makeMove(move);
		b.currentPlayer = -1;
		move = b.makeRandomChoice(random);
		b.makeMove(move);		
		
		while (!b.gameOver()){
			if (b.currentPlayer == 0) {
				move = mcts.runMCTS(b, 60000, false);
				b.makeMove(move);
				System.out.println("---");
				for (int y = 0; y < 4; y++) {
					for (int x = 0; x < 4; x++) {
						System.out.print(b.board[x][y] + " ");
					}
					System.out.println("");
				}
				System.out.println("turns: " + b.turns);
			} else {
				move = b.makeRandomChoice(random);
				b.makeMove(move);
			}
		}
		
		System.out.println("---");
		for (int y = 0; y < 4; y++) {			
			for (int x = 0; x < 4; x++) {
				System.out.print(b.board[x][y] + " ");
			}
			System.out.println("");
		}

		System.out.println("Total # moves: " + b.turns);
	}
}
