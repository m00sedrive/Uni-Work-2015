package uk.ac.reading.tom.xw009807;

public class GenoKey implements Cloneable{

	private KEYS key;
	private int power;
	private int score;
	
	public enum KEYS {
		// Turn Left
		TURN_LEFT,
		// Turn Right
		TURN_RIGHT,
		// Move Forward
		MOVE_FORWARD,
		// Move Backward
		MOVE_BACKWARD,
		// Turn Turret Left
		TURRET_LEFT,
		// Turn Turret Right
		TURRET_RIGHT,
		// Fire Gun
		FIRE_GUN,
		// Health
		HEALTH,
	}
	
	/**
	 * manage condition  
	 * @param bool condition
	 */
	public GenoKey(boolean condition) {
		// set this key with random generated key
		this.key = GenoMap.get(getRandomKey());
		if (key == null) {
			System.out.println(String.format("1: Null for: key: %s",
					key == null ? "null" : key.toString()));
		}
		if(condition) {
			// while condition is true and key = health generate new key
			while(this.key == KEYS.HEALTH) {
				Integer num = getRandomKey();
				this.key = GenoMap.get(num);
				if (key == null) {
					System.out.println(String.format("2: Null for: num: %s, key: %s",
							num == null ? "null" : num.toString(),
							key == null ? "null" : key.toString()));
				}
			}
		}
	}
	
	private final static int getRandomKey() {
		return (int) Math.random() * (GenoMap.maxValue + 1);
	}
	
	public GenoKey(KEYS key, int power) {
		this.key = key;
		this.power = power;
	}
	
	public GenoKey clone(){
		return new GenoKey(key, power);
	}

	public KEYS getKey() {
		return key;
	}

	public void setKey(KEYS key) {
		this.key = key;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public String keyToString() {
		return "{" + key.toString() + "," + "}";
	}
}