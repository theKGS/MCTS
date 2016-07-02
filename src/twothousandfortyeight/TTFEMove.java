package twothousandfortyeight;

import main.Move;

public class TTFEMove implements Move {
	int x;
	int y;
	Direction dir = Direction.Up;
	int val;
	
	public TTFEMove(int _x, int _y, int _val) {
		x = _x;
		y = _y;
		val = _val;
	}
	
	public TTFEMove(Direction d) {
		dir = d;
	}
}
