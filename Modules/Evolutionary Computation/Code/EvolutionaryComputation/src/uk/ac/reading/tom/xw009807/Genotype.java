package uk.ac.reading.tom.xw009807;

public class Genotype implements Cloneable {

	public static int mutationWeight;
	
	public static void run() {
		
		// load file data
		// set chromosome data
		// check if children exist
		// do something
	}
	
	public String getGenoString() {
		StringBuilder genoString = new StringBuilder();
		constructString(this, genoString);
		return genoString.toString();
	}
	
	public void constructString(Genotype gt, StringBuilder sb) {
		
	}
	
}
