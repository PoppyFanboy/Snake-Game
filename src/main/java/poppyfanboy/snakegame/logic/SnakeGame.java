package poppyfanboy.snakegame.logic;

/**
 * Class "Snake Game"
 *
 * Classic game "Snake"
 * Conducts the main loop of the game session,
 * paints the snake, food, walls of the labyrinth, score and speed
 * on the Pane object when constructed
 *
 * @author PoppyFanboy
 */

import java.util.*;

import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.input.*;

import javafx.beans.value.ChangeListener;

import poppyfanboy.snakegame.gui.GameOverListener;
import poppyfanboy.snakegame.data.options.*;

public class SnakeGame {
	public static final Color FOOD_COLOR = Color.rgb(34, 86, 67);
	public static final Color SNAKE_COLOR = Color.rgb(206, 83, 16);

	private static final int MAX_BUFFERED_MOVES = 3;
	private static final double UPDATE_INTERVAL = 0.005; // 5 millis
	private static final double SPEED_UP_MULTIPLIER = 0.85;
	private static final double INITAL_SPEED = 0.25;

	private double speedUpInterval = 30.0;

	// Fields related to the game state
	private Options options;
	private String gameMode = "";

	private int score;
	private int speedLevel;
	private int multiplier = 1;
	// time needed for a snake to make exactly one movement (in seconds)
	private double speed;

	private int slowMotion = -1;
	private double slowMotionBuffer;
	private double slowMoMult;

	private GameState gameState = GameState.OFF;


	// Fields related to graphics
	private GraphicsContext gc;

	private Canvas canvas;
	private Pane pane;
	private Rectangle border;

	private double gameWidth;
	private double gameHeight;


	// Fields related to the game logic
	private Snake snake;
	private Field field;

	private boolean foodEaten;

	private ArrayList<GameOverListener> listeners = new ArrayList<>();
	private Timeline timer;
	private Queue<KeyCode> moves;

	// an indicator of buffer time available
	// for performing movement of a snake in seconds
	private double timeBuffer;
	// point of time in seconds when the last sequence
	// of movements was performed
	private long lastNano;
	// time passed after last increment of speed
	private double lastSpeedUp;


	// Fields related to the game interface
	private Text scoreField;
	private Text speedField;
	private Text pauseField;
	private Text multField;


	public SnakeGame(Pane pane, Options options) {
		this.pane = pane;
		this.options = options;

		canvas = new Canvas(gameWidth, gameHeight);
		gc = canvas.getGraphicsContext2D();

		ChangeListener<Number> stageSizeListener =
			(observable, oldValue, newValue) -> {
				redraw();
			};
		pane.widthProperty().addListener(stageSizeListener);
		pane.heightProperty().addListener(stageSizeListener);

		scoreField = new Text("Points: 0");
		speedField = new Text("Speed:  1");
		pauseField = new Text("GAME PAUSED");
		multField = new Text("Multiplier: ");

		border = new Rectangle();
		border.setFill(Color.TRANSPARENT);
		border.setStroke(Color.BLACK);

		pane.getChildren().add(canvas);
		pane.getChildren().add(scoreField);
		pane.getChildren().add(speedField);
		pane.getChildren().add(pauseField);
		pane.getChildren().add(multField);
		pane.getChildren().add(border);

		applyOptions(options);
		redraw();
	}

	private void redraw() {
		pause(false);

		double windowWidth = pane.getScene().getWidth();
		double windowHeight = pane.getScene().getHeight();
		gameWidth = Math.min(((int)(windowWidth * 0.9 / Field.FIELD_WIDTH / 5)) * Field.FIELD_WIDTH * 5,
				((int)(windowHeight * 0.9 / Field.FIELD_HEIGHT) / 5) * Field.FIELD_HEIGHT * 5);
		gameHeight = gameWidth;

		canvas.setWidth(gameWidth);
		canvas.setHeight(gameHeight);
		canvas.setLayoutX((windowWidth - gameWidth) / 2.0);
		canvas.setLayoutY((windowHeight - gameHeight) / 2.0);

		border.setX((windowWidth - gameWidth) / 2.0);
		border.setY((windowHeight - gameHeight) / 2.0);
		border.setWidth(gameWidth);
		border.setHeight(gameHeight);

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

		if (snake != null) {
			snake.setGameWidth(gameWidth, gc);
		}

		if (field != null) {
			field.setGameWidth(gameWidth, gc);
			// this method redraws all the objects on the field
			field.redraw();
		}

		double fontSize = (windowWidth - gameWidth) / 2 / 12;

		scoreField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		scoreField.setLayoutX((windowWidth + gameWidth) / 2.0 + fontSize);
		scoreField.setLayoutY((windowHeight - gameHeight) / 2.0 + fontSize);

		speedField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		speedField.setLayoutX((windowWidth + gameWidth) / 2.0 + fontSize);
		speedField.setLayoutY((windowHeight - gameHeight) / 2.0 + 2 * fontSize + 5);

		multField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		multField.setLayoutX((windowWidth + gameWidth) / 2.0 + fontSize);
		multField.setLayoutY((windowHeight - gameHeight) / 2.0 + 3 * fontSize + 10);

		pauseField.setFont(Font.font("Monospaced", FontWeight.BOLD, fontSize));
		pauseField.setLayoutX((windowWidth + gameWidth) / 2.0 + fontSize);
		pauseField.setLayoutY((windowHeight - gameHeight) / 2.0 + fontSize * 4 + 15);
	}

