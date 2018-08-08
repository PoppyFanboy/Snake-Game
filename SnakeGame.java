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

import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;

import java.util.*;


public class SnakeGame extends Application {
	static Snake snake;
	private static Timer timer;
	
	@Override
	public void start(Stage stage) {
		Group gameDisplayNode = new Group();
		Scene scene = new Scene(gameDisplayNode, 640, 480);
		
		Canvas canvas = new Canvas(640, 480);		
		gameDisplayNode.getChildren().add(canvas);
		
		stage.setScene(scene);
		stage.setTitle("Snake Game | by PoppyFanboy");
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		snake = new Snake(gc);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				snake.controlInp(event.getCode());
			}
		});
		
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