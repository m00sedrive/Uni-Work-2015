package application;

import java.awt.image.BufferedImage;

import Jama.Matrix;
import database.Database;

public class CustomPCA {

	private int imgSetSize;
	private Matrix[] imageSet;

	public void setPCAData(int imgSetSize, Database database) {
		// set up database
		database.setUpDatabase();
		// get image dimensions
		this.imgSetSize = imgSetSize;
		imageSet = new Matrix[imgSetSize];
		// load image set into class
		loadImageSet(database);
	}

	public void performPCA() {
		// prepare images for PCA
		prepareFaceMatrix();
	}
	
	public void loadImageSet(Database db) {
		// set matrix array with database image set
		for (int i = 0; i < imgSetSize; i++) {
			imageSet[i] = new Matrix(buffImg2array(db.getPerson(i).getImage()));
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

	public void prepareFaceMatrix() {
		if (imageSet.length != 0) {
			// read image from set into 1d array
			double[] image;
	}

	public void calculateEigenValueDecomposition(Matrix[] imageSet) {
	}

}