package application;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class PCA {

	// create buffer needed for PCA data
	// perform PCA analysis
	// draw axis
	// get orientation
	// draw principal components
	
	
	/*public void drawAxis(Mat img, Point p, Point q, Scalar colour)
	{
		double angle;
		double hypotenuse;
		
		angle = Math.atan2((double) p.y - q.y, (double) p.x - q.x); // angle in radians
		hypotenuse = Math.sqrt((double)(p.y - q.y) * (p.y - q.y) + (p.x - q.x) * (p.x - q.x));
		
		// need to calculate scale here !!!
		// lengthen arrow in accordance to scale
		q.x = (int) (p.x - scale * hypotenuse * Math.cos(angle));
		q.x = (int) (p.y - scale * hypotenuse * Math.sin(angle));
		line(img, p, q, colour, 1, CV_AA);
	}*/
	
	public void getOrientation()
	{
		
	}
}
