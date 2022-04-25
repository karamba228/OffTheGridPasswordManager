package application.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class SceneController {
	@FXML
	abstract protected void switchToMain(ActionEvent event) throws IOException;
}
