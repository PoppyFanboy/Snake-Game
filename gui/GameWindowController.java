package poppyfanboy.snakegame.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import poppyfanboy.snakegame.logic.SnakeGame;

import java.io.FileInputStream;
import java.io.IOException;

import static poppyfanboy.snakegame.Main.HOME;

public class GameWindowController {
    @FXML private Pane gamePane;
    @FXML private Scene gameScene;
    private SnakeGame game;
    
    // called right after FXML page is constructed
    @FXML
    private void initialize() {
        this.game = new SnakeGame(gamePane);
        gameScene.addEventHandler(KeyEvent.KEY_PRESSED,
                event -> game.handleKey(event.getCode()));
    }
    
    @FXML
    private void newGame() {
        Stage window = (Stage) gameScene.getWindow();
        window.setOnHiding(event -> game.stop());
        game.stop();
        game.start();
    }
    
    @FXML
    private void pause() {
        game.pause();
    }
    
    @FXML
    private void exit() {
        game.stop();
        Stage window = (Stage) gameScene.getWindow();
        window.close();
    }
    
    @FXML
    private void openScoreboard() {
        game.pause(false);
        Stage scoreboardWindow = new Stage();
        scoreboardWindow.setTitle("Scoreboard");
        
        try {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fxmlStream = new FileInputStream(HOME + "gui/FXMLScoreboardWindow.fxml");
            Scene scene = (Scene) loader.load(fxmlStream);
            scoreboardWindow.setScene(scene);
            
            // modality
            Stage window = (Stage) gameScene.getWindow();
            scoreboardWindow.initOwner(window);
            scoreboardWindow.initModality(Modality.APPLICATION_MODAL);
            scoreboardWindow.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            scoreboardWindow.close();
            return;
        }
    }
}