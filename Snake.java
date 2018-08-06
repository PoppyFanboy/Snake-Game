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
  * @version 0.1.1
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
		head = new SnakeBlock(initX, initY);
		
		SnakeBlock previous = head;
		for (int i = x + 1; i <= x + initLength; i++) {
			previous = new SnakeBlock(previous, i, y);
		}
		tail = previous;
		
		dir = Direction.WEST;
	}
	
	// returns the number of gained points
	int move() {
		int newX = head.x;
		int newY = head.y;
		
		switch (dir) {
			NORTH:	newY--;
					break;
					
			EAST:	newX++;
					break;
					
			SOUTH:	nexY++;
					break;
					
			WEST:	newX--;
					break;
		}
		
		SnakeBlock newHead = new SnakeBlock(newX, newY);
		head.next = newHead;
		head = newHead;
		
		tail = tail.next;
		
		return 0;
	}
	
	// returns true if the snake is able to move further
	boolean isSafe() {
		return true;
	}
}