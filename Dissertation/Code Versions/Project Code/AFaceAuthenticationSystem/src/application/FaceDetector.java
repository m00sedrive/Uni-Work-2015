package application;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;


public class FaceDetector {
	
	public void detection(Mat imageMat)
	{
		int absoluteFaceSize = 0;
		
		//create cascade classifier
		CascadeClassifier faceDetector = new CascadeClassifier();
		//path to classifiers
		String classifierPath = "C:\\Users\\user\\workspace\\AFaceAuthenticationSystem\\src\\application\\Resources\\HaarCascades\\haarcascade_frontalface_alt.xml";
		//load classifiers
		faceDetector.load(classifierPath);
		
		//Create Mat canvas to store detections
		MatOfRect faceDetections = new MatOfRect();
		Mat greyScaleImg = new Mat();
		
        // convert the frame in gray scale
        Imgproc.cvtColor(imageMat, greyScaleImg, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(greyScaleImg, greyScaleImg);
		
        // compute minimum face size (20% of the frame height)
        if (absoluteFaceSize == 0)
        {
                int height = greyScaleImg.rows();
                if (Math.round(height * 0.2f) > 0)
                {
                        absoluteFaceSize = Math.round(height * 0.2f);
                }
        }
        
        // detect faces
        faceDetector.detectMultiScale(greyScaleImg, faceDetections, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(
                        absoluteFaceSize, absoluteFaceSize), new Size());

		
		//iterate through the image
		for (Rect rect: faceDetections.toArray())
		{
			Imgproc.rectangle(imageMat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 250, 0));
		}
		
		//user details can be mapped to image name when acquired from login screen
		Imgcodecs.imwrite("DetectedFace.jpg", imageMat);
	}
	
}
 