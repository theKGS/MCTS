# MCTS
Java implementation of UCT based MCTS.

## Main Algorithm
Everything in the main package is related to the algorithm 
itself. The other package (connectFour) is an implementation
of a game which the algorithm can play. Use it for reference.

MCTS specifies the method runMCTS which implements the 
full algorithm with UCT default policy. After thinking the
specified number of iterations it will return a move.

This move is what the algorithm concluded was the best move
to make at this point in time. The more iterations you
let it think, the better a movie will be returned.

Board is an interface that must be implemented if you want
to make the algorithm play your game. Move is another 
interface that also must be implemented. Your implementation
of Board must be able to return a list of all moves
possible at the current state of the game.

Node represents a node in the MCTS tree. This is only used
internally.

## Score Bounds
You can enable score bounds by passing a 'true' to
the runMCTS function as the third parameter. When score
bounds is enabled, the algorithm will, when backtracking,
also propagate an upper and lower score bound.

These represent the potentially highest and potentially
lowest possible scores for the given player from this
point on. For example: A node with a lower score bound of
1.0 for player 1 means that from that state and onwards down 
the tree no possible move by any player can lead to a worse
state for player 1. Player 1 is guaranteed to win.

Conversely an upper bound of 0.0 implies there is no way
for that player to win.

Bounds are propagated back up the tree such that the
upper bound for the active player in any node is always
the maximum upper bound for that player in any child node.

The lower bound for the active player in any node is always
the maximum lower bound for that player in any child node.

The rules also propagate the bounds for the opponents. The
upper and lower bound for an opponent is always the lowest
upper and lower bounds for any node respectively.

## Stochastic Games
Support for randomness is pending. It will likely be part
of the next update.