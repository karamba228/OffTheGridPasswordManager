package application;

import application.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/** 4/1/2022
 * This is the Main class that contains the first scene and the primary stage of the program
 * @authors Garrett Ashley, Evan Ashley, Eddie Morales
 *
 */
public class Main extends Application{
	/**
	 *This method loads in the necessary files to display the objects and sets the dimensions of the stage.
	 *It is use to invoke the lauch method in the main method
	 */
	@Override
	public void start(Stage primaryStage) {
		try {			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/view/Main.fxml"));
			Parent root = (Parent)loader.load();
			MainController controller = loader.getController();
			Scene scene = new Scene(root,600,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Off The Grid");
			controller.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * invokes launch method that starts the program
	 * @throws Exception in case of a fatal error
	 */
	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
