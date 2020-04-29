package poppyfanboy.snakegame.gui;

import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.stage.Modality;
import javafx.stage.Stage;
import poppyfanboy.snakegame.data.*;
import poppyfanboy.snakegame.data.Board;

import java.io.FileInputStream;
import java.io.IOException;

public class ScoreboardWindowController {
    @FXML private TabPane tabPane;
    @FXML private FlowPane scoreboardPane1, scoreboardPane2, scoreboardPane3, scoreboardPane4, scoreboardPane5;
    
    @FXML
    private void initialize() {

    }

    // arguments indicate the record to be highlighted
    private void fillWithContent(int boardNumber, int position) throws IOException {
        SingleSelectionModel<Tab> select = tabPane.getSelectionModel();
        select.select(boardNumber);

        FlowPane[] boards = { this.scoreboardPane1, this.scoreboardPane2, this.scoreboardPane3,
            scoreboardPane4, scoreboardPane5};
        for (int i = 0; i < boards.length; i++) {
            try {
                Board board = Board.getScoreBoard(i, "data/Scoreboard.tab");
                for (int j = 0; j < board.size(); j++) {
                    Record record = board.get(j);
                    Label score =   new Label(String.format("%-4s", (j + 1) + ".") +
                            String.format("%-25s", Record.truncateString(record.getNick(), 24)) +
                            String.format("%d", record.getScore()));

                    score.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
                    if (j == position - 1 && boardNumber == i) {
                        score.setTextFill(Color.RED);
                    }
                    boards[i].getChildren().add(score);
                }
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

    static void openScoreboard(int boardNumber, int position, Stage stage) {
        Stage scoreboardWindow = new Stage();
        scoreboardWindow.setTitle("Scoreboard");

        try (InputStream fxmlStream = NewHighscoreWindowController.class
                .getResourceAsStream("/gui/FXMLScoreboardWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = loader.load(fxmlStream);
            scoreboardWindow.setScene(scene);

            ScoreboardWindowController controller = loader.<ScoreboardWindowController>getController();
            controller.fillWithContent(boardNumber, position);

            // modality
            if (stage != null) {
                scoreboardWindow.initOwner(stage);
                scoreboardWindow.initModality(Modality.APPLICATION_MODAL);
                scoreboardWindow.showAndWait();
            } else {
                scoreboardWindow.show();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            scoreboardWindow.close();
        }
    }
}
