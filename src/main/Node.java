package main;

import java.util.ArrayList;
import java.util.HashMap;

import testgame1.TestGame1Move;

public class Node {
	public double[] score;
	public double games;
	public Move move;
	public ArrayList<Node> unvisitedChildren;
	public ArrayList<Node> children;
	public Node parent;
	public int player;
	public double[] pess;
	public double[] opti;
	public boolean pruned;

	public Node(ArrayList<Node> rootNodes) {
		HashMap<Node, ArrayList<Node>> map = new HashMap<Node, ArrayList<Node>>();

		for (Node n : rootNodes) {
			for (Node child : n.children) {
				if (map.containsKey(child)) {
					map.get(child).add(child);
				} else {
					ArrayList<Node> nlist = new ArrayList<Node>();
					nlist.add(child);
					map.put(child, nlist);
				}
			}
		}
	
		children = new ArrayList<Node>();
		for (Node n : map.keySet()){
			children.add(new Node(map.get(n), false));
		}
	}	
	
	private Node(ArrayList<Node> nodes, boolean d){
		move = nodes.get(0).move;
		score = new double[nodes.get(0).score.length];
		for (Node n : nodes){
			games += n.games;
			for (int i = 0; i < score.length; i++)
				score[i] += n.score[i];
		}
	}
	
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
		move = m;
		Board tempBoard = b.duplicate();
		tempBoard.makeMove(m);
		player = tempBoard.getCurrentPlayer();
		score = new double[b.getQuantityOfPlayers()];
		pess = new double[b.getQuantityOfPlayers()];
		opti = new double[b.getQuantityOfPlayers()];
		for (int i = 0; i < b.getQuantityOfPlayers(); i++)
			opti[i] = 1;
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
		return score[parent.player] / games + c * Math.sqrt(Math.log(parent.games + 1) / games);
	}

	/**
	 * Update the tree with the new score.
	 * 
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
	 * Expand this node by populating its list of unvisited child nodes.
	 * 
	 * @param currentBoard
	 */
	public void expandNode(Board currentBoard) {
		ArrayList<Move> legalMoves = currentBoard.getMoves(CallLocation.treePolicy);
		unvisitedChildren = new ArrayList<Node>();
		for (int i = 0; i < legalMoves.size(); i++) {
			Node tempState = new Node(currentBoard, legalMoves.get(i), this);
			unvisitedChildren.add(tempState);
		}
	}

	/**
	 * Set the bounds in the given node and propagate the values back up the
	 * tree.
	 * 
	 * @param optimistic
	 * @param pessimistic
	 */
	public void backPropagateBounds(double[] score) {
		for (int i = 0; i < score.length; i++) {
			opti[i] = score[i];
			pess[i] = score[i];
		}

		if (parent != null)
			parent.backPropagateBoundsHelper();
	}

	private void backPropagateBoundsHelper() {
		for (int i = 0; i < opti.length; i++) {
			if (i == player) {
				opti[i] = Integer.MIN_VALUE;
				pess[i] = Integer.MIN_VALUE;
			} else {
				opti[i] = Integer.MAX_VALUE;
				pess[i] = Integer.MAX_VALUE;
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
					opti[i] = Integer.MAX_VALUE;
				} else {
					pess[i] = Integer.MIN_VALUE;
				}
			}
		}

		pruneBranches();
		if (parent != null)
			parent.backPropagateBoundsHelper();
	}

	private void pruneBranches() {
		for (Node s : children) {
			if (pess[player] >= s.opti[player]) {
				s.pruned = true;
			}
		}

		if (parent != null)
			parent.pruneBranches();
	}

	/**
	 * Select a child node at random and return it.
	 * 
	 * @param board
	 * @return
	 */
	public int randomSelect(Board board) {
		double[] weights = board.getMoveWeights();

		double totalWeight = 0.0d;
		for (int i = 0; i < weights.length; i++) {
			totalWeight += weights[i];
		}

		int randomIndex = -1;
		double random = Math.random() * totalWeight;
		for (int i = 0; i < weights.length; ++i) {
			random -= weights[i];
			if (random <= 0.0d) {
				randomIndex = i;
				break;
			}
		}

		return randomIndex;
	}

	@Override
	public int hashCode() {
		return move.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		Node n = (Node) obj;
		return move.equals(n.move);
	}	
	
	public void print(){
		String s = (move != null) ? Integer.toString(((TestGame1Move)move).tstate) : " ";
		System.out.print("{N(" + player + " " + s + ") ");
		for (Node n: children){
			n.print();
		}
		System.out.print(" }");
	}
}