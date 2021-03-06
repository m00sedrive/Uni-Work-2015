package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import Jama.Matrix;
import database.Database;

public class CustomPCA extends AppTools {

	private int imgSetSize = 8;
	private List<List<Double>> faceMatrix;
	private Database database;
	private double[] dataAverageOfEachRow = null;
	private double[][] faceMatrixMinusAverages = null;
	private double[] eigenValues = null;
	private double[][] eigenVectors = null;
	private double[][] eigenFaces = null;
	private double[][] eigenWeights = null;
	private int numOfEigenFacesSelected = 150;
	private CustomImage[] images;

	private static List<List<Double>> debugMatrix = null;
	private EigenCache cache;

	public void setPCAData(Database database) {
		// set up database
		this.database = database;
		this.database.setUpDatabase();
		// get image dimensions
		this.imgSetSize = database.getImageSetSize();
		System.out.println();
	}
	
	public EigenCache getPCAResults() {
		return cache;
	}

	public void prepareFaceMatrix() {
		images = new CustomImage[imgSetSize];
		int[][] image = null;
		faceMatrix = new ArrayList<List<Double>>(imgSetSize);
		List<Double> imageRow = null;

		// read values from each row into a column in face matrix
		for (int i = 0; i < imgSetSize; i++) {
			images[i] = new CustomImage(database.getPerson(i).getImage());
			image = buffImg2array(database.getPerson(i).getImage());
			{
				BufferedImage test = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_RGB);
				for (int a = 0; a < test.getWidth(); a++) {
					for (int b = 0; b < test.getHeight(); b++) {
						test.setRGB(a, b, image[a][b]);
					}
				}
				//double[][] normalised = normalizeImageData(image);
				//test = denormaliseImageData(image.length, image[0].length, (float) 0, (float) 255, normalised);
			}

			int imageLength = image.length;
			// normalize image data
			double[][] normalised_matrix = normalizeImageData(image);
			imageRow = new ArrayList<Double>(imageLength * imageLength);
			for (int x = 0; x < normalised_matrix.length; x++) {
				for (int y = 0; y < image[x].length; y++) {
					imageRow.add(normalised_matrix[x][y]);
				}
			}
			faceMatrix.add(imageRow);
		}

