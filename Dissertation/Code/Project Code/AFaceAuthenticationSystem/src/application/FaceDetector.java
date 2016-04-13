
package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;


public class FaceDetector {
	
	private Mat FD;
	private Mat faceDetectionG;

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
			faceDetectionG = greyScaleImg.submat(faceArray[i]);
			//set local variables
			setFD(crop);
			setFDGrey(faceDetectionG);
		}				
	}
	
	public void saveDetection2File() {
		
		try {
			ImageIO.write(Mat2BufferedImage(faceDetectionG) ,".jpg", new File("C:\\Users\\user\\Desktop\\images\\detectedFace.jpg"));
			System.out.println("try statement hit");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("try statement caught");
			e.printStackTrace();
		}
	}
	
	private BufferedImage Mat2BufferedImage(Mat m) {
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