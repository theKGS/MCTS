package main;

public class BoardNodePair {
	private Board b;
	private Node p;
	
	public BoardNodePair(Board _b, Node _n){
		b = _b;
		p = _n;		
	}
	
	public Board getBoard(){
		return b;
	}
	
	public Node getNode(){
		return p;
	}
	
}
