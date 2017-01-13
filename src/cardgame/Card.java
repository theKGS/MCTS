package cardgame;

import java.util.LinkedList;

public class Card {
	private final ACard data;
	private LinkedList<Effect> effects;
	int damage;
	
	public Card(ACard data){
		this.data = data;
		damage = 0;
		effects = new LinkedList<Effect>();
	}
	
	public Card(Card c) {
		data = c.data;
		// TODO: Incomplete
	}

	public int getHealth(){
		return getMaxHealth() - damage;
	}
	
	public int getMaxHealth(){
		int r = data.getHealth();
		for (Effect e : effects){
			r = e.getHealth(r);
		}
		return r;
	}
	
	public int getAttack(){
		int r = data.getAttack();
		for (Effect e : effects){
			r = e.getAttack(r);
		}
		return r;
	}
	
	public boolean isDead(){
		return (getHealth() <= 0);
	}
}
