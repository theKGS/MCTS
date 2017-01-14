package agricola;

import java.util.LinkedList;

public class LandGrid {
	private boolean [][] stable = new boolean[5][3];
	private boolean [][] pasture = new boolean[5][3];
	private boolean [][] room = new boolean[5][3];
	private RL roomlevel = RL.Wood;
	
	private boolean [][] horizontal = new boolean[4][5];
	private boolean [][] vertical = new boolean[6][3];
	
	public LandGrid(){
	}
	
	public boolean checkAbove(int x, int y){
		return horizontal[x][y];
	}
	
	public boolean checkBelow(int x, int y){
		return horizontal[x][y + 1];
	}	
	
	public boolean checkLeft(int x, int y){
		return vertical[x][y];
	}
	
	public boolean checkRight(int x, int y){
		return vertical[x + 1][y];
	}	
	
	LinkedList<AgricolaMove> getMoves(){
		// TODO: Fix
		return null;
	}
}
