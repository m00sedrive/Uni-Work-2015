package uk.ac.reading.tom.xw009807;

public class PhenotypeState implements Cloneable{

	private ACTION action;
	
	public enum ACTION {
		// Turn Left
		TURN_LEFT,
		// Turn Right
		TURN_RIGHT,
		// Move Left
		MOVE_LEFT,
		// Move Right
		MOVE_RIGHT,
		// Move Forward
		MOVE_FORWARD,
		// Move Backward
		MOVE_BACKWARD,
		// Fire Gun
		FIRE_GUN,	
	}
	
	public PhenotypeState(ACTION action) {
		this.action = action;
	}
	
	public PhenotypeState clone(){
		return new PhenotypeState(this.action);
	}

	public ACTION getAction() {
		return action;
	}

	public void setAction(ACTION action) {
		this.action = action;
	}
	
	public String stateToString() {
		return "{" + action.toString() + "," + "}";
	}
	
}