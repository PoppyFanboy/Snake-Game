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
  * @version 0.1
  * @author PoppyFanboy
  */
  
enum Direction {
	NORTH, EAST, SOUTH, WEST
}
  
class Snake {
	private Direction dir;
	
	private SnakeBlock head;
	private SnakeBlock tail;
	
	Snake(int initX, int initY, int initLength) {
		
	}
	
	// returns the number of gained points
	int move() {
		return 0;
	}
	
	// returns true if the snake is able to move further
	boolean isSafe() {
		return true;
	}
}