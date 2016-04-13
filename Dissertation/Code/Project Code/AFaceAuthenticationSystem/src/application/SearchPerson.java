package application;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class SearchPerson {

	private double threshold;
	private PCACache cache;
	private String personSearch = null;
	
	public SearchPerson(PCACache cache, String personSearch, double threshold) {
		this.threshold = threshold;
		this.cache = cache;
		this.personSearch = personSearch;
	}
	
	public void searchPersonInCache() {
	
		// convert to grey scale and resize image
		
		// linked list for results
		LinkedList<SearchResults> results = new LinkedList<SearchResults>();
		
		// get face image data
		// normalise data
		// convert to matrix
		// subtract average face from input image
		// get weight
		// get distance
		

		
	}
	
	
	
}
