package application;

import java.awt.Button;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.stage.Stage;


/** Controls the main application screen */
public class TrainingController {
  
	@FXML private Button addImages;
	@FXML private Button loadImages;
	@FXML private Button logoutButton;
	@FXML private Button returnToMainScreen; 

	  
	  public void init(final LoginManager loginManager) { 

		   /* returnToMainScreen.setOnAction(new EventHandler<ActionEvent>() {
			  @Override public void handle(ActionEvent event) {
			    loginManager.training();
			  }
			});
		    logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			  @Override public void handle(ActionEvent event) {
			    loginManager.logout();
			  }
			});
		    */
	  }
	  
	  @FXML public void loadImages() {
	  }
  
	  @FXML public void addImages(){
	  }
}