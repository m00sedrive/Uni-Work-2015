package uk.ac.reading.xw009807.robocode;

import java.util.HashMap;

import uk.ac.reading.xw009807.robocode.Token.TOKENS;

public class TokenMap {
	private static final HashMap<Integer, TOKENS> tokenMap;
	private static final HashMap<TOKENS, Integer> mapToken;
	public static final int maxValue;

	static {
		tokenMap = new HashMap<Integer, TOKENS>();
		mapToken = new HashMap<TOKENS, Integer>();
		int i = 0;
		for (TOKENS t : TOKENS.values()) {
			tokenMap.put(i, t);
			mapToken.put(t, i);
			++i;
		}
		maxValue = --i;
	}

	public static TOKENS get(int k) {
		return tokenMap.get(k);
	}

	public static int get(TOKENS t) {
		return mapToken.get(t);
	}
}