	public void applyOptions(Options options) {
		this.options = options;
		score = 0;
		speedLevel = 1;

		Labyrinth labyrinth = options.getLabyrinth();
		GameMode gameMode = options.getGameMode();
		multiplier = options.getMult();
		speedUpInterval = gameMode.getSpeedUpInterval();

		this.gameMode = gameMode.toString();

		if (!gameMode.toString().equals("Zen")) {
			speed = INITAL_SPEED;
		} else {
			speed = INITAL_SPEED * Math.pow(SPEED_UP_MULTIPLIER, options.getZenSpeedLevel());
			speedLevel = options.getZenSpeedLevel();
			System.out.println(options.getZenSpeedLevel());
		}

		scoreField.setText("Score:  " + score);
		speedField.setText("Speed:  " + speedLevel);
		pauseField.setFill(Color.TRANSPARENT);

		timeBuffer = speed;
		lastNano = 0;
		lastSpeedUp = 0.0;

		foodEaten = false;

		moves = new LinkedList<KeyCode>();
	}

	public void start() {
		applyOptions(options);
		multField.setText("Multiplier: x" + multiplier);

		snake = new Snake(gc, gameWidth);
		field = new Field(gc, options.getLabyrinth(), snake, gameWidth);

		redraw();

		gameState = GameState.INITIALIZATION;

		slowMotionBuffer = 0;
		slowMotion = 10;
		slowMoMult = Math.pow(speed / 2.5, 0.1);
		speed = 2.5;

		timer = new Timeline(new KeyFrame(Duration.millis((long) (1e3 * UPDATE_INTERVAL)), new GameLoop()));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	public void stop() {
		if (gameState != GameState.OFF) {
			timer.stop();
			gameState = GameState.OFF;
		}
	}
	
	public void pause(boolean toggle) {
		moves.clear();

		switch (gameState) {
			case ON: case INITIALIZATION:
				gameState = GameState.PAUSE;
				pauseField.setFill(Color.RED);
				timer.pause();
				break;
				
			case PAUSE:
				if (toggle) {
					gameState = GameState.ON;
					lastNano = System.nanoTime();
					pauseField.setFill(Color.TRANSPARENT);
                    timer.play();
				}
				break;
		}
	}

	// updates the state of the snake according to time
	// elapsed since last call of updateSnake method
	private void updateSnake(double elapsedTime) {
		timeBuffer += elapsedTime;
		if (!gameMode.equals("Zen")) {
			lastSpeedUp += elapsedTime;
		}
		slowMotionBuffer += elapsedTime;

		if (gameState == GameState.INITIALIZATION) {
			snake.movePaint(gc, timeBuffer / speed, false);
		} else {
			snake.movePaint(gc, timeBuffer / speed, !foodEaten);
		}

		if (slowMotion > 0 && slowMotionBuffer > 0.2) {
			double oldSpeed = speed;
			speed *= slowMoMult;
			// preserving proportions
			timeBuffer = timeBuffer / oldSpeed * speed;
			slowMotion--;
			slowMotionBuffer -= 0.2;
		}

		while (timeBuffer >= speed) {
			if (snake.isDead(field)) {
				this.stop();
				for (GameOverListener listener : listeners) {
					listener.gameOver(score);
				}
				return;
			}

			if (moves.size() != 0) {
				snake.controlInp(moves.poll());
			}
			foodEaten = false;

			int newScore = multiplier * snake.move(field);
			if (newScore != 0) {
				if (!field.generateFood()) {
					this.stop();
					for (GameOverListener listener : listeners) {
						listener.gameOver(score);
					}
				}
				foodEaten = true;
			}

			score += newScore;
			scoreField.setText("Score:  " + score);

			timeBuffer -= speed;
		}

		while (!gameMode.equals("Zen") && lastSpeedUp >= speedUpInterval) {
			lastSpeedUp -= speedUpInterval;
			if (speedLevel < 10) {
				double oldSpeed = speed;
				speed *= SPEED_UP_MULTIPLIER;
				// preserving proportions
				timeBuffer = timeBuffer / oldSpeed * speed;

				speedLevel++;
				speedField.setText(String.format("Speed:  %d", speedLevel));
			}
		}
	}

	public void handleKey(KeyCode code) {
		if (moves.size() < MAX_BUFFERED_MOVES &&
				(code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.LEFT || code == KeyCode.RIGHT)) {
			moves.add(code);
		}
		if (gameState != GameState.OFF) {
			if (code == KeyCode.P) {
				pause(true);
			}
		}
	}

	public void addGameOverListener(GameOverListener listener) {
		listeners.add(listener);
	}

	public GameState getGameState() {
        return gameState;
    }

    public int getScore() {
		return score;
	}

	public Labyrinth getLabyrinth() {
	    return options.getLabyrinth();
    }

	class GameLoop implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			long currentNano = System.nanoTime();
			if (gameState == GameState.INITIALIZATION) {
				timeBuffer = speed;
				updateSnake(0);
				gameState = GameState.ON;
			} else if (gameState == GameState.ON) {
				updateSnake((currentNano - lastNano) / 1e9);
			}

			lastNano = currentNano;
		}
	}
}