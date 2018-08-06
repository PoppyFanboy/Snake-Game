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
	static Snake snake;
	static Timer timer;
	static Group snakeSegments;
	
	@Override
	public void start(Stage stage) throws InterruptedException{
		snakeSegments = new Group();
		
		Scene scene = new Scene(snakeSegments, 1024, 768);
		
		stage.setScene(scene);
		stage.setTitle("Snake Game | by PoppyFanboy");
		
		snake = new Snake(15, 15, 20);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, new ControlKeyEvent());
		
		SnakeBlock block = snake.tail;
		do {
			snakeSegments.getChildren().add(block.rect);
			block = block.next;			
		} while (block != null);
		
		stage.show();
		
		timer = new Timer();
		timer.schedule(new MoveSnakeTask(), 20, 20);
	}
	
	public static void main(String args[]) throws InterruptedException {
		launch(args);
		timer.cancel();
	}
}

class ControlKeyEvent implements EventHandler<KeyEvent> {
	public void handle(KeyEvent event) {
		KeyCode code = event.getCode();
		
		if (code == KeyCode.UP) {
			SnakeGame.snake.dir = Direction.NORTH;
		} else if (code == KeyCode.DOWN) {
			SnakeGame.snake.dir = Direction.SOUTH;
		} else if (code == KeyCode.LEFT) {
			SnakeGame.snake.dir = Direction.WEST;
		} else if (code == KeyCode.RIGHT) {
			SnakeGame.snake.dir = Direction.EAST;
		}
	}
}

class MoveSnakeTask extends TimerTask {
	public void run() {
		SnakeGame.snake.move();
	}
}