		// debug
		//setDebugMatrix(faceMatrix);
		//print2dListToFile("faceMatrix.txt", faceMatrix);
	}

	public void performPCA() {
		// Convert face data from List --> array
		double[][] faceMatrix_array = new double[faceMatrix.size()][faceMatrix.get(0).size()];
		for (int i = 0; i < faceMatrix_array.length; ++i) {
			for (int j = 0; j < faceMatrix_array[i].length; ++j) {
				faceMatrix_array[i][j] = faceMatrix.get(i).get(j).doubleValue();
			}
		}

		// save constructed eigen faces to file
		for (int i = 0; i < faceMatrix_array.length; ++i) {
			BufferedImage im = denormaliseImageData(55, 51, 0, 255, faceMatrix_array[i]);
			try {
				ImageIO.write(im, "jpg", new File("C:\\Users\\user\\Desktop\\FAResults\\face\\debug_" + i + ".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// calculate averages and coefficents
		calculateAverageAndCoevariance(faceMatrix_array);
		// calculate Eigen values and vectors
		calculateEigenVectorsAndValues();
		// calculate principal components
		calculatePrincipalComponents();
		// calculate weights and Eigen Faces
		calculateEigenFacesAndWeights();

		// construct buffered image from eigen faces
		BufferedImage[] constructedEFaces = new BufferedImage[faceMatrix_array.length];
		for (int i = 0; i < faceMatrix_array.length; i++) {
			constructedEFaces[i] = getImageByEigenValues(eigenWeights[i], eigenFaces);
		}
		// save constructed weight images to file
		for (int i = 0; i < constructedEFaces.length; ++i) {
			File outputFile = new File("C:\\Users\\user\\Desktop\\FAResults\\weights\\weight_" + i + ".jpg");
			try {
				ImageIO.write(constructedEFaces[i], "jpg", outputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// save constructed eigen faces to file
		for (int i = 0; i < eigenFaces.length; ++i) {
			BufferedImage im = denormaliseImageData(55, 51, 0, 255, eigenFaces[i]);
			try {
				ImageIO.write(im, "jpg", new File("C:\\Users\\user\\Desktop\\FAResults\\face\\face_" + i + ".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// set eigen cache
		cache = new EigenCache(imgSetSize, numOfEigenFacesSelected, faceMatrix_array.length, eigenWeights,
				dataAverageOfEachRow, eigenFaces, eigenValues, images);

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
		// debug
		//print1dToFile("dataAverageEachRow.txt", dataAverageOfEachRow);

		faceMatrixMinusAverages = new double[rowLength][columnLength];
		for (int i = 0; i < rowLength; ++i) {
			faceMatrixMinusAverages[i] = matrixSubtract(faceMatrix[i], dataAverageOfEachRow);
		}

		// debug
		//print2dArrayToFile("faceMatrixMinusAverages.txt", faceMatrixMinusAverages);
	}

	private void calculateEigenFacesAndWeights() {
		int pixelTotal = faceMatrixMinusAverages[0].length;
		int imageTotal = faceMatrixMinusAverages.length;
		int vectorTotal = eigenVectors.length;

		// calculate eigen faces
		double[][] eigenFace = new double[vectorTotal][pixelTotal];
		for (int i = 0; i < vectorTotal; ++i) {
			double squaredSum = 0;
			for (int j = 0; j < pixelTotal; ++j) {
				for (int k = 0; k < imageTotal; ++k) {
					eigenFace[i][j] += faceMatrixMinusAverages[k][j] * eigenVectors[i][k];
				}
				squaredSum += eigenFace[i][j] * eigenFace[i][j];
			}
			double norm = Math.sqrt(squaredSum);
			for (int j = 0; j < pixelTotal; j++) {
				eigenFace[i][j] /= norm;
			}
		}
		// get specified amount of eigen faces
		this.eigenFaces = new Matrix(eigenFace).getMatrix(0,
				eigenFace.length <= numOfEigenFacesSelected ? eigenFace.length - 1 : numOfEigenFacesSelected, 0,
				eigenFace[0].length - 1).getArray();
		this.eigenWeights = new Matrix(faceMatrixMinusAverages).times(new Matrix(eigenFaces).transpose()).getArray();

	}

	private BufferedImage getImageByEigenValues(double[] weights, double[][] eigenFaces) {
		int imageHeight = database.getPerson(0).getImageHeight();
		int imageWidth = database.getPerson(0).getImageWidth();

		Matrix subMatrix = new Matrix(eigenFaces);
		// getMatrix(initial row index, final row index, initial column index,
		// final column index)
		subMatrix = subMatrix.getMatrix(0, subMatrix.getRowDimension() <= numOfEigenFacesSelected
				? subMatrix.getRowDimension() - 1 : numOfEigenFacesSelected, 0, subMatrix.getColumnDimension() - 1);

		double[] imageData = multiply1DMatrix(weights, subMatrix);
		imageData = add1DMatrix(imageData, dataAverageOfEachRow);
		return denormaliseImageData(imageHeight, imageWidth, 0, 255, imageData);
	}

	private void calculateEigenVectorsAndValues() {

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

		//print1dToFile("EigenValues.txt", eigenValues);
		//print2dArrayToFile("EigenVectors.txt", eigenVectors);
	}

	private void calculatePrincipalComponents() {
		int numOfComponents = eigenVectors.length;
		// Get principle components
		ArrayList<PrincipalComponent> principalComponents = new ArrayList<PrincipalComponent>();
		for (int i = 0; i < numOfComponents; i++) {
			double[] eigenVector = new double[numOfComponents];
			for (int j = 0; j < numOfComponents; j++) {
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
		while (iterator.hasNext()) {
			PrincipalComponent pc = iterator.next();
			if (count < 3) {
				tempVectors[count] = pc.eigenVector;
				tempValues[count] = pc.eigenValue;
			} else {
				eigenVectors[count - 3] = pc.eigenVector;
				eigenValues[count - 3] = pc.eigenValue;
			}
			count++;
		}
	}

	private double[][] normalizeImageData(int[][] image) {
		double[][] returnData = new double[image.length][image[0].length];
		// Normalise data between 0 and 1
		for (int faceInd = 0; faceInd < image.length; ++faceInd) {
			double min = (double) getMinValue(image[faceInd]);
			double max = (double) getMaxValue(image[faceInd]);

			for (int j = 0; j < image[faceInd].length; ++j)
				returnData[faceInd][j] = ((((double) image[faceInd][j])) - min) / (max - min);
		}
		return returnData;
	}

	protected BufferedImage denormaliseImageData(int width, int height, float goalMin, float goalMax, double[] data) {
		BufferedImage returnVal = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		double min = getMinValue(data);
		double max = getMaxValue(data);
		int row = 0;
		for (int j = 0; j < height; ++j) {
			for (int i = 0; i < width; ++i) {
				// Normalise
				double fgrey = goalMin + (goalMax - goalMin) * (((data[row++]) - min) / (max - min));
				int grey = (int) fgrey;
				if (grey > 255)
					grey = 255;
				else if (grey < 0)
					grey = 0;
				grey = 0xff000000 | (grey << 16) | (grey << 8) | grey;
				returnVal.setRGB(i, j, grey);
			}
		}
		return returnVal;
	}

	protected BufferedImage denormaliseImageData(int width, int height, float goalMin, float goalMax, double[][] data) {
		BufferedImage returnVal = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < data.length; ++j) {
			for (int i = 0; i < data[j].length; ++i) {
				double min = getMinValue(data[j]);
				double max = getMaxValue(data[j]);
				// Normalise
				double fgrey = goalMin + (goalMax - goalMin) * (((data[j][i]) - min) / (max - min));
				int grey = (int) fgrey;
				if (grey > 255)
					grey = 255;
				else if (grey < 0)
					grey = 0;
				grey = 0xff000000 | (grey << 16) | (grey << 8) | grey;
				returnVal.setRGB(j, i, grey);
			}
		}
		return returnVal;
	}

	public static double[][] getDebugMatrix() {
		double[][] array = new double[debugMatrix.size()][debugMatrix.get(0).size()];
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array[i].length; ++j) {
				array[i][j] = debugMatrix.get(i).get(j).doubleValue();
			}
		}
		return array;
	}

	public void setDebugMatrix(List<List<Double>> matrix) {
		debugMatrix = matrix;
	}
	
	/*try {
	ImageIO.write(test, "jpg",
			new File("C:\\Users\\user\\Desktop\\FAResults\\face\\" + i + "_normed_and_unnormed.jpg"));
	} catch (IOException e) {
		e.printStackTrace();
	}*/

}