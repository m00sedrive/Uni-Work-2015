package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/** Controls the main application screen */
public class MainController {
  
	@FXML private Button logoutButton;
	@FXML private Button captureImageButton;
	@FXML private Label  sessionLabel;
	@FXML private Button cameraButton;
	@FXML private ImageView originalImage;
	@FXML private ImageView capturedImage;
	@FXML private ImageView greyscale;
	
	private boolean cameraActive;
	private Image CameraStream;
	private Timer timer;
	private int imgCounter = 0;
	//object for handling video capture
	private VideoCapture vidCapture = new VideoCapture();
	//object for handling Face detection
	FaceDetector faceDetector = new FaceDetector();
	
	public void initialize() {}
	  
	  public void initSessionID(final LoginManager loginManager, String sessionID) {
	    sessionLabel.setText(sessionID);
	    logoutButton.setOnAction(new EventHandler<ActionEvent>() {
	      @Override public void handle(ActionEvent event) {
	        loginManager.logout();
	      }
	    });
	  }
  
	
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
          		          
                                  
							}
						});
					
						//collect images untill counter reaches 10
						while(imgCounter <=10)
						{
							//get image and put in matrix
							Mat setOfImg = grabFrame();
							faceDetector.detection(setOfImg);
							//write image to file
							Imgcodecs.imwrite("CapturedImgSet" + imgCounter + ".jpg", setOfImg);
							//increment counter
							imgCounter++;
						}
					
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

	@FXML
	private void captureImage()
	{
			try {
				//read in crop and grey crop and scale
				BufferedImage imageCaught = ImageIO.read(new File("DetectedFace.jpg"));
				BufferedImage imageCaughtGrey = ImageIO.read(new File("DetectedFaceGreyScale.jpg"));
				
				//set captured image view
				capturedImage.setImage(bufferedImg2Img(imageCaught));
				
				//set grey scale + histogram average image view
				greyscale.setImage(bufferedImg2Img(imageCaughtGrey));
				
				//set Canny edge image view
				
			} catch (IOException e) {
				
				e.printStackTrace();
				System.out.println("Capture Image Error");
			}
	}

	private WritableImage bufferedImg2Img(BufferedImage bi)
	{
		//write buffered image to image
		WritableImage newImage = null;
		if(bi != null)
		{
			// create writable image with same width and height as buff image
			newImage = new WritableImage(bi.getHeight(), bi.getWidth());
			PixelWriter pixWrite = newImage.getPixelWriter();
			
			for(int x=0; x<bi.getWidth(); x++)
			{
				for(int y=0; y<bi.getHeight(); y++)
				{
					//get pixel value at x and y co-ordinate
					pixWrite.setArgb(x,y,bi.getRGB(x,y));
				}
			}
		}
		else
			System.out.println("buffered image is empty");
		return newImage;
	}

	
	private void showInformationAlert(String string)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Alert");
		alert.setHeaderText(string);
		String s = "Would be good to display some more details here!! ";
		alert.setContentText(s);
		alert.show();
	}
  //FXFaceAuthController functions need to be transfered to this class!!
  
}