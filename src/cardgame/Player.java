package cardgame;

public class Player {
	int health;
	int maxhealth;
	
	private HeroPower heropower;
	private Hand hand;
	
	public Player(){
		health = 30;
		maxhealth = health;
	}
	
}
