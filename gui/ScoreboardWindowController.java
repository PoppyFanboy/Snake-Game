package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import poppyfanboy.snakegame.data.*;
import static poppyfanboy.snakegame.Main.HOME;
import poppyfanboy.snakegame.data.Board;

public class ScoreboardWindowController {
    @FXML private Scene scoreboardScene;
    @FXML private TabPane tabPane;
    @FXML private FlowPane scoreboardPane1, scoreboardPane2, scoreboardPane3;
    
    @FXML
    private void initialize() {

    }

    // arguments indicate the record to be highlighted
    void fillWithContent(int boardNumber, int position) {
        SingleSelectionModel<Tab> select = tabPane.getSelectionModel();
        select.select(boardNumber);

        FlowPane[] boards = { this.scoreboardPane1, this.scoreboardPane2, this.scoreboardPane3 };
        for (int i = 0; i < boards.length; i++) {
            try {
                Board board = Board.getScoreBoard(i);
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
}
