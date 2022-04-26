package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SettingsController extends SceneController implements Initializable{
	
	private Settings preferences;
	@FXML
	private ColorPicker colorPicker1,colorPicker2,colorPicker3,colorPicker4,colorPicker5,colorPicker6;
	@FXML
	private Rectangle rectangleButtonShape;
	@FXML
	private VBox colorPickers;
	private Animations animation;
	@FXML
	private Button mainMenu,status,save;
	@FXML
	private Circle circle1, circle2;
	private Scene scene;
	
	/**
	 * This method will disable all animation when pressed. If the user decides to do so.
	 * @param event the action for the button 
	 */
	@FXML
	private void disableAnimations(ActionEvent event) {
		if(animation.gettAnimationStatus() == false) {
			colorPickers.setDisable(true);
			animation.setAnimationStatus(true);
			status.setText("Enable");
		}
		else {
			animation.setAnimationStatus(false);
			colorPickers.setDisable(false);
			status.setText("Disable");
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
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("application/view/Main.fxml"));
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
	 * This method will create a new settings object with the preferred colors the user has selected and will call the static method to remember
	 * the preferences.
	 * @param e the button event
	 */
	@FXML
	private void savePreferences(ActionEvent e) {
			Settings settings = new Settings(colorPicker1.getValue().toString(), colorPicker2.getValue().toString(), colorPicker3.getValue().toString()
					,colorPicker4.getValue().toString(), colorPicker5.getValue().toString(), colorPicker6.getValue().toString(), animation.gettAnimationStatus());
			try {
				Preferences.setPreferences(settings);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			preferences = Preferences.loadPreferences();
			animation = new Animations();
			animation.setAnimationStatus(preferences.isDisableAnimation());
			if(preferences.isDisableAnimation() == true)
				status.setText("Enable");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * This method detects when the mouse entered one of the three smaller circles that
	 * contain the buttons. it will proceed to play the circle rotation animation and will 
	 * play the stroke color fill animation aka the fillAnimation method.
	 * @param event this event is when the mouse enters the circle diameter
	 */
	@FXML 
	private void mouseEnteredButton(MouseEvent event){
		if(event.getSource() == mainMenu) {
			animation.playRotationAnimation(circle1,false,360,0.5,1,1);
			animation.fillAnimation(circle1,Color.valueOf(preferences.getColorMainMenuForSettings()));
		}
		else if(event.getSource() == status) {
			animation.playRotationAnimation(circle2,false,360,0.5,1,1);
			animation.fillAnimation(circle2,Color.valueOf(preferences.getsettingsColorDisableButton()));
		}
		else if(event.getSource() == save) {
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
			animation.playRotationAnimation(circle1,true,-360,0.5,1,1);
			animation.fillAnimation(circle1,Color.GRAY);
		}
		else if(event.getSource() == status) {
			animation.playRotationAnimation(circle2,false,-360,0.5,1,1);
			animation.fillAnimation(circle2,Color.GRAY);
		}
		else if(event.getSource() == save) {
			animation.fillRectangle(rectangleButtonShape, Color.valueOf("#ffffff00"));
		}
	}
	public void setScene(Scene scene) {
		this.scene = scene;;
		
	}

}
