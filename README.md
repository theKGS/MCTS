# MCTS
Java implementation of UCT based MCTS and Flat MCTS

Everything in the main package is related to the algorithm 
itself. The other package is a definition of a game which 
the algorithm can play.

MCTS specifies two important functions runMCTS and 
runFlatMCTS. The former implements the full algorithm with
UCT default policy. The second implements a flat algorithm
that does not build a search tree. Either function returns
a Move instance which is the move the algorithm has
concluded is the best move to make.

Board is an interface that must be implemented if you want
to make the algorithm play your game. Move is another 
interface that also must be implemented. Your implementation
of Board must be able to return a list of all moves
possible at the current state of the game.

Node represents a node in the MCTS tree. This is only used
internally.
