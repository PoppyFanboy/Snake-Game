package poppyfanboy.snakegame;

/**
  * Classic game "Snake"
  * Main class "SnakeGame"
  * Implements GUI part of the application,
  * conducts the main loop of the game session
  *
  * @version 0.1.1
  * @author PoppyFanboy
  */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.shape.*;

import javafx.event.*;
import javafx.scene.input.*;

import java.util.*;


public class SnakeGame extends Application {
	@Override
	public void start(Stage stage) {
		Group root = new Group();
		
		Scene scene = new Scene(root, 640, 480);
		
		stage.setScene(scene);
		stage.setTitle("Snake Game | by PoppyFanboy");
		stage.show();
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}