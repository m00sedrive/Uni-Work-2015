package application;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import database.Database;

import org.apache.commons.math3.linear.BlockRealMatrix; 
import org.apache.commons.math3.linear.EigenDecomposition; 
import org.apache.commons.math3.linear.RealMatrix; 
import org.apache.commons.math3.stat.correlation.Covariance; 


public class CustomPCA {

	private int imgSetSize;
	//private Matrix[] imageSet;
	private List<List<Double>> faceMatrix;
	private Database database;
	

	public void setPCAData(int imgSetSize, Database database) {
		// set up database
		this.database = database;
		this.database.setUpDatabase();
		// get image dimensions
		this.imgSetSize = imgSetSize;	
		//imageSet = new Matrix[imgSetSize];
	}
	
	public void prepareFaceMatrix() {	
		double[][] image = null;
		faceMatrix = new ArrayList<List<Double>>();
		List<Double> imageRow = null;
		
		// read values from each row into a column in face matrix
		for (int i = 0; i < imgSetSize; i++) {
			image = buffImg2array(database.getPerson(i).getImage());
			imageRow = new ArrayList<Double>(image.length * image.length);
			for(int x=0; x<image.length;x++) {
				for(int y=0; y<image[x].length; y++){
					//imageRow[x + (y * image[x].length)] = image[x][y];
					imageRow.add(image[x][y]);
				}
			}
			faceMatrix.add(imageRow);
		}
	}
	
	public void performPCA() {
		// This is how we go List -> Array
		double[][] faceMatrix_array = faceMatrix.toArray(new double[faceMatrix.size()][faceMatrix.get(0).size()]);
		
		print2DArrayMatrix(faceMatrix_array);
		
		// CallLibrary(faceMatrix_array);
		
		// This is how we go Array -> List
		faceMatrix = new ArrayList<List<Double>>(faceMatrix_array.length);
		for (int i = 0; i < faceMatrix_array.length; ++i) {
			List<Double> face = new ArrayList<Double>(faceMatrix_array[i].length);
			for (int j = 0; j < faceMatrix_array[i].length; ++j) {
				face.add(faceMatrix_array[i][j]);
			}
			faceMatrix.add(face);
		}
	}
	
	private void print2DArrayMatrix(double[][] matrix) {
		
		for(int i=0;i<matrix.length; i++) {
			for(int j=0; j<matrix[i].length; j++) {
				System.out.println(matrix[i][j]);
			}
		}
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


	public void calculateAverageAndCoevariance(double[][] matrix) {
		int columnLength = matrix[0].length;
		int rowLength = matrix.length;
		//dataAverageForEachRow = new double[columnLength]; 
		for (int i = 0; i < columnLength; ++i) { 
 			double sum = 0; 
 			for (int counter = 0; counter < rowLength; ++counter) { 
 				//sum += data[counter][i]; 
 			} 
 			//dataAverageForEachRow[i] = sum / rowLength; 
 		} 
 		//dataWithAverageSubtracted = new double[rowLength][columnLength]; 
 		for (int i = 0; i < rowLength; ++i) { 
 			//dataWithAverageSubtracted[i] = matrixSubtract(data[i], 
 					//dataAverageForEachRow); 
 		} 

	}

}