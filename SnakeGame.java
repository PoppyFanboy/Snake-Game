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
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;

import javafx.scene.control.*;

import javafx.event.*;
import javafx.scene.input.*;

import java.util.*;

// Current state of the game session
// "INITIALIZATION" state - first step of a snake
// that requires special conditions
enum GameState { OFF, ON, PAUSE, INITIALIZATION };

public class SnakeGame extends Application {
	private static Snake snake;
	private static Timer timer;
	private static Field field;

	private static Stage stage;

	private static Text pointsField = new Text("Points: 0");
	private static Text speedField = new Text("Speed: 5/100");
	private static Text pauseField = new Text("GAME PAUSED");

	private static int points = 0;
	// movements of snake per second
	private static int speed = 5;
	// an indicator of buffer time available
	// for performing movement of a snake in nanoseconds
	private static long timeBuffer = 0;
	// point of time in nanoseconds when last sequence
	// of movements was performed
	private static long lastMove = 0;
	// time passed after last increment of speed
	private static long lastSpeedUp = 0;

	private static GameState gameState = GameState.OFF;

	// GAME_WIDTH/HEIGHT are sizes of the game field in pixels
	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 768;
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 600;

	// 5 milliseconds
	public static final long UPDATE_INTERVAL = 5000000L;
	// 5 seconds
	public static final long SPEED_UP_INTERVAL = 1000000000L * 5;

	@Override
	public void start(Stage stage) {
		gameState = GameState.INITIALIZATION;

		Pane gameDisplayNode = new Pane();
		Scene scene = new Scene(gameDisplayNode, WINDOW_WIDTH, WINDOW_HEIGHT);
		stage.setScene(scene);
		stage.setTitle("Snake Game | by PoppyFanboy");
		SnakeGame.stage = stage;

		initializeGUI(gameDisplayNode);

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				snake.controlInp(event.getCode());
				if (event.getCode() == KeyCode.P) {
					switch (gameState) {
						case ON:
						case INITIALIZATION:
							gameState = GameState.PAUSE;
							pauseField.setFill(Color.RED);
							break;
						case PAUSE:
							gameState = GameState.INITIALIZATION;
							pauseField.setFill(Color.TRANSPARENT);
							break;
					}

				}
			}
		});

		stage.show();
		startGame(gameDisplayNode);
	}

	private void initializeGUI(Pane pane) {
		MenuBar menuBar = new MenuBar();
		final Menu menu1 = new Menu("Game");
		final Menu menu2 = new Menu("Reference");

		MenuItem menuNewGame = new MenuItem("New Game");
		MenuItem menuScore = new MenuItem("Scoreboard");
		MenuItem menuParam = new MenuItem("Parameters");
		MenuItem menuExit = new MenuItem("Exit");
		menuExit.setOnAction(new MenuActionHandler());

		menu1.getItems().addAll(menuNewGame, new SeparatorMenuItem(), menuScore,
				menuParam, new SeparatorMenuItem(), menuExit);

		MenuItem menuReference = new MenuItem("Show Reference");
		MenuItem menuAbout = new MenuItem("About");
		menu2.getItems().addAll(menuReference, new SeparatorMenuItem(), menuAbout);

		menuBar.getMenus().addAll(menu1, menu2);
		pane.getChildren().add(menuBar);
	}

	// starts new game, creates game window on the Pane,
	// creates instances of Snake and Field classes
	private static void startGame(Pane pane) {
		GraphicsContext gc = createGameField(pane);
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		snake = new Snake(gc, GAME_WIDTH / Field.FIELD_WIDTH);
		field = new Field(gc, new int[Field.FIELD_HEIGHT][Field.FIELD_WIDTH], snake);

		pointsField.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
		pointsField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2 + 20);
		pointsField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2 + 20);

		speedField.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
		speedField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2 + 20);
		speedField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2 + 50);

		pauseField.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
		pauseField.setFill(Color.TRANSPARENT);
		pauseField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2 + 20);
		pauseField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2 + 80);

		pane.getChildren().add(pointsField);
		pane.getChildren().add(speedField);
		pane.getChildren().add(pauseField);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (gameState == GameState.INITIALIZATION) {
					timeBuffer = 1000000000L / speed;
					updateSnake(0);
					gameState = GameState.ON;
				} else if (gameState == GameState.ON) {
					updateSnake(System.nanoTime() - lastMove);
				}

				lastMove = System.nanoTime();
			}
		}, UPDATE_INTERVAL / 1000000L, UPDATE_INTERVAL / 1000000L);
	}

	// updates the state of the snake according to time
	// elapsed since last call of updateSnake method
	private static void updateSnake(long elapsedTime) {
		timeBuffer += elapsedTime;
		lastSpeedUp += elapsedTime;

		if (lastSpeedUp >= SPEED_UP_INTERVAL) {
			lastSpeedUp = 0;
			if (speed < 100) {
				speed++;
				speedField.setText("Speed:  " + SnakeGame.speed + "/100");
			}
		}

		while (timeBuffer >= 1000000000L / speed) {
			if (snake.isSafeToMove(field)) {
				SnakeGame.points += snake.move(field);
				pointsField.setText("Points: " + SnakeGame.points);
			} else {
				timer.cancel();
			}
			timeBuffer -= 1000000000L / speed;
		}
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

	class MenuActionHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent mouseEvent) {
			if (mouseEvent.getTarget() instanceof MenuItem) {
				MenuItem target = (MenuItem) mouseEvent.getTarget();
				if (target.getText() == "Exit") {
					timer.cancel();
					stage.close();
				}
			}
		}
	}
}