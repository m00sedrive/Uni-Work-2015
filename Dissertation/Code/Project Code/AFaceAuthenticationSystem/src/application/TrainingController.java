package application;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import database.Database;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Controls the main application screen */
public class TrainingController {
  
	@FXML private Button add_images;
	@FXML private Button loadDatabase;
	@FXML private Button logoutButton;
	@FXML private Button returnToMainScreen;
	@FXML private Button selectImageButton;
	@FXML private GridPane image_grid;
	@FXML private ImageView image2train;
	@FXML private VBox vBox_right;
	@FXML private HBox hBox_imgGallery_r1;
	@FXML private HBox hBox_imgGallery_r2;
	@FXML private HBox hBox_imgGallery_r3;
	
	Database database = new Database();
	
	private ArrayList<ImageView> database_image = new ArrayList<ImageView>();
	
	  public void initSessionID(final LoginManager loginManager, String sessionID) { 
		  
		    logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			  @Override public void handle(ActionEvent event) {
			    loginManager.logout();
			  }
			});
		    returnToMainScreen.setOnAction(new EventHandler<ActionEvent>() {
			  @Override public void handle(ActionEvent event) {
			    loginManager.authenticated(sessionID);
			  }
			});
	  }
	  
	  @FXML public void selectImage() {

		  //FileChooserClass fc = new FileChooserClass();
		  
	  }
	  
	  @FXML public void loadDatabase() {
		  
		  // setup database
		  database.setUpDatabase();
		  
		  // load buffered image set into image array 
		  for(int i=0;i< database.getPersonImageSet().length; i++){
			  BufferedImage temp_img = database.getPerson(i).getImage();
			  WritableImage wi = new WritableImage(temp_img.getWidth(), temp_img.getHeight()); 
			  Image image = SwingFXUtils.toFXImage(temp_img, wi);		  
			  ImageView iv = new ImageView(image);
			  iv.setFitHeight(100);
			  iv.setFitWidth(80);
			  database_image.add(i, iv);
			  
			  if(i < 4) {
				  hBox_imgGallery_r1.getChildren().add(database_image.get(i));
			  }
			  else
				  hBox_imgGallery_r2.getChildren().add(database_image.get(i));
		  }
		 
	  }
  
	  @FXML public void addImages(){
		  CustomPCA cpca = new CustomPCA();
		  cpca.setPCAData(8, database);
		  cpca.loadImageSet(database);
		  cpca.performPCA();
	  }
}