package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import poppyfanboy.snakegame.data.Board;
import poppyfanboy.snakegame.data.Record;

public class NewHighscoreWindowController {
    @FXML private Pane newHighscorePane;
    @FXML private Scene newHighscoreScene;
    @FXML private Text congratsText;
    @FXML private TextField nickTextField;

    private int newScore;

    @FXML
    private void initialize() {

    }

    @FXML
    private void confirmInput() {
        Stage window = (Stage) newHighscoreScene.getWindow();
        if (!nickTextField.getText().equals("")) {
            Record newRecord = new Record(nickTextField.getText(), newScore);
            Board.saveRecord(newRecord, 0);
            window.close();
        }
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }
}
