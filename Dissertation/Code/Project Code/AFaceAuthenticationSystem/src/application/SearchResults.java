package application;

public class SearchResults {

	private int results;
	private double distance;
	
	public SearchResults(int result, double distance) {
		this.results = result;
		this.distance = distance;
	}

	public int getResults() {
		return results;
	}

	public double getDistance() {
		return distance;
	}
	
}
