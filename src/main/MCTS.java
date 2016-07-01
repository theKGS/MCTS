package main;

import java.util.ArrayList;
import java.util.Random;

public class MCTS {
	private Random random;
	private Node rootNode;
	private double explorationConstant = Math.sqrt(2.0);
	private double pessimisticBias;
	private double optimisticBias;

	private boolean scoreBounds;
	private boolean trackTime; // display thinking time used
	
	private final int cores = Runtime.getRuntime().availableProcessors();

	public MCTS() {
		random = new Random();
	}

	/**
	 * Run a UCT-MCTS simulation for a number of iterations.
	 * 
	 * @param s starting board
	 * @param runs how many iterations to think
	 * @param bounds enable or disable score bounds.
	 * @return
	 */
	public Move runMCTS(Board s, int runs, boolean bounds) {
		scoreBounds = bounds;
		rootNode = new Node(s);

		long startTime = System.nanoTime();
		
		boolean[] finishedcores = new boolean[cores];
		final int runs_f = runs;
		final Board s_f = s.duplicate();
		synchronized(MCTS.this){
			for (int c = 0; c < cores; c++) {
				final int c_f = c;
				Thread t = new Thread(()-> {
					for (int i = 0; i < runs_f/cores; i++) {
						select(s_f.duplicate(), rootNode);
					}
					finishedcores[c_f]=true;
					synchronized(MCTS.this){
						notify();
					}
				});
				t.setName("Select thread "+c);
				t.setDaemon(true);
				t.start();
			}
			boolean finished = false;
			while(finished == false) {
				if (finished == false) {
					try {wait(400);} catch (InterruptedException e) {e.printStackTrace();}
				}
				finished = true;
				for (boolean core : finishedcores) {
					if (core == false) {
						finished = false;
						break;
					}
				}
			}
		}

		long endTime = System.nanoTime();

		if (this.trackTime)
			Log.log("Thinking time per move in milliseconds: "
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
	 * @param brd
	 * 			  Board state to work from.
	 */
	private synchronized void select(Board brd, Node node) {
		Node currentNode = node;
		Board currentBoard = brd;
		
		while (true) {
			// Break procedure if end of tree
			if (currentBoard.gameOver()) {
				currentNode.backPropagateScore(currentBoard.getScore());
				if (scoreBounds) {
					// This runs only if bounds propagation is enabled.
					// It propagates bounds from solved nodes and prunes
					// branches from the when needed.
					currentNode.backPropagateBounds(currentBoard.optimisticBounds(),
							currentBoard.pessimisticBounds());
				}
				return;
			}

			// We have not visited this node hence the list is null
			if (currentNode.unvisitedChildren == null) {
				currentNode.expandNode(currentBoard);
			}

			// If player ID is 0 or positive it means this is a normal node
			// A negative ID means the node is a random node
			if (currentNode.player >= 0){
				// This node has unexplored children
				if (!currentNode.unvisitedChildren.isEmpty()) {
					// it picks a move at random from list of unvisited children
					Node temp = currentNode.unvisitedChildren.remove(random.nextInt(currentNode.unvisitedChildren.size()));
					currentNode.children.add(temp);
					playout(temp, brd);
					return;
				} else {
					// This node had no unexplored children
					// hence we can proceed down to the next node
					ArrayList<Node> bestNodes = currentNode.select(optimisticBias, pessimisticBias, explorationConstant);
					
					// This only occurs if all branches have been 
					// pruned from the tree 
					if (currentNode == rootNode && !bestNodes.isEmpty())
						return;
					
					Node finalNode = bestNodes.get(random.nextInt(bestNodes.size()));
					currentNode = finalNode;
					currentBoard.makeMove(finalNode.move);
				}
			} else {
				// We're in a random node, so pick a child at random
				Node temp = currentNode.randomSelect(currentBoard);
				currentNode = temp;
				currentBoard.makeMove(temp.move);
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
			//tempBest += s.opti[n.player] * optimisticBias;
			//tempBest += s.pess[n.player] * pessimisticBias;
			// tempBest += 1.0 / Math.sqrt(s.games);
			//tempBest = Math.min(tempBest, s.opti[n.player]);
			//tempBest = Math.max(tempBest, s.pess[n.player]);
			if (tempBest > bestValue) {
				bestNodes.clear();
				bestNodes.add(s);
				bestValue = tempBest;
			} else if (tempBest == bestValue) {
				bestNodes.add(s);
			}
		}

		Node finalNode = bestNodes.get(random.nextInt(bestNodes.size()));
		
		Log.log("Highest value: " + bestValue + ", O/P Bounds: "
				+ finalNode.opti[n.player] + ", " + finalNode.pess[n.player]);
		return finalNode.move;
	}

	/**
	 * Playout function for MCTS
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
			if (brd.getCurrentPlayer() >= 0) {
				// make random selection normally
				mv = moves.get(random.nextInt(moves.size()));
			} else {
				/*
				 * This situation only occurs when a move
				 * is entirely random, for example a die
				 * roll. We must consider the random weights
				 * of the moves. 
				 */
				double []weights = board.getMoveWeights();
				
				double totalWeight = 0.0d;
				for (int i = 0; i < weights.length; i++)
				{
				    totalWeight += weights[i];
				}
				
				int randomIndex = -1;
				double random = Math.random() * totalWeight;
				for (int i = 0; i < weights.length; ++i)
				{
				    random -= weights[i];
				    if (random <= 0.0d)
				    {
				        randomIndex = i;
				        break;
				    }
				}
				
				mv = moves.get(randomIndex);
			}
									
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

	/**
	 * This is multiplied by the pessimistic bounds of any
	 * considered move during selection.	 
	 * @param b
	 */
	public void setPessimisticBias(double b) {
		pessimisticBias = b;
	}

	/**
	 * This is multiplied by the optimistic bounds of any
	 * considered move during selection.
	 * @param b
	 */
	public void setOptimisticBias(double b) {
		optimisticBias = b;
	}

	public void setTimeDisplay(boolean displayTime) {
		this.trackTime = displayTime;
	}
}
