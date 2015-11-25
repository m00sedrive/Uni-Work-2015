package application;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;


public class PCA {

	// binarize or edge detect grey image
	// find contours within given threshold
	// create buffer needed for PCA data
	// perform PCA analysis
	// get orientation
	// draw principal components
	
	public static void filterContours(Mat greyImage)
	{
		// store each column from matrix into individual vectors
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat heirachy = new Mat();
		// calculate edge detected image
		Imgproc.Canny(greyImage, greyImage, 300, 600);   // display image after canny edge detection
		//find contours
		Imgproc.findContours(greyImage, contours, heirachy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		
	    for(int i=0; i< contours.size();i++){
	        System.out.println(Imgproc.contourArea(contours.get(i))); 
	        if (Imgproc.contourArea(contours.get(i)) > 50 )
	        {
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            System.out.println(rect.height);
	            if (rect.height > 28)
	            {
	            	//System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
	            	Imgproc.rectangle(greyImage, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
	            }
	        }
	    }
	}
	
	public void getOrientation()
	{
		// create PCA analysis buffer
	}
	
	public void drawAxis(Mat img, Point p, Point q, Scalar colour)
	{
		double angle;
		double hypotenuse;
		float scale = (float) 0.2;
		
		angle = Math.atan2((double) p.y - q.y, (double) p.x - q.x); // angle in radians
		hypotenuse = Math.sqrt((double)(p.y - q.y) * (p.y - q.y) + (p.x - q.x) * (p.x - q.x));
		
		// lengthen arrow in accordance to scale
		q.x = (int) (p.x - scale * hypotenuse * Math.cos(angle));
		q.x = (int) (p.y - scale * hypotenuse * Math.sin(angle));
		//line(img, p, q, colour, 1, CV_AA);
	}
}
