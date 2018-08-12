package poppyfanboy.snakegame;

/**
 * Class "Field"
 * Represents the game field: allocation of walls and food
 * Provides methods for generating and painting food on the game field
 *
 * @author PoppyFanboy
 */

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class Field {
    static final int FIELD_WIDTH = 30;
    static final int FIELD_HEIGHT = 30;

    private int blockSize = 10;
    private GraphicsContext gc;

    int[][] gameField = new int[FIELD_HEIGHT][FIELD_WIDTH];
    private Snake snake;

    // paints walls, generates food
    Field(GraphicsContext gc, int[][] gameField, Snake snake) {
        this.gc = gc;
        this.snake = snake;

        blockSize = SnakeGame.GAME_WIDTH / Field.FIELD_WIDTH;

        // copying array
        for (int i = 0; i < Math.min(gameField.length, FIELD_HEIGHT); i++) {
            this.gameField[i] = Arrays.copyOf(gameField[i], FIELD_WIDTH);
        }

        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (this.gameField[i][j] == 1) {
                    gc.setFill(Color.DARKGRAY);
                    gc.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);
                }
            }
        }

        generateFood();
    }

    // returns false if (x,y) coordinate belongs to the snake
    boolean checkSnakeCollision(int x, int y) {
        SnakeBlock block = snake.getTail();
        do {
            if (block.getX() == x && block.getY() == y) {
                return true;
            }
            block = block.next;
        } while (block != null);

        return false;
    }

    // generates and paints food on game field
    boolean generateFood() {
        int randomCell = (int) (FIELD_WIDTH * FIELD_HEIGHT * Math.random());

        while (gameField[randomCell / FIELD_HEIGHT][randomCell % FIELD_WIDTH] != 0 ||
               checkSnakeCollision(randomCell % FIELD_WIDTH, randomCell / FIELD_HEIGHT)) {
            randomCell++;
        }

        int foodX = randomCell % FIELD_WIDTH;
        int foodY = randomCell / FIELD_WIDTH;
        gameField[foodY][foodX] = 2;
        gc.setFill(Color.GREEN);
        gc.fillRect(foodX * blockSize + 4, foodY * blockSize + 4, blockSize - 8, blockSize - 8);

        return true;
    }
}
