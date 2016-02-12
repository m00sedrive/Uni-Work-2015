package application;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginManager {

	private Scene scene;
	
	public LoginManager(Scene scene)
	{
		this.scene = scene;
	}
	
	public void authenticated(String sessionID)
	{
		showMainView(sessionID);
	}
	
	public void training() 
	{
		showTrainingView();
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
	
	public void showTrainingView()
	{
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ScreenTraining.fxml"));
			scene.setRoot((Parent) loader.load());
			TrainingController controller = loader.<TrainingController>getController();
			controller.init(this);
		} catch (IOException ex) {
			Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);

		}
	}
}