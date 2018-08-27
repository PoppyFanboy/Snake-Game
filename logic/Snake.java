package poppyfanboy.snakegame.logic;

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

import static poppyfanboy.snakegame.logic.IntVector.vector;

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

		IntVector init = vector(Field.FIELD_WIDTH / 2, Field.FIELD_HEIGHT / 2);
		int initLength = 5;
		
		head = new SnakeBlock(init);
		head.paint(gc, blockSize, Color.BLACK);
		
		SnakeBlock previous = head;
		for (int i = 1; i <= initLength; i++) {
			previous = new SnakeBlock(previous, previous.getCoords().incX(1));
			previous.paint(gc, blockSize, Color.BLACK);
		}
		tail = previous;
		
		dir = Direction.LEFT;
	}
	
	Snake(GraphicsContext gc) {
		this(gc, 10);
	}

	int move(Field field) {
		keyPressed = false;
		int earnedPoints = 0;

		IntVector newCoord = head.getCoords().add(dir.getOffset()).mod(Field.FIELD_WIDTH);

		SnakeBlock newHead = new SnakeBlock(newCoord);
		head.next = newHead;
		head = newHead;

		if (!mouthfull) {
			tail.paint(gc, blockSize, Color.WHITE);
		}
		mouthfull = false;
		head.paint(gc, blockSize, Color.BLACK);
		
		tail = tail.next;

		if (field.gameField[newCoord.getY()][newCoord.getX()] == 2) {
			earnedPoints = 10;
			field.gameField[newCoord.getY()][newCoord.getX()] = 0;
			mouthfull = true;
			SnakeBlock newTail = new SnakeBlock(tail, tail.getCoords());
			tail = newTail;
			field.generateFood();
		}

		return earnedPoints;
	}
	
	// returns true if the snake is able to move further
	boolean isSafeToMove(Field field) {
		IntVector newCoords = head.getCoords().add(dir.getOffset()).mod(Field.FIELD_WIDTH);

        // because on the next step tail block will move
        SnakeBlock block = tail.next;
        do {
            if (newCoords.equals(block.getCoords()) || field.gameField[newCoords.getY()][newCoords.getX()] == 1) {
                return false;
            }
            block = block.next;
        } while (block != null);

		return true;
	}

	// changes the direction of the snake
	void controlInp(KeyCode code) {
		if ((code == KeyCode.UP || code == KeyCode.DOWN || code == KeyCode.RIGHT ||
				code == KeyCode.LEFT) && !keyPressed) {
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
