package application;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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
	@FXML private ImageView canny_image;
	
	private boolean cameraActive;
	private Image CameraStream;
	private Timer timer;
	
	//object for handling video capture
	private VideoCapture vidCapture = new VideoCapture();
	//object for handling Face detection
	FaceDetector faceDetector = new FaceDetector();
	//object for handling PCA
	//PCA pca = new PCA();
	
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
			//clear image view
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
	
	@FXML
	private void captureImage()
	{
		// get face detections
		Mat greyFaceDetected = faceDetector.getFDGrey();
		Mat cropFD = faceDetector.getFD();
		// set image views with face detections
		capturedImage.setImage(Mat2Image(cropFD));
		greyscale.setImage(Mat2Image(greyFaceDetected));
		
		//pca test
		//pca.getOrientation();
		FaceRecognition pca = new FaceRecognition();
		pca.initManager();
		
		
	}
	
	public ImageView getCanny_image() {
		return canny_image;
	}

	public void setCanny_image(ImageView canny_image) {
		this.canny_image = canny_image;
	}
	
	private BufferedImage mat2BufferedImg(Mat image)
	{
		//Mat image = Imgcodecs.imread("/Users/Sumit/Desktop/image.jpg");

		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return img;
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
	
	private Image Mat2Image(Mat frame)
	{
		//temporary buffer
		MatOfByte buffer = new MatOfByte();
		//encode image frame into PNG format
		Imgcodecs.imencode(".PNG", frame, buffer);
		//build image from encoded buffered data		
        return new Image(new ByteArrayInputStream(buffer.toArray()));
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
}