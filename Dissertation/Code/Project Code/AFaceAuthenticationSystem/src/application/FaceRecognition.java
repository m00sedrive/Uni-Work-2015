package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class FaceRecognition {

	// binarize or edge detect grey image
	// find contours within given threshold
	// create buffer needed for PCA data
	// perform PCA analysis
	// get orientation
	// draw principal components
	
	Mat image;
	Mat grey_image;
	
	//constructor
	public void initManager()
	{
		loadImage();
		grey_image = img2grey(image);
		filterContours(grey_image);
	}
	
	public void loadImage() 
	{
		String filepath = "C:\\Users\\user\\workspace\\AFaceAuthenticationSystem\\images\\detected_face.jpg";
		image = Imgcodecs.imread(filepath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
	}
	
	public Mat img2grey(Mat img)
	{
		Mat greyImg = new Mat(img.height(), img.width(), CvType.CV_8UC1);
		// if image is already grey
		if(img.type() == CvType.CV_8UC1) {
			greyImg = img;
		}
		// if RGB image convert to grey
		else {
			Imgproc.cvtColor(img, greyImg, CvType.CV_8UC1);
		}
		return greyImg;
	}
	
	public static void filterContours(Mat greyImage)
	{
		// store each column from matrix into individual vectors
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat heirachy = new Mat();
		
		//Imshow im = new Imshow("Title");
		//im.showImage(greyImage);
		
		// calculate edge detected image
		
		Imgproc.Canny(greyImage, greyImage, 300, 600);   // display image after canny edge detection
		
		// set thresholding
		//threshold(greyImage, greyImage, 150, 255, Imgproc.CV_THRESH_BINARY);
		//threshold(greyImage, greyImage, 150, Imgproc.THRESH_BINARY, 3);
		
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
	/*
	public double getOrientation(Vector<Point> pts, Mat img)
	{
	    //Construct a buffer used by the pca analysis
	    int size = pts.size();
	    Mat dataPts = new Mat(size, 2, CvType.CV_64FC1);
	    for (int i = 0; i < data_pts.rows; ++i)
	    {
	        dataPts(i, 0) = new Point();
	    	//dataPts(i, 0) = pts[i].x;
	        dataPts.at<double>(i, 1) = pts[i].y;
	    }
	    //Perform PCA analysis
	    PCA pca_analysis = new PCA(data_pts, Mat(), CV_PCA_DATA_AS_ROW);
	    //Store the center of the object
	    Point cntr = Point(static_cast<int>(pca_analysis.mean.at<double>(0, 0)),
	                      static_cast<int>(pca_analysis.mean.at<double>(0, 1)));
	    //Store the eigenvalues and eigenvectors
	    vector<Point2d> eigen_vecs(2);
	    vector<double> eigen_val(2);
	    for (int i = 0; i < 2; ++i)
	    {
	        eigen_vecs[i] = Point2d(pca_analysis.eigenvectors.at<double>(i, 0),
	                                pca_analysis.eigenvectors.at<double>(i, 1));
	        eigen_val[i] = pca_analysis.eigenvalues.at<double>(0, i);
	    }
	    // Draw the principal components
	    circle(img, cntr, 3, Scalar(255, 0, 255), 2);
	    Point p1 = cntr + 0.02 * Point(static_cast<int>(eigen_vecs[0].x * eigen_val[0]), static_cast<int>(eigen_vecs[0].y * eigen_val[0]));
	    Point p2 = cntr - 0.02 * Point(static_cast<int>(eigen_vecs[1].x * eigen_val[1]), static_cast<int>(eigen_vecs[1].y * eigen_val[1]));
	    drawAxis(img, cntr, p1, Scalar(0, 255, 0), 1);
	    drawAxis(img, cntr, p2, Scalar(255, 255, 0), 5);
	    double angle = atan2(eigen_vecs[0].y, eigen_vecs[0].x); // orientation in radians
	    return angle;
	}
	*/
	
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
