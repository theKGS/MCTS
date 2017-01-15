package agricola;

/**
 * Represents a possible non-action of converting one
 * resource to another, as configured upon creation.
 * 
 * Does not store state and as such does not need cloning.
 *  
 * @author Ganryu
 *
 */
public class Conversion {
	private Resource r1;
	private Resource r2;
	private int q1;
	private int q2;
	
	public Conversion(Resource from, int q1, Resource to, int q2){
		this.q1 = q1;
		this.q2 = q2;
		this.r1 = from;
		this.r2 = to;		
	}
	
	public boolean isPossible(Player p){
		return false; //(p.countResource(!=))
	}
	
	public void Convert(Player p){
		
	}
	
	
}
