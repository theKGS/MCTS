package connectFour;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Random;

import main.MCTS;
import main.Move;
import main.Node;

public class Main {
	static int scorePlayer0 = 0;
	static int scorePlayer1 = 0;

	public static void run(int games, int it, double exp, boolean bounds, double pess, double opti) {
		MCTS m0 = new MCTS();
		MCTS m1 = new MCTS();
		m1.setExplorationConstant(exp);
		m0.setExplorationConstant(exp);
		m1.setOptimisticBias(opti);
		m1.setPessimisticBias(pess);
		m0.setOptimisticBias(opti);
		m0.setPessimisticBias(pess);
		m0.setTimeDisplay(true);
		m1.setTimeDisplay(true);
		m0.setScoreBounds(bounds);
		m1.setScoreBounds(bounds);
		
		scorePlayer0 = 0;
		scorePlayer1 = 0;
		for (int i = 0; i < games; i++) {
			ConnectFour cf = new ConnectFour(1);
			while (true) {
				Move m = m1.runMCTS(cf, it);
				cf.makeMove(m);
				cf.print();
				
				if (cf.checkWinCondition(1)) {
					scorePlayer1++;
					System.out.println("Score: " + scorePlayer0 + " - "
							+ scorePlayer1);
					break;
				}

				if (cf.checkDraw()) {
					System.out.println("Score: " + scorePlayer0 + " - "
							+ scorePlayer1);
					break;
				}

				if (cf.checkLossCondition(1)) {
					scorePlayer0++;
					System.out.println("Score: " + scorePlayer0 + " - "
							+ scorePlayer1);
					break;
				}

				m = m0.runMCTS(cf, it);
				cf.makeMove(m);
				cf.print();

				if (cf.checkWinCondition(0)) {
					scorePlayer0++;
					System.out.println("Score: " + scorePlayer0 + " - "
							+ scorePlayer1);
					break;
				}

				if (cf.checkDraw()) {
					System.out.println("Score: " + scorePlayer0 + " - "
							+ scorePlayer1);
					break;
				}

				if (cf.checkLossCondition(0)) {
					scorePlayer1++;
					System.out.println("Score: " + scorePlayer0 + " - "
							+ scorePlayer1);
					break;
				}
			}
		}

		System.out.println("Iterations: " + it + " Exp. Con.: " + exp);
		System.out.println("pessBias: " + pess + " optiBias: " + opti);
		System.out.println("Score for this run: " + scorePlayer0 + " / " + scorePlayer1);

		try {
		    FileOutputStream fos = new FileOutputStream("long.txt", true);
			Writer writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			
		    writer.write("Iterations: " + it + " Exp. Con.: " + exp + ", Bounds = " + bounds + "\n");
			writer.write("pessBias: " + pess + " optiBias: " + opti + "\n");
			writer.write("Score for this run: " + scorePlayer0 + " / " + scorePlayer1 + "\n");
			writer.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {		
		run(10, 250000, 4.0, false, 0, 0);
	}
}
