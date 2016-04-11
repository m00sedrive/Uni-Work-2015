package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import Jama.Matrix;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class CustomAppTools {

	/**
	 * Converts a buffered image into 2d array
	 * @param bufferdImage bi
	 * @return double[][] returnArray
	 */
	protected double[][] buffImg2array(BufferedImage bi) {
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

	// source:
	// http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
	/**
	 * Converts an OpenCV Mat to buffered image 
	 * @param Mat m
	 * @return The output can be assigned either to BufferedImage or to Image
	 */
	protected BufferedImage Mat2BufferedImage(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);
		return image;
	}

	/**
	 * Converts a buffered image to java Image
	 * @param bufferdimage bi
	 * @return Image newImage
	 */
	protected WritableImage bufferedImg2Img(BufferedImage bi) {
		WritableImage newImage = null;
		if (bi != null) {
			// create writable image with same width and height as buff image
			newImage = new WritableImage(bi.getHeight(), bi.getWidth());
			PixelWriter pixWrite = newImage.getPixelWriter();

			for (int x = 0; x < bi.getWidth(); x++) {
				for (int y = 0; y < bi.getHeight(); y++) {
					// get pixel value at x and y co-ordinate
					pixWrite.setArgb(x, y, bi.getRGB(x, y));
				}
			}
		} else
			System.out.println("buffered image is empty");
		return newImage;
	}

	/**
	 *  Converts an OpenCV Mat to java Image
	 * @param Mat frame
	 * @return new Image
	 */
	protected Image Mat2Image(Mat frame) {
		// temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode image frame into PNG format
		Imgcodecs.imencode(".PNG", frame, buffer);
		// build image from encoded buffered data
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	protected double[] matrixSubtract(double[] matrix1, double[] matrix2) {
		double[] result = new double[matrix1.length];
		for (int i = 0; i < result.length; ++i)
			result[i] = matrix1[i] - matrix2[i];
		return result;
	}

	protected String getMatrix2dFileString(double[][] input) {
		List<List<Double>> list = new ArrayList<List<Double>>(input.length);
		for (int i = 0; i < input.length; ++i) {
			List<Double> list2 = new ArrayList<Double>(input[i].length);
			for (int j = 0; j < input[i].length; ++j) {
				list2.add(new Double(input[i][j]));
			}
			list.add(list2);
		}
		return getMatrix2dFileString(list);
	}

	protected String getMatrix2dFileString(List<List<Double>> matrix) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < matrix.size(); ++i) {
			for (int j = 0; j < matrix.get(i).size(); ++j) {
				boolean isEnd = j == matrix.get(i).size() - 1;
				str.append(String.format("%.5f%s", matrix.get(i).get(j), isEnd ? "" : ", "));
			}
			str.append(System.lineSeparator());
		}
		return str.toString();
	}

	protected double[] multiply1DMatrix(double[] weights, Matrix subMatrix) {
		double[] returnVal = new double[subMatrix.getColumnDimension()];
		for (int i = 0; i < returnVal.length; ++i) {
			double[] column = getColumn(subMatrix, i);
			double sum = 0;
			for (int j = 0; j < column.length; ++j)
				sum += weights[j] * column[j];
			returnVal[i] = sum;
		}
		return returnVal;
	}

	protected double[] add1DMatrix(double[] m1, double[] m2) {
		double[] returnVal = new double[m1.length];
		for (int i = 0; i < returnVal.length; ++i)
			returnVal[i] = m1[i] + m2[i];
		return returnVal;
	}

	private double[] getColumn(Matrix m, int column) {
		double[] col = new double[m.getRowDimension()];
		for (int i = 0; i < m.getRowDimension(); ++i)
			col[i] = m.get(i, column);
		return col;
	}

	protected void print1dToFile(String fileName, double[] array) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < array.length; ++i) {
			boolean isEnd = i == array.length - 1;
			str.append(String.format("%.5f%s", array[i], isEnd ? "" : ", "));
		}
		List<String> toWrite = new ArrayList<String>(1);
		toWrite.add(str.toString());
		try {
			Files.write(Paths.get(fileName), toWrite, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void print2dArrayToFile(String filename, double[][] matrix) {
		String fileContents = getMatrix2dFileString(matrix);
		List<String> toWrite = new ArrayList<String>(1);
		toWrite.add(fileContents);
		try {
			Files.write(Paths.get(filename), toWrite, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void print2dListToFile(String filename, List<List<Double>> matrix) {
		String fileContents = getMatrix2dFileString(matrix);
		List<String> toWrite = new ArrayList<String>(1);
		toWrite.add(fileContents);
		try {
			Files.write(Paths.get(filename), toWrite, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
