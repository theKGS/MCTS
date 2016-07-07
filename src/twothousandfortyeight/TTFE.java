package twothousandfortyeight;

import java.util.ArrayList;
import java.util.Random;

import main.Board;
import main.Move;

public class TTFE implements Board {
	int currentPlayer;
	public int [][]board;
	int size;
	int scoremax;
	int turns;
	
	public TTFE(int s) {
		size = s;
		turns = 0;
		scoremax = 1;
		board = new int[size][size];
		currentPlayer = 0;
	}
	
	@Override
	public Board duplicate() {
		TTFE n = new TTFE(size);
		n.turns = turns;
		n.scoremax = scoremax;
		n.currentPlayer = this.currentPlayer;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				n.board[x][y] = this.board[x][y];
			}				
		}		
		
		return n;
	}

	@Override
	public ArrayList<Move> getMoves() {
		ArrayList<Move> out = new ArrayList<Move>();
		if (currentPlayer == 0){
			// It's the player's turn
			out.add(new TTFEMove(Direction.Up));
			out.add(new TTFEMove(Direction.Down));
			out.add(new TTFEMove(Direction.Left));
			out.add(new TTFEMove(Direction.Right));
		} else {
			// It's chance's turn
			for (int x = 0; x < size; x++){
				for (int y = 0; y < size; y++){
					if (board[x][y] == 0) {
						out.add(new TTFEMove(x, y, 1));
						out.add(new TTFEMove(x, y, 2));
					}
				}				
			}
		}		

		return out;
	}

	public Move makeRandomChoice(Random r){
		ArrayList<Move> out = new ArrayList<Move>();
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (board[x][y] == 0) {
					out.add(new TTFEMove(x, y, 1));
					out.add(new TTFEMove(x, y, 2));
				}
			}				
		}
		
		Move m = out.get(r.nextInt(out.size()));
		return m;
	}
	
	@Override
	public void makeMove(Move m) {
		TTFEMove move = (TTFEMove)m;
		if (currentPlayer == 0){
			// Shift all lines maximum amount in one direction
			// Then merge and shift again
			for (int i = 0; i < size; i++){
				fixLine(i, move.dir);
				mergeLine(i, move.dir);
				fixLine(i, move.dir);
			}
		
			turns++;
			currentPlayer = -1;
		} else {
			board[move.x][move.y] = move.val;
			currentPlayer = 0;
		}
	}

	/**
	 * Merge same integers
	 * @param i
	 * @param d
	 */
	public void mergeLine(int i, Direction d){
		if (d == Direction.Up){
			for (int r = 0; r < size - 1; r++) {
				if (board[i][r] == board[i][r + 1] && board[i][r] > 0){
					board[i][r]++;
					board[i][r + 1] = 0;
					r++;
				}
			}
		} else if (d == Direction.Down){
			for (int r = size - 1; r > 0; r--) {
				if (board[i][r] == board[i][r - 1] && board[i][r] > 0){
					board[i][r] = 0;
					board[i][r - 1]++;
					r--;
				}
			}
		} else if (d == Direction.Left){
			for (int r = size - 1; r > 0; r--) {
				if (board[r][i] == board[r - 1][i] && board[r][i] > 0){
					board[r][i]++;
					board[r - 1][i] = 0;
					r--;
				}
			}
		} else if (d == Direction.Right){
			for (int r = 0; r < size - 1; r++) {
				if (board[r][i] == board[r + 1][i] && board[r][i] > 0){
					board[r][i]++;
					board[r + 1][i] = 0;
					r++;
				}
			}
		}
	}
	
	/**
	 * Pack the line by removing empty tiles
	 * @param i
	 * @param d
	 */
	public void fixLine(int i, Direction d){
		int step = 1;
		if (d == Direction.Up){
			for (int r = 0; r < size; r++) {
				if (board[i][r]==0){
					// find next non zero
					for (int f = r + step; f < size; f++){
						if (board[i][f] != 0){
							board[i][r] = board[i][f];
							board[i][f] = 0;
							break;
						}
						step++;
					}
				}
			}
		} else if (d == Direction.Down) {
			for (int r = size - 1; r >= 0 ; r--) {
				if (board[i][r]==0){
					// find next non zero
					for (int f = r - step; f >= 0; f--){
						if (board[i][f] != 0){
							board[i][r] = board[i][f];
							board[i][f] = 0;
							break;
						}
						step++;
					}
				}
			}
		} else if (d == Direction.Left) {
			for (int r = size - 1; r >= 0 ; r--) {
				if (board[r][i]==0){
					// find next non zero
					for (int f = r - step; f >= 0; f--){
						if (board[f][i] != 0){
							board[r][i] = board[f][i];
							board[f][i] = 0;
							break;
						}
						step++;
					}
				}
			}
		} else if (d == Direction.Right) {
			for (int r = 0; r < size; r++) {
				if (board[r][i]==0){
					// find next non zero
					for (int f = r + step; f < size; f++){
						if (board[f][i] != 0){
							board[r][i] = board[f][i];
							board[f][i] = 0;
							break;
						}
						step++;
					}
				}
			}
		}

	}
	
		
	@Override
	public boolean gameOver() {
		int freeSlots = 0;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (board[x][y] == 0) {
					freeSlots++;
				}
				
				// Update max score
				if (board[x][y] > scoremax){
					scoremax = board[x][y];
				}
			}		
		}

		return freeSlots == 0 || scoremax == 9;
	}

	@Override
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public int getQuantityOfPlayers() {
		return 1;
	}

	@Override
	public double[] getScore() {
		double []score = new double[1];
		if (scoremax == 9)
			score[0] = 1;
		else{
			score[0] = 0;
			if (gameOver()){
				score[0] = 1;
			}
		}
		
		return score;
	}

	@Override
	public double[] getMoveWeights() {
		int moveCount = 0;
		for (int x = 0; x < size; x++){
			for (int y = 0; y < size; y++){
				if (board[x][y] == 0)
					moveCount+= 2;
			}				
		}
		
		double []weights = new double[moveCount];
		for (int i = 0; i < weights.length; i++)
			weights[i] = 1.0d;
		
		return weights;
	}

	@Override
	public void bPrint() {
		// TODO Auto-generated method stub
		
	}
}
