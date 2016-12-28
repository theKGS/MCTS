package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

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

		for (CardBlock b : n.bundles) {
			if (b.canBeDeterminised) {
				for (Card c : b.cards) {
					ncards.add(c);
				}
			}
		}

		Collections.shuffle(ncards);
		for (CardBlock b : n.bundles) {
			if (b.canBeDeterminised) {
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

}