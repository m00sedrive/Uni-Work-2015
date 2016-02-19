package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import Jama.Matrix;


public class PCA {

	private double[][] imageData;
	private int numOfFaces;
	private int numEigenFaceUsed = 150;
	private double[] eigenValues = null;
	private double[][] eigenVectors = null;
	private double[][] eigenFaces = null;
	private double[] rowDataAverage = null;
	private double[][] imageDataAverage = null;
	private double[][] weights = null;
	private Matrix resultEigenFaces;
	
	public void setPCAData(int numOfFaces) {
		this.numOfFaces = numOfFaces;
	}
	
	public void run() {
		// record system start time
		long currentTime = System.currentTimeMillis();
		System.out.println("System start time: " + currentTime);
		try{
			//set image data
			double[][] imageData  = this.imageData;			
			// calculate weighted difference of eigen data
			calculateAverageAndDifferenceData(imageData);
			// calculate eigen values and vectors
			calculateEigenValues();		
			// compute PCA
			calculatePrincipalComponents();
			// compute average pca
			calculateWeightsOfEigenFaces();
			//get eigenfaces by weight
			
		
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("System finish time: " + currentTime);
	}
	
	private void calculateAverageAndDifferenceData(double[][] imageData) {
		
		// get image dimensions
		int colSize = imageData[0].length;
		int rowSize = imageData.length;
		
		rowDataAverage = new double[colSize];
		for(int i=0;i<colSize;++i) {
			double sum = 0;
			for(int j=0;j<colSize;++j) {
				sum+= imageData[j][i];
			}
			rowDataAverage[i] = sum/rowSize;
		}
		imageDataAverage = new double[rowSize][colSize];
		for(int i=0;i<rowSize;++i) {
			
			// subtract averages from image data
			imageDataAverage[i] = matrixSubtract(imageData[i], rowDataAverage);
		}
	}
	
	private void calculateWeightsOfEigenFaces() {
		
		int pixels = imageDataAverage[0].length;
		int images = imageDataAverage.length;
		int vectors = eigenVectors.length;
		
		//debug
		System.out.println(pixels);
		System.out.println(images);
		System.out.println(vectors);
		
		// calculate and normalise images
		double[][] tempEigenFaces = new double[vectors][pixels];
		for(int i=0; i<vectors; i++) {
			double squaredSum = 0;
			for(int j=0; j<pixels; j++) {
				for(int k=0; k<images; k++) {
					tempEigenFaces[i][j] += imageDataAverage[k][j] * eigenVectors[i][k];
				}
				squaredSum += tempEigenFaces[i][j] * tempEigenFaces[i][j];
			}
			double normalized = Math.sqrt(squaredSum);
			for(int j=0; j<pixels ;j++) {
				tempEigenFaces[i][j] /= normalized;
			}
		}
		// get set amount of eigenFaces
		this.resultEigenFaces = new Matrix(eigenFaces).getMatrix(0, eigenFaces.length <= numEigenFaceUsed ? eigenFaces.length - 1 : numEigenFaceUsed, 0, eigenFaces[0].length - 1);
		
		// calculate weights
		//this.weights = new Matrix(imageDataAverage).times(new Matrix(eigenFaces).transpose().getArray());
		
	}
	
	private void calculateEigenValues() {
		
		// compute covariance matrix
		RealMatrix realMat = new Covariance(new BlockRealMatrix(imageDataAverage).transpose()).getCovarianceMatrix();
		// get eigenvalues and vectors
		EigenDecomposition eigen = new EigenDecomposition(realMat);
		eigenValues = eigen.getRealEigenvalues();	
		// transpose rows into columns
		eigenVectors = eigen.getV().transpose().getData();
		
		// print eigen values and vectors
		printEigenValues(eigenValues);
		printEigenVectors(eigenVectors);
	}
	
	private void calculatePrincipalComponents(){
		
		//get number of components
		int numComp = eigenVectors.length; 
 		// Get principle components 
 		ArrayList<PrincipalComponent> principleComponents = new ArrayList<PrincipalComponent>(); 
 		for (int i = 0; i < numComp; ++i) { 
 			double[] eigenVector = new double[numComp]; 
 			for (int j = 0; j < numComp; ++j) { 
 				eigenVector[j] = eigenVectors[i][j]; 
 			} 
 			principleComponents.add(new PrincipalComponent(eigenValues[i], 
 					eigenVector)); 
 		} 
 		// sort principal components
 		Collections.sort(principleComponents); 
 		// Update original values 
 		Iterator<PrincipalComponent> it = principleComponents.iterator(); 
 		int count = 0; 
 		double[][] cacheTempVectors = new double[3][eigenVectors[0].length]; 
 		double[] cacheTempValues = new double[3]; 
 		while (it.hasNext()) { 
 			PrincipalComponent pc = it.next(); 
 			if (count < 3) { 
 				cacheTempVectors[count] = pc.eigenVector; 
 				cacheTempValues[count] = pc.eigenValue; 
 			} else { 
 				eigenValues[count - 3] = pc.eigenValue; 
 				eigenVectors[count - 3] = pc.eigenVector; 
 			} 
 			count++; 
		} 
	}
	
	private double[] matrixSubtract(double[] mat1, double[] mat2) { 
		
		double[] result = new double[mat1.length]; 
 		for (int i = 0; i < result.length; ++i)
 		{
 			result[i] = mat1[i] - mat2[i];
 		}
 		return result; 
	} 

	
	private void printEigenValues(double[] eigenValues){
		
		for(int i=0; i<eigenValues.length; i++) {
			
			System.out.println(eigenValues[i]);
		}
	}
	private void printEigenVectors(double[][] eigenVectors){
		
		for(int i=0;i<eigenVectors.length; i++) {
			for(int j=0;j<eigenVectors.length; j++) {
				System.out.println(eigenVectors[i][j]);
			}
		}
		
	}
}


