package connectFour;

import java.util.ArrayList;

import main.Board;
import main.Move;

public class ConnectFour implements Board {

	public boolean[][][] brd;
	public int currentPlayer = 0;
	public int freeSlots[];
	public int totalFreeSlots = 6 * 7;
	public int winner = -1;
	public boolean draw = false;

	public ConnectFour(){
		brd = new boolean[7][6][2];
		freeSlots = new int[7];
		for (int i = 0; i < 7; i++)
			freeSlots[i] = 6;
		winner = -1;
	}

	@Override
	public Board duplicate(){
	
		ConnectFour newBoard = new ConnectFour();
		for (int x = 0; x < 7; x++){
			for (int y = 0; y < 6; y++){
				for (int z = 0; z < 2; z++){
					newBoard.brd[x][y][z] = brd[x][y][z];
				}	
			}				
		}
		newBoard.currentPlayer = this.currentPlayer;
		for (int k = 0; k < 7; k++)
			newBoard.freeSlots[k] = freeSlots[k];
		newBoard.totalFreeSlots = totalFreeSlots;
		newBoard.winner = winner;
		newBoard.draw = draw;
		return newBoard;		
	}
	
	@Override
	public ArrayList<Move> getMoves() {
		ArrayList<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < 7; i++){
			if (freeSlots[i] > 0){
				ConnectFourMove cfm = new ConnectFourMove(i);
				moves.add(cfm);
			}
		}
					
		return moves;
	}

	// Return true if last move won the game for that player
	private boolean thisMoveWonTheGame(int x, int y, int pl){
		int horizontal = 1;
		int vertical = 1;
		int risingDiagonal = 1;
		int sinkingDiagonal = 1;

		horizontal += scanLine(x, y, -1, 0, pl);
		horizontal += scanLine(x, y, 1, 0, pl);
		vertical += scanLine(x, y, 0, 1, pl);
		risingDiagonal += scanLine(x, y, 1, -1, pl);
		risingDiagonal += scanLine(x, y, -1, 1, pl);
		sinkingDiagonal += scanLine(x, y, 1, 1, pl);
		sinkingDiagonal += scanLine(x, y, -1, -1, pl);
		
		//System.out.println(horizontal + " " + vertical  + " " + risingDiagonal  + " " + sinkingDiagonal);
		return (horizontal >= 4 || vertical >= 4 ||
				risingDiagonal >= 4 || sinkingDiagonal >= 4);
	}
	
	private int scanLine(int x, int y, int xf, int yf, int playr){
		int sum = 0;
		for (int i = 1; i < 4; i++){
			if (x + i * xf > 6 || x + i * xf < 0)
				break;
			if (y + i * yf > 5 || y + i * yf < 0)
				break;
			
			if (brd[x + i * xf][y + i * yf][playr])
				sum++;
			else
				break;
		}
		
		return sum;
	}
	
	@Override
	public void makeMove(Move m) {
		ConnectFourMove cfm = (ConnectFourMove)m;
		
		int xIndex = cfm.row;
		int yIndex = freeSlots[cfm.row] - 1;
		
		//System.out.println("Making move in: " + xIndex + "/" +yIndex + " freeslots: " + freeSlots[cfm.row]);
		brd[xIndex][yIndex][currentPlayer] = true;
		freeSlots[xIndex]--;
		totalFreeSlots--;

		// Check if the move won the game, if so update the winner
		if (thisMoveWonTheGame(xIndex, yIndex, currentPlayer)){
			winner = currentPlayer;
			//System.out.println("Winner updated!");
			//this.print();
		} else {
			if (totalFreeSlots == 0)
				draw = true;
		}
	
		// Switch player after every move
		if (currentPlayer == 0)
			currentPlayer = 1;
		else
			currentPlayer = 0;
	}
	
	@Override
	public int getQuantityOfPlayers(){
		return 2;
	}
	
	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void print(){
		System.out.println("--------------");
		for (int y = 0; y < 6; y++){
			for (int x = 0; x < 7; x++){
				if (brd[x][y][0] && !brd[x][y][1])
					System.out.print("()");
				else if (!brd[x][y][0] && brd[x][y][1])
					System.out.print("<>");
				else if (!brd[x][y][0] && !brd[x][y][1])
					System.out.print("  ");
				else
					System.out.print("{}");
			}
			System.out.println("");
		}
	}

	@Override
	public boolean gameOver() {
		return draw || winner >= 0;
	}
	
	@Override
	public double[] pessimisticBounds() {
		double[] pessimistic = new double[2];
		
		if (winner >= 0){
			pessimistic[winner] = 1;		
		} else { // draw
			pessimistic[0] = 0.5;
			pessimistic[1] = 0.5;
		}
		
		//System.out.println(Arrays.toString(pessimistic));
		return pessimistic;
	}

	@Override
	public double[] optimisticBounds() {
		double[] optimistic = new double[2];

		if (winner >= 0){
			optimistic[winner] = 1;		
		} else { // draw
			optimistic[0] = 0.5;
			optimistic[1] = 0.5;			
		}

		//System.out.println(Arrays.toString(optimistic));
		return optimistic;
	}

	@Override
	public double[] getScore() {
		double[] score = new double[2];
		if (winner >= 0)
			score[winner] = 1;
		else if (draw){
			score[0] = 0.5;
			score[1] = 0.5;
		}

		return score;
	}

	// This is not used by connectFour, but it
	// must be implemented anyway.
	@Override
	public double[] getMoveWeights() {
		return null;
	}
}
