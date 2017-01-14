package agricola;

/**
 * This is a computer implementation of the board game Agricolam by Uwe Rosenberg, 
 * for testing my MCTS implementation. The original game is fantastic and you should 
 * go buy it right now.
 * 
 * This implementation has no support for human players. The intent is to observe
 * how the AI performs.
 * 
 * To avoid dealing with hidden information we use a house rule that all cards
 * are played openly. All players are aware of all cards owned by all players.
 *
 * Players are not aware of the facedown action space cards.
 * 
 * @author Ganryu
 *
 */
public class AgricolaMain {

	public static void main(String[] args) {
		ABoard game = new ABoard();
		game.printLandGrid();
		
	}

}
