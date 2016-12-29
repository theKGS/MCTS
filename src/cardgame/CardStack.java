package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * This represents a stack of cards, i.e a deck. The deck is
 * defined as a list of blocks where each block is a set of
 * cards. A block consists of a sequence of cards which is
 * either random or non-random.
 * 
 * All cards in a deck are initially random. Anything that
 * confirms a particular card at some particular position
 * in the deck makes that particular card non-random and thus
 * part of a new block.
 * 
 * A card that allows the player to peek at the top three
 * cards would confirm these three cards and create a block
 * of 3 non-random cards.
 * 
 * A card that that is random will be shuffled when the deck
 * is determinised. Non-random cards will stay in place.
 * 
 * @author Ganryu
 *
 */
public class CardStack {
	ArrayList<CardBlock> bundles;

	/**
	 * Create a copy of this object and randomise it
	 * 
	 * @return
	 */
	CardStack determinise() {
		CardStack n = new CardStack(this);
		LinkedList<Card> ncards = new LinkedList<Card>();

		// Collect all random cards from all bundles
		for (CardBlock b : n.bundles) {
			if (b.israndom) {
				for (Card c : b.cards) {
					ncards.add(c);
				}
			}
		}
		Collections.shuffle(ncards);

		// Insert the shuffled cards back into the bundles
		for (CardBlock b : n.bundles) {
			if (b.israndom) {
				b.cards.clear();
				while (b.cards.size() < b.size)
					b.cards.add(ncards.remove());
			}		
		}
		
		return n;
	}

	/**
	 * Copy constructor with deep copy
	 * 
	 * @param other
	 */
	public CardStack(CardStack other) {
		ArrayList<CardBlock> newbundles = new ArrayList<CardBlock>();
		for (CardBlock m : bundles) {
			newbundles.add(new CardBlock(m));
		}
	}

	/**
	 * Shuffle all the cards in the deck together
	 */
	public void shuffle() {
		CardBlock n = new CardBlock(true);

		for (CardBlock m : bundles) {
			for (Card c : m.cards) {
				n.cards.add(c);
			}
		}

		n.shuffle();
		bundles.clear();
		bundles.add(n);
	}
	
	/**
	 * Draw a card. Get a reference to the top card
	 * @return
	 */
	public Card draw(){
		CardBlock n = bundles.get(0);
		n.size--;
		if (n.size == 0)
			bundles.remove(0);
		
		Card c = n.cards.get(0);  
		n.cards.remove(0);		
		return c;
	}
	
	public LinkedList<Card> peek(int n){
		return null;
	}

}