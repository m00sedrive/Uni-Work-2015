package application;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.imgscalr.Scalr;

import Jama.Matrix;
import database.Database;

public class SearchPerson extends AppTools {

	private double threshold = 250;
	private EigenCache cache = null;
	private BufferedImage searchImage;
	private List<String> sortedResultFilenames;
	private List<String> thresholdResults;

	public SearchPerson(EigenCache cache, BufferedImage inputImage, double threshold) {
		this.cache = cache;
		this.searchImage = inputImage;
	}

	public SearchResults[] searchPersonInCache(Database database) {

		// resize image
		searchImage = Scalr.resize(searchImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, 51, 55, Scalr.OP_ANTIALIAS);

		// list for results
		thresholdResults = new ArrayList<String>();
		List<SearchResults> results = new ArrayList<SearchResults>();
		sortedResultFilenames = new ArrayList<String>();

		// get face image data
		int count = 0;
		double[] pixels = new double[searchImage.getWidth() * searchImage.getHeight()];
		for (int i = 0; i < searchImage.getWidth(); ++i) {
			for (int j = 0; j < searchImage.getHeight(); ++j) {
				pixels[count++] = searchImage.getRGB(i, j);
			}
		}

		// normalise data
		double minValue = getMinValue(pixels);
		double maxValue = getMaxValue(pixels);
		for (int i = 0; i < pixels.length; ++i) {
			pixels[i] = 0 + (1 - 0) * (((pixels[i]) - minValue) / (maxValue - minValue));
		}

		// convert to matrix
		Matrix input = new Matrix(pixels, 1);

		// subtract average face from input matrix
		input = input.minus(new Matrix(cache.getAverageFace(), 1));

		// get weights
		Matrix weights = getWeights(input, cache.getNumOfEigenFaces());
		// get distance
		double[] distances = getDistances(weights);
		double[] minDistance = getMinDistance(distances);
		
		// sort and display results
		System.out.println("---- Printing Results:");
		Map<Double, Integer> sorting = new TreeMap<Double, Integer>();
		for (int i = 0; i < distances.length; ++i) {
			sorting.put(distances[i], i);
		}
		for(Map.Entry<Double, Integer> entry : sorting.entrySet()) {
			database.Person person = database.getPerson(entry.getValue().intValue());
			System.out.println("[" + entry.getKey().toString() + "] = " + person.getPersonName());
			sortedResultFilenames.add(person.getPersonName());
			String line = "Threshold/Distance: [" + entry.getKey().toString() + "] = " + person.getPersonName() + "\n";
			thresholdResults.add(line);
		}
		
		// check results are within set threshold
		int result = -1;
		double distance = 0;
		if(minDistance[0] <= threshold) {
			result = (int) minDistance[1];
			distance = minDistance[0];
		}
		else {
			result = -1;
		}
		// add results to list of results
		results.add(new SearchResults(result, distance));
		
		return results.toArray(new SearchResults[results.size()]);
	}

	private Matrix getWeights(Matrix matrix, int numOfEigenFaces) {
		Matrix eigenFaces = new Matrix(cache.getEigenFaces());
		Matrix selectedEigenFaces = eigenFaces.getMatrix(0,
				eigenFaces.getRowDimension() <= numOfEigenFaces ? eigenFaces.getRowDimension() - 1 : numOfEigenFaces, 0,
				eigenFaces.getColumnDimension() - 1);
		return matrix.times(selectedEigenFaces.transpose());
	}
	
	private double[] getMinDistance(double[] distances) {
		
		double minDistance = Double.MAX_VALUE;
		int distanceComponentNumber = 0;
		for(int i=0;i<distances.length;++i) {
			if(distances[i] < minDistance) {
				minDistance = distances[i];
				distanceComponentNumber = i;
			}
		}
		return new double[] {minDistance, distanceComponentNumber};
	}

	private double[] getDistances(Matrix inputWeights) {
		double[] weightsData = inputWeights.getArray()[0];
		double[][] tempWeights = subtractFromEachRow(cache.getEigenWeights(), weightsData);
		tempWeights = squareNonMatrix(tempWeights);
		double[] distances = new double[tempWeights.length];
		for (int i = 0; i < tempWeights.length; ++i) {
			double total = 0;
			for(int j=0;j<tempWeights[i].length ;++j) {
				total += tempWeights[i][j];
			}
			distances[i] = total;
		}
		return distances;
	}
	
	public List<String> getListOfFileNums() {
		return sortedResultFilenames;
	}
	
	public List<String> getThresholdResults() {
		return thresholdResults;
	}
}
