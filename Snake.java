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

enum Direction { UP, RIGHT, DOWN, LEFT };

class Snake {
	// same order as in the Direction enum
	private static final Offset[] OFFSETS = { new Offset(0, -1), new Offset(1, 0),
			                                  new Offset(0, 1),  new Offset(-1, 0)};

	private Direction dir;
	private SnakeBlock head;
	private SnakeBlock tail;
	
	private GraphicsContext gc;
	private int blockSize;
	
	Snake(GraphicsContext gc, int blockSize) {
		this.gc = gc;
		this.blockSize = blockSize;
		
		int initX = (int) gc.getCanvas().getWidth() / blockSize / 2;
		int initY = (int) gc.getCanvas().getHeight() / blockSize / 2;
		int initLength = (int) gc.getCanvas().getWidth() / blockSize / 5;
		
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
	
	// returns the number of gained points
	int move() {
		int newX = head.getX() + OFFSETS[dir.ordinal()].offsetX;
		int newY = head.getY() + OFFSETS[dir.ordinal()].offsetY;
		
		SnakeBlock newHead = new SnakeBlock(newX, newY);
		head.next = newHead;
		head = newHead;
		
		head.paint(gc, blockSize, Color.BLACK);
		tail.paint(gc, blockSize, Color.WHITE);
		
		tail = tail.next;
		
		return 0;
	}
	
	// returns true if the snake is able to move further
	boolean isSafeToMove() {
		int newX = head.getX() + OFFSETS[dir.ordinal()].offsetX;
		int newY = head.getY() + OFFSETS[dir.ordinal()].offsetY;

        // because on the next step tail block will move
        SnakeBlock block = tail.next;
        do {
            if (newX == block.getX() && newY == block.getY()) {
                return false;
            }
            block = block.next;
        } while (block != null);

		return true;
	}

	// changes the direction of the snake
	void controlInp(KeyCode code) {
		if (code.getCode() >= 37 && code.getCode() <= 40) {
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

			if ((newDir.ordinal() - dir.ordinal() + 4) % 4 != 2) {
				dir = newDir;
			}
		}
	}
}