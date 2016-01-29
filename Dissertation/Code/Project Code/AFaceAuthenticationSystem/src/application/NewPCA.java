package application;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class NewPCA {

	
	private void calculateEigenValues(Mat[] images, int rowSize, int colSize) {
		
		
		Mat eigen_vector = new Mat(rowSize,colSize,CvType.CV_64FC1);
		Mat eigen_value = new Mat(1,colSize,CvType.CV_64FC1);
		Mat dataMat = new Mat(rowSize, colSize, CvType.CV_64FC1);
		
		// iterate through each matrix in images
		for(Mat img : images) {
			// Check cv type
			if (img.type() == CvType.CV_64FC1) {
				
			}
		}
		
		
				
		for(int i=0; i<rowSize; i++) {
			
			//Mat coeffiecents = ;
			
		}

	}

}
