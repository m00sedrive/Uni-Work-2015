package uk.ac.reading.tom.xw009807;

import java.util.HashMap;

import uk.ac.reading.tom.xw009807.PhenotypeState.ACTION;

public class PhenotypeStateMap {

	private static final HashMap <Integer, ACTION> stateMap;
	private static final HashMap <ACTION, Integer> MapState; 

	static {
		stateMap = new HashMap <Integer, ACTION>();
		MapState = new HashMap <ACTION, Integer>();
	} 
}
