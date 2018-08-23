package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.TextField;

public class NewHighscoreWindowController {
    @FXML private Pane newHighscorePane;
    @FXML private Scene newHighscoreScene;
    @FXML private Text congratsText;
    @FXML private TextField nickTextField;

    @FXML
    private void initialize() {

    }

    @FXML
    private void confirmInput() {
        Stage window = (Stage) newHighscoreScene.getWindow();
        window.close();
    }
}
