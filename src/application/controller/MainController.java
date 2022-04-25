package application.controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.model.Encryption;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
/** 4/1/2022
 * This is the main controller that manages the data that is requested in the
 * main menu and also manages the visuals in the main menu.
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales
 *
 */
public class MainController implements Initializable {
	/**
	 * This will get the preferences loaded by the preferences class and perform the necessary action to get the user preferences
	 * shown.
	 */
	private Settings preferences;
	
	private FileHandlingController fileHandlingController;
	/**
	 * all different circle objects that are shown on the screen.
	 * Each will be set to a different color and will have an animation
	 * 
	 */
	@FXML
	private Circle circle,circle1,circle2,circle3;
	/**
	 * This class will call any custom animation that wants to be played
	 */
	private Animations animation;
	/**
	 * This shape is a custom shape made in svg path builder and it looks like a key hole that is in
	 * the middle of the circle.
	 */
	@FXML
	private SVGPath Key;
	/**
	 * This is the root pane that holds all the objects that are stacked on it. 
	 */
	@FXML
	private BorderPane MainPane;
	/**
	 * These buttons are in the 3 smaller circles and have different functionality when pressed.
	 */
	@FXML
	private Button locate,newFile,settings;
	
	private Scene scene;
	/**
	 * This method detects when the mouse entered one of the three smaller circles that
	 * contain the buttons. it will proceed to play the circle rotation animation and will 
	 * play the stroke color fill animation aka the fillAnimation method.
	 * @param event this event is when the mouse enters the circle diameter
	 */
	@FXML 
	private void mouseEnteredButton(MouseEvent event){
		if(event.getSource() == locate) {
			animation.playRotationAnimation(circle1,false,360,0.5,1,1);
			animation.fillAnimation(circle1,Color.valueOf(preferences.getColorLocate()));
		}
		else if(event.getSource() == newFile) {
			animation.playRotationAnimation(circle2,false,360,0.5,1,1);
			animation.fillAnimation(circle2,Color.valueOf(preferences.getColorNewFile()));
		}
		else if(event.getSource() == settings) {
			animation.playRotationAnimation(circle3,false,360,0.5,1,1);
			animation.fillAnimation(circle3,Color.valueOf(preferences.getColorSettings()));
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
		if(event.getSource() == locate) {
			animation.playRotationAnimation(circle1,true,-360,0.5,1,1);
			animation.fillAnimation(circle1,Color.GRAY);
		}
		else if(event.getSource() == newFile) {
			animation.playRotationAnimation(circle2,true,-360,0.5,1,1);
			animation.fillAnimation(circle2,Color.GRAY);
		}
		else if(event.getSource() == settings) {
			animation.playRotationAnimation(circle3,false,-360,0.5,1,1);
			animation.fillAnimation(circle3,Color.GRAY);
		}
	}
	/**
	 * This method will detect when the mouse is being dragged and will accept files that
	 * want to be handled.
	 * @param event this event is when the mouse drag is detected
	 */
	@FXML
	private void dragDetected(DragEvent event) {
		if(event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}
	/**
	 * This method will handle whenever a file has been dropped onto the program. It will only accept one folder at a time.
	 * These files are contained in a folder and only will be processed when its stored in a folder. If more than one folder or
	 * file is dropped, then it will raise an error. else it will continue to check the individual files in the folder.
	 * @param event the event is the drag drop event which detect when a file has been dropped on the the program
	 * @throws Exception this will throw an exception when a fatal error has occurred
	 */
	@FXML
	private void handleDrop(DragEvent event) throws Exception{
		List<File> files = event.getDragboard().getFiles();
		if (files.size() > 1) {
			circle.setStroke(Color.RED);
			Key.setStroke(Color.RED);
			animation.playRotationAnimation(circle,false,360,10,1,10);
			raiseAlert(AlertType.ERROR,"Invalid operation.","Please locate or drag the files your password contents are stored in."); 
			files.clear();
			event.consume();
			return;
		}
		else {
			Encryption enc = fileHandlingController.handleFileDecryption(files.get(0),circle,Key);
			files.clear();
			event.consume();
			if(enc == null)
        		return;
			var fadeOutTransition = animation.fadeOut(MainPane);
		    fadeOutTransition.setOnFinished(evt -> {
		    	try {
					switchToCredentialTableView(enc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    });
		}
		
	}
	/**
	 * This method is the alternative to the drag and drop feature, just incase you don't
	 * have the file with you and it's located else where. It will get the root stage and display the 
	 * operating systems file manager to retrieve a folder only. It will then proceed to the handleFiles method
	 * @param event this event is when button is pressed on action.
	 */
	@FXML
	private void locateFile(ActionEvent event) {
	    try {
	        // create a File chooser
	    	Stage stage = (Stage) MainPane.getScene().getWindow();
	    	DirectoryChooser chooser = new DirectoryChooser();
	        chooser.setTitle("Open Directory");
	        File selectedFile = chooser.showDialog(stage);
	        if (selectedFile != null) {
	        	Encryption enc = fileHandlingController.handleFileDecryption(selectedFile,circle,Key);
	        	if(enc == null)
	        		return;
	        	var fadeOutTransition = animation.fadeOut(MainPane);
			    fadeOutTransition.setOnFinished(evt -> {
			    	try {
						switchToCredentialTableView(enc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    });
	        }
	 
	    }catch (Exception e) {
	    	raiseAlert(AlertType.ERROR,"Invalid operation.","Please locate or drag the files your password contents are stored in."); 
	    }
	}
	/**
	 * This method is responsible for displaying the type of alert, header, and context of an alert.
	 * it will also set the color of the key circle to a neutral color previously being red for an error.
	 * @param alertType the type of alert the user wants to display
	 * @param header the header of the text
	 * @param content whatever the context will be of the alert
	 */
	private void raiseAlert(AlertType alertType,String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
		alert.setOnCloseRequest((e -> {circle.setStroke(Color.GRAY);Key.setStroke(Color.GRAY);}));
		
	}
	/**
	 * This method will set up a new scene whenever the button called 'New file' is pressed
	 * it will then prompt the user to enter there credentials for encryption.
	 * @param event gets the button action for New File when it is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	private void switchToFileCreation(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("application/view/FileCreation.fxml"));
		Parent root = (Parent)loader.load();
		FileCreationController controller = loader.getController();
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		scene.setRoot(root);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		controller.setScene(scene);
		stage.show();
	}
	
	private void switchToCredentialTableView(Encryption enc) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("application/view/CredentialView.fxml"));
		loader.setControllerFactory(controllerClass -> new CredentialManagerController(enc,scene));
		Parent root = (Parent)loader.load();
		Stage stage = (Stage) MainPane.getScene().getWindow();
		scene.setRoot(root);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		stage.show();
	}
	/**
	 * This method will set up a new scene whenever the button called 'New file' is pressed
	 * it will then prompt the user to enter there credentials for encryption.
	 * @param event gets the button action for New File when it is pressed
	 * @throws IOException in case of a fatal error
	 */
	@FXML
	private void switchToSettings(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("application/view/Settings.fxml"));
		Parent root = (Parent)loader.load();
		SettingsController controller = loader.getController();
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		scene.setRoot(root);
		scene.getStylesheets().add("application/application.css");
		stage.setScene(scene);
		controller.setScene(scene);
		stage.show();	
	}
	/**
	 *This method initializes when the scene is done being generated. the font of the main title is located
	 *and displayed. Something's were successfully done in CSS, but other things like fonts were an issue. So
	 *it was easier to invoke it in java code instead.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        	try {
            	fileHandlingController = new FileHandlingController();
            	animation = new Animations();
				preferences = Preferences.loadPreferences();
				animation.setAnimationStatus(preferences.isDisableAnimation());
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
}
