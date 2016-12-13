# Monte Carlo Tree Search
Java implementation of UCT based MCTS.

## Main Algorithm
Everything in the main package is related to the algorithm 
itself. The other packages (connectFour, twothousandfortyeight) 
are implementations of games which the algorithm can play. 
Use them for reference. 

MCTS specifies the method runMCTS which implements the 
full algorithm with UCT default policy. After thinking for
the specified number of iterations it will do a final selection
where it decides, ultimately, which move to make.

This move is what the algorithm concluded was the best move
to make at this point in time. The more iterations you
let it think, the better a movie will be returned.

Board is an interface that must be implemented if you want
to make the algorithm play your game. Move is another 
interface that also must be implemented. Your implementation
of Board must be able to return a list of all moves
possible at the current state of the game.

## Score Bounds
You can enable score bounds by passing a 'true' to
the runMCTS function as the third parameter. When score
bounds is enabled, the algorithm will, when backtracking,
also propagate an upper and lower score bound which is
used to cut down the size of the tree near end states.

Clarifying some terms here: The active player for a given
node is the player who is to make a decision at that point. 
The opponent for a given node is a player who is not making
a decision.

These represent the potentially highest and potentially
lowest possible scores for the given active player from this
point on. For example: A node with a lower score bound of
1.0 for player 1 means that from that state and onwards down 
the tree no possible move by any opponent can lead to a worse
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

Stochastic nodes are not yet implemented for score bounds,
but when that is done they will propagate, for each player,
the highest upper bound among all child nodes and the lowest 
lower bound among all child nodes for that player.

## Multi-threading by Root Parallelisation
Enable this by calling the enableRootParallelisation() method of 
the MCTS instance. When this is enabled it is absolutely 
vital that any class you create implementing the Move 
interface implements a correct hashCode() and equals() method.

More options will be available for controlling multithreading
in the future. It is very likely that the way it works
internally will also change. As such, the hashCode and equals
requirements might change to the Comparable interface instead.

## Stochastic Games
The new version of the algorithm implements support for
stochastic games. The algorithm treats any random behavior
as its own node and each child node is a potential random
event. Moves from a stochastic node are always randomly
selected.

The algorithm detects that the current state is random
by examining the player ID of the getCurrentPlayer()
method in the Board interface. When that function returns
-1 it means that the current state is a random state.

MCTS proceeds to selection a child node at random using
the weight vector from getMoveWeights(). Random events 
are not yet compatible with score bounds, but will be.