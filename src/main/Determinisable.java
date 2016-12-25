package main;

public interface Determinisable extends Board {

	/**
	 * Make a copy of the board, but any hidden
	 * information is randomized upon calling this
	 * such that the new copy is an exact copy of
	 * the original sans any information hidden
	 * to the AI.
	 * 
	 * @return
	 */
	public Determinisable determinize();
	
}
