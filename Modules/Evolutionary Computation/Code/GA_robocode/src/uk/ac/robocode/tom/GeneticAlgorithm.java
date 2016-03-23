package uk.ac.robocode.tom;

import java.util.ArrayList;
import java.util.Collections;

public class GeneticAlgorithm {
	
	protected final int numOfChildren = 100;
	protected final int numOfParents = 10;
	
	private void printArrayList(ArrayList al) {
		for(int i=0; i<al.size();i++) {
			System.out.println(al.get(i));
		}
	}
	
	public ArrayList<String> createChildren(ArrayList<String> parents) {
		
		return new ArrayList();
	}
}
