package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import poppyfanboy.snakegame.Main;
import poppyfanboy.snakegame.data.Board;
import poppyfanboy.snakegame.logic.GameState;
import poppyfanboy.snakegame.logic.SnakeGame;

import java.io.FileInputStream;
import java.io.IOException;

import static poppyfanboy.snakegame.Main.HOME;

public class GameWindowController implements GameOverListener{
    @FXML private Pane gamePane;
    @FXML private Scene gameScene;
    private SnakeGame game;
    
    // called right after FXML page is constructed
    @FXML
    private void initialize() {
        this.game = new SnakeGame(gamePane);
        game.addGameOverListener(this);
        gameScene.addEventHandler(KeyEvent.KEY_PRESSED,
                event -> game.handleKey(event.getCode()));
    }
    
    @FXML
    private void newGame() {
        Stage window = (Stage) gameScene.getWindow();
        window.setOnHiding(event -> game.stop());

        if (game.getGameState() != GameState.OFF) {
            game.stop();
            if (Board.getScoreBoard(game.getLabyrinth()).isNewHighScore(game.getScore())) {
                openNewHighscoreWindow(game.getScore());
            } else {
                game.start(1);
            }
        } else {
            game.start(1);
        }
    }
    
    @FXML
    private void pause() {
        game.pause();
    }
    
    @FXML
    private void exit() {
        if (game.getGameState() != GameState.OFF) {
            game.stop();
            if (Board.getScoreBoard(game.getLabyrinth()).isNewHighScore(game.getScore())) {
                openNewHighscoreWindow(game.getScore());
            } else {
                Stage window = (Stage) gameScene.getWindow();
                window.close();
            }
        } else {
            Stage window = (Stage) gameScene.getWindow();
            window.close();
        }
    }
    
    @FXML
    private void openScoreboard() {
        game.pause();
        openScoreboard(0, -1);
    }

    static void openScoreboard(int boardNumber, int position) {
        Stage scoreboardWindow = new Stage();
        scoreboardWindow.setTitle("Scoreboard");
        
        try (FileInputStream fxmlStream = new FileInputStream(HOME + "gui/FXMLScoreboardWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = (Scene) loader.load(fxmlStream);
            scoreboardWindow.setScene(scene);

            ScoreboardWindowController controller = loader.<ScoreboardWindowController>getController();
            controller.fillWithContent(boardNumber, position);
            
            // modality
            Stage window = Main.getStage();
            scoreboardWindow.initOwner(window);
            scoreboardWindow.initModality(Modality.APPLICATION_MODAL);
            scoreboardWindow.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            scoreboardWindow.close();
        }
    }

    private void openNewHighscoreWindow(int newScore) {
        Stage gameWindow = Main.getStage();

        Stage newHighscoreWindow = new Stage();
        newHighscoreWindow.setTitle("New Highscore!");
        try (FileInputStream fxmlStream = new FileInputStream(HOME + "gui/FXMLNewHighscoreWindow.fxml")) {
            FXMLLoader loader = new FXMLLoader();
            Scene scene = (Scene) loader.load(fxmlStream);
            newHighscoreWindow.setScene(scene);
            newHighscoreWindow.initOwner(gameWindow);
            newHighscoreWindow.initModality(Modality.APPLICATION_MODAL);

            NewHighscoreWindowController controller = loader.<NewHighscoreWindowController>getController();
            controller.setNewScore(newScore);
            controller.setLabyrinthNumber(game.getLabyrinth());

            newHighscoreWindow.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            newHighscoreWindow.close();
        }
    }

    @Override
    public void gameOver(int score) {
        if (Board.getScoreBoard(game.getLabyrinth()).isNewHighScore(score)) {
            openNewHighscoreWindow(score);
        }
    }
}