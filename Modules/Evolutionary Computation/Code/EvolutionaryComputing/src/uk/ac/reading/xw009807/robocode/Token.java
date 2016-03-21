package uk.ac.reading.xw009807.robocode;

import uk.ac.reading.pm002501.misc.Random;

public class Token implements Cloneable {

	private TOKENS token;
	private int p;

	public enum TOKENS {
		/** Turn left **/
		M_LEFT,
		/** Turn right **/
		M_RIGHT,
		/** Fire the gun **/
		FIREGUN,
		/** Move forwards (ahead) **/
		FOWARDS,
		/** Move backwards (back) **/
		M_BACK,
		/** Turn turret left **/
		T_LEFT,
		/** Move turret right **/
		T_RIGHT,
		/** Health greater than N **/
		H_GT_N,
		/** Health less than N **/
		H_LT_N
	}

	public Token(TOKENS token, int p) {
		this.token = token;
		this.p = p;
	}

	public Token(boolean canBeCondition) {
		this.token = TokenMap.get(getRandomToken());
		if (!canBeCondition) {
			while (this.token == TOKENS.H_GT_N || this.token == TOKENS.H_LT_N)
				this.token = TokenMap.get(getRandomToken());
		}
		this.p = Random.getRandom(TBedford_Robot.minPower,
				TBedford_Robot.maxPower);
	}

	@Override
	public Token clone() {
		return new Token(token, p);
	}

	private final static int getRandomToken() {
		return (int) (Math.random() * (TokenMap.maxValue + 1));
	}

	public TOKENS getToken() {
		return token;
	}

	public int getAmount() {
		return p;
	}

	@Override
	public String toString() {
		return "{" + token.toString() + ", " + p + "}";
	}
}
