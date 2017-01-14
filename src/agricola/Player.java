package agricola;

import java.util.ArrayList;

import agricola.cards.MajorImprovement;
import agricola.cards.MinorImprovement;
import agricola.cards.Occupation;

public class Player {
	private LandGrid landgrid;
	private ArrayList<Occupation> occupations;
	private ArrayList<MinorImprovement> minorimprovements;
	private ArrayList<MajorImprovement> majorimprovements;
	
	// Note: Game uses house rule. All hands are open.
	private ArrayList<Occupation> occupations_hand;
	private ArrayList<MinorImprovement> minorimprovements_hand;
	
	public Player(){
		occupations = new ArrayList<Occupation>();
		minorimprovements = new ArrayList<MinorImprovement>();
		majorimprovements = new ArrayList<MajorImprovement>();
				
		occupations_hand = new ArrayList<Occupation>();
		minorimprovements_hand = new ArrayList<MinorImprovement>();
		
		landgrid = new LandGrid();
	}
	
	public Player(Player other){
		landgrid = other.landgrid.duplicate();
	}
	
	public Player duplicate(){
		return new Player(this);
	}
	
	public void printBoard(){
		landgrid.printBoard();
	}
}
