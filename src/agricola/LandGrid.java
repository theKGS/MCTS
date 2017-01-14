package agricola;

import java.util.LinkedList;

public class LandGrid {
	private boolean[][] stable = new boolean[5][3];
	private boolean[][] pasture = new boolean[5][3];
	private boolean[][] room = new boolean[5][3];
	private boolean[][] field = new boolean[5][3];
	private int stablecount;
	private int roomcount;

	private int[][] cowcount = new int[5][3];
	private int[][] pigcount = new int[5][3];
	private int[][] sheepcount = new int[5][3];

	private RL roomLevel = RL.Wood;

	private boolean[][] horizontal = new boolean[4][5];
	private boolean[][] vertical = new boolean[6][3];

	public LandGrid() {
		room[0][1] = true;
		room[0][2] = true;
		
		stablecount = 0;
		roomcount = 2;
	}

	public LandGrid(LandGrid other) {
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 3; y++) {
				stable[x][y] = other.stable[x][y];
				pasture[x][y] = other.pasture[x][y];
				room[x][y] = other.room[x][y];
				field[x][y] = other.field[x][y];

				cowcount[x][y] = other.cowcount[x][y];
				pigcount[x][y] = other.pigcount[x][y];
				sheepcount[x][y] = other.sheepcount[x][y];
			}
		}

		stablecount = other.stablecount;
		roomcount = other.roomcount;

		roomLevel = other.roomLevel;

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 5; y++) {
				horizontal[x][y] = other.horizontal[x][y];
			}
		}

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 3; y++) {
				vertical[x][y] = other.vertical[x][y];
			}
		}
	}

	public LandGrid duplicate() {
		return new LandGrid(this);
	}

	public boolean checkAbove(int x, int y) {
		return horizontal[x][y];
	}

	public boolean checkBelow(int x, int y) {
		return horizontal[x][y + 1];
	}

	public boolean checkLeft(int x, int y) {
		return vertical[x][y];
	}

	public boolean checkRight(int x, int y) {
		return vertical[x + 1][y];
	}

	public LinkedList<AgricolaMove> getFencePlacementMoves(int wood) {
		LinkedList<AgricolaMove> moves = new LinkedList<AgricolaMove>();

		return null;
	}

	public void printBoard() {
		// - = empty
		// P = pasture
		// F = field
		// B = barn
		// D = barn in field
		// W = wood room
		// C = clay room
		// S = stone room

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 5; x++) {
				if (pasture[x][y]) {
					System.out.print("P");
				} else if (field[x][y]) {
					System.out.print("F");
				} else if (room[x][y]) {
					switch (roomLevel) {
					case Wood:
						System.out.print("W");
						break;

					case Clay:
						System.out.print("C");
						break;

					default:
						System.out.print("S");
						break;
					}
				} else {
					System.out.print("-");
				}
			}
			System.out.println("");
		}
	}

	public void printFences() {

	}
}
