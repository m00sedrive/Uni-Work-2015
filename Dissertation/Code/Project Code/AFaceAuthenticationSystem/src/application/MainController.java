package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import database.Database;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/** Controls the main application screen */
public class MainController {

	@FXML
	private Button logoutButton;
	@FXML
	private Button imageDBButton;
	@FXML
	private Button detectedFaceButton;
	@FXML
	private Label sessionLabel;
	@FXML
	private Button cameraButton;
	@FXML
	private Button addImage_button;
	@FXML
	private ImageView originalImage;
	@FXML
	private ImageView faceDetected_IV;
	@FXML
	private ImageView greyscale;
	@FXML
	private ImageView canny_image;
	@FXML
	private HBox imageSet_hBox;

	private boolean cameraActive;
	private Image CameraStream;
	private Timer timer;
	private Mat greyFaceDetected;
	private int imageSetCount = 0;

	// object for handling video capture
	private VideoCapture vidCapture = new VideoCapture();
	// object for handling Face detection
	private FaceDetector faceDetector = new FaceDetector();
	private ArrayList<ImageView> new_imageSet = new ArrayList<ImageView>();
	private Database database;

	// custom image set variables
	@FXML
	private HBox hboxImageGallery_r1;
	@FXML
	private HBox hboxImageGallery_r2;
	@FXML
	private GridPane imageGalleryGrid;

	
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
		iv.setFitWidth(100);
		iv.setFitHeight(80);
		// add imageviews to image gallery grid
		new_imageSet.add(iv);
		
		// add image views to image gallery grid
		for(int i=0; i < new_imageSet.size() - 1; i++) {
			imageGalleryGrid.add(new_imageSet.get(i), i, 0);
		}
		
		// need to debug this function // images not displaying in image gallery
	}

	public ImageView getCanny_image() {
		return canny_image;
	}

	public void setCanny_image(ImageView canny_image) {
		this.canny_image = canny_image;
	}

	// source:
	// http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
	/**
	 * @param
	 * @return The output can be assigned either to BufferedImage or to Image
	 */
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

	// Source:
	// http://www.answers.opencv.org/question/28348/converting-bufferedimage-to-mat-in-java/
	// Convert image to Mat
	private Mat matify(BufferedImage im) {
		// Convert INT to BYTE
		// im = new BufferedImage(im.getWidth(),
		// im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		// Convert bufferedimage to byte array
		byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();

		// Create a Matrix the same size of image
		Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_64FC1);
		// Fill Matrix with image values
		image.put(0, 0, pixels);

		return image;
	}

	private WritableImage bufferedImg2Img(BufferedImage bi) {
		// write buffered image to image
		WritableImage newImage = null;
		if (bi != null) {
			// create writable image with same width and height as buff image
			newImage = new WritableImage(bi.getHeight(), bi.getWidth());
			PixelWriter pixWrite = newImage.getPixelWriter();

			for (int x = 0; x < bi.getWidth(); x++) {
				for (int y = 0; y < bi.getHeight(); y++) {
					// get pixel value at x and y co-ordinate
					pixWrite.setArgb(x, y, bi.getRGB(x, y));
				}
			}
		} else
			System.out.println("buffered image is empty");
		return newImage;
	}

	private Image Mat2Image(Mat frame) {
		// temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode image frame into PNG format
		Imgcodecs.imencode(".PNG", frame, buffer);
		// build image from encoded buffered data
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	private void showInformationAlert(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Alert");
		alert.setHeaderText(string);
		String s = "Would be good to display some more details here!! ";
		alert.setContentText(s);
		alert.show();
	}
}