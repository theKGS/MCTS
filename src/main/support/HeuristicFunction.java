package main.support;

import main.Board;

/**
 * Create a class implementing this interface and instantiate
 * it. Pass the instance to the MCTS instance using the
 * {@link #setHeuristicFunction(HeuristicFunction h) setHeuristicFunction} method.

 * @author Ganryu
 *
 */
public interface HeuristicFunction {
	public double h(Board board);
}
