package application;


import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;


public class FaceDetector {
	
	public Mat FD;
	public Mat faceDetectionG;

	public void detection(Mat imageMat)
	{
		int absoluteFaceSize = 0;
		
		//create cascade classifier
		CascadeClassifier faceDetector = new CascadeClassifier();
		//absolute path to classifiers
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

		//create array of face detections computed
		Rect[] faceArray = faceDetections.toArray(); 
        //iterate through the image
		for (int i=0; i<faceArray.length; i++)
		{
			//find rectangle contours of faces
			Imgproc.rectangle(imageMat, faceArray[i].tl(), faceArray[i].br(), new Scalar(0, 250, 0, 255), 3);
			//crop image of face
			Mat crop = imageMat.submat(faceArray[i]);
			//crop grey image of face
			Mat faceDetectGrey = greyScaleImg.submat(faceArray[i]);
			//set local variables
			setFD(crop);
			setFDGrey(faceDetectGrey);
		}				
	}
	
	public Mat getFD() {
		return FD;
	}
	
	public void setFD(Mat crop) {
		this.FD = crop;
	}
	
	public Mat getFDGrey() {
		return faceDetectionG;
	}

	public void setFDGrey(Mat faceDetectGrey) {
		this.faceDetectionG = faceDetectGrey;
	}
}