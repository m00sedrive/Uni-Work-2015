package application;

import java.awt.Button;
import java.awt.TextField;
import java.util.TimerTask;

import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.videoio.VideoCapture;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class FXController {
	
	@FXML
	private TextField numBoards;
	@FXML
	private TextField numHorCorners;
	@FXML
	private TextField numVertCorners;
	@FXML
	private Button applyButton;
	@FXML
	private Button cameraButton;
	@FXML
	private Button snapshotButton;
	@FXML
	private ImageView originalFrame;
	@FXML
	private ImageView calibratedFrame;
	
	private int boardsNumber;
	private int numCornersHor;
	private int numCornersVer;
	private MatOfPoint3f obj;
	private boolean cameraActive = false;
	private VideoCapture capture;

	
	@FXML
	private void startCamera()
	{
		if(!this.cameraActive)
		{
			if(this.capture.open(0));
			{
				if(this.capture.isOpened())
				{
					this.cameraActive = true;
					
					TimerTask FrameGrabber = new TimerTask() {
					@Override					
						public void run() 
						{
							//CamStream=grabFrame();
						}
					};
				}
			}
			
		}
	}
	
	@FXML
	private void takeSnapshot()
	{
		
	}
	
	@FXML
	private void updateSettings()
	{
		this.boardsNumber = Integer.parseInt(this.numBoards.getText());
		this.numCornersHor = Integer.parseInt(this.numHorCorners.getText());
		this.numCornersVer = Integer.parseInt(this.numVertCorners.getText());
		int numSquare = this.numCornersHor * this.numCornersVer;
		for (int j=0; j<numSquare; j++)
		{
			obj.push_back(new MatOfPoint3f(new Point3(j / this.numCornersHor / this.numCornersVer, j % this.numCornersVer, 0.0f)));
		}
		//this.cameraButton.setDisable(false); 
	}
	
}
