package application;

import java.awt.Image;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

public class PCA {

	private Image[] images;
	private double[][] imageData;
	private int numFaces;
	private int numOfEigenFaces = 0;
	private double[] eigenValues = null;
	private double[][] eigenVectors = null;
	private double[][] eigenfaces = null;
	private double[][] dataMinusAverage = null;
	
	public void run() {
		
		// record system start time
		long currentTime = System.currentTimeMillis();
		
		try{
			double[][] data  = this.imageData;
			
			// calculate weighted difference of eigen data
			
			// calculate eigen values and vectors
			calculateEigenValues();
			
			// compute PCA
			
			// compute average pca
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void calculateDataAverages() {
		
	}
	
	private void calculateEigenValues() {
		
		// compute covariance matrix
		RealMatrix realMat = new Covariance(new BlockRealMatrix(dataMinusAverage).transpose()).getCovarianceMatrix();
		// get eigenvalues and vectors
		EigenDecomposition eigen = new EigenDecomposition(realMat);
		eigenValues = eigen.getRealEigenvalues();	
		// transpose rows into columns
		eigenVectors = eigen.getV().transpose().getData();
	}
}
