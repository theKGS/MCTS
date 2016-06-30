package main;

import java.util.ArrayList;

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
		return score[parent.player] / games + c
				* Math.sqrt(Math.log(parent.games + 1) / games);
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
	 * Expand this node by populating its list of
	 * unvisited child nodes.
	 * @param currentBoard
	 */
	public void expandNode(Board currentBoard){
		ArrayList<Move> legalMoves = currentBoard.getMoves();
		unvisitedChildren = new ArrayList<Node>();
		for (int i = 0; i < legalMoves.size(); i++) {
			Node tempState = new Node(currentBoard, legalMoves.get(i), this);
			unvisitedChildren.add(tempState);
		}
	}

	/**
	 * Produce a list of viable nodes to visit. The actual 
	 * selection is done in runMCTS
	 * @param optimisticBias
	 * @param pessimisticBias
	 * @param explorationConstant
	 * @return
	 */
	public ArrayList<Node> select(double optimisticBias, double pessimisticBias, double explorationConstant){
		double bestValue = Double.NEGATIVE_INFINITY;
		double tempBest;
		ArrayList<Node> bestNodes = new ArrayList<Node>();
		for (Node s : children) {
			// Pruned is only ever true if a branch has been pruned 
			// from the tree and that can only happen if bounds 
			// propagation mode is enabled.
			if (s.pruned == false) {
				tempBest = s.upperConfidenceBound(explorationConstant)
						+ optimisticBias * s.opti[player]
						+ pessimisticBias * s.pess[player];

				// If we found a better node
				if (tempBest > bestValue) {
					bestNodes.clear();
					bestNodes.add(s);
					bestValue = tempBest;
				} else if (tempBest == bestValue) {
					// If we found an equal node
					bestNodes.add(s);
				}
			}
		}
		
		return bestNodes;
	}
	
	/**
	 * Set the bounds in the given node and propagate the values back up the tree.
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

		// TODO: This causes redundant pruning. Fix it
		pruneBranches();
		if (parent != null)
			parent.backPropagateBoundsHelper();
	}

	public void pruneBranches() {
		for (Node s : children) {
			if (pess[player] >= s.opti[player]) {
				s.pruned = true;
			}
		}

		if (parent != null)
			parent.pruneBranches();
	}

	public Node randomSelect(Board board) {
		double []hello = board.getMoveWeights();
		
		
		
		return null;
	}
}