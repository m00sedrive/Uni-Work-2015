package uk.ac.reading.tom.xw009807;

import java.util.HashMap;

import uk.ac.reading.tom.xw009807.GenoKey.KEYS;

public class GenoMap {

	private static final HashMap <Integer, KEYS> keyMap;
	private static final HashMap <KEYS, Integer> mapKey;
	public static final int maxValue;

	static {
		keyMap = new HashMap <Integer, KEYS>();
		mapKey = new HashMap <KEYS, Integer>();
		int counter = 0;
		for(KEYS key: KEYS.values()) {
			
			keyMap.put(counter, key);
			mapKey.put(key, counter);
			counter++;
		}
		maxValue = --counter;
	} 
	
	public static KEYS get(int k) {
		return keyMap.get(k);
	}
	
	public static int get(KEYS key) {
		return mapKey.get(key);
	}
}