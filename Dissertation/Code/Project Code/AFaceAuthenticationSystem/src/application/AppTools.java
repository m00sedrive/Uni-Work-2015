package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
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

public class AppTools {

	/**
	 * Converts a buffered image into 2d array
	 * @param bufferdImage bi
	 * @return double[][] returnArray
	 */
	protected int[][] buffImg2array(BufferedImage bi) {
		int cols = bi.getWidth();
		int rows = bi.getHeight();
		int returnArray[][] = new int[cols][rows];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				returnArray[i][j] = bi.getRGB(i, j);
			}
		}
		return returnArray;
	}
	
	protected WritableImage buffToWriteImage(BufferedImage bi) {
        WritableImage wr = null;
        if (bi != null) {
            wr = new WritableImage(bi.getWidth(), bi.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bi.getWidth(); x++) {
                for (int y = 0; y < bi.getHeight(); y++) {
                    pw.setArgb(x, y, bi.getRGB(x, y));
                }
            }
        }
        return wr;
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
			newImage = new WritableImage(bi.getWidth(), bi.getHeight());
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
	 * Converts an OpenCV Mat to java Image
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

	protected double[][] squareNonMatrix(double[][] data) { 
		double[][] squaredNMResult = new double[data.length][data[0].length]; 
		for (int i = 0; i < data.length; ++i) 
			for (int j = 0; j < data[i].length; ++j) 
				squaredNMResult[i][j] = data[i][j]; 
 		for (int i = 0; i < data.length; ++i) { 
 			for (int j = 0; j < data[i].length; ++j) { 
 				squaredNMResult[i][j] = squaredNMResult[i][j] * squaredNMResult[i][j]; 
 			} 
 		}
 		return squaredNMResult; 
 	} 
	
	protected double[] matrixSubtract(double[] matrix1, double[] matrix2) {
		double[] result = new double[matrix1.length];
		for (int i = 0; i < result.length; ++i)
			result[i] = matrix1[i] - matrix2[i];
		return result;
	}
	
	protected double[][] subtractFromEachRow(double[][] data, double[] data2subtract) { 
 		double[][] subtractMatResult = new double[data.length][data2subtract.length]; 
 		for (int i = 0; i < data.length; ++i) 
 			for (int j = 0; j < data2subtract.length; ++j) 
 				subtractMatResult[i][j] = data[i][j]; 
 		for (int i = 0; i < data.length; ++i) { 
 			for (int j = 0; j < data2subtract.length; ++j) { 
 				subtractMatResult[i][j] -= data2subtract[j]; 
 			} 
 		} 
 		return subtractMatResult; 
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
	
	protected double[][] bufferedImageTo2DArray(BufferedImage bi) {
		Raster raster = bi.getData();
		int width = raster.getWidth();
		int height = raster.getHeight();
		double[][] returnArray = new double[width][height];
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				returnArray[i][j] = raster.getSample(i, j, 0);
			}
		}
		return returnArray;
	}
	
	protected int getMinValue(int[] data2) {
		int minVal = Integer.MAX_VALUE;
		for (int i = 0; i < data2.length; ++i) {
			minVal = Math.min(minVal, data2[i]);
		}
		return minVal;
	}

	protected int getMaxValue(int[] data) {
		int maxVal = Integer.MIN_VALUE;
		for (int i = 0; i < data.length; ++i)
			maxVal = Math.max(maxVal, data[i]);
		return maxVal;
	}

	protected double getMinValue(double[] data2) {
		double minVal = Double.MAX_VALUE;
		for (int i = 0; i < data2.length; ++i) {
			minVal = Math.min(minVal, data2[i]);
		}
		return minVal;
	}

	protected double getMaxValue(double[] data) {
		double maxVal = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < data.length; ++i)
			maxVal = Math.max(maxVal, data[i]);
		return maxVal;
	}
}
