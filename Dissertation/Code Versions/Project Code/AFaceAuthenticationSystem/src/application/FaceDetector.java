package application;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


public class FaceDetector {
	
	public void Detection(Mat matOfImage)
	{
		//set temporary matrix to parameter matrix
		Mat image = matOfImage;
		//create cascade classifier
		CascadeClassifier faceDetector = new CascadeClassifier();
		//Create Mat canvas to store detections
		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);
		
		//iterate through the image
		for (Rect rect: faceDetections.toArray())
		{
			Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 250, 0));
		}
		//user details can be mapped to image name when acquired from login screen
		Imgcodecs.imwrite("userFaceDetectedImage", image);
	}
	
}
 