package agricola;

import java.util.ArrayList;

import main.Board;
import main.CallLocation;
import main.Move;

public class ABoard implements Board {
	private Player[] players = new Player[3];
	private ArrayList<ActionSpace> actionspaces;
	int startingplayer = 0;
	int currentplayer;
	
	public ABoard(){
		actionspaces = new ArrayList<ActionSpace>();
		
		currentplayer = 0;
		players[0] = new Player();
		players[1] = new Player();
		players[2] = new Player();
	}
	
	public ABoard(ABoard other) {
		actionspaces = new ArrayList<ActionSpace>();
		for (ActionSpace a : other.actionspaces){
			actionspaces.add(0, a.duplicate());
		}
		
		players[0] = other.players[0].duplicate();
		players[1] = other.players[1].duplicate();
		players[2] = other.players[2].duplicate();
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
		if (currentplayer == -1){
			
		} else {
			
		}
		
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
		players[0].printBoard();
		System.out.println("Player 1");
		players[1].printBoard();
		System.out.println("Player 2");
		players[2].printBoard();
	}
}
