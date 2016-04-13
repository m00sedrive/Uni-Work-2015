package application;

import java.util.ArrayList;

import database.Database;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Controls the main application screen */
public class TrainingController {

	@FXML
	private Button add_images;
	@FXML
	private Button loadDatabase;
	@FXML
	private Button logoutButton;
	@FXML
	private Button returnToMainScreen;
	@FXML
	private Button selectImageButton;
	@FXML
	private Button harrysPCA;
	@FXML
	private GridPane image_grid;
	@FXML
	private ImageView image2train;
	@FXML
	private VBox vBox_right;
	@FXML
	private HBox hBox_imgGallery_r1;
	@FXML
	private HBox hBox_imgGallery_r2;
	@FXML
	private HBox hBox_imgGallery_r3;
	@FXML
	private TextArea textAreaTrain;

	public Database database;

	private ArrayList<ImageView> database_image;
	private ArrayList<ImageView> placeHolderImages;

	public void initSessionID(final LoginManager loginManager, String sessionID) {

		textAreaTrain.setEditable(false);
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loginManager.logout();
			}
		});
		returnToMainScreen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loginManager.authenticated(sessionID);
			}
		});
		/*
		 * for(int i=0;i<8;i++) {
		 * 
		 * placeHolderImages = new ArrayList<ImageView>(); BufferedImage
		 * imagePH; try { imagePH = ImageIO.read(new
		 * File("images/userImage.png")); WritableImage wi = new
		 * WritableImage(imagePH.getWidth(), imagePH.getHeight()); Image image =
		 * SwingFXUtils.toFXImage(imagePH, wi); ImageView iv = new
		 * ImageView(image); iv.setFitHeight(100); iv.setFitWidth(80);
		 * placeHolderImages.add(i, iv); } catch (IOException e) {
		 * e.printStackTrace(); } } for(int i=0; placeHolderImages.length;i++) {
		 * if (i < 4) { System.out.println("Breakpoint: " + i + " before h1");
		 * hBox_imgGallery_r1.getChildren().add(placeHolderImages.get(i));
		 * System.out.println("Breakpoint: " + i + "after h1"); } else
		 * hBox_imgGallery_r2.getChildren().add(placeHolderImages.get(i)); }
		 */
	}

	@FXML
	public void selectImage() {

		FileChooser sfc = new FileChooser();
		sfc.setVisible(true);
	}

	@FXML
	public void loadDatabase() {
		database = new Database();
		// set up database
		database.setUpDatabase();

		/*
		 * // displaying images from database in GUI database_image = new
		 * ArrayList<ImageView>(); // load buffered image set into image array
		 * for (int i = 0; i < database.getPersonImageSet().length; i++) {
		 * BufferedImage temp_img = database.getPerson(i).getImage();
		 * WritableImage wi = new WritableImage(temp_img.getWidth(),
		 * temp_img.getHeight()); Image image = SwingFXUtils.toFXImage(temp_img,
		 * wi); ImageView iv = new ImageView(image); iv.setFitHeight(100);
		 * iv.setFitWidth(80); database_image.add(i, iv); if (i < 4) {
		 * hBox_imgGallery_r1.getChildren().remove(placeHolderImages.get(i));
		 * hBox_imgGallery_r1.getChildren().add(database_image.get(i)); } else
		 * hBox_imgGallery_r2.getChildren().remove(placeHolderImages.get(i));
		 * hBox_imgGallery_r2.getChildren().add(database_image.get(i)); }
		 */
		textAreaTrain.setText("Database loaded");

	}

	@FXML
	public void trainImages() {
		CustomPCA cpca = new CustomPCA();
		if (database != null) {
			cpca.setPCAData(8,database);
			cpca.prepareFaceMatrix();
			cpca.performPCA();
			textAreaTrain.setText("Training Complete!");
		} else {
			// display to user database not set
			textAreaTrain.setText("No database detected:  Please load selected database!");
		}
	}
	
	@FXML
	public void testAlternativePca() {
		API.PCA.PCA pca = new API.PCA.PCA();
		pca.image_data = CustomPCA.getDebugMatrix();
		pca.run();
	}
}