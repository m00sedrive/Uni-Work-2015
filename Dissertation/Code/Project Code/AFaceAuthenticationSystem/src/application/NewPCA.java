package application;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.lang.instrument.Instrumentation;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import database.Database;

import Jama.Matrix;

public class NewPCA {
	
	private int imgSetSize;
	//private Mat[] imageSet;
	private Vector<Mat> imageSet;
	public static Instrumentation instrumentation; 
	
	public void setPCAData(int imgSetSize) {
		this.imgSetSize = imgSetSize;
	}
	
	public void loadPersonImageSet(Database db) { 
		// set mat array with database image set
		for(int i=0; i<imgSetSize;++i) {
			imageSet.add(i,matify(db.getPerson(i).getImage()));
		}
	}
	
	public void calculateEigenData() {
		
		// compute compostion matrix
	}
	
	// Source: http://www.answers.opencv.org/question/28348/converting-bufferedimage-to-mat-in-java/
	// Convert image to Mat
	private Mat matify(BufferedImage im) {
		// Convert bufferedimage to byte array
		byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();
		// Create a Matrix the same size of image
		Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_64FC1);
		// Fill Matrix with image values
		image.put(0, 0, pixels);
		
		return image;
	}
	
}
