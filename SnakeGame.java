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

import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.concurrent.*;

import javafx.event.*;
import javafx.scene.input.*;

import java.util.*;

public class SnakeGame extends Application {
	private static Snake snake;
    private static Timer timer;
    private static Field field;

    private static Text pointsField = new Text("Points: 0");
    private static Text speedField = new Text("Speed: 1");
    private static int points = 0;
    private static int speed = 5;

	static int steps = 0;

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
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		snake = new Snake(gc, GAME_WIDTH / Field.FIELD_WIDTH);
		field = new Field(gc, new int[Field.FIELD_HEIGHT][Field.FIELD_WIDTH], snake);

		int points = 0;

        //statusField.setPrefWidth((WINDOW_WIDTH - GAME_WIDTH) / 2 - 30);
        //statusField.setWrapText(true);
        //statusField.setEditable(false);
        pointsField.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
        pointsField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2 + 20);
        pointsField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2 + 20);

        speedField.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
        speedField.setLayoutX((WINDOW_WIDTH + GAME_WIDTH) / 2 + 20);
        speedField.setLayoutY((WINDOW_HEIGHT - GAME_HEIGHT) / 2 + 50);


        pane.getChildren().add(pointsField);
        pane.getChildren().add(speedField);

		timer = new Timer();
		timer.schedule(new TimerTask() {
            @Override
            public void run() {
                steps++;
                if (steps == 10) {
                    if (speed < 10) {
                        speed++;
                    }
                    steps = 0;
                }
                try {
                    Thread.sleep((11 - speed) * 20);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (snake.isSafeToMove(field)) {
                    SnakeGame.points += snake.move(field);
                    pointsField.setText("Points: " + SnakeGame.points);
                    speedField.setText("Speed:  " + SnakeGame.speed);
                } else {
                    timer.cancel();
                }
            }
        }, 5, 5);
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