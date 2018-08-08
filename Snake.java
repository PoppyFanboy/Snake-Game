package poppyfanboy.snakegame;

/**
 * Class "Snake"
 * The class is used to represent a whole
 * snake from a classic game
 *
 * The snake stores:
 * - references to its "head" and "tail"
 *   represented as instances of "SnakeBlock" class
 * - its current direction (as a enumeration)
 *
 * "Snake" class provides methods for:
 * - moving the snake in the direction stated in "dir" field
 * - checking whether it is possible for the snake to move
 *   (if the snake meets its own body)
 *
 * @author PoppyFanboy
 */
  
import javafx.scene.canvas.*;
import javafx.scene.paint.*;

import javafx.scene.input.KeyCode;

class Snake {
	private Direction dir;
	private SnakeBlock head;
	private SnakeBlock tail;
	
	private GraphicsContext gc;
	private int blockSize;

	// костыль: если mouthfull == true, то на следующем шаге змейки хвостовой
	// блок визуально не убирается
	private boolean mouthfull = false;
	// костыль: если значение == true, то нажатия любых клавиш не обрабатываются
	private boolean keyPressed = false;
	
	Snake(GraphicsContext gc, int blockSize) {
		this.gc = gc;
		this.blockSize = blockSize;
		
		int initX = (int) gc.getCanvas().getWidth() / blockSize / 2;
		int initY = (int) gc.getCanvas().getHeight() / blockSize / 2;
		int initLength = 5;
		
		head = new SnakeBlock(initX, initY);
		head.paint(gc, blockSize, Color.BLACK);
		
		SnakeBlock previous = head;
		for (int i = initX + 1; i <= initX + initLength; i++) {
			previous = new SnakeBlock(previous, i, initY);
			previous.paint(gc, blockSize, Color.BLACK);
		}
		tail = previous;
		
		dir = Direction.LEFT;
	}
	
	Snake(GraphicsContext gc) {
		this(gc, 10);
	}

	void move(Field field) {
		keyPressed = false;

		int newX = (head.getX() + dir.offsetX() + Field.FIELD_WIDTH) % Field.FIELD_WIDTH;
		int newY = (head.getY() + dir.offsetY() + Field.FIELD_HEIGHT) % Field.FIELD_HEIGHT;
		
		SnakeBlock newHead = new SnakeBlock(newX, newY);
		head.next = newHead;
		head = newHead;

		if (!mouthfull) {
			tail.paint(gc, blockSize, Color.WHITE);
		}
		mouthfull = false;
		head.paint(gc, blockSize, Color.BLACK);
		
		tail = tail.next;

		if (field.gameField[newY][newX] == 2) {
			field.gameField[newY][newX] = 0;
			mouthfull = true;
			SnakeBlock newTail = new SnakeBlock(tail, tail.getX(), tail.getY());
			tail = newTail;
			field.generateFood();
		}
	}
	
	// returns true if the snake is able to move further
	boolean isSafeToMove(Field field) {
		int newX = (head.getX() + dir.offsetX() + Field.FIELD_WIDTH) % Field.FIELD_WIDTH;
		int newY = (head.getY() + dir.offsetY() + Field.FIELD_HEIGHT) % Field.FIELD_HEIGHT;

        // because on the next step tail block will move
        SnakeBlock block = tail.next;
        do {
            if (newX == block.getX() && newY == block.getY() || field.gameField[newX][newY] == 1) {
                return false;
            }
            block = block.next;
        } while (block != null);

		return true;
	}

	// changes the direction of the snake
	void controlInp(KeyCode code) {
		if (code.getCode() >= 37 && code.getCode() <= 40 && !keyPressed) {
			Direction newDir = null;
			if (code == KeyCode.UP) {
				newDir = Direction.UP;
			} else if (code == KeyCode.RIGHT) {
				newDir = Direction.RIGHT;
			} else if (code == KeyCode.DOWN) {
				newDir = Direction.DOWN;
			} else if (code == KeyCode.LEFT) {
				newDir = Direction.LEFT;
			}

			if (dir.ableToChange(newDir)) {
				dir = newDir;
			}
			keyPressed = true;
		}
	}

	SnakeBlock getTail() {
		return tail;
	}
}
