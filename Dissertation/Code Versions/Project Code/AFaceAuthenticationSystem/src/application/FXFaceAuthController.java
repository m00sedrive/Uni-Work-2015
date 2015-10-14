package application;

import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXFaceAuthController {
	
	@FXML
	private ImageView originalImage;
	
	@FXML 
	private ImageView capturedImage;
	
	@FXML
	private ImageView greyscale;
	
	@FXML
	private ImageView canny;
	
	@FXML
	private Button cameraButton;
	
	@FXML
	private Button captureImageButton;
	
	//class variables
	private Image CameraStream;
	private boolean cameraActive;
	private Timer timer;
	
	//object that handles video capture
	private VideoCapture vidCapture = new VideoCapture();
	
	@FXML
	private void startCamera()
	{
		if(!this.cameraActive)
		{
			//start camera stream
			this.vidCapture.open(0);
			
			//if camera stream has started
			if(this.vidCapture.isOpened())
			{
				//set camera boolean to active
				this.cameraActive = true;
				
				//grab frame every 33 ms and apply to imageView
				TimerTask frameGrab = new TimerTask(){
					@Override
					public void run()
					{
						// set cameras stream image to grabbedFrame image 
						CameraStream = grabFrame();
						Platform.runLater(new Runnable(){
							@Override
							public void run () {
								
								//set original frame
								originalImage.setImage(CameraStream);
								//set original frame width
								originalImage.setFitWidth(600);
								//Preserve image ratio
                                originalImage.setPreserveRatio(true);
            					
                                //set grey scale image view
                                greyscale.setImage(CameraStream);
                                
                                //set canny image view 
                                canny.setImage(CameraStream);
                                
                                //need to consider frame size and ratio!
                               
							}
						});
					}
				};
				this.timer = new Timer();
		        this.timer.schedule(frameGrab, 0, 33);

                this.cameraButton.setText("Stop Camera");	
			}
			else 
			{
				System.out.println("Failed to establish camera connection!");
			}
		}
		else
		{
			//camera is not active
			cameraActive = false;
			//set camera button text
			this.cameraButton.setText("Start Camera");
			//stop the timer
			if(this.timer != null)
			{
				this.timer.cancel();
				this.timer = null;
			}
			//release camera
			this.vidCapture.release();
			//clear the image area
			originalImage.setImage(null);
		}
	}
	
	@FXML
	private void captureImage()
	{
		//if camera is active
		if(this.cameraActive)
		{
			//create temp image and assign grabbed image
			Image temp = grabFrame();
			//set captured image view
			capturedImage.setImage(temp);
		}
		else
		{
			System.out.println("Camera is not active!");
		}
	}
	
	private Image grabFrame()
	{
		Image grabbedFrame = null;
		Mat frameCanvas = new Mat();
		Mat greyScale = new Mat();
		
		//check video capture is open
		if(vidCapture.isOpened())
		{
			try 
			{
				//read and store video capture frame into matrix
				this.vidCapture.read(frameCanvas);
				
				//Check frame is not empty
				if(!frameCanvas.empty())
				{
					//This is where analysis such as edge detection, greyscale and image analysis is applied
						//Convert canvas matrix to JavaFX Image
						grabbedFrame = Mat2Image(frameCanvas);
						
				}
			}
			catch (Exception e) 
			{
				System.err.print("ERROR");
				e.printStackTrace();
			}
			
		}
		
		return grabbedFrame;
	}
	
	private Image Mat2Image(Mat frame)
	{
		//temporary buffer
		MatOfByte buffer = new MatOfByte();
		
		//encode image frame into PNG format
		Imgcodecs.imencode(".PNG", frame, buffer);
		
		//build image from encoded buffered data		
        return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
	
	// this function is currently useless and needs re thinking
	private Mat Image2Mat(Image image)
	{
		//temporary buffer
		Mat matty = null;
		//conversion?
		//build matrix
		
		return matty;
	}
}
