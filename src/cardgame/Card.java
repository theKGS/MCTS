package cardgame;

import java.util.LinkedList;

public class Card {
	private final ACard data;
	private LinkedList<Effect> effects;
	
	public Card(ACard data){
		this.data = data;
		effects = new LinkedList<Effect>();
	}
	
	public Card(Card c) {
		data = c.data;
		// TODO: Incomplete
	}

	public int getHealth(){
		return 0;
	}
	
	public int getAttack(){
		return 0;
	}
	
	public boolean isDead(){
		return (getHealth() <= 0);
	}
}
