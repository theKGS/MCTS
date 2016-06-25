package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MCTS {
	private Random random;
	private Node rootNode;
	private double explorationConstant = Math.sqrt(2.0);
	private double pessimisticBias;
	private double optimisticBias;

	private boolean scoreBounds;
	private boolean trackTime; // display thinking time used
	private int timeLimit; // Set a time limit per move.

	private ArrayList<Node> path;
	
	public MCTS() {
		random = new Random();
	}

	/**
	 * Run a UCT-MCTS simulation for a number of iterations.
	 * 
	 * @param s
	 * @param runs
	 * @return
	 */
	public Move runMCTS(Board s, int runs) {
		rootNode = new Node(s);

		long startTime = System.nanoTime();

		for (int i = 0; i < runs; i++) {
			select(s.duplicate(), rootNode);
		}

		long endTime = System.nanoTime();

		if (this.trackTime)
			System.out.println("Thinking time per move in milliseconds: "
					+ (endTime - startTime) / 1000000);

		return finalSelect(rootNode);
	}

	/**
	 * This represents the select stage, or default policy, of the algorithm.
	 * Traverse down to the bottom of the tree using the selection strategy
	 * until you find an unexpanded child node. Expand it. Run a random playout
	 * Backpropagate results of the playout.
	 * 
	 * @param node
	 *            Node from which to start selection
	 */
	private void select(Board brd, Node node) {
		Node currentNode = node;
		Board currentBoard = brd;
		ArrayList<Node> visited = new ArrayList<Node>();
		
		while (true) {
			// Break procedure if end of tree
			if (currentBoard.gameOver()) {
				currentNode.backPropagateScore(currentBoard.getScore());
				//System.out.println("Le whut: " + Arrays.toString(currentBoard.getScore()));
				if (scoreBounds) {
					// This runs only if bounds propagation is enabled.
					// It propagates bounds from solved nodes and prunes
					// branches from the when needed.
					currentNode.backPropagateBounds(currentBoard.optimisticBounds(),
							currentBoard.pessimisticBounds());
				}
				return;
			}

			if (currentNode.unvisitedChildren == null) {
				//currentNode.initializeUnexplored(currentBoard);
				ArrayList<Move> legalMoves = currentBoard.getMoves();
				currentNode.unvisitedChildren = new ArrayList<Node>();
				for (int i = 0; i < legalMoves.size(); i++) {
					Node tempState = new Node(currentBoard, legalMoves.get(i), currentNode);
					currentNode.unvisitedChildren.add(tempState);
				}
			}

			if (!currentNode.unvisitedChildren.isEmpty()) {
				// it picks a move at random from list of unvisited children
				Node temp = currentNode.unvisitedChildren.remove(random.nextInt(currentNode.unvisitedChildren.size()));
				currentNode.children.add(temp);
				playout(temp, brd);
				return;
			} else {
				double bestValue = Double.NEGATIVE_INFINITY;
				Node bestChild = null;
				double tempBest;
				ArrayList<Node> bestNodes = new ArrayList<Node>();

				for (Node s : currentNode.children) {
					// Pruned is only ever true if a branch has been pruned 
					// from the tree and that can only happen if bounds 
					// propagation mode is enabled.
					if (s.pruned == false) {
						tempBest = s.upperConfidenceBound(explorationConstant)
								+ optimisticBias * s.opti[currentNode.player]
								+ pessimisticBias * s.pess[currentNode.player];

						// If we found a better node
						if (tempBest > bestValue) {
							bestNodes.clear();
							bestNodes.add(s);
							bestChild = s;
							bestValue = tempBest;
						} else if (tempBest == bestValue) {
							// If we found an equal node
							bestNodes.add(s);
						}
					}
				}

				// This only occurs when all branches have been pruned from the
				// tree
				if (currentNode == rootNode && bestChild == null)
					return;

				Node finalNode = bestNodes.get(random.nextInt(bestNodes.size()));
				currentNode = finalNode;
				currentBoard.makeMove(finalNode.move);
			}
		}
	}

	/**
	 * This is the final step of the algorithm, to pick the best move to
	 * actually make.
	 * 
	 * @param n
	 *            this is the node whose children are considered
	 * @return the best Move the algorithm can find
	 */
	private Move finalSelect(Node n) {
		double bestValue = Double.NEGATIVE_INFINITY;
		double tempBest;
		ArrayList<Node> bestNodes = new ArrayList<Node>();

		for (Node s : n.children) {
			tempBest = s.games;
			// tempBest += 1.0 / Math.sqrt(s.games);
			// tempBest = Math.min(tempBest, s.opti[n.player]);
			// tempBest = Math.max(tempBest, s.pess[n.player]);
			if (tempBest > bestValue) {
				bestNodes.clear();
				bestNodes.add(s);
				bestValue = tempBest;
			} else if (tempBest == bestValue) {
				bestNodes.add(s);
			}
		}

		Node finalNode = bestNodes.get(random.nextInt(bestNodes.size()));
		
		System.out.println("Highest value: " + bestValue + ", O/P Bounds: "
				+ finalNode.opti[n.player] + ", " + finalNode.pess[n.player]);
		return finalNode.move;
	}

	/**
	 * Playout function for MCTS (non-flat)
	 * 
	 * @param state
	 * @return
	 */
	private void playout(Node state, Board board) {
		ArrayList<Move> moves;
		Move mv;
		Board brd = board.duplicate();

		// Start playing random moves until the game is over
		while (true) {
			if (brd.gameOver()) {
				state.backPropagateScore(brd.getScore());
				return;
			}

			moves = brd.getMoves();
			mv = moves.get(random.nextInt(moves.size()));
			brd.makeMove(mv);
		}
	}

	/**
	 * Sets the exploration constant for the algorithm. You will need to find
	 * the optimal value through testing. This can have a big impact on
	 * performance. Default value is sqrt(2)
	 * 
	 * @param exp
	 */
	public void setExplorationConstant(double exp) {
		explorationConstant = exp;
	}

	public void setPessimisticBias(double b) {
		pessimisticBias = b;
	}

	public void setOptimisticBias(double b) {
		optimisticBias = b;
	}

	public void setScoreBounds(boolean b) {
		scoreBounds = b;
	}

	public void setTimeDisplay(boolean displayTime) {
		this.trackTime = displayTime;
	}
}
