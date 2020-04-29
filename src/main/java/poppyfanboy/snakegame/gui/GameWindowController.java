package poppyfanboy.snakegame.gui;

/**
 * The logic behind the main window of the application
 */

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;

import poppyfanboy.snakegame.data.Board;
import poppyfanboy.snakegame.data.options.*;
import poppyfanboy.snakegame.logic.GameState;
import poppyfanboy.snakegame.logic.SnakeGame;

public class GameWindowController implements GameOverListener{
    @FXML private Pane gamePane;
    @FXML private Scene gameScene;
    private Stage stage;

    private SnakeGame game;
    private Options options;

    // Initialization of SnakeGame object (must be called after showing the window)
    public void start(Stage stage) {
        if (this.stage == null) {
            this.stage = stage;
            options = new Options(new Labyrinth(0), GameMode.EASY, 1);
            game = new SnakeGame(gamePane, options);

            // tells the SnakeGame object to notify controller, if the game ends
            game.addGameOverListener(this);
            gameScene.addEventHandler(KeyEvent.KEY_PRESSED,
                    event -> game.handleKey(event.getCode()));

            stage = (Stage) gameScene.getWindow();
            stage.setOnHiding(event -> game.stop());
        }
    }

    @FXML
    private void newGame() {
        if (game.getGameState() != GameState.OFF) {
            game.stop();
            try {
                if (!openNewHighscoreWindow()) {
                    game.start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                game.start();
            }
        } else {
            game.start();
        }
    }
    
    @FXML
    private void exit() {
        if (game.getGameState() != GameState.OFF) {
            game.stop();
            try {
                if (!openNewHighscoreWindow()) {
                    stage.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                stage.close();
            }
        } else {
            stage.close();
        }
    }

    // Returns true if "new highscore" window has been opened
    private boolean openNewHighscoreWindow() throws IOException {
        int labyrinthType = game.getLabyrinth().getLabyrinthType();
        Board board = Board.getScoreBoard(labyrinthType, "data/Scoreboard.tab");
        if (board.isNewHighScore(game.getScore())) {
            NewHighscoreWindowController.openNewHighscoreWindow(game.getScore(), labyrinthType, stage);
            return true;
        }
        return false;
    }

    @FXML
    private void pause() {
        game.pause(true);
    }

    @FXML
    private void openScoreboard() {
        game.pause(false);
        ScoreboardWindowController.openScoreboard(0, -1, stage);
    }

    @FXML
    private void openOptionsWindow() {
        game.pause(false);
        if (game.getGameState() == GameState.OFF) {
            OptionsWindowController.openOptionsWindow(options, this, stage);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING,
                    "You cannot change options of the game while there is an active game session." +
                            "Press 'New Game' to end this game.",
                    ButtonType.YES);
            alert.showAndWait();
        }
    }

    void setOptions(Options options) {
        this.options = options;
        game.applyOptions(options);
    }

    @Override
    public void gameOver(int score) {
        try {
            openNewHighscoreWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}