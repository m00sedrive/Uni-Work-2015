package application;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/** Controls the main application screen */
public class MainController extends AppTools {
	
	@FXML private Label sessionLabel;
	@FXML private Button logoutButton;
	@FXML private Button imageDBButton;
	@FXML private Button detectedFaceButton;
	@FXML private Button cameraButton;
	@FXML private Button addImage_button;
	@FXML private ImageView originalImage;
	@FXML private ImageView faceDetected_IV;
	@FXML private ImageView greyscale;
	@FXML private ImageView canny_image;
	@FXML private HBox imageSet_hBox;
	@FXML private HBox hboxImageGallery_r1;
	@FXML private HBox hboxImageGallery_r2;
	@FXML private GridPane imageGalleryGrid;

	private boolean cameraActive;
	private Image CameraStream;
	private Timer timer;
	private Mat greyFaceDetected;
	private ArrayList<ImageView> new_imageSet = new ArrayList<ImageView>();
	// object for handling video capture
	private VideoCapture vidCapture = new VideoCapture();
	// object for handling Face detection
	private FaceDetector faceDetector = new FaceDetector();

	
	public void initialize() {
	}

	public void initSessionID(LoginManager loginManager, String sessionID) {

		sessionLabel.setText(sessionID);
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loginManager.logout();
			}
		});
		imageDBButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loginManager.training(sessionID);
			}
		});
		addImage_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addImage();
			}
		});
	}

	@FXML
	public void startCamera() {
		if (!this.cameraActive) {
			// start camera stream
			this.vidCapture.open(0);

			// if camera stream has started
			if (this.vidCapture.isOpened()) {
				// set camera boolean to active
				this.cameraActive = true;

				// grab frame every 33 ms and apply to imageView
				TimerTask frameGrab = new TimerTask() {
					@Override
					public void run() {
						// set cameras stream image to grabbedFrame image
						CameraStream = Mat2Image(grabFrame());
						Platform.runLater(new Runnable() {
							@Override
							public void run() {

								// set original frame
								originalImage.setImage(CameraStream);
								// set original frame width
								originalImage.setFitWidth(600);
								// Preserve image ratio
								originalImage.setPreserveRatio(true);

							}
						});
					}
				};
				this.timer = new Timer();
				this.timer.schedule(frameGrab, 0, 33);

				this.cameraButton.setText("Stop Camera");

			} else {
				System.out.println("Failed to establish camera connection!");
				// show user dialog alert with message instead of print to
				// console!
				showInformationAlert("Failed to establish camera connection!");
			}
		} else {
			// camera is not active
			cameraActive = false;
			// set camera button text
			this.cameraButton.setText("Start Camera");
			// stop the timer
			if (this.timer != null) {
				this.timer.cancel();
				this.timer = null;
			}
			// release camera
			this.vidCapture.release();
			// clear image view
			originalImage.setImage(null);
		}

	}

	public Mat grabFrame() {
		Mat frameCanvas = new Mat();
		// check video capture is open
		if (vidCapture.isOpened()) {
			try {
				// read and store video capture frame into matrix
				this.vidCapture.read(frameCanvas);
				// Check frame is not empty
				if (!frameCanvas.empty()) {
					// detect and display face detections
					faceDetector.detection(frameCanvas);
				}
			} catch (Exception e) {
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}
		return frameCanvas;
	}

	@FXML
	private void detectFace() {
		// get face detections
		greyFaceDetected = faceDetector.getFDGrey();
		// normalize face detected
		Mat cropFD = faceDetector.getFD();
		// set image views with face detections
		faceDetected_IV.setImage(Mat2Image(cropFD));
		greyscale.setImage(Mat2Image(greyFaceDetected));
	}

	private void addImage() {
		
		// get captured image
		BufferedImage buffImage = Mat2BufferedImage(faceDetector.getFD());
		// convert image Mat to Image
		WritableImage wi = new WritableImage(buffImage.getWidth(), buffImage.getHeight());
		Image newImage = SwingFXUtils.toFXImage(buffImage, wi);
		//create image view
		ImageView iv = new ImageView(newImage);
		iv.setFitWidth(80);
		iv.setFitHeight(100);
		// add image view to list
		new_imageSet.add(iv);
		
		// add image views to image gallery grid
		for(int i=0; i < new_imageSet.size(); i++) {
			imageGalleryGrid.add(new_imageSet.get(i), i, 0);
		}
		
		// need to debug this function // images not displaying in image gallery
	}

	private void showInformationAlert(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Alert");
		alert.setHeaderText(string);
		String s = "Would be good to display some more details here!! ";
		alert.setContentText(s);
		alert.show();
	}
	
	public ImageView getCanny_image() {
		return canny_image;
	}

	public void setCanny_image(ImageView canny_image) {
		this.canny_image = canny_image;
	}
}