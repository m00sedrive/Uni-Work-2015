package application;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import database.Database;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class CustomPCA {

	private int imgSetSize;
	private List<List<Double>> faceMatrix;
	private Database database;
	private double[] dataAverageOfEachRow = null;
	private double[][] faceMatrixMinusAverages = null;
	private double[] eigenValues = null;
	private double[][] eigenVectors = null;

	public void setPCAData(int imgSetSize, Database database) {
		// set up database
		this.database = database;
		this.database.setUpDatabase();
		// get image dimensions
		this.imgSetSize = imgSetSize;
		// imageSet = new Matrix[imgSetSize];
	}

	public void prepareFaceMatrix() {
		double[][] image = null;
		faceMatrix = new ArrayList<List<Double>>(imgSetSize);
		List<Double> imageRow = null;

		// read values from each row into a column in face matrix
		for (int i = 0; i < imgSetSize; i++) {
			image = buffImg2array(database.getPerson(i).getImage());
			// normalize image data
			normalizeImageData(image);
			imageRow = new ArrayList<Double>(image.length * image.length);
			for (int x = 0; x < image.length; x++) {
				for (int y = 0; y < image[x].length; y++) {
					// imageRow[x + (y * image[x].length)] = image[x][y];
					imageRow.add(image[x][y]);
				}
			}
			faceMatrix.add(imageRow);
		}
	}
	
	public void performPCA() {

		// Convert face data from List --> array
		double[][] faceMatrix_array = new double[faceMatrix.size()][faceMatrix.get(0).size()];
		for (int i = 0; i < faceMatrix_array.length; ++i) {
			for (int j = 0; j < faceMatrix_array[i].length; ++j) {
				faceMatrix_array[i][j] = faceMatrix.get(i).get(j).doubleValue();
			}
		}
		// calculate averages and coefficents
		calculateAverageAndCoevariance(faceMatrix_array);
		// calculate eigen values and vectors
		calculateEigenValuesAndVectors();

		
		// debug
		System.out.println("Eigen Values: ");
		for(int i=0; i<eigenValues.length; i++)
			System.out.print(eigenValues[i] + ", ");
		System.out.println();
		System.out.println("Eigen Vectors: ");
		print2DArrayMatrix(eigenVectors);
		System.out.println();	
		
		
		// calculate principal components
		calculatePrincipalComponents();
		
		// debug
		System.out.println("Principal Components: ");
		for(int i=0; i<eigenValues.length; i++)
			System.out.print(eigenValues[i] + ", ");

		// Convert face data from Array --> List
		faceMatrix = new ArrayList<List<Double>>(faceMatrix_array.length);
		for (int i = 0; i < faceMatrix_array.length; ++i) {
			List<Double> face = new ArrayList<Double>(faceMatrix_array[i].length);
			for (int j = 0; j < faceMatrix_array[i].length; ++j) {
				face.add(faceMatrix_array[i][j]);
			}
			faceMatrix.add(face);
		}
	}

	private void calculateAverageAndCoevariance(double[][] faceMatrix) {
		int columnLength = faceMatrix[0].length;
		int rowLength = faceMatrix.length;
		dataAverageOfEachRow = new double[columnLength];
		for (int i = 0; i < columnLength; ++i) {
			double sum = 0;
			for (int counter = 0; counter < rowLength; ++counter) {
				sum += faceMatrix[counter][i];
			}
			dataAverageOfEachRow[i] = sum / rowLength;
		}
		faceMatrixMinusAverages = new double[rowLength][columnLength];
		for (int i = 0; i < rowLength; ++i) {
			faceMatrixMinusAverages[i] = matrixSubtract(faceMatrix[i], dataAverageOfEachRow);
		}
	}

	private void calculateEigenValuesAndVectors() {

		// Log.append("Computing covariance matrix...");
		// Compute covariance matrix
		RealMatrix matrix = new Covariance(new BlockRealMatrix(faceMatrixMinusAverages).transpose())
				.getCovarianceMatrix();
		// Log.append("Computing eigen decomposition...");
		// Get the eigenvalues and eigenvectors
		EigenDecomposition eigen = new EigenDecomposition(matrix);
		eigenValues = eigen.getRealEigenvalues();
		// Transpose so that eigenvalues are in vectors/columns
		eigenVectors = eigen.getV().transpose().getData();
	}
	
	private void calculatePrincipalComponents() {
		int numOfComponents = eigenVectors.length;
		// Get principle components
		ArrayList<PrincipalComponent> principalComponents = new ArrayList<PrincipalComponent>();
		for(int i=0; i<numOfComponents; i++) {
			double[] eigenVector = new double[numOfComponents];
			for(int j=0;j< numOfComponents;j++) {
				eigenVector[j] = eigenVectors[i][j];
			}
			principalComponents.add(new PrincipalComponent(eigenValues[i], eigenVector));
		}
		// sort components
		Collections.sort(principalComponents);
		Iterator<PrincipalComponent> iterator = principalComponents.iterator();
		int count = 0;
		double[][] tempVectors = new double[3][eigenVectors.length];
		double[] tempValues = new double[3];
		while(iterator.hasNext()) {
			PrincipalComponent pc = iterator.next();
			if(count < 3) {
				tempVectors[count] = pc.eigenVector;
				tempValues[count] = pc.eigenValue;
			}
			else {
				eigenVectors[count - 3] = pc.eigenVector;
				eigenValues[count - 3] = pc.eigenValue;
			}
			count++;
		}
	}

	private double[][] normalizeImageData(double[][] image) {
		// Normalise data between 0 and 1
		for (int faceInd = 0; faceInd < image.length; ++faceInd) {
			double[] data2 = image[faceInd];
			double min = getMinValue(data2);
			double max = getMaxValue(data2);
			for (int j = 0; j < data2.length; ++j)
				data2[j] = 0 + (1 - 0) * (((data2[j]) - min) / (max - min));
		}
		return image;
	}

	protected double getMinValue(double[] data) {
		double minVal = Double.MAX_VALUE;
		for (int i = 0; i < data.length; ++i) {
			minVal = Math.min(minVal, data[i]);
		}
		return minVal;
	}

	protected double getMaxValue(double[] data) {
		double maxVal = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < data.length; ++i)
			maxVal = Math.max(maxVal, data[i]);
		return maxVal;
	}

	private void print2DArrayMatrix(double[][] matrix_array) {
		for (int i = 0; i < matrix_array.length; i++) {
			for (int j = 0; j < matrix_array[i].length; j++) {
				System.out.print(matrix_array[i][j] + " | ");
			}
			System.out.println();
		}
	}

	protected double[] matrixSubtract(double[] matrix1, double[] matrix2) {
		double[] result = new double[matrix1.length];
		for (int i = 0; i < result.length; ++i)
			result[i] = matrix1[i] - matrix2[i];
		return result;
	}

	private double[][] buffImg2array(BufferedImage bi) {
		int cols = bi.getWidth();
		int rows = bi.getHeight();
		double returnArray[][] = new double[cols][rows];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				returnArray[i][j] = bi.getRGB(i, j);
			}
		}
		return returnArray;
	}

}