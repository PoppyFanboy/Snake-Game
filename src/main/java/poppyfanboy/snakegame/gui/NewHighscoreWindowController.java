package poppyfanboy.snakegame.gui;

import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import poppyfanboy.snakegame.data.Board;
import poppyfanboy.snakegame.data.Record;

import java.io.FileInputStream;
import java.io.IOException;

public class NewHighscoreWindowController extends ScoreboardWindowController {
    @FXML private Scene newHighscoreScene;
    @FXML private TextField nickTextField;

    private Stage primaryStage;
    private int newScore;
    private int labyrinthType;

    @FXML
    private void handle(final KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            confirmInput();
        }
    }

    @FXML
    private void confirmInput() throws IOException {
        Stage window = (Stage) newHighscoreScene.getWindow();
        if (!Record.removeExcessWhitespaces(nickTextField.getText()).equals("")) {

            Record newRecord = new Record(nickTextField.getText(), newScore);
            int position = Board.saveRecord(newRecord, labyrinthType, "data/Scoreboard.tab");

            if (position > 0) {
                ScoreboardWindowController.openScoreboard(labyrinthType, position, primaryStage);
            }

            window.close();
        }
    }

    static void openNewHighscoreWindow(int newScore, int labyrinthType, Stage primaryStage) {
        Stage newHighscoreWindow = new Stage();
        newHighscoreWindow.setTitle("New Highscore: " + newScore + "!");

        try (InputStream fxmlStream = NewHighscoreWindowController.class
                .getResourceAsStream("/gui/FXMLNewHighscoreWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = loader.load(fxmlStream);
            newHighscoreWindow.setScene(scene);
            newHighscoreWindow.initOwner(primaryStage);
            newHighscoreWindow.initModality(Modality.APPLICATION_MODAL);

            NewHighscoreWindowController controller = loader.getController();
            controller.newScore = newScore;
            controller.labyrinthType = labyrinthType;
            controller.primaryStage = primaryStage;

            newHighscoreWindow.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            newHighscoreWindow.close();
        }
    }
}
