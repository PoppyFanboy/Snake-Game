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
import poppyfanboy.snakegame.data.IntVector;
import poppyfanboy.snakegame.data.options.Labyrinth;

import java.util.*;

import static poppyfanboy.snakegame.data.IntVector.vector;

public class Field {
    public static final int FIELD_WIDTH = 15;
    public static final int FIELD_HEIGHT = 15;

    private double blockSize;
    private double gameWidth;
    private GraphicsContext gc;

    HashSet<ObjectOnField> gameField = new HashSet<>();

    private Block food;

    // paints walls, generates food
    Field(GraphicsContext gc, Labyrinth labyrinth, Snake snake, double gameWidth) {
        this.gc = gc;
        this.gameWidth = gameWidth;
        this.blockSize =  gameWidth / FIELD_WIDTH;

        gameField.addAll(getBlocks(labyrinth));

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

    boolean checkCollision(IntVector coords, Class<? extends ObjectOnField> exception) {
        for (ObjectOnField object : gameField) {
            if (object.getClass() != exception && object.collision(coords)) {
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

        food = new Block(vector(foodX, foodY), blockSize, SnakeGame.FOOD_COLOR, 0.5);
        food.paint(gc);
        return true;
    }

    Block getFood() {
        return food;
    }

    void redraw() {
        food.paint(gc);
        for (ObjectOnField object : gameField) {
            object.paint(gc);
        }
    }

    void setGameWidth(double gameWidth, GraphicsContext gc) {
        this.gameWidth = gameWidth;
        this.blockSize =  gameWidth / FIELD_WIDTH;
        this.gc = gc;

        for (ObjectOnField object : gameField) {
            object.setBlockSize(blockSize);
        }
        food.setBlockSize(blockSize);
    }

    private HashSet<Block> getBlocks(Labyrinth labyrinth) {
        HashSet<IntVector> blocksCoords = labyrinth.getBlocksCoords();
        HashSet<Block> blocks = new HashSet<>();

        for (IntVector blockCoord : blocksCoords) {
            blocks.add(new Block(blockCoord, blockSize, Color.BROWN, 1.0));
        }

        return blocks;
    }
}
