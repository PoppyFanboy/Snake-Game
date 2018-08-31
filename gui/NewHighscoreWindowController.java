package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private int labyrinthNumber;

    @FXML
    private void initialize() { }

    @FXML
    private void handle(final KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            confirmInput();
        }
    }

    @FXML
    private void confirmInput() {
        Stage window = (Stage) newHighscoreScene.getWindow();
        if (!Record.removeExcessWhitespaces(nickTextField.getText()).equals("")) {

            Record newRecord = new Record(nickTextField.getText(), newScore);
            int position = Board.saveRecord(newRecord, labyrinthNumber);

            if (position > 0) {
                GameWindowController.openScoreboard(labyrinthNumber, position);
            }

            window.close();
        }
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }

    public void setLabyrinthNumber(int labyrinthNumber) {
        this.labyrinthNumber = labyrinthNumber;
    }
}
