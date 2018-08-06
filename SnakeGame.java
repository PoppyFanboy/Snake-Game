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
		stage.setTitle("Snake Game | by PoppyFanboy");
		stage.show();
	}
	
	public static void main(String args[]) {
		launch(args);
	}
}

/*private static void initNewGame() {
	int x = FIELD_SIZE / 2;
	int y = FIELD_SIZE / 2;
	head = new SnakeBlock(FIELD_SIZE / 2, FIELD_SIZE / 2);
		
	SnakeBlock previous = head;
	for (int i = x + 1; i < x + FIELD_SIZE / 4; i++) {
		previous = new SnakeBlock(previous, i, y);
	}
	tail = previous;
}*/