package poppyfanboy.snakegame.logic;

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

import poppyfanboy.snakegame.Main;
import static poppyfanboy.snakegame.logic.IntVector.vector;

public class Field {
    static final int FIELD_WIDTH = 15;
    static final int FIELD_HEIGHT = 15;

    private int blockSize = Main.GAME_WIDTH / FIELD_WIDTH;
    private GraphicsContext gc;

    HashSet<ObjectOnField> gameField = new HashSet<>();

    private Snake snake;
    private Block food;

    // paints walls, generates food
    Field(GraphicsContext gc, HashSet<ObjectOnField> labyrinth, Snake snake) {
        this.gc = gc;
        this.snake = snake;

        blockSize = Main.GAME_WIDTH / Field.FIELD_WIDTH;

        gameField.addAll(labyrinth);
        gameField.add(snake);
        generateFood();

        for (ObjectOnField object : gameField) {
            object.paint(gc);
        }
    }

    boolean checkCollision(IntVector coords) {
        for (ObjectOnField object : gameField) {
            if (object.collision(coords)) {
                return true;
            }
        }
        return false;
    }

    // generates and paints food on game field
    boolean generateFood() {
        int size = FIELD_WIDTH * FIELD_HEIGHT;
        int randomCell = (int) (size * Math.random());
        int offset = 0;
        int foodX = randomCell % FIELD_WIDTH;
        int foodY = randomCell / FIELD_HEIGHT;

        while (offset < size) {
            if (checkCollision(vector(foodX, foodY))) {
                offset++;
                foodX = (randomCell + offset) % FIELD_WIDTH;
                foodY = ((randomCell + offset) % size) / FIELD_WIDTH;
            } else {
                break;
            }
        }

        if (offset >= size) {
            food = null;
            return false;
        }

        food = new Block(vector(foodX, foodY), blockSize, Color.GREEN, 0.5);
        food.paint(gc);
        return true;
    }

    Block getFood() {
        return food;
    }
}
