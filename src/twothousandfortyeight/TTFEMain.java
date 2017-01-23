package twothousandfortyeight;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

import main.MCTS;
import main.Move;

public class TTFEMain {

	public static void main(String[] args) {
		//runGame(100, 1.4d, 500, false, "2048-100games-500i-1.4c.txt");
		//runGame(100, 1.4d, 500, false, "2048-100games-500i-0.5c.txt");
		//runGame(4, 1.4d, 500, false, "2048-10games-500i-0.5c.txt");
		runGame(1, 0.3d, 8000, true, "2048-100games.txt", "300games-2000i-0.3c ");
		//runGame(100, 0.4d, 2000, false, "2048-100games.txt", "300games-2000i-0.4c ");
		//runGame(100, 0.5d, 2000, false, "2048-100games.txt", "300games-2000i-0.5c ");
		//runGame(100, 1.0d, 2000, false, "2048-100games.txt", "300games-2000i-1.0c ");
		//runGame(100, 1.4d, 2000, false, "2048-100games.txt", "300games-2000i-1.4c ");
		//runGame(100, 2.8d, 2000, false, "2048-300100gamesgames.txt", "300games-6000i-2.8c ");
	}

	static void runGame(int games, double constant, int iterations, boolean print, String filename, String lprefix) {
		MCTS mcts = new MCTS();
		mcts.setExplorationConstant(constant);
		mcts.enableRootParallelisation(4);
		mcts.setTimeDisplay(true);
		Move move;
		Random random = new Random();
		double []data = new double[games];
		
		for (int i = 0; i < games; i++) {
			TTFE b = new TTFE(4);

			// Start the game with two random pieces placed
			b.currentPlayer = -1;
			move = b.makeRandomChoice(random);
			b.makeMove(move);
			b.currentPlayer = -1;
			move = b.makeRandomChoice(random);
			b.makeMove(move);

			if (print) {
				System.out.println("S-----");
				for (int y = 0; y < 4; y++) {
					for (int x = 0; x < 4; x++) {
						System.out.print(b.board[x][y] + " ");
					}
					System.out.println("");
				}
			}

			int roundcount = 0;
			while (!b.gameOver()) {
				roundcount++;
				if (b.currentPlayer == 0) {
					move = mcts.runMCTS_UCT(b, iterations, false);
					b.makeMove(move);

					if (print) {
						System.out.println("P-----");
						for (int y = 0; y < 4; y++) {
							for (int x = 0; x < 4; x++) {
								System.out.print(b.board[x][y] + " ");
							}
							System.out.println("");
						}
						System.out.println("turns: " + b.turns);
					}
				} else {
					move = b.makeRandomChoice(random);
					b.makeMove(move);

					if (print) {
						System.out.println("R-----");
						for (int y = 0; y < 4; y++) {
							for (int x = 0; x < 4; x++) {
								System.out.print(b.board[x][y] + " ");
							}
							System.out.println("");
						}
						System.out.println("turns: " + b.turns);
					}
				}
				
				if (roundcount % 50 == 0){
					System.out.print("-");
				}
			}

			System.out.println("");
			System.out.println(i + ": Total # moves: " + b.turns + " Score: " + b.score + " Max: " + (1 << findMax(b)));

			data[i] = b.score;
			
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(filename, true);
				Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
				
				/*
				writer.write(
						i + ": Total # moves: " + b.turns + " Score: " + b.score + " Max: " + (1 << findMax(b)) + "\n");
				 */

				writer.close();
				fos.close();
			} catch (IOException err) {
				err.printStackTrace();
			}
		}
		String variance = String.format("%.12f", computeVariance(data));
		System.out.println("Mean: " + computeMean(data) + " Variance " + variance + " Deviation " + computeDeviation(data));
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename, true);
			Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			writer.write(lprefix + "Mean: " + computeMean(data) + " Variance " + variance + " Deviation " + computeDeviation(data) + "\n");

			writer.close();
			fos.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
		

	}

	static int findMax(TTFE board) {
		int max = 0;
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if (board.board[x][y] > max) {
					max = board.board[x][y];
				}
			}
		}

		return max;
	}
	
	static double computeMean(double []score){
		double sum = 0;
		for (int r = 0; r < score.length; r++){
			sum += score[r] / score.length;
		}
		return sum;
	}
	
	static double computeVariance(double []score){
		double []squared = new double[score.length];
		double mean = computeMean(score);
		double sum = 0;
		for (int r = 0; r < score.length; r++){
			squared[r] = Math.pow(score[r] - mean, 2.0d);
			sum += squared[r] / score.length;
		}
		return sum;
	}
	
	static double computeDeviation(double []score){
		return Math.sqrt(computeVariance(score));
	}
	
}
