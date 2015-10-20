package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class FXFaceAuthController {
	
	// FXFaceAuth.fxml variables
	@FXML private TextField statsTextField;
	@FXML private ImageView originalImage;
	@FXML private ImageView capturedImage;
	@FXML private ImageView greyscale;
	@FXML private ImageView canny;
	@FXML private Button cameraButton;
	@FXML private Button captureImageButton;
	@FXML private Button logout;
		
	//class variables
	private Image CameraStream;
	private boolean cameraActive;
	private Timer timer;
	private Mat grabbedFrame;
	
	//object for handling video capture
	private VideoCapture vidCapture = new VideoCapture();
	//object for handling Face detection
	FaceDetector faceDetector = new FaceDetector();
	
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
						CameraStream = Mat2Image(grabFrame());
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
				//show user dialog alert with message instead of print to console!
				showInformationAlert("Failed to establish camera connection!");
				
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
	private void saveImage()
	{
		
	}
	
	private void showInformationAlert(String string)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Alert");
		alert.setHeaderText(string);
		String s = "Would be could to display some more details here!! ";
		alert.setContentText(s);
		alert.show();
	}
	
	private Mat grabFrame()
	{
		Mat frameCanvas = new Mat();
				
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
						// detect and display face detections
						faceDetector.detection(frameCanvas);
					}
			}
			catch (Exception e) 
			{
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}
		return frameCanvas;
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
		
	public BufferedImage getBufferedImage(Mat amatrix, String fileExtension){
		
		Mat matrix = amatrix;
		String fileExten = fileExtension;
		MatOfByte mob = new MatOfByte();
		
		//convert the matrix into a matrix of bytes appropriate for
		//this file extension
		Imgcodecs.imencode(fileExten, matrix ,mob); 
		//convert the "matrix of bytes" into a byte array
		 byte[] byteArray = mob.toArray();
		 BufferedImage bufImage = null;
		 try {
		        InputStream in = new ByteArrayInputStream(byteArray);
		        bufImage = ImageIO.read(in);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		 return bufImage;
	}
}
