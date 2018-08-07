package poppyfanboy.snakegame;

/**
  * Classic game "Snake"
  * Main class "SnakeGame"
  * Implements GUI part of the application,
  * conducts the main loop of the game session
  *
  * @author PoppyFanboy
  */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;

import javafx.scene.layout.*;

import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;

import java.util.*;


public class SnakeGame extends Application {
	static Snake snake;
	private static Timer timer;
	
	@Override
	public void start(Stage stage) throws InterruptedException{
		Group gameDisplayNode = new Group();
		Scene scene = new Scene(gameDisplayNode, 640, 480);
		
		Canvas canvas = new Canvas(640, 480);		
		gameDisplayNode.getChildren().add(canvas);
		
		stage.setScene(scene);
		stage.setTitle("Snake Game | by PoppyFanboy");
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		snake = new Snake(gc);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, new ControlKeyEvent());
		
		stage.show();
		
		timer = new Timer();
		timer.schedule( new TimerTask() {
			public void run() {
				snake.move();
			}
		}, 50, 50);
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
			SnakeGame.snake.setDir(Direction.NORTH);
		} else if (code == KeyCode.DOWN) {
			SnakeGame.snake.setDir(Direction.SOUTH);
		} else if (code == KeyCode.LEFT) {
			SnakeGame.snake.setDir(Direction.WEST);
		} else if (code == KeyCode.RIGHT) {
			SnakeGame.snake.setDir(Direction.EAST);
		}
	}
}