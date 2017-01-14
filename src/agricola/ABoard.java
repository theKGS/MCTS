package agricola;

import java.util.ArrayList;

import main.Board;
import main.CallLocation;
import main.Move;

public class ABoard implements Board {
	private LandGrid[] landgrid = new LandGrid[3];
	int startingplayer = 0;
	
	public ABoard(){
		landgrid[0] = new LandGrid();
		landgrid[1] = new LandGrid();
		landgrid[2] = new LandGrid();
	}
	
	public ABoard(ABoard other) {
		landgrid[0] = other.landgrid[0].duplicate();
		landgrid[1] = other.landgrid[1].duplicate();
		landgrid[2] = other.landgrid[2].duplicate();
		startingplayer = other.startingplayer;
	}

	@Override
	public Board duplicate() {
		return new ABoard(this);
	}

	@Override
	public ArrayList<Move> getMoves(CallLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeMove(Move m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean gameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCurrentPlayer() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getQuantityOfPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getScore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getMoveWeights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void bPrint() {
		// TODO Auto-generated method stub
	}

	public void printLandGrid() {
		System.out.println("Player 0");
		landgrid[0].printBoard();
		System.out.println("Player 1");
		landgrid[1].printBoard();
		System.out.println("Player 2");
		landgrid[2].printBoard();
	}
}
