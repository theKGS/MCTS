package main;

import java.util.ArrayList;

public class Node {
	public double[] score;
	public int games;
	public Move move;
	public ArrayList<Node> unvisitedChildren;
	public ArrayList<Node> children;
	public Node parent;
	public int player;
	public double[] pess;
	public double[] opti;
	public boolean pruned;
	public int depth;

	/**
	 * This creates the root node
	 * 
	 * @param b
	 */
	public Node(Board b) {
		children = new ArrayList<Node>();
		player = b.getCurrentPlayer();
		score = new double[b.getQuantityOfPlayers()];
		pess = new double[b.getQuantityOfPlayers()];
		opti = new double[b.getQuantityOfPlayers()];
		for (int i = 0; i < b.getQuantityOfPlayers(); i++)
			opti[i] = 1;
	}

	/**
	 * This creates non-root nodes
	 * 
	 * @param b
	 * @param m
	 * @param prnt
	 */
	public Node(Board b, Move m, Node prnt) {
		children = new ArrayList<Node>();
		parent = prnt;
		depth = parent.depth + 1;
		move = m;
		player = b.getCurrentPlayer();
		score = new double[b.getQuantityOfPlayers()];
		pess = new double[b.getQuantityOfPlayers()];
		opti = new double[b.getQuantityOfPlayers()];
		for (int i = 0; i < b.getQuantityOfPlayers(); i++)
			opti[i] = 1;
	}

	public void initializeUnexplored(Board b) {
		ArrayList<Move> legalMoves = b.getMoves();
		unvisitedChildren = new ArrayList<Node>();
		for (int i = 0; i < legalMoves.size(); i++) {
			Node tempState = new Node(b, legalMoves.get(i),
					this);
			unvisitedChildren.add(tempState);
		}
	}

	/**
	 * Return the upper confidence bound of this state
	 * 
	 * @param c
	 *            typically sqrt(2). Increase to emphasize exploration. Decrease
	 *            to incr. exploitation
	 * @param t
	 * @return
	 */
	public double upperConfidenceBound(double c) {
		return score[parent.player] / (double) games + c
				* Math.sqrt(Math.log(this.parent.games + 1) / (double) games);
	}

	/**
	 * Update the tree with the new score.
	 * @param scr
	 */
	public void backPropagateScore(double[] scr) {
		this.games++;
		for (int i = 0; i < scr.length; i++)
			this.score[i] += scr[i];

		if (parent != null)
			parent.backPropagateScore(scr);
	}

	/**
	 * Backpropagate the bounds.
	 * 
	 * @param optimistic
	 * @param pessimistic
	 */
	public void backPropagateBounds(double[] optimistic, double[] pessimistic) {
		for (int i = 0; i < optimistic.length; i++) {
			opti[i] = optimistic[i];
			pess[i] = pessimistic[i];
		}

		if (parent != null)
			parent.backPropagateBoundsHelper();
	}

	private void backPropagateBoundsHelper() {
		for (int i = 0; i < opti.length; i++) {
			if (i == player) {
				opti[i] = 0;
				pess[i] = 0;
			} else {
				opti[i] = 1;
				pess[i] = 1;
			}
		}

		for (int i = 0; i < opti.length; i++) {
			for (Node c : children) {
				if (i == player) {
					if (opti[i] < c.opti[i])
						opti[i] = c.opti[i];
					if (pess[i] < c.pess[i])
						pess[i] = c.pess[i];
				} else {
					if (opti[i] > c.opti[i])
						opti[i] = c.opti[i];
					if (pess[i] > c.pess[i])
						pess[i] = c.pess[i];
				}
			}
		}

		// This compares against a dummy node with bounds 1 0
		// if not all children have been explored
		if (!unvisitedChildren.isEmpty()) {
			for (int i = 0; i < opti.length; i++) {
				if (i == player) {
					opti[i] = 1;
				} else {
					pess[i] = 0;
				}
			}
		}

		pruneBranches();
		if (parent != null)
			parent.backPropagateBoundsHelper();
	}

	public void pruneBranches() {
		for (Node s : children) {
			if (pess[player] >= s.opti[player]) {
				s.pruned = true;

				/*
				 * System.out.println("A branch was pruned from the tree!");
				 * System.out.println("Player in parent: " + player);
				 * System.out.println("Player in child: " + s.player);
				 * System.out.println("Depth of parent: " + depth);
				 * System.out.println("O/P for parent:");
				 * System.out.println(opti[0] + " " + opti[1]);
				 * System.out.println(pess[0] + " " + pess[1]);
				 * System.out.println("Children:"); for (Node c : children) { if
				 * (s == c) System.out.println("This one was pruned: ");
				 * System.out.println(c.opti[0] + " " + c.opti[1]);
				 * System.out.println(c.pess[0] + " " + c.pess[1]); }
				 * System.out.println("Parent:"); board.print();
				 * System.out.println("Child:"); s.board.print();
				 */
			}
		}

		if (parent != null)
			parent.pruneBranches();
	}
}