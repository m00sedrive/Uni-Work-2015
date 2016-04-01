package application;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class LoginController {

	@FXML private TextField usernameEntered;
	@FXML private PasswordField passwordEntered;
	@FXML private Button loginButton;
	@FXML private ImageView loginImage;
	@FXML private Button logout_button;
	@FXML private Button capture;
	
	// test purposes, will store encode elsewhere
	String username = "Tom";
	String password = "hello";
	
	public void initialize() {}
	
	public void initManager(final LoginManager loginManager)
	{
		try{
			BufferedImage image = ImageIO.read(new File("images/userImage.png"));
			loginImage.setImage(bufferedImg2Img(image));	
		}catch(Exception ex)
		{
			System.out.println("Error: " + ex.getMessage());
		}
		loginButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event) {
				String sessionID = authorize();
				if(sessionID != null)
				{
					loginManager.authenticated(sessionID);
				}
			}
		});
		logout_button.setOnAction(new EventHandler<ActionEvent>(){
			@Override public void handle(ActionEvent event) {
				String sessionID = authorize();
				if(sessionID != null)
				{
					exit(0);
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
			// alert user and print to console wrong username or password entered!
			System.out.println("Invalid username or password: Please re-enter!!");
			JOptionPane.showMessageDialog(null, "Invalis username or password: Please re-enter!");
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
	
	private static void exit(int status) {
		System.exit(status);
	}
	
	private WritableImage bufferedImg2Img(BufferedImage bi)
	{
		//write buffered image to image
		WritableImage newImage = null;
		if(bi != null)
		{
			// create writable image with same width and height as buff image
			newImage = new WritableImage(bi.getHeight(), bi.getWidth());
			PixelWriter pixWrite = newImage.getPixelWriter();
			
			for(int x=0; x<bi.getWidth(); x++)
			{
				for(int y=0; y<bi.getHeight(); y++)
				{
					//get pixel value at x and y co-ordinate
					pixWrite.setArgb(x,y,bi.getRGB(x,y));
				}
			}
		}
		else
			System.out.println("buffered image is empty");
		return newImage;
	}
}
