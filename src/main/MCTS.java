package main;

import java.util.ArrayList;
import java.util.Random;

import connectFour.ConnectFour;

public class MCTS {
	private Random random;
	private Node rootNode;
	private double explorationConstant = Math.sqrt(2.0);
	private double pessimisticBias;
	private double optimisticBias;
	
	private boolean scoreBounds;
	private boolean trackTime; // display thinking time used
	private int timeLimit; // Set a time limit per move.

	public MCTS() {
		random = new Random();
	}

	public Move runMCTS(Board s, int runs) {
		rootNode = new Node(s);
		
		long startTime = System.nanoTime();

		for (int i = 0; i < runs; i++) {
			select(rootNode);
		}

		long endTime = System.nanoTime();

		if (this.trackTime)
			System.out.println("Thinking time per move in milliseconds: "
					+ (endTime - startTime) / 1000000);

		return pickBest(rootNode);
	}

	private int countNodes(Node root) {
		int sum = 0;
		if (root.children.size() > 0) {
			for (Node s : root.children) {
				sum += countNodes(s);
			}
			return sum;
		} else {
			return 1;
		}
	}

	private void select(Node node) {
		// Break procedure if end of tree
		if (node.board.gameOver()) {
			node.backPropagateScore(node.board.getScore());
			if (scoreBounds) {
				node.backPropagateBounds(node.board.optimisticBounds(), node.board.pessimisticBounds());
			}
			return;
		}

		if (node.unvisitedChildren == null) {
			node.initializeUnexplored();
		}

		if (!node.unvisitedChildren.isEmpty()) {
			// it picks a move at random from list of unvisited children
			Node temp = node.unvisitedChildren.remove(random.nextInt(node.unvisitedChildren.size()));
			//Node temp = node.unvisitedChildren.remove(0);

			node.children.add(temp);
			playout(temp);
		} else {
			double bestValue = Double.NEGATIVE_INFINITY;
			Node bestChild = null;
			double tempBest;
			ArrayList<Node> bestNodes = new ArrayList<Node>();
			
			for (Node s : node.children) {
				// Skip a node if its branch has been pruned
				if (s.pruned == false){
					tempBest = s.upperConfidenceBound(explorationConstant) 
							+ optimisticBias * s.opti[node.player]
							+ pessimisticBias * s.pess[node.player];

					// If we found a better node
					if (tempBest > bestValue) {
						bestNodes.clear();
						bestNodes.add(s);
						bestChild = s;
						bestValue = tempBest;
					} else if (tempBest == bestValue) {
						//If we found an equal node
						bestNodes.add(s);
					}
				}
			}

			// This only occurs when all branches have been pruned from the tree
			if (node == rootNode && bestChild == null)
				return;

			Node finalNode = bestNodes.get(random.nextInt(bestNodes.size()));
			select(finalNode);
		}
	}

	private Move pickBest(Node n) {
		double bestValue = Double.NEGATIVE_INFINITY;
		Node bestChild = null;
		double tempBest;

		//System.out.println("Curr. player: " + n.player);
		for (Node s : n.children) {
			tempBest = s.score[n.player] / (double) s.games;
			tempBest = Math.min(tempBest, s.opti[n.player]);
			tempBest = Math.max(tempBest, s.pess[n.player]);
			//System.out.println("Move: " + s.move.toString() + ", Score: " + tempBest);
			if (tempBest >= bestValue) {
				bestChild = s;
				bestValue = tempBest;
			}
		}

		System.out.println("Highest value: " + bestValue + ", O/P Bounds: " + 
				bestChild.opti[n.player] + ", " + bestChild.pess[n.player]);
		return bestChild.move;
	}

	/**
	 * Playout function for MCTS (non-flat)
	 * 
	 * @param state
	 * @return
	 */
	private void playout(Node state) {
		ArrayList<Move> moves;
		Move mv;
		Board brd = state.board.duplicate();

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
	 * Sets the exploration constant for the algorithm. You will need to find the optimal value
	 * through testing. This can have a big impact on performance. Default value is sqrt(2) 
	 * @param exp
	 */
	public void setExplorationConstant(double exp){
		explorationConstant = exp;
	}
	
	public void setPessimisticBias(double b){
		pessimisticBias = b;
	}

	public void setOptimisticBias(double b){
		optimisticBias = b;
	}
	
	public void setScoreBounds(boolean b){
		scoreBounds = b;
	}
	
	/**
	 * 
	 * @param s
	 */
	public Move runFlatMCTS(Board s, int runs) {
		ArrayList<Move> legalMoves = s.getMoves();
		ArrayList<Board> boards = s.duplicate(legalMoves.size());
		ArrayList<FlatGameState> states = new ArrayList<FlatGameState>();

		// There's only one move left, make that move
		if (legalMoves.size() == 1)
			return legalMoves.get(0);

		for (Board b : boards)
			states.add(new FlatGameState(b,
					legalMoves.remove(legalMoves.size() - 1)));

		for (FlatGameState st : states) {
			st.board.makeMove(st.move);
			// ConnectFour cf = (ConnectFour) st.board;
			for (int i = 0; i < runs; i++) {
				playout(st);
			}
		}

		double ratio = 0;
		Move bestMove = null;

		// System.out.println("AVAILABLE MOVES:");
		for (FlatGameState st : states) {
			// System.out.println("Rating: " + (double)st.wins /
			// (double)st.games + ", Wins: " + st.wins + ", Games: " + st.games
			// + ", Move: " + st.move.toString());
			if (ratio <= (double) st.wins / (double) st.games) {
				ratio = (double) st.wins / (double) st.games;
				bestMove = st.move;
			}
		}

		// System.out.println("Chose move: " + bestMove.toString() +
		// ", With ratio: " + ratio);
		return bestMove;
	}

	/**
	 * 
	 * @param board
	 */
	private void playout(FlatGameState st) {
		ArrayList<Move> moves;
		Move mv;
		Board brd = st.board.duplicate();
		// Start playing random moves until the game is over
		while (true) {
			if (checkWinCondition(st, brd))
				return;
			moves = brd.getMoves();
			mv = moves.get(random.nextInt(moves.size()));
			brd.makeMove(mv);
		}
	}

	private boolean checkWinCondition(FlatGameState state, Board brd) {
		// if AI won the game
		if (brd.checkWinCondition(brd.getCurrentPlayer())) {
			state.wins++;
			state.games++;
			return true;
		}

		// if AI lost the game
		if (brd.checkLossCondition(brd.getCurrentPlayer())) {
			state.games++;
			return true;
		}

		// if draw
		if (brd.checkDraw()) {
			state.games++;
			return true;
		}

		return false;
	}

	public void setTimeDisplay(boolean displayTime) {
		this.trackTime = displayTime;
	}
}
