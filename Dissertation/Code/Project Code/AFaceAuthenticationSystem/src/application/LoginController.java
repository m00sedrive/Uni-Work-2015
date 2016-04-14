package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController extends AppTools {

	@FXML
	private TextField usernameEntered;
	@FXML
	private PasswordField passwordEntered;
	@FXML
	private ImageView loginImage;
	@FXML
	private ImageView capturedImage;
	@FXML
	private Button loginButton;
	@FXML
	private Button logout_button;
	@FXML
	private Button captureImageButton;
	@FXML
	private Button cameraButton;

	boolean cameraActive;
	private FaceDetector faceDetector = new FaceDetector();
	private VideoCapture vidCapture = new VideoCapture();
	private Timer timer;
	private Image CameraStream;
	private String username = "Tom";
	private String password = "hello";
	private boolean getNewFaceImage = true;
	private boolean defaultImage = true;

	public void initialize() {
	}

	public void initManager(final LoginManager loginManager) {
		try {
			if (defaultImage) {
				BufferedImage image = ImageIO.read(new File("images/userImage.png"));
				loginImage.setImage(bufferedImg2Img(image));
				capturedImage.setImage(bufferedImg2Img(image));
			}
		} catch (Exception ex) {
			System.out.println("Error: " + ex.getMessage());
		}
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String sessionID = authorize();
				if (sessionID != null) {
					loginManager.authenticated(sessionID);
				}
			}
		});
		logout_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String sessionID = authorize();
				if (sessionID != null) {
					exit(0);
				}
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
								loginImage.setImage(CameraStream);
								// set original frame width
								loginImage.setFitWidth(600);
								// Preserve image ratio
								loginImage.setPreserveRatio(true);

							}
						});
					}
				};
				this.timer = new Timer();
				this.timer.schedule(frameGrab, 0, 33);
				this.cameraButton.setText("Stop Camera");

			} else {
				System.out.println("Failed to establish camera connection!");
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

			// reset image view to default image
			try {
				BufferedImage image = ImageIO.read(new File("images/userImage.png"));
				loginImage.setImage(bufferedImg2Img(image));
			} catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage());
			}
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

					// set detected face a user image once
					if (faceDetector.getFD() != null) {
						if (getNewFaceImage) {
							getNewFaceImage = false;
							capturedImage.setImage(Mat2Image(faceDetector.getFD()));
						}
					}
				}
			} catch (Exception e) {
				System.err.print("ERROR");
				e.printStackTrace();
			}
		}
		return frameCanvas;
	}

	@FXML
	public void setUserImage() {
		// get detected face image
		// Mat cropCamShot = faceDetector.getFD();
		// set image view with face detection
		// capturedImage.setImage(Mat2Image(cropCamShot));
		// debug
		faceDetector.saveDetection2File();
	}

	private String authorize() {
		String authorized = null;
		if (usernameEntered.getText().equals(username) && passwordEntered.getText().equals(password)) {
			authorized = generateSessionID();
		} else {
			// alert user and print to console wrong username or password
			// entered!
			System.out.println("Invalid username or password: Please re-enter!!");
			JOptionPane.showMessageDialog(null, "Invalis username or password: Please re-enter!");
		}
		return authorized;
	}

	private static int sessionID = 0;

	private String generateSessionID() {
		sessionID++;
		String id = null;
		id = "session - " + sessionID;
		return id;
	}

	private static void exit(int status) {
		System.exit(status);
	}

	public void setCapturedImage(ImageView capturedImage) {
		this.capturedImage = capturedImage;
	}

}
