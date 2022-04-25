package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import application.model.Credentials;
import application.model.Preferences;
import application.model.Settings;
import application.view.Animations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
/** 4/1/2022
 * This is the main controller that manages the data that is requested in the
 * main menu and also manages the visuals in the main menu.
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales,Eduardo Riveragarza
 *
 */
public class FileCreationController extends SceneController implements Initializable {
	/**
	 * This controller contains anything related so it can be concise
	 */
	private FileHandlingController fileHandlingController;	
	/**
	 * This arraylist contains the credentials that were inputted from the textfield and text area 
	 */
	private List<Credentials> credentials = new ArrayList<Credentials>();	
	/**
	 * This is a text area for the notes section to input the desired notes without restriction
	 */
	@FXML
	private TextArea notesInput;	
	/**
	 * Test contains the text to input the desired data
	 */
	@FXML
	private TextField userIDInput,passwordInput,urlInput;	
	/**
	 * This is the root pane that holds all of the nodes on top of it
	 */
	@FXML
	private BorderPane mainPane;
	/**
	 * This class will call any custom animation that wants to be played
	 */
	private Animations animation;
	/**
	 * The circle will be set to a different color and will have an animation
	 */
	@FXML
	private Circle circle,circle1;
	/**
	 * This shape is behind the continuePrompt button to give its shape. It also plays an animation when hovered
	 */
	@FXML
	private Rectangle rectangleButtonShape;	
	/**
	 * These buttons have the functionality of going back to the main menu, done with input and continue inputing data.
	 */
	@FXML
	private Button mainMenu,done,continuePrompt;
	/**
	 * This method detects when the mouse entered one of the three smaller circles that
	 * contain the buttons. it will proceed to play the circle rotation animation and will 
	 * play the stroke color fill animation aka the fillAnimation method.
	 * @param event this event is when the mouse enters the circle diameter
	 */
	private Settings settings;
	
	private Scene scene;
	/**
	 * This method detects when the mouse entered one of the three smaller circles that
	 * contain the buttons. it will proceed to play the circle rotation animation and will 
	 * play the stroke color fill animation aka the fillAnimation method.
	 * @param event this event is when the mouse enters the circle diameter
	 */
	@FXML 
	private void mouseEnteredButton(MouseEvent event){
		if(event.getSource() == mainMenu) {
			animation.playRotationAnimation(circle,false,360,0.5,1,1);
			animation.fillAnimation(circle,Color.valueOf(settings.getColorMainMenuForNewFile()));
		}
		else if(event.getSource() == done) {
			animation.playRotationAnimation(circle1,false,360,0.5,1,1);
			animation.fillAnimation(circle1,Color.valueOf(settings.getColorMainMenuForNewFile()));
		}
		else if(event.getSource() == continuePrompt) {
			animation.fillRectangle(rectangleButtonShape, Color.valueOf("#0b3e75"));
		}
	}
	/**
	 * this method detects when the mouse exits the button that is in the circle.(The mouse and circle
	 * basically match the same diameter, but technically its the button that contains the action). It will 
	 * proceed to play the circle rotation animation but in reverse,and will set the color to be grey to notify that
	 * the mouse is not in the area of the circle anymore.
	 * @param event this event is when the mouse exists the circle diameter
	 */
	@FXML
	private void mouseExitedButton(MouseEvent event){
		if(event.getSource() == mainMenu) {
			animation.playRotationAnimation(circle,true,-360,0.5,1,1);
			animation.fillAnimation(circle,Color.GRAY);
		}
		else if(event.getSource() == done) {
			animation.playRotationAnimation(circle1,false,-360,0.5,1,1);
			animation.fillAnimation(circle1,Color.GRAY);
		}
		else if(event.getSource() == continuePrompt) {
			animation.fillRectangle(rectangleButtonShape, Color.valueOf("#ffffff00"));
		}
	}
	/**
	 * This method will be called whenever the user presses the main button. It will
	 * take the user back to the main menu
	 * @param event the action whenever the button is pressed
	 * @throws IOException in case of a fatal error
	 */
	@Override
	protected void switchToMain(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Main.fxml"));
		Parent root = (Parent)loader.load();
		MainController controller = loader.getController();
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		scene.setRoot(root);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		controller.setScene(scene);
		stage.show();	
	}
	/**
	 * This method will process the data that is finished being inputed once the user hits the 'done' button.
	 * It will prompt the user for confirmation, then will continue with the file handling process.
	 * @param event for whenever the button is pressed it will perform the action
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	private void done(ActionEvent event) throws IOException {
		if(userIDInput.getText() == "" && passwordInput.getText() == "" && urlInput.getText() == "" && notesInput.getText() == "") {
			if(credentials.size() == 0) {
				Alert alert = new Alert(AlertType.WARNING, "You must enter credentials, try again", ButtonType.OK);
				alert.showAndWait();
				return;
			}
			Alert alert = new Alert(AlertType.CONFIRMATION, "Select yes if you are done entering your credentials", ButtonType.YES,ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				fileHandlingController.handleFileEncryptionCreation(mainPane,credentials);
				credentials.clear();
			}
		}
		else {
			credentials.add(new Credentials(userIDInput.getText(),passwordInput.getText(),urlInput.getText(),notesInput.getText()));
			userIDInput.clear();
			passwordInput.clear();
			urlInput.clear();
			notesInput.clear();
			Alert alert = new Alert(AlertType.CONFIRMATION, "Select yes if you are done entering your credentials", ButtonType.YES,ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				fileHandlingController.handleFileEncryptionCreation(mainPane,credentials);
				credentials.clear();
			}
		}
	}
	/**
	 * This method will collect all the inputed data and reset the parameters to offer the ability for the user to enter new inputs
	 * @param event whenever the new input button is pressed this method will be invoked. It will get the action
	 */
	@FXML
	private void newEntry(ActionEvent event) {
		if(userIDInput.getText() == "" && passwordInput.getText() == "" && urlInput.getText() == "" && notesInput.getText() == "") {
			Alert alert = new Alert(AlertType.WARNING, "You must enter credentials, try again", ButtonType.OK);
			alert.showAndWait();
			return;
		}
		credentials.add(new Credentials(userIDInput.getText(),passwordInput.getText(),urlInput.getText(),notesInput.getText()));
		userIDInput.clear();
		passwordInput.clear();
		urlInput.clear();
		notesInput.clear();
	}
	/**
	 *This method will be called the moment the scene is transitioned and will get all the necessary classes and set
	 *up the stage for use.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	try {
        	animation = new Animations();
        	fileHandlingController = new FileHandlingController();
			settings = Preferences.loadPreferences();
			animation.setAnimationStatus(settings.isDisableAnimation());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
