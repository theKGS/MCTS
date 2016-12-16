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

	/**
	 * This is necessary for the multi-threading. Note
	 * that the method need only compare the information
	 * in the move that relates to choices made by the
	 * AI player. Any information in the move that
	 * pertains to random choices etc. etc. can be
	 * freely ignored.
	 */
	@Override
	public int compareTo(Move o) {
		TTFEMove mv = (TTFEMove) o;
		return dir.compareTo(mv.dir);
	}

}
