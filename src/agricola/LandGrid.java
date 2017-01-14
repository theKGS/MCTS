package agricola;

import java.util.LinkedList;

public class LandGrid {
	private boolean [][] stable = new boolean[5][3];
	private boolean [][] pasture = new boolean[5][3];
	private boolean [][] room = new boolean[5][3];
	private int stablecount;
	private int pasturecount;
	private int roomcount;
	
	private int [][] cowcount = new int[5][3];
	private int [][] pigcount = new int[5][3];
	private int [][] sheepcount = new int[5][3];
	
	private RL roomLevel = RL.Wood;
	
	private boolean [][] horizontal = new boolean[4][5];
	private boolean [][] vertical = new boolean[6][3];

	public LandGrid(){
		stablecount = 0;
		pasturecount = 0;
		roomcount = 2;		
	}
	
	public LandGrid(LandGrid other){
		for (int x = 0; x < 5; x++){
			for (int y = 0; y < 3; y++){
				stable[x][y] = other.stable[x][y];
				pasture[x][y] = other.pasture[x][y];
				room[x][y] = other.room[x][y];
				
				cowcount[x][y] = other.cowcount[x][y];
				pigcount[x][y] = other.pigcount[x][y];
				sheepcount[x][y] = other.sheepcount[x][y];
			}
		}

		stablecount = other.stablecount;
		pasturecount = other.pasturecount;
		roomcount = other.roomcount;		

		roomLevel = other.roomLevel;
		
		for (int x = 0; x < 4; x++){
			for (int y = 0; y < 5; y++){
				horizontal[x][y] = other.horizontal[x][y];
			}
		}

		for (int x = 0; x < 6; x++){
			for (int y = 0; y < 3; y++){
				vertical[x][y] = other.vertical[x][y];
			}
		}
	}
	
	public LandGrid duplicate(){
		return new LandGrid(this);
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
	
	LinkedList<AgricolaMove> getFencePlacementMoves(int wood){
		LinkedList<AgricolaMove> moves = new LinkedList<AgricolaMove>();
				
		return null;
	}
}
