package application;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginManager {

	private Scene scene;
	private Stage stage;
	
	public LoginManager(Scene scene, Stage stage)
	{
		this.scene = scene;
		this.stage = stage;
	}
	
	public void authenticated(String sessionID)
	{
		showMainView(sessionID);
	}
	
	public void training(String sessionID) 
	{
		showTrainingView(sessionID);
	}
	
	public void logout()
	{
		showLoginScreen();
	}
	
	public void showLoginScreen()
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ScreenLogin.fxml"));
			scene.setRoot((Parent) loader.load());
			LoginController controller = loader.<LoginController>getController();
			controller.initManager(this);
		} catch (IOException ex) {
			Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void showMainView(String sessionID)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ScreenMain.fxml"));
			scene.setRoot((Parent) loader.load());
			MainController controller = loader.<MainController>getController();
			controller.initSessionID(this, sessionID);
		} catch (IOException ex) {
			Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
	
	public void showTrainingView(String sessionID)
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ScreenTraining.fxml"));
			scene.setRoot((Parent) loader.load());
			TrainingController controller = loader.<TrainingController>getController();
			controller.initSessionID(this, sessionID);
		} catch (IOException ex) {
			Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);

		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}