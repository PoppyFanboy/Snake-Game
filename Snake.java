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
	// 0 - North, 1 - East, 2 - South, 3 - West
	private int dir;
	
	private SnakeBlock head;
	private SnakeBlock tail;
	
	private GraphicsContext gc;
	private int blockSize;
	
	Snake(GraphicsContext gc, int blockSize) {
		this.gc = gc;
		this.blockSize = blockSize;
		
		int initX = (int) gc.getCanvas().getWidth() / blockSize / 2;
		int initY = (int) gc.getCanvas().getHeight() / blockSize / 2;
		int initLength = 10;
		
		head = new SnakeBlock(initX, initY);
		head.paint(gc, blockSize, Color.BLACK);
		
		SnakeBlock previous = head;
		for (int i = initX + 1; i <= initX + initLength; i++) {
			previous = new SnakeBlock(previous, i, initY);
			previous.paint(gc, blockSize, Color.BLACK);
		}
		tail = previous;
		
		dir = 3;
	}
	
	Snake(GraphicsContext gc) {
		this(gc, 10);
	}
	
	// returns the number of gained points
	int move() {
		int newX = head.getX();
		int newY = head.getY();
		
		switch (dir) {
			case 0:	newY--;
					break;
					
			case 1:	newX++;
					break;
					
			case 2:	newY++;
					break;
					
			case 3:	newX--;
					break;
		}
		
		SnakeBlock newHead = new SnakeBlock(newX, newY);
		head.next = newHead;
		head = newHead;
		
		head.paint(gc, blockSize, Color.BLACK);
		tail.paint(gc, blockSize, Color.WHITE);
		
		tail = tail.next;
		
		return 0;
	}
	
	// returns true if the snake is able to move further
	boolean isSafe() {
		return true;
	}
	
	void changeDir(KeyCode code) {
		if (code == KeyCode.UP) {
			dir = 0;
		} else if (code == KeyCode.RIGHT) {
			dir = 1;
		} else if (code == KeyCode.DOWN) {
			dir = 2;
		} else if (code == KeyCode.LEFT) {
			dir = 3;
		} 
	}
}
