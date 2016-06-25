# MCTS
Java implementation of UCT based MCTS and Flat MCTS

Everything in the main package is related to the algorithm 
itself. The other package is a definition of a game which 
the algorithm can play.

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

===Pending Changes===
  * Support for stochastic moves will be added