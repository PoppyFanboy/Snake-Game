package poppyfanboy.snakegame.logic;

/**
 * Class "Snake Game"
 *
 * Classic game "Snake"
 * Conducts the main loop of the game session,
 * paints the snake, food, walls of the labyrinth, points and speed
 * on the Pane object when constructed
 *
 * @author PoppyFanboy
 */

import javafx.fxml.FXMLLoader;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.input.*;

import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Duration;

import java.io.*;

import static poppyfanboy.snakegame.Main.*;

// Current state of the game session
// "INITIALIZATION" state - first step of a snake
// that requires special conditions
enum GameState { OFF, ON, PAUSE, INITIALIZATION }

public class SnakeGame {
	private Snake snake;
	private Timeline timer;
	private Field field;

	private GraphicsContext gc;
	private Stage gameWindow;
	private Stage newHighscoreWindow;

	private Text pointsField;
	private Text speedField;
	private Text pauseField;

	private int points;
	private int speedLevel;
	// time needed for a snake to make exactly one movement (in seconds)
	private double speed;
	// an indicator of buffer time available
	// for performing movement of a snake in seconds
	private double timeBuffer;
	// point of time in seconds when last sequence
	// of movements was performed
	private double lastMove;
	// time passed after last increment of speed
	private double lastSpeedUp;

	private GameState gameState;

	// 5 milliseconds
	public static final double UPDATE_INTERVAL = 0.005;
	// 20 seconds
	public static final double SPEED_UP_INTERVAL = 20.0;

	public SnakeGame(Pane pane) {
		//initializing all the GUI stuff
        gameWindow = (Stage) pane.getScene().getWindow();

        newHighscoreWindow = new Stage();
        newHighscoreWindow.setTitle("New Highscore!");
        try {
            FXMLLoader loader = new FXMLLoader();
            FileInputStream fxmlStream = new FileInputStream(HOME + "gui/FXMLNewHighscoreWindow.fxml");
            Scene scene = (Scene) loader.load(fxmlStream);
            newHighscoreWindow.setScene(scene);
            newHighscoreWindow.initOwner(gameWindow);
            newHighscoreWindow.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException ex) {
            ex.printStackTrace();
            newHighscoreWindow.close();
            return;
        }

		Canvas canvas = new Canvas(GAME_WIDTH, GAME_HEIGHT);
		pane.getChildren().add(canvas);
		canvas.setLayoutX((WINDOW_WIDTH - GAME_WIDTH) / 2.0);
		canvas.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2.0);

		Rectangle border = new Rectangle((WINDOW_WIDTH - GAME_WIDTH) / 2.0, (WINDOW_HEIGHT - GAME_HEIGHT) / 2.0, GAME_WIDTH, GAME_HEIGHT);
		border.setFill(Color.TRANSPARENT);
		border.setStroke(Color.BLACK);
		pane.getChildren().add(border);

		gc = canvas.getGraphicsContext2D();
		
		int fontSize = (WINDOW_WIDTH - GAME_WIDTH) / 2 / 8;
		pointsField = new Text("Points: 0");
		pointsField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		pointsField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2.0 + fontSize);
		pointsField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2.0 + fontSize);
		
		speedField = new Text("Speed:  0");
		speedField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		speedField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2.0 + fontSize);
		speedField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2.0 + 2 * fontSize + 5);
		
		pauseField = new Text("GAME PAUSED");
		pauseField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		pauseField.setFill(Color.TRANSPARENT);
		pauseField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2.0 + fontSize);
		pauseField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2.0 + fontSize * 4);

		pane.getChildren().add(pointsField);
		pane.getChildren().add(speedField);
		pane.getChildren().add(pauseField);
		
		gameState = GameState.OFF;
		setDefaultSettings();
	}
	
	private void setDefaultSettings() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		pauseField.setFill(Color.TRANSPARENT);
		pointsField.setText("Points: 0");
		speedField.setText("Speed:  0");
		
		points = 0;
		speedLevel = 0;
		
		speed = 0.5;
		timeBuffer = 0.0;
		lastMove = 0.0;
		lastSpeedUp = 0.0;
	}
	
	public void start() {
		setDefaultSettings();
		gameState = GameState.INITIALIZATION;

		snake = new Snake(gc, GAME_WIDTH / Field.FIELD_WIDTH);
		field = new Field(gc, new int[Field.FIELD_HEIGHT][Field.FIELD_WIDTH], snake);

		IntVector.setModuloX(Field.FIELD_WIDTH);
		IntVector.setModuloY(Field.FIELD_HEIGHT);

		timer = new Timeline(new KeyFrame(Duration.millis((long) (1e3 * UPDATE_INTERVAL)), new GameLoop()));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	public void stop() {
		if (gameState != GameState.OFF) {
			timer.stop();
			gameState = GameState.OFF;
            newHighscoreWindow.show();
		}
	}
	
	public void pause(boolean toggle) {
		switch (gameState) {
			case ON: case INITIALIZATION:
				gameState = GameState.PAUSE;
				pauseField.setFill(Color.RED);
				timer.pause();
				break;
				
			case PAUSE:
				if (toggle) {
					gameState = GameState.INITIALIZATION;
					pauseField.setFill(Color.TRANSPARENT);
                    timer.play();
				}
				break;
		}
	}
	
	public void pause() {
		this.pause(true);
	}

	// updates the state of the snake according to time
	// elapsed since last call of updateSnake method
	private void updateSnake(double elapsedTime) {
		timeBuffer += elapsedTime;
		lastSpeedUp += elapsedTime;

		while (timeBuffer >= speed) {
			if (snake.isSafeToMove(field)) {
				points += snake.move(field);
				pointsField.setText("Points: " + points);
			} else {
				this.stop();
			}
			timeBuffer -= speed;
		}

		if (lastSpeedUp >= SPEED_UP_INTERVAL) {
			lastSpeedUp = 0;
			if (speed >= 0.025) {
				speed *= 0.75;
				speedLevel++;
				speedField.setText(String.format("Speed:  %d", speedLevel));
			}
		}
	}

	public void handleKey(KeyCode code) {
		if (gameState != GameState.OFF) {
			snake.controlInp(code);
			if (code == KeyCode.P) {
				pause();
			}
		}
	}

	class GameLoop implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
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