package connectFour;

import java.util.ArrayList;

import main.Move;

public class ConnectFourMove implements Move {
	int player;
	int row;
	
	ConnectFourMove(int player, int row){
		this.player = player;
		this.row = row;
	}
	
	public String toString(){
		StringBuilder strb = new StringBuilder();
		strb.append(row);
		return strb.toString();
	}
}
