package poppyfanboy.snakegame;

/**
 * Classic game "Snake"
 * Conducts the main loop of the game session,
 * paints the snake, food, walls of the labyrinth, points and speed
 * on the Pane object when constructed
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.input.*;

import java.util.*;

import static poppyfanboy.snakegame.GUIPart.GAME_HEIGHT;
import static poppyfanboy.snakegame.GUIPart.GAME_WIDTH;
import static poppyfanboy.snakegame.GUIPart.WINDOW_HEIGHT;
import static poppyfanboy.snakegame.GUIPart.WINDOW_WIDTH;

// Current state of the game session
// "INITIALIZATION" state - first step of a snake
// that requires special conditions
enum GameState { OFF, ON, PAUSE, INITIALIZATION };

public class SnakeGame {
	private static Snake snake;
	private static Timer timer;
	private static Field field;

	private GraphicsContext gc;

	private static Text pointsField = new Text("Points: 0");
	private static Text speedField = new Text("Speed: 1.000");
	private static Text pauseField = new Text("GAME PAUSED");

	private static int points = 0;
	// time needed for a snake to make exactly one movement (in seconds)
	private static double speed = 1.0;
	// an indicator of buffer time available
	// for performing movement of a snake in seconds
	private static double timeBuffer = 0.0;
	// point of time in seconds when last sequence
	// of movements was performed
	private static double lastMove = 0.0;
	// time passed after last increment of speed
	private static double lastSpeedUp = 0.0;

	private static GameState gameState;

	// 5 milliseconds
	public static final double UPDATE_INTERVAL = 0.005;
	// 5 seconds
	public static final double SPEED_UP_INTERVAL = 5.0;

	public SnakeGame(Pane pane) {
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

		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

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

		gameState = GameState.OFF;
	}

	// starts new game, creates game window on the Pane,
	// creates instances of Snake and Field classes
	public void start() {
		gameState = GameState.INITIALIZATION;

		snake = new Snake(gc, GAME_WIDTH / Field.FIELD_WIDTH);
		field = new Field(gc, new int[Field.FIELD_HEIGHT][Field.FIELD_WIDTH], snake);

		timer = new Timer();
		timer.schedule(new GameLoop(), (long) (1e3 * UPDATE_INTERVAL),
				(long) (1e3 * UPDATE_INTERVAL));
	}

	void stop() {
		if (gameState != GameState.OFF) {
			timer.cancel();
		}
	}

	// updates the state of the snake according to time
	// elapsed since last call of updateSnake method
	private static void updateSnake(double elapsedTime) {
		timeBuffer += elapsedTime;
		lastSpeedUp += elapsedTime;

		while (timeBuffer >= speed) {
			if (snake.isSafeToMove(field)) {
				SnakeGame.points += snake.move(field);
				pointsField.setText("Points: " + SnakeGame.points);
			} else {
				timer.cancel();
			}
			timeBuffer -= speed;
		}

		if (lastSpeedUp >= SPEED_UP_INTERVAL) {
			lastSpeedUp = 0;
			if (speed >= 0.025) {
				speed = speed * 0.75;
				speedField.setText(String.format("Speed:  %.2f", SnakeGame.speed));
			}
		}
	}

	void handleKey(KeyCode code) {
		snake.controlInp(code);
		if (code == KeyCode.P) {
			switch (gameState) {
				case ON:
				case INITIALIZATION:
					gameState = GameState.PAUSE;
					pauseField.setFill(Color.RED);
					timer.purge();
					break;
				case PAUSE:
					gameState = GameState.INITIALIZATION;
					pauseField.setFill(Color.TRANSPARENT);
					timer.schedule(new SnakeGame.GameLoop(), (long) (1e3 * UPDATE_INTERVAL),
							(long) (1e3 * UPDATE_INTERVAL));
					break;
			}
		}
	}

	static class GameLoop extends TimerTask {
		@Override
		public void run() {
			double currentTime = System.nanoTime() / 1e9;
			if (gameState == GameState.INITIALIZATION) {
				timeBuffer = 0;
				updateSnake(0);
				gameState = GameState.ON;
			} else if (gameState == GameState.ON) {
				updateSnake(currentTime - lastMove);
			}

			lastMove = currentTime;
		}
	}
}