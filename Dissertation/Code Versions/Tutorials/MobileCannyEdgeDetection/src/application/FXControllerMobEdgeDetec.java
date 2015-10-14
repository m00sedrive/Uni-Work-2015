package application;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXControllerMobEdgeDetec {

    // FXML buttons
    @FXML
    private Button cameraButton;
    // the FXML area for showing the current frame
    @FXML
    private ImageView originalFrame;
    // checkbox for enabling/disabling Canny
    @FXML
    private CheckBox canny;
    // canny threshold value
    @FXML
    private Slider threshold;
    // checkbox for enabling/disabling background removal
    @FXML
    private CheckBox dilateErode;
    // inverse the threshold value for background removal
    @FXML
    private CheckBox inverse;

    // a timer for acquiring the video stream
    private Timer timer;
    // the OpenCV object that performs the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive;
    private Image CamStream;

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    protected void startCamera()
    {
            if (!this.cameraActive)
            {
                    // disable setting checkboxes
                    this.canny.setDisable(true);
                    this.dilateErode.setDisable(true);

                    // start the video capture
                    this.capture.open(0);

                    // is the video stream available?
                    if (this.capture.isOpened())
                    {
                            this.cameraActive = true;

                            // grab a frame every 33 ms (30 frames/sec)
                            TimerTask frameGrabber = new TimerTask() {
                                    @Override
                                    public void run()
                                    {
                                            CamStream = grabFrame();
                                            Platform.runLater(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                            // show the original frames
                                                            originalFrame.setImage(CamStream);
                                                            // set fixed width
                                                            originalFrame.setFitWidth(600);
                                                            // preserve image ratio
                                                            originalFrame.setPreserveRatio(true);

                                                    }
                                            });
                                    }
                            };
                            this.timer = new Timer();
                            this.timer.schedule(frameGrabber, 0, 33);

                            // update the button content
                            this.cameraButton.setText("Stop Camera");
                    }
                    else
                    {
                            // log the error
                            System.err.println("Failed to open the camera connection...");
                    }
            }
            else
            {
                    // the camera is not active at this point
                    this.cameraActive = false;
                    // update again the button content
                    this.cameraButton.setText("Start Camera");
                    // enable setting checkboxes
                    this.canny.setDisable(false);
                    this.dilateErode.setDisable(false);

                    // stop the timer
                    if (this.timer != null)
                    {
                            this.timer.cancel();
                            this.timer = null;
                    }
                    // release the camera
                    this.capture.release();
                    // clean the image area
                    originalFrame.setImage(null);
            }
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Image} to show
     */
    private Image grabFrame()
    {
            // init everything
            Image imageToShow = null;
            Mat frame = new Mat();

            // check if the capture is open
            if (this.capture.isOpened())
            {
                    try
                    {
                            // read the current frame
                            this.capture.read(frame);

                            // if the frame is not empty, process it
                            if (!frame.empty())
                            {
                                    // handle edge detection
                                    if (this.canny.isSelected())
                                    {
                                            frame = this.doCanny(frame);
                                    }
                                    // foreground detection
                                    else if (this.dilateErode.isSelected())
                                    {
                                            frame = this.doBackgroundRemoval(frame);
                                    }

                                    // convert the Mat object (OpenCV) to Image (JavaFX)
                                    imageToShow = mat2Image(frame);
                            }

                    }
                    catch (Exception e)
                    {
                            // log the (full) error
                            System.err.print("ERROR");
                            e.printStackTrace();
                    }
            }

            return imageToShow;
    }

    /**
     * Perform the operations needed for removing a uniform background
     *
     * @param frame
     *            the current frame
     * @return an image with only foreground objects
     */
    private Mat doBackgroundRemoval(Mat frame)
    {
            // init
            Mat hsvImg = new Mat();
            List<Mat> hsvPlanes = new ArrayList<>();
            Mat thresholdImg = new Mat();

            // threshold the image with the histogram average value
            hsvImg.create(frame.size(), CvType.CV_8U);
            Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);
            Core.split(hsvImg, hsvPlanes);

            double threshValue = this.getHistAverage(hsvImg, hsvPlanes.get(0));

            if (this.inverse.isSelected())
                    Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY_INV);
            else
                    Imgproc.threshold(hsvPlanes.get(0), thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);

            Imgproc.blur(thresholdImg, thresholdImg, new Size(5, 5));

            // dilate to fill gaps, erode to smooth edges
            Imgproc.dilate(thresholdImg, thresholdImg, new Mat(), new Point(-1, 1), 6);
            Imgproc.erode(thresholdImg, thresholdImg, new Mat(), new Point(-1, 1), 6);

            Imgproc.threshold(thresholdImg, thresholdImg, threshValue, 179.0, Imgproc.THRESH_BINARY);

            // create the new image
            Mat foreground = new Mat(frame.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
            frame.copyTo(foreground, thresholdImg);

            return foreground;
    }

    /**
     * Get the average value of the histogram representing the image Hue
     * component
     *
     * @param hsvImg
     *            the current frame in HSV
     * @param hueValues
     *            the Hue component of the current frame
     * @return the average value
     */
    private double getHistAverage(Mat hsvImg, Mat hueValues)
    {
            // init
            double average = 0.0;
            Mat hist_hue = new Mat();
            MatOfInt histSize = new MatOfInt(180);
            List<Mat> hue = new ArrayList<>();
            hue.add(hueValues);

            // compute the histogram
            Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));

            // get the average for each bin
            for (int h = 0; h < 180; h++)
            {
                    average += (hist_hue.get(h, 0)[0] * h);
            }

            return average = average / hsvImg.size().height / hsvImg.size().width;
    }

    /**
     * Apply Canny
     *
     * @param frame
     *            the current frame
     * @return an image elaborated with Canny
     */
    private Mat doCanny(Mat frame)
    {
            // init
            Mat grayImage = new Mat();
            Mat detectedEdges = new Mat();

            // convert to grayscale
            Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

            // reduce noise with a 3x3 kernel
            Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));

            // canny detector, with ratio of lower:upper threshold of 3:1
            Imgproc.Canny(detectedEdges, detectedEdges, this.threshold.getValue(), this.threshold.getValue() * 3, 3, false);

            // using Canny's output as a mask, display the result
            Mat dest = new Mat();
            Core.add(dest, Scalar.all(0), dest);
            frame.copyTo(dest, detectedEdges);

            return dest;
    }

    /**
     * Action triggered when the Canny checkbox is selected
     *
     */
    @FXML
    protected void cannySelected()
    {
            // check whether the other checkbox is selected and deselect it
            if (this.dilateErode.isSelected())
            {
                    this.dilateErode.setSelected(false);
                    this.inverse.setDisable(true);
            }

            // enable the threshold slider
            if (this.canny.isSelected())
                    this.threshold.setDisable(false);
            else
                    this.threshold.setDisable(true);
            
            // now the capture can start
            this.cameraButton.setDisable(false);
    }

    /**
     * Action triggered when the "background removal" checkbox is selected
     */
    @FXML
    protected void dilateErodeSelected()
    {
            // check whether the canny checkbox is selected, deselect it and disable
            // its slider
            if (this.canny.isSelected())
            {
                    this.canny.setSelected(false);
                    this.threshold.setDisable(true);
            }

            if(this.dilateErode.isSelected())
                    this.inverse.setDisable(false);
            else
                    this.inverse.setDisable(true);

            // now the capture can start
            this.cameraButton.setDisable(false);
    }

    /**
     * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
     *
     * @param frame
     *            the {@link Mat} representing the current frame
     * @return the {@link Image} to show
     */
    private Image mat2Image(Mat frame)
    {
            // create a temporary buffer
            MatOfByte buffer = new MatOfByte();
            // encode the frame in the buffer, according to the PNG format
            Imgcodecs.imencode(".png", frame, buffer);
            // build and return an Image created from the image encoded in the
            // buffer
            return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

}