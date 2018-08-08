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
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

import javafx.event.*;
import javafx.scene.input.*;

import java.util.*;

public class SnakeGame extends Application {
	private static Snake snake;
	private static Timer timer;
	private static Field field;

	// GAME_WIDTH/HEIGHT are sizes of the game field in pixels
	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 768;
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 600;

	// the larger value yields to higher speed
	private static int gameSpeed = 40;
	
	@Override
	public void start(Stage stage) {
		Pane gameDisplayNode = new Pane();
		Scene scene = new Scene(gameDisplayNode, WINDOW_WIDTH, WINDOW_HEIGHT);
		stage.setScene(scene);
		stage.setTitle("Snake Game | by PoppyFanboy");

		stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				snake.controlInp(event.getCode());
			}
		});

		stage.show();
		startGame(gameDisplayNode);
	}

	// starts new game, creates game window on the Pane,
	// creates instances of Snake and Field classes
	private static void startGame(Pane pane) {
		GraphicsContext gc = createGameField(pane);
		snake = new Snake(gc, GAME_WIDTH / Field.FIELD_WIDTH);
		field = new Field(gc, new int[Field.FIELD_HEIGHT][Field.FIELD_WIDTH], snake);

		timer = new Timer();
		timer.schedule( new TimerTask() {
			public void run() {
				if (snake.isSafeToMove(field)) {
					snake.move(field);
				} else {
					timer.cancel();
				}
			}
		}, gameSpeed, gameSpeed);
	}

	// creates game field on a Pane and returns GraphicsContext instance
	// (there is no need in storing Canvas object)
	private static GraphicsContext createGameField(Pane pane) {
		Canvas canvas = new Canvas(GAME_WIDTH, GAME_WIDTH);
		pane.getChildren().add(canvas);
		canvas.setLayoutX((WINDOW_WIDTH - GAME_WIDTH) / 2);
		canvas.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2);

		Rectangle border = new Rectangle((WINDOW_WIDTH - GAME_WIDTH) / 2,
				(WINDOW_HEIGHT - GAME_HEIGHT) / 2,
				GAME_WIDTH, GAME_HEIGHT);
		border.setFill(Color.TRANSPARENT);
		border.setStroke(Color.BLACK);
		pane.getChildren().add(border);

		return canvas.getGraphicsContext2D();
	}
	
	public static void main(String args[]) throws InterruptedException {
		launch(args);
		timer.cancel();
	}
}