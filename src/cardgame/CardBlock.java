package cardgame;

import java.util.ArrayList;
import java.util.Collections;

public class CardBlock {
	public ArrayList<Card> cards;
	public boolean israndom;
	public int size;
	
	public CardBlock(boolean det){
		cards = new ArrayList<Card>();
		size = 0;
		israndom = det;
	}
	
	/**
	 * Copy constructor
	 * @param m
	 */
	public CardBlock(CardBlock m) {
		israndom = m.israndom;
		size = m.size;
		cards = new ArrayList<Card>();
		
		for (Card c : m.cards){
			cards.add(new Card(c));
		}
	}

	public void insert(Card c){
		cards.add(c);		
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
}
