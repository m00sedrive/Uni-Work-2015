package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

	@FXML private TextField usernameEntered;
	@FXML private PasswordField passwordEntered;
	@FXML private Button loginButton;
	
	String username = "Tom";
	String password = "hello";
	
	public void initialize() {}
	
	public void initManager(final LoginManager loginManager)
	{
		loginButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event) {
				String sessionID = authorize();
				if(sessionID != null)
				{
					loginManager.authenticated(sessionID);
				}
			}
		});
	}
	
	private String authorize()
	{
		String authorized = null;
		if(usernameEntered.getText().equals(username) && passwordEntered.getText().equals(password))
		{
			authorized = generateSessionID();
		}
		else
		{
			System.out.println("Invalid username or password: Please re-enter!!");
		}
		return authorized;
	}
	
	private static int sessionID = 0;
	
	private String generateSessionID()
	{
		sessionID++;
		String id = null;
		id = "session - " + sessionID;
		return id;
	}
}
