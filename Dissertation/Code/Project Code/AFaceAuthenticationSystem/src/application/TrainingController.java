package application;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


/** Controls the main application screen */
public class TrainingController {
  
	@FXML private Button add_images;
	@FXML private Button loadImages;
	@FXML private Button logoutButton;
	@FXML private Button returnToMainScreen; 
	private LoginManager loginManager;
	private String sessionID;
	  
	  public void initSessionID(final LoginManager loginManager, String sessionID) { 

		  this.loginManager = loginManager;
		  this.sessionID = sessionID;
		  
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
	  
	  @FXML public void loadImages() {
	  }
  
	  @FXML public void addImages(){
	  }
}