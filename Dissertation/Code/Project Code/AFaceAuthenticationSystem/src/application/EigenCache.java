package application;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class EigenCache implements Serializable, Cloneable {

	private static final long serialVersionUID = -2625076268442336253L;

	private int numOfFace;
	private int numOfEigenFaces;
	private int imageDimensions;
	private double[][] eigenWeights;
	private double[] averageFace;
	private double[][] eigenFaces;
	private double[] eigenValues;
	private BufferedImage[] images;

	@Override
	public EigenCache clone() {
		return new EigenCache(numOfFace, numOfEigenFaces, imageDimensions, eigenWeights, averageFace, eigenFaces,
				eigenValues, images);
	}

	public EigenCache(int numOfFaces, int numEigenFaces, int imageDimension, double[][] eigenWeights,
			double[] averageFaces, double[][] eigenFaces, double[] eigenValues, BufferedImage[] images) {
		this.numOfFace = numOfFaces;
		this.numOfEigenFaces = numEigenFaces;
		this.imageDimensions = imageDimension;	
		this.eigenWeights = copy(eigenWeights);
		this.averageFace = averageFaces.clone();
		this.eigenFaces = copy(eigenFaces);
		this.eigenValues = eigenValues.clone();
		this.images = images;
	}
	
	public static double[][] copy(double[][] data) { 
 		if (data.length == 0) 
 			return new double[0][0]; 
 		double[][] returnData = new double[data.length][data[0].length]; 
 		for (int i = 0; i < data.length; ++i) 
 			returnData[i] = data[i].clone(); 
 		return returnData; 
 	} 

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getNumOfFace() {
		return numOfFace;
	}

	public int getNumOfEigenFaces() {
		return numOfEigenFaces;
	}

	public int getImageDimensions() {
		return imageDimensions;
	}

	public double[][] getEigenWeights() {
		return eigenWeights;
	}

	public double[] getAverageFace() {
		return averageFace;
	}

	public double[][] getEigenFaces() {
		return eigenFaces;
	}

	public double[] getEigenValues() {
		return eigenValues;
	}

}